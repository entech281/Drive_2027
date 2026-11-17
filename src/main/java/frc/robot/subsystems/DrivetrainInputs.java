package frc.robot.subsystems;

import org.wpilib.epilogue.CustomLoggerFor;
import org.wpilib.epilogue.logging.ClassSpecificLogger;
import org.wpilib.epilogue.logging.EpilogueBackend;

import frc.robot.Constants;

/** Cached drivetrain sensor readings and their Epilogue logger. */
@CustomLoggerFor(DrivetrainInputs.class)
public class DrivetrainInputs extends ClassSpecificLogger<DrivetrainInputs> {
  /** Normalized driver requests. Translation axes are forward and left; turn is counterclockwise. */
  private double forward;
  private double left;
  private double turn;
  private boolean fieldRelative = true;

  /** Cached sensor readings refreshed by the drivetrain once each robot loop. */
  private double gyroHeadingDegrees;
  private double[] driveDistanceMeters = new double[Constants.Drive.MODULE_LOCATIONS.length];
  private double[] driveVelocityMetersPerSecond = new double[Constants.Drive.MODULE_LOCATIONS.length];
  private double[] steerAngleRadians = new double[Constants.Drive.MODULE_LOCATIONS.length];

  public DrivetrainInputs() {
    super(DrivetrainInputs.class);
  }

  public DrivetrainInputs(DrivetrainInputs template) {
    this();
    forward = template.getForward();
    left = template.getLeft();
    turn = template.getTurn();
    fieldRelative = template.isFieldRelative();
    gyroHeadingDegrees = template.getGyroHeadingDegrees();
    driveDistanceMeters = template.getDriveDistanceMeters().clone();
    driveVelocityMetersPerSecond = template.getDriveVelocityMetersPerSecond().clone();
    steerAngleRadians = template.getSteerAngleRadians().clone();
  }

  public DrivetrainInputs copy() {
    return new DrivetrainInputs(this);
  }

  public double getForward() { return forward; }
  public void setForward(double forward) { this.forward = forward; }
  public double getLeft() { return left; }
  public void setLeft(double left) { this.left = left; }
  public double getTurn() { return turn; }
  public void setTurn(double turn) { this.turn = turn; }
  public boolean isFieldRelative() { return fieldRelative; }
  public void setFieldRelative(boolean fieldRelative) { this.fieldRelative = fieldRelative; }
  public double getGyroHeadingDegrees() { return gyroHeadingDegrees; }
  public void setGyroHeadingDegrees(double gyroHeadingDegrees) { this.gyroHeadingDegrees = gyroHeadingDegrees; }
  public double[] getDriveDistanceMeters() { return driveDistanceMeters; }
  public void setDriveDistanceMeters(double[] driveDistanceMeters) { this.driveDistanceMeters = driveDistanceMeters; }
  public double[] getDriveVelocityMetersPerSecond() { return driveVelocityMetersPerSecond; }
  public void setDriveVelocityMetersPerSecond(double[] driveVelocityMetersPerSecond) {
    this.driveVelocityMetersPerSecond = driveVelocityMetersPerSecond;
  }
  public double[] getSteerAngleRadians() { return steerAngleRadians; }
  public void setSteerAngleRadians(double[] steerAngleRadians) { this.steerAngleRadians = steerAngleRadians; }

  @Override
  public void update(EpilogueBackend backend, DrivetrainInputs inputs) {
    backend.log("ForwardInput", inputs.getForward());
    backend.log("LeftInput", inputs.getLeft());
    backend.log("TurnInput", inputs.getTurn());
    backend.log("FieldRelativeInput", inputs.isFieldRelative());
    backend.log("GyroHeadingDegrees", inputs.getGyroHeadingDegrees());
    backend.log("DriveDistanceMeters", inputs.getDriveDistanceMeters());
    backend.log("DriveVelocityMetersPerSecond", inputs.getDriveVelocityMetersPerSecond());
    backend.log("SteerAngleRadians", inputs.getSteerAngleRadians());
  }

}
