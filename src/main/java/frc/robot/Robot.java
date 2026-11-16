package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Drivetrain;

/** Swerve-drive-only robot: the only actuator subsystem is the drivetrain. */
public class Robot extends TimedRobot {
  private final Drivetrain drivetrain = new Drivetrain();
  private final XboxController driver = new XboxController(Constants.OI.DRIVER_USB_PORT);

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
    double forward = -applyDeadband(driver.getLeftY());
    double left = -applyDeadband(driver.getLeftX());
    double turn = -applyDeadband(driver.getRightX());
    drivetrain.drive(forward, left, turn, !driver.getRightBumper());
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
