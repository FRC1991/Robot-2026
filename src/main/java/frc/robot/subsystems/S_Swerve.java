// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.handlers.CheckableSubsystem;

public class S_Swerve extends SubsystemBase implements CheckableSubsystem {
  private final SwerveModule m_frontLeft = new SwerveModule(
    CANConstants.FRONT_LEFT_DRIVING_ID,
    CANConstants.FRONT_LEFT_TURNING_ID,
    CANConstants.FRONT_LEFT_ENCODER_CHANNEL,
    SwerveConstants.FRONT_LEFT_CHASSIS_OFFSET);

  private final SwerveModule m_frontRight = new SwerveModule(
    CANConstants.FRONT_RIGHT_DRIVING_ID,
    CANConstants.FRONT_RIGHT_TURNING_ID,
    CANConstants.FRONT_RIGHT_ENCODER_CHANNEL,
    SwerveConstants.FRONT_RIGHT_CHASSIS_OFFSET);

  private final SwerveModule m_backRight = new SwerveModule(
    CANConstants.BACK_RIGHT_DRIVING_ID,
    CANConstants.BACK_RIGHT_TURNING_ID,
    CANConstants.BACK_RIGHT_ENCODER_CHANNEL,
    SwerveConstants.BACK_RIGHT_CHASSIS_OFFSET);

  private final SwerveModule m_backLeft = new SwerveModule(
    CANConstants.BACK_LEFT_DRIVING_ID,
    CANConstants.BACK_LEFT_TURNING_ID,
    CANConstants.BACK_LEFT_ENCODER_CHANNEL,
    SwerveConstants.BACK_LEFT_CHASSIS_OFFSET);

  private boolean initialized = false, status = false;

  public final Pigeon2 m_gyro = new Pigeon2(CANConstants.GYRO_ID);

  SwerveDrivePoseEstimator m_poseEstimator = new SwerveDrivePoseEstimator(
    SwerveConstants.DRIVE_KINEMATICS,
    Rotation2d.fromDegrees(getHeading()),
    new SwerveModulePosition[] {
      m_frontLeft.getPosition(),
      m_frontRight.getPosition(),
      m_backLeft.getPosition(),
      m_backRight.getPosition()
    },
    new Pose2d());

  private static S_Swerve m_Instance;

  private double desiredHeading;
  
  /** Creates a new S_Swerve. */
  private S_Swerve() {
    setHeading(180);
  }

  public static S_Swerve getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Swerve();
    }

    return m_Instance;
  }

  public Pose2d getPose() {
    return m_poseEstimator.getEstimatedPosition();
  }

  public void resetPoseEstimator(Pose2d pose) {
    m_poseEstimator.resetPosition(
      Rotation2d.fromDegrees(getHeading()),
      new SwerveModulePosition[] {
        m_frontLeft.getPosition(),
        m_frontRight.getPosition(),
        m_backLeft.getPosition(),
        m_backRight.getPosition()
      },
      pose
    );
  }

  public void resetPoseEstimatorZero() {
    resetPoseEstimator(new Pose2d(0, 0, new Rotation2d(0)));
  }

  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    double xSpeedDelivered = xSpeed * SwerveConstants.MAX_SPEED_METERS_PER_SECOND;
    double ySpeedDelivered = ySpeed * SwerveConstants.MAX_SPEED_METERS_PER_SECOND;
    double rotDelivered = rot * SwerveConstants.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND * SwerveConstants.ROTATION_SPEED_SCALE;

    rotDelivered = rotDelivered * (SwerveConstants.GYRO_REVERSED ? -1 : 1);

    var swerveModuleStates = SwerveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(
      fieldRelative
        ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, Rotation2d.fromDegrees(getHeading()))
        : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));

    setModuleStates(swerveModuleStates);
  }

  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, double speedScale) {
    xSpeed *= speedScale;
    ySpeed *= speedScale;
    rot *= speedScale + ((1 - speedScale) / 2);

    drive(xSpeed, ySpeed, rot, fieldRelative);
  }

  public void setX() {
    m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_backLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_backRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  public void setTank() {
    m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(180)));
    m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)));
    m_backLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(180)));
    m_backRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(0)));
  }

  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
      desiredStates, SwerveConstants.MAX_SPEED_METERS_PER_SECOND);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_backLeft.setDesiredState(desiredStates[2]);
    m_backRight.setDesiredState(desiredStates[3]);
  }

  public SwerveModuleState[] getModuleStates() {
    return new SwerveModuleState[] {
      m_frontLeft.getState(),
      m_frontRight.getState(),
      m_backLeft.getState(),
      m_backRight.getState()
    };
  }

  public void resetDriveEncoders() {
    m_frontLeft.resetDriveEncoder();
    m_frontRight.resetDriveEncoder();
    m_backLeft.resetDriveEncoder();
    m_backRight.resetDriveEncoder();
  }

  public void setHeading(double heading) {
    m_gyro.setYaw(heading);
    desiredHeading = heading;
  }

  public void setDesiredHeading(double heading) {
    desiredHeading = heading;
  }

  public double getHeading() {
    return MathUtil.inputModulus(m_gyro.getYaw().getValueAsDouble(), 0, 360);
  }

  public void updatePoseEstimator() {
    m_poseEstimator.update(
      Rotation2d.fromDegrees(getHeading()),
      new SwerveModulePosition[] {
        m_frontLeft.getPosition(),
        m_frontRight.getPosition(),
        m_backLeft.getPosition(),
        m_backRight.getPosition()
      });
  }

  @Override
  public void stop() {
    m_frontLeft.stop();
    m_frontRight.stop();
    m_backLeft.stop();
    m_backRight.stop();
  }

  @Override
  public boolean checkSubsystem() {
    status = m_frontLeft.checkSubsystem();
    status &= m_frontRight.checkSubsystem();
    status &= m_backLeft.checkSubsystem();
    status &= m_backRight.checkSubsystem();
    status &= getInitialized();

    return status;
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }
}
