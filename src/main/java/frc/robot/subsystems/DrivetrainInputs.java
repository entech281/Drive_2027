package frc.robot.subsystems;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;
import frc.robot.Constants;

/** Cached drivetrain sensor readings and their Epilogue logger. */
@CustomLoggerFor(DrivetrainInputs.class)
public final class DrivetrainInputs extends ClassSpecificLogger<DrivetrainInputs> {
  /** Normalized driver requests. Translation axes are forward and left; turn is counterclockwise. */
  public double forward;
  public double left;
  public double turn;
  public boolean fieldRelative = true;

  /** Cached sensor readings refreshed by the drivetrain once each robot loop. */
  public double gyroHeadingDegrees;
  public final double[] driveDistanceMeters = new double[Constants.Drive.MODULE_LOCATIONS.length];
  public final double[] driveVelocityMetersPerSecond = new double[Constants.Drive.MODULE_LOCATIONS.length];
  public final double[] steerAngleRadians = new double[Constants.Drive.MODULE_LOCATIONS.length];

  public DrivetrainInputs() {
    super(DrivetrainInputs.class);
  }

  @Override
  public void update(EpilogueBackend backend, DrivetrainInputs inputs) {
    backend.log("ForwardInput", inputs.forward);
    backend.log("LeftInput", inputs.left);
    backend.log("TurnInput", inputs.turn);
    backend.log("FieldRelativeInput", inputs.fieldRelative);
    backend.log("GyroHeadingDegrees", inputs.gyroHeadingDegrees);
    backend.log("DriveDistanceMeters", inputs.driveDistanceMeters);
    backend.log("DriveVelocityMetersPerSecond", inputs.driveVelocityMetersPerSecond);
    backend.log("SteerAngleRadians", inputs.steerAngleRadians);
  }
}
