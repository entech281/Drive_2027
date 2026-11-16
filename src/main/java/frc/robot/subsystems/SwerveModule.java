package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import frc.robot.Constants;

/** Single module using PWM motor controllers, a quadrature drive encoder, and an analog absolute angle sensor. */
public final class SwerveModule {
  private final PWMSparkMax driveMotor;
  private final PWMSparkMax steerMotor;
  private final Encoder driveEncoder;
  private final AnalogInput steerSensor;
  private final PIDController steerController = new PIDController(Constants.Drive.STEER_KP, 0.0, 0.0);
  private final int index;

  public SwerveModule(int index) {
    this.index = index;
    driveMotor = new PWMSparkMax(Constants.Drive.DRIVE_PWM[index]);
    steerMotor = new PWMSparkMax(Constants.Drive.STEER_PWM[index]);
    driveMotor.setInverted(Constants.Drive.DRIVE_INVERTED[index]);
    steerMotor.setInverted(Constants.Drive.STEER_INVERTED[index]);
    driveEncoder = new Encoder(
        Constants.Drive.DRIVE_ENCODER_A[index], Constants.Drive.DRIVE_ENCODER_B[index],
        Constants.Drive.DRIVE_ENCODER_REVERSED[index]);
    steerSensor = new AnalogInput(Constants.Drive.STEER_ANALOG[index]);
    steerController.enableContinuousInput(-Math.PI, Math.PI);
    driveEncoder.setDistancePerPulse(
        Math.PI * Constants.Drive.WHEEL_DIAMETER_METERS
            / (Constants.Drive.DRIVE_REDUCTION * 2048.0));
  }

  public Rotation2d getAngle() {
    double fraction = (steerSensor.getVoltage() - Constants.Drive.STEER_ZERO_VOLTS[index]) / 5.0;
    double radians = MathUtil.inputModulus(fraction * 2.0 * Math.PI, -Math.PI, Math.PI);
    if (Constants.Drive.STEER_SENSOR_REVERSED[index]) radians = -radians;
    return Rotation2d.fromRadians(radians);
  }

  public double getDriveDistanceMeters() {
    return driveEncoder.getDistance();
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driveEncoder.getRate(), getAngle());
  }

  public void setDesiredState(SwerveModuleState desired) {
    SwerveModuleState optimized = SwerveModuleState.optimize(desired, getAngle());
    double speed = MathUtil.clamp(optimized.speedMetersPerSecond,
        -Constants.Drive.MAX_SPEED_METERS_PER_SECOND, Constants.Drive.MAX_SPEED_METERS_PER_SECOND);
    double driveOutput = Constants.Drive.DRIVE_KV * speed;
    if (Math.abs(speed) > 0.02) driveOutput += Math.copySign(Constants.Drive.DRIVE_KS, speed);
    driveMotor.set(MathUtil.clamp(driveOutput, -1.0, 1.0));
    double steerOutput = steerController.calculate(getAngle().getRadians(), optimized.angle.getRadians());
    steerMotor.set(MathUtil.clamp(steerOutput, -0.65, 0.65));
  }

  public void stop() {
    driveMotor.stopMotor();
    steerMotor.stopMotor();
  }

  public void resetDriveDistance() {
    driveEncoder.reset();
  }
}
