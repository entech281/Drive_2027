package frc.robot.subsystems;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

/** Last chassis-level drivetrain request and its Epilogue logger. */
@CustomLoggerFor(DrivetrainOutputs.class)
public final class DrivetrainOutputs extends ClassSpecificLogger<DrivetrainOutputs> {
  public double forwardMetersPerSecond;
  public double leftMetersPerSecond;
  public double angularVelocityRadiansPerSecond;
  public boolean fieldRelative;

  public DrivetrainOutputs() {
    super(DrivetrainOutputs.class);
  }

  @Override
  public void update(EpilogueBackend backend, DrivetrainOutputs outputs) {
    backend.log("ForwardMetersPerSecond", outputs.forwardMetersPerSecond);
    backend.log("LeftMetersPerSecond", outputs.leftMetersPerSecond);
    backend.log("AngularVelocityRadiansPerSecond", outputs.angularVelocityRadiansPerSecond);
    backend.log("FieldRelative", outputs.fieldRelative);
  }
}
