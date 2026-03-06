// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_Claw;

public class Claw extends SubsystemBase implements StateSubsystem {
  private ClawStates desiredState, currentState = ClawStates.IDLE;
  private S_Claw claw = S_Claw.getInstance();

  private static Claw m_Instance;
  
  /** Creates a new Claw. */
  private Claw() {}

  public static Claw getInstance() {
    if(m_Instance == null) {
      m_Instance = new Claw();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (ClawStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        claw.stop();

        break;
      
      case HOME:
      case HOLDING:

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
      case BROKEN:
      case HOME:
      case HOLDING:

        break;

      default:

        break;
    }

    if(!claw.checkSubsystem()) {
      setDesiredState(ClawStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public ClawStates getState() {
    return currentState;
  }

  public enum ClawStates implements State {
    IDLE,
    BROKEN,
    HOME,
    HOLDING;
  }
}
