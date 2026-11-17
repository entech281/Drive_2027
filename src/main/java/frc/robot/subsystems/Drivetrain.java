package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import frc.robot.Constants;

/** Four-module swerve drive and wheel/gyro odometry. */
@Logged(strategy = Logged.Strategy.OPT_IN)
public final class Drivetrain {
  private final SwerveModule[] modules = {
    new SwerveModule(0), new SwerveModule(1), new SwerveModule(2), new SwerveModule(3)
  };
  private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(Constants.Drive.MODULE_LOCATIONS);
  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro(Constants.Drive.GYRO_SPI_PORT);
  private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(
      kinematics, gyro.getRotation2d(), getModulePositions());
  @Logged private DrivetrainInputs inputs = new DrivetrainInputs();
  @Logged private final DrivetrainOutputs outputs = new DrivetrainOutputs();

  /** Applies the driver's normalized requests and records the resulting chassis commands. */
  public void drive(DrivetrainInputs inputs) {
    this.inputs = inputs;
    double vx = MathUtil.clamp(inputs.forward, -1.0, 1.0) * Constants.Drive.MAX_SPEED_METERS_PER_SECOND;
    double vy = MathUtil.clamp(inputs.left, -1.0, 1.0) * Constants.Drive.MAX_SPEED_METERS_PER_SECOND;
    double omega = MathUtil.clamp(inputs.turn, -1.0, 1.0) * Constants.Drive.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND;
    outputs.forwardMetersPerSecond = vx;
    outputs.leftMetersPerSecond = vy;
    outputs.angularVelocityRadiansPerSecond = omega;
    outputs.fieldRelative = inputs.fieldRelative;
    ChassisSpeeds speeds = inputs.fieldRelative
        ? ChassisSpeeds.fromFieldRelativeSpeeds(vx, vy, omega, gyro.getRotation2d())
        : new ChassisSpeeds(vx, vy, omega);
    setModuleStates(kinematics.toSwerveModuleStates(speeds));
  }

  public void setModuleStates(SwerveModuleState[] states) {
    SwerveDriveKinematics.desaturateWheelSpeeds(states, Constants.Drive.MAX_SPEED_METERS_PER_SECOND);
    for (int i = 0; i < modules.length; i++) modules[i].setDesiredState(states[i]);
  }

  public void periodic() {
    odometry.update(gyro.getRotation2d(), getModulePositions());
    inputs.gyroHeadingDegrees = gyro.getRotation2d().getDegrees();
    for (int i = 0; i < modules.length; i++) {
      inputs.driveDistanceMeters[i] = modules[i].getDriveDistanceMeters();
      inputs.driveVelocityMetersPerSecond[i] = modules[i].getState().speedMetersPerSecond;
      inputs.steerAngleRadians[i] = modules[i].getState().angle.getRadians();
    }
  }

  public double getHeadingDegrees() {
    return gyro.getRotation2d().getDegrees();
  }

  public void zeroHeading() {
    gyro.reset();
  }

  public void stop() {
    for (SwerveModule module : modules) module.stop();
    outputs.forwardMetersPerSecond = 0.0;
    outputs.leftMetersPerSecond = 0.0;
    outputs.angularVelocityRadiansPerSecond = 0.0;
    outputs.fieldRelative = false;
  }

  private SwerveModulePosition[] getModulePositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[modules.length];
    for (int i = 0; i < modules.length; i++) {
      positions[i] = new SwerveModulePosition(modules[i].getDriveDistanceMeters(), modules[i].getState().angle);
    }
    return positions;
  }
}
