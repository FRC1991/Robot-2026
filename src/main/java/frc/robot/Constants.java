// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final String LIMELIGHT_NAME = "limelight";

  public static abstract class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kAuxControllerPort = 1;
  }

  public static abstract class CANConstants {
    // Driving motor CAN ids
    public static final int FRONT_LEFT_DRIVING_ID = 2;
    public static final int FRONT_RIGHT_DRIVING_ID = 3;

    public static final int BACK_RIGHT_DRIVING_ID = 4;
    public static final int BACK_LEFT_DRIVING_ID = 5;

    // Turning motor CAN ids
    public static final int FRONT_LEFT_TURNING_ID = 6;
    public static final int FRONT_RIGHT_TURNING_ID = 7;

    public static final int BACK_RIGHT_TURNING_ID = 8;
    public static final int BACK_LEFT_TURNING_ID = 9;

    public static final int GYRO_ID = 10;

    // These are for the motors that power the elevator for the climber
    public static final int CLIMBER_ONE_ID = 11;
    public static final int CLIMBER_TWO_ID = 12;

    // These are for the motors that power the hooks on the climber
    public static final int HOOK_ONE_ID = 13;
    public static final int HOOK_TWO_ID = 14;

    public static final int SHOOTER_ID = 15;

    // Encoder channels for each swerve module
    public static final int FRONT_LEFT_ENCODER_CHANNEL = 0;
    public static final int FRONT_RIGHT_ENCODER_CHANNEL = 1;

    public static final int BACK_RIGHT_ENCODER_CHANNEL = 2;
    public static final int BACK_LEFT_ENCODER_CHANNEL = 3;
  }

  public static abstract class ModuleConstants {
    public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(4);
    public static final double DRIVING_MOTOR_REDUCTION = 8.14;
  }

  public static abstract class SwerveConstants {
    // Speed modifiers for swerve drive
    public static final double SPEED_SCALE = 0.5;
    public static final double ROTATION_SPEED_SCALE = 0.7;

    // Deadband to clamp the driving input
    public static final double DRIVING_DEADBAND = 0.07;

    // Chassis configuration
    // Distance from center of left wheels to center of right wheels
    public static final double TRACK_WIDTH = Units.inchesToMeters(19.25);
    // Distance from center of front wheels to center of back wheels
    public static final double WHEEL_BASE = Units.inchesToMeters(24);

    // Swerve drive kinematics for the robot
    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
      new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2));

    // Offsets for each swerve module
    public static final double FRONT_LEFT_CHASSIS_OFFSET = -(Math.PI / 2) + (Math.PI / 3) - 0.385; // GOOD
    public static final double FRONT_RIGHT_CHASSIS_OFFSET = Math.PI + (Math.PI / 3) - 0.525; // GOOD

    public static final double BACK_RIGHT_CHASSIS_OFFSET = -(Math.PI + (Math.PI / 3)) - 0.305; // GOOD
    public static final double BACK_LEFT_CHASSIS_OFFSET = Math.PI - 0.505; // GOOD

    // Whether or not the gyro is reversed (angle)
    public static final boolean GYRO_REVERSED = false;

    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double MAX_SPEED_METERS_PER_SECOND = 5.7;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = 2 * Math.PI;
    public static final double MAX_DEGREES_PER_SCHEDULER_LOOP = MAX_ANGULAR_SPEED_RADIANS_PER_SECOND * (180 / Math.PI) / 1000 * 20 * 0.7;
  }
}
