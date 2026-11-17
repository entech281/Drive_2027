package frc.robot.subsystems;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import frc.robot.Constants;

/** Four-module swerve drive and wheel/gyro odometry. */
public final class Drivetrain {
  private final SwerveModule[] modules = {
    new SwerveModule(0), new SwerveModule(1), new SwerveModule(2), new SwerveModule(3)
  };
  private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(Constants.Drive.MODULE_LOCATIONS);
  private final ADXRS450_Gyro gyro = new ADXRS450_Gyro(Constants.Drive.GYRO_SPI_PORT);
  private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(
      kinematics, gyro.getRotation2d(), getModulePositions());
  private DrivetrainInputs inputs = new DrivetrainInputs();
  private DrivetrainOutputs outputs = new DrivetrainOutputs();

  /** Applies a mutable input snapshot and records the resulting chassis commands. */
  public void updateInputs(DrivetrainInputs inputs) {
    this.inputs = inputs;
    double vx = MathUtil.clamp(inputs.getForward(), -1.0, 1.0) * Constants.Drive.MAX_SPEED_METERS_PER_SECOND;
    double vy = MathUtil.clamp(inputs.getLeft(), -1.0, 1.0) * Constants.Drive.MAX_SPEED_METERS_PER_SECOND;
    double omega = MathUtil.clamp(inputs.getTurn(), -1.0, 1.0) * Constants.Drive.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND;
    outputs = new DrivetrainOutputs();
    outputs.setForwardMetersPerSecond(vx);
    outputs.setLeftMetersPerSecond(vy);
    outputs.setAngularVelocityRadiansPerSecond(omega);
    outputs.setFieldRelative(inputs.isFieldRelative());
    ChassisSpeeds speeds = inputs.isFieldRelative()
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
    inputs.setGyroHeadingDegrees(gyro.getRotation2d().getDegrees());
    for (int i = 0; i < modules.length; i++) {
      inputs.getDriveDistanceMeters()[i] = modules[i].getDriveDistanceMeters();
      inputs.getDriveVelocityMetersPerSecond()[i] = modules[i].getState().speedMetersPerSecond;
      inputs.getSteerAngleRadians()[i] = modules[i].getState().angle.getRadians();
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
    outputs = new DrivetrainOutputs();
  }

  public DrivetrainOutputs getOutputs() {
    return new DrivetrainOutputs(outputs);
  }

  public void updateIsGood() {
    outputs.setGood(true);
  }

  private SwerveModulePosition[] getModulePositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[modules.length];
    for (int i = 0; i < modules.length; i++) {
      positions[i] = new SwerveModulePosition(modules[i].getDriveDistanceMeters(), modules[i].getState().angle);
    }
    return positions;
  }
}
