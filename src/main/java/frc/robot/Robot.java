package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DataLogManager;
import org.wpilib.epilogue.Epilogue;
import org.wpilib.epilogue.Logged;
import frc.robot.subsystems.Drivetrain;
import frc.robot.io.RobotIO;
import frc.robot.subsystems.DrivetrainInputs;

/** Swerve-drive-only robot: the only actuator subsystem is the drivetrain. */
@Logged(strategy = Logged.Strategy.OPT_IN)
public class Robot extends TimedRobot {
  @Logged
  private final RobotIO robotIO = RobotIO.getInstance();
  private final Drivetrain drivetrain = new Drivetrain();
  private final XboxController driver = new XboxController(Constants.OI.DRIVER_USB_PORT);

  public Robot() {
    DataLogManager.start();
    Epilogue.bind(this);
  }

  @Override
  public void robotPeriodic() {
    drivetrain.periodic();
    robotIO.updateDrivetrainOutputs(drivetrain.getOutputs());
  }

  @Override
  public void teleopPeriodic() {
    if (driver.getStartButtonPressed()) {
      drivetrain.zeroHeading();
    }

    // Standard FRC convention: left stick translates, right stick turns.
    // Xbox Y axes are positive upward; negate them so forward is +X.
    DrivetrainInputs inputs = robotIO.getDrivetrainInputs();
    inputs.setForward(-applyDeadband(driver.getLeftY()));
    inputs.setLeft(-applyDeadband(driver.getLeftX()));
    inputs.setTurn(-applyDeadband(driver.getRightX()));
    inputs.setFieldRelative(!driver.getRightBumper());
    robotIO.updateDrivetrainInputs(inputs);
    drivetrain.updateInputs(inputs);
  }

  @Override
  public void autonomousInit() {
    drivetrain.stop();
  }

  @Override
  public void autonomousPeriodic() {
    drivetrain.stop();
  }

  @Override
  public void disabledInit() {
    drivetrain.stop();
  }

  @Override
  public void testInit() {
    drivetrain.stop();
  }

  @Override
  public void testPeriodic() {
    drivetrain.stop();
  }

  private static double applyDeadband(double value) {
    double magnitude = Math.abs(value);
    if (magnitude <= Constants.OI.DEADBAND) return 0.0;
    return Math.copySign((magnitude - Constants.OI.DEADBAND) / (1.0 - Constants.OI.DEADBAND), value);
  }
}
