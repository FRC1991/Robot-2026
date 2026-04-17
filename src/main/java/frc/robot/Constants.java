// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TorqueCurrentConfigs;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

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
  public static final String LIMELIGHT_NAME = "";

  public static final double KRAKEN_MAX_SPEED = 0;
  public static final double NEO_MAX_SPEED = 5600;

  public static final TalonFXConfiguration KRAKEN_CONFIG = new TalonFXConfiguration()
    .withTorqueCurrent(new TorqueCurrentConfigs().withPeakForwardTorqueCurrent(80.0).withPeakReverseTorqueCurrent(-80.0))
    .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

  public static final SparkBaseConfig NEO_CONFIG = new SparkMaxConfig().idleMode(IdleMode.kBrake).smartCurrentLimit(40);

  public static abstract class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kAuxControllerPort = 1;
  }

  public static abstract class FieldConstants {
    public static final double FIELD_WIDTH_METERS = Units.inchesToMeters(316.64);
    public static final double FIELD_HALF_METERS = FIELD_WIDTH_METERS / 2;

    public static final Translation2d BLUE_HUB_LOC = new Translation2d(Units.inchesToMeters(181.56), Units.inchesToMeters(158.32));
    public static final Translation2d RED_HUB_LOC = new Translation2d(Units.inchesToMeters(468.56), Units.inchesToMeters(158.32));

    public static final Translation2d BLUE_LEFT_BUMP_LOC = new Translation2d(Units.inchesToMeters(181.56), Units.inchesToMeters(254.78));
    public static final Translation2d RED_LEFT_BUMP_LOC = new Translation2d(Units.inchesToMeters(468.56), Units.inchesToMeters(61.86));

    public static final Translation2d BLUE_RIGHT_BUMP_LOC = new Translation2d(Units.inchesToMeters(181.56), Units.inchesToMeters(61.86));
    public static final Translation2d RED_RIGHT_BUMP_LOC = new Translation2d(Units.inchesToMeters(468.56), Units.inchesToMeters(254.78));
  }

  public static abstract class CANConstants {
    // Driving motor CAN ids
    public static final int FRONT_LEFT_DRIVING_ID = 2;
    public static final int FRONT_RIGHT_DRIVING_ID = 3;

    public static final int BACK_RIGHT_DRIVING_ID = 4;
    public static final int BACK_LEFT_DRIVING_ID = 5;

    // Turning motor CAN ids
    public static final int FRONT_LEFT_TURNING_ID = 6;
    public static final int FRONT_RIGHT_TURNING_ID = 8;

    public static final int BACK_RIGHT_TURNING_ID = 7;
    public static final int BACK_LEFT_TURNING_ID = 9;

    public static final int GYRO_ID = 10;

    public static final int SHOOTER_ID_ONE = 11;
    public static final int SHOOTER_ID_TWO = 12;

    public static final int INDEXER_ID = 13;

    public static final int HOPPER_ID = 14;

    public static final int INTAKE_ID = 15;

    public static final int SLIDER_ONE_ID = 16;
    public static final int SLIDER_TWO_ID = 17;

    // Encoder channels for each swerve module
    public static final int FRONT_LEFT_ENCODER_CHANNEL = 0;
    public static final int FRONT_RIGHT_ENCODER_CHANNEL = 1;

    public static final int BACK_RIGHT_ENCODER_CHANNEL = 3;
    public static final int BACK_LEFT_ENCODER_CHANNEL = 2;
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
    public static final double TRACK_WIDTH = Units.inchesToMeters(24);
    // Distance from center of front wheels to center of back wheels
    public static final double WHEEL_BASE = Units.inchesToMeters(19.25);

    // Swerve drive kinematics for the robot
    public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
      new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
      new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2));

    // Offsets for each swerve module
    public static final double FRONT_LEFT_CHASSIS_OFFSET = 0.9270997038300972; // GOOD!
    // public static final double FRONT_RIGHT_CHASSIS_OFFSET = 2.6354094568666446; // GOOD!
    public static final double FRONT_RIGHT_CHASSIS_OFFSET = 2.604847385044587;

    public static final double BACK_LEFT_CHASSIS_OFFSET = -2.656949091930007; // GOOD!
    public static final double BACK_RIGHT_CHASSIS_OFFSET = -1.8354411901614043; // GOOD!

    // Whether or not the gyro is reversed (angle)
    public static final boolean GYRO_REVERSED = false;

    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double MAX_SPEED_METERS_PER_SECOND = 5.7;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = 2 * Math.PI;
    public static final double MAX_DEGREES_PER_SCHEDULER_LOOP = MAX_ANGULAR_SPEED_RADIANS_PER_SECOND * (180 / Math.PI) / 1000 * 20 * 0.7;
  }

  public static abstract class ShooterConstants {
    public static final double INDEXER_SPEED = -0.8;
    public static final double SHOOTER_SPEED = 0.33 * NEO_MAX_SPEED;
    public static final double PASSING_SPEED = 0.75 * NEO_MAX_SPEED;
  }

  public static abstract class SliderConstants {
    public static final double HOME_POSITION = 0;
    public static final double INTAKING_POSITION = -65;

    public static final double HOME_ONE_POS = 0;
    public static final double HOME_TWO_POS = 0;

    public static final double INTAKING_ONE_POS = -24;
    public static final double INTAKING_TWO_POS = -24;
  }

  public static abstract class HopperConstants {
    public static final double HOPPER_SPEED = 1.0;
  }

  public static abstract class IntakeConstants {
    public static final double INTAKE_SPEED = -1.0;
  }
}
