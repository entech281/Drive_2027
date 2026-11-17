package frc.robot.subsystems;

import org.wpilib.epilogue.CustomLoggerFor;
import org.wpilib.epilogue.logging.ClassSpecificLogger;
import org.wpilib.epilogue.logging.EpilogueBackend;

/** Last chassis-level drivetrain request and its Epilogue logger. */
@CustomLoggerFor(DrivetrainOutputs.class)
public class DrivetrainOutputs extends ClassSpecificLogger<DrivetrainOutputs> {
  private double forwardMetersPerSecond;
  private double leftMetersPerSecond;
  private double angularVelocityRadiansPerSecond;
  private boolean fieldRelative;
  private boolean isGood;

  public DrivetrainOutputs() {
    super(DrivetrainOutputs.class);
  }

  public DrivetrainOutputs(DrivetrainOutputs template) {
    this();
    forwardMetersPerSecond = template.getForwardMetersPerSecond();
    leftMetersPerSecond = template.getLeftMetersPerSecond();
    angularVelocityRadiansPerSecond = template.getAngularVelocityRadiansPerSecond();
    fieldRelative = template.isFieldRelative();
    isGood = template.isGood();
  }

  public double getForwardMetersPerSecond() { return forwardMetersPerSecond; }
  public void setForwardMetersPerSecond(double value) { forwardMetersPerSecond = value; }
  public double getLeftMetersPerSecond() { return leftMetersPerSecond; }
  public void setLeftMetersPerSecond(double value) { leftMetersPerSecond = value; }
  public double getAngularVelocityRadiansPerSecond() { return angularVelocityRadiansPerSecond; }
  public void setAngularVelocityRadiansPerSecond(double value) { angularVelocityRadiansPerSecond = value; }
  public boolean isFieldRelative() { return fieldRelative; }
  public void setFieldRelative(boolean fieldRelative) { this.fieldRelative = fieldRelative; }
  public boolean isGood() { return isGood; }
  public void setGood(boolean isGood) { this.isGood = isGood; }

  @Override
  public void update(EpilogueBackend backend, DrivetrainOutputs outputs) {
    backend.log("ForwardMetersPerSecond", outputs.getForwardMetersPerSecond());
    backend.log("LeftMetersPerSecond", outputs.getLeftMetersPerSecond());
    backend.log("AngularVelocityRadiansPerSecond", outputs.getAngularVelocityRadiansPerSecond());
    backend.log("FieldRelative", outputs.isFieldRelative());
    backend.log("isGood", outputs.isGood());
  }
}
