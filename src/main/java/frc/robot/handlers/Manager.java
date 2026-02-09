// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.S_CPivot;
import frc.robot.subsystems.S_Climber;
import frc.robot.subsystems.S_IPivot;
import frc.robot.subsystems.S_Intake;
import frc.robot.subsystems.S_Shooter;
import frc.robot.subsystems.S_Turret;

public class Manager extends SubsystemBase implements CheckableSubsystem, StateSubsystem {
  private boolean initialized = false, status = false;

  private static Manager m_Instance;
  
  private ManagerStates desiredState, currentState = ManagerStates.IDLE;

  private S_Climber climber = S_Climber.getInstance();
  private S_CPivot cPivot = S_CPivot.getInstance();
  private S_Intake intake = S_Intake.getInstance();
  private S_IPivot iPivot = S_IPivot.getInstance();
  private S_Shooter shooter = S_Shooter.getInstance();
  private S_Turret turret = S_Turret.getInstance();
  
  /** Creates a new Manager. */
  private Manager() {
    initialized = climber.getInitialized();
    initialized &= cPivot.getInitialized();
    initialized &= intake.getInitialized();
    initialized &= iPivot.getInitialized();
    initialized &= shooter.getInitialized();
    initialized &= turret.getInitialized();
  }

  public static Manager getInstance() {
    if(m_Instance == null) {
      m_Instance = new Manager();
    }

    return m_Instance;
  }

  @Override
  public void stop() {
    climber.stop();
    cPivot.stop();
    intake.stop();
    iPivot.stop();
    shooter.stop();
    turret.stop();
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }

  @Override
  public boolean checkSubsystem() {
    status = climber.checkSubsystem();
    status &= cPivot.checkSubsystem();
    status &= intake.checkSubsystem();
    status &= iPivot.checkSubsystem();
    status &= shooter.checkSubsystem();
    status &= turret.checkSubsystem();

    return status;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (ManagerStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
        stop();

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

        break;

      default:
    
        break;
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public ManagerStates getState() {
    return currentState;
  }

  public Trigger bindState(Trigger button, ManagerStates onTrue, ManagerStates onFalse) {
    return button
      .onTrue(new InstantCommand(() -> setDesiredState(onTrue), this))
      .onFalse(new InstantCommand(() -> setDesiredState(onFalse), this));
  }

  public enum ManagerStates implements State {
    IDLE;
  }
}
