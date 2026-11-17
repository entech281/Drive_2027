package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.epilogue.Epilogue;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.DataLogManager;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.DrivetrainInputs;

/** Swerve-drive-only robot: the only actuator subsystem is the drivetrain. */
@Logged(strategy = Logged.Strategy.OPT_IN)
public class Robot extends TimedRobot {
  @Logged
  private final Drivetrain drivetrain = new Drivetrain();
  private final DrivetrainInputs drivetrainInputs = new DrivetrainInputs();
  private final XboxController driver = new XboxController(Constants.OI.DRIVER_USB_PORT);

  public Robot() {
    DataLogManager.start();
    Epilogue.bind(this);
  }

  @Override
  public void robotPeriodic() {
    drivetrain.periodic();
  }

  @Override
  public void teleopPeriodic() {
    if (driver.getStartButtonPressed()) {
      drivetrain.zeroHeading();
    }

    // Standard FRC convention: left stick translates, right stick turns.
    // Xbox Y axes are positive upward; negate them so forward is +X.
    drivetrainInputs.forward = -applyDeadband(driver.getLeftY());
    drivetrainInputs.left = -applyDeadband(driver.getLeftX());
    drivetrainInputs.turn = -applyDeadband(driver.getRightX());
    drivetrainInputs.fieldRelative = !driver.getRightBumper();
    drivetrain.drive(drivetrainInputs);
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
