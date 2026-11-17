package frc.robot.io;

import org.wpilib.epilogue.Logged;
import frc.robot.subsystems.DrivetrainInputs;
import frc.robot.subsystems.DrivetrainOutputs;

/** Holds the latest mutable drivetrain input and output snapshots for robot code. */
@Logged(strategy = Logged.Strategy.OPT_IN)
public class RobotIO {
  private static final RobotIO instance = new RobotIO();

  @Logged
  private DrivetrainInputs latestDrivetrainInputs = new DrivetrainInputs();

  @Logged
  private DrivetrainOutputs latestDrivetrainOutputs = new DrivetrainOutputs();

  public static RobotIO getInstance() {
    return instance;
  }

  private RobotIO() {}

  /** Returns a mutable copy of the latest drivetrain inputs for this control update. */
  public DrivetrainInputs getDrivetrainInputs() {
    return new DrivetrainInputs(latestDrivetrainInputs);
  }

  public DrivetrainOutputs getDrivetrainOutputs() {
    return new DrivetrainOutputs(latestDrivetrainOutputs);
  }

  public void updateDrivetrainInputs(DrivetrainInputs inputs) {
    latestDrivetrainInputs = inputs;
  }

  public void updateDrivetrainOutputs(DrivetrainOutputs outputs) {
    latestDrivetrainOutputs = outputs;
  }
}
