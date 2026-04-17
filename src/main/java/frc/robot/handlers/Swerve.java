// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.OI;
import frc.robot.Constants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.subsystems.S_Swerve;
import frc.utils.LimelightHelpers;
import frc.utils.Utils;
import frc.utils.Utils.ElasticUtil;

public class Swerve extends SubsystemBase implements StateSubsystem {
  private SwerveStates desiredState, currentState = SwerveStates.IDLE;
  private S_Swerve swerve = S_Swerve.getInstance();
  private static Swerve m_Instance;

  private PIDController rotController = new PIDController(0.05, 0, 0);

  private double rotOffset = 0;

  private double autoXSpeed = 0, autoYSpeed = 0, autoRotSpeed = 0;
  
  /** Creates a new Swerve. */
  private Swerve() {
    rotController.setTolerance(1);
    rotController.setSetpoint(0);

    ElasticUtil.putDouble("Rot Offset", () -> rotOffset);
  }

  public static Swerve getInstance() {
    if(m_Instance == null) {
      m_Instance = new Swerve();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (SwerveStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        swerve.stop();

        break;

      case DRIVING:
      case AUTODRIVING:
      case AIMING:

        break;

      case LOCKED:
        swerve.setX();

        break;

      default:

        break;
    }

    currentState = desiredState;
  }

  @Override
  public void update() {
    switch(currentState) {
      case IDLE:
        setDesiredState(SwerveStates.DRIVING);

        break;
      
      case BROKEN:

        break;

      case DRIVING:
        if(DriverStation.isTeleopEnabled()) {
          drive();
        }

        break;

      case AUTODRIVING:
        swerve.drive(autoXSpeed, autoYSpeed, autoRotSpeed, true, SwerveConstants.SPEED_SCALE);

        break;

      case AIMING:
        if(LimelightHelpers.getTV(Constants.LIMELIGHT_NAME)) {
          rotOffset = LimelightHelpers.getTX(Constants.LIMELIGHT_NAME);

          swerve.drive(
            MathUtil.applyDeadband(OI.driverController.getLeftY(), SwerveConstants.DRIVING_DEADBAND),
            MathUtil.applyDeadband(OI.driverController.getLeftX(), SwerveConstants.DRIVING_DEADBAND),
            Utils.normalize(rotController.calculate(rotOffset)),
            true, SwerveConstants.SPEED_SCALE
          );
        } else {
          drive();
        }

        // swerve.drive(
        //   MathUtil.applyDeadband(OI.driverController.getLeftY(), SwerveConstants.DRIVING_DEADBAND),
        //   MathUtil.applyDeadband(OI.driverController.getLeftX(), SwerveConstants.DRIVING_DEADBAND),
        //   Utils.normalize(rotController.calculate(S_Swerve.getInstance().getAngleToHub())),
        //   true, SwerveConstants.SPEED_SCALE
        // );
        
        break;

      case LOCKED:

        break;

      default:

        break;
    }
  }

  private void drive() {
    swerve.drive(
      MathUtil.applyDeadband(OI.driverController.getLeftY(), SwerveConstants.DRIVING_DEADBAND),
      MathUtil.applyDeadband(OI.driverController.getLeftX(), SwerveConstants.DRIVING_DEADBAND),
      MathUtil.applyDeadband(OI.driverController.getRightX(), SwerveConstants.DRIVING_DEADBAND),
      true, SwerveConstants.SPEED_SCALE
    );
  }

  public void setAutoSpeed(double xSpeed, double ySpeed, double rotSpeed) {
    autoXSpeed = xSpeed;
    autoYSpeed = ySpeed;
    autoRotSpeed = rotSpeed;
  }

  @Override
  public void periodic() {
    update();
  }

  public Trigger bindState(Trigger button, SwerveStates onTrue, SwerveStates onFalse) {
    return button
      .onTrue(new InstantCommand(() -> setDesiredState(onTrue), m_Instance))
      .onFalse(new InstantCommand(() -> setDesiredState(onFalse), m_Instance));
  }

  public SwerveStates getState() {
    return currentState;
  }

  public enum SwerveStates implements State {
    IDLE,
    BROKEN,
    DRIVING,
    AUTODRIVING,
    AIMING,
    LOCKED;
  }
}
