package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;

/** All robot-specific dimensions, channels, and gains live here. Verify before enabling motors. */
public final class Constants {
  private Constants() {}

  public static final class OI {
    public static final int DRIVER_USB_PORT = 0;
    public static final double DEADBAND = 0.08;
    private OI() {}
  }

  public static final class Drive {
    // Example geometry only: measure the actual center-to-wheel distances.
    public static final double TRACK_WIDTH_METERS = 0.5588;
    public static final double WHEEL_BASE_METERS = 0.5588;
    public static final double WHEEL_DIAMETER_METERS = 0.1016;
    public static final double DRIVE_REDUCTION = 6.75;
    public static final double MAX_SPEED_METERS_PER_SECOND = 4.0;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = 2.0 * Math.PI;

    // Rotation center to each module; order is FL, FR, BL, BR.
    public static final Translation2d[] MODULE_LOCATIONS = {
      new Translation2d(+WHEEL_BASE_METERS / 2, +TRACK_WIDTH_METERS / 2),
      new Translation2d(+WHEEL_BASE_METERS / 2, -TRACK_WIDTH_METERS / 2),
      new Translation2d(-WHEEL_BASE_METERS / 2, +TRACK_WIDTH_METERS / 2),
      new Translation2d(-WHEEL_BASE_METERS / 2, -TRACK_WIDTH_METERS / 2)
    };

    // PWM mapping example: drive FL/FR/BL/BR = 0..3, steer FL/FR/BL/BR = 4..7.
    public static final int[] DRIVE_PWM = {0, 1, 2, 3};
    public static final int[] STEER_PWM = {4, 5, 6, 7};
    // DIO quadrature pairs. Each pair consumes channel n and n+1.
    public static final int[] DRIVE_ENCODER_A = {0, 2, 4, 6};
    public static final int[] DRIVE_ENCODER_B = {1, 3, 5, 7};
    // Analog absolute steering sensors, voltage at 0 degrees and direction must be calibrated.
    public static final int[] STEER_ANALOG = {0, 1, 2, 3};
    public static final double[] STEER_ZERO_VOLTS = {0.0, 0.0, 0.0, 0.0};
    public static final boolean[] DRIVE_INVERTED = {false, false, false, false};
    public static final boolean[] STEER_INVERTED = {false, false, false, false};
    public static final boolean[] DRIVE_ENCODER_REVERSED = {false, false, false, false};
    public static final boolean[] STEER_SENSOR_REVERSED = {false, false, false, false};

    // Example gains. Start at low output and tune on blocks with wheels clear.
    public static final double DRIVE_KP = 0.18;
    public static final double DRIVE_KS = 0.08;
    public static final double DRIVE_KV = 0.22;
    public static final double STEER_KP = 2.5;
    public static final edu.wpi.first.wpilibj.SPI.Port GYRO_SPI_PORT = edu.wpi.first.wpilibj.SPI.Port.kMXP;

    private Constants() {}
  }
}
