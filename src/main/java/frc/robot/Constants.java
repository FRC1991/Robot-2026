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
  }

  public static abstract class SwerveConstants {
    // Speed modifiers for swerve drive
    public static final double SPEED_SCALE = 0.5;
    public static final double ROTATION_SPEED_SCALE = 0.7;

    // Deadband to clamp the driving input
    public static final double DRIVING_DEADBAND = 0.07;

    // Chassis configuration
    // Distance from center of left wheels to center of right wheels
    public static final double TRACK_WIDTH = Units.inchesToMeters(999); // TODO: OBVIOUSLY CHANGE THIS
    // Distance from center of front wheels to center of back wheels
    public static final double WHEEL_BASE = Units.inchesToMeters(999); // TODO: AND THIS

    // Swerve drive kinematics for the robot
    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
      new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double FRONT_LEFT_CHASSIS_ANGULAR_OFFSET = 0; // These probably need to be changed
    public static final double FRONT_RIGHT_CHASSIS_ANGULAR_OFFSET = 0;
    public static final double BACK_LEFT_CHASSIS_ANGULAR_OFFSET = 0;
    public static final double BACK_RIGHT_CHASSIS_ANGULAR_OFFSET = 0;

    // Whether or not the gyro is reversed (angle)
    public static final boolean GYRO_REVERSED = false;

    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double MAX_SPEED_METERS_PER_SECOND = 5.7;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = 2 * Math.PI;
    public static final double MAX_DEGREES_PER_SCHEDULER_LOOP = MAX_ANGULAR_SPEED_RADIANS_PER_SECOND * (180 / Math.PI) / 1000 * 20 * 0.7;
  }
}
