// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_IPivot;

public class IPivot extends SubsystemBase implements StateSubsystem {
  private IPivotStates desiredState, currentState = IPivotStates.IDLE;
  private S_IPivot iPivot = S_IPivot.getInstance();

  private static IPivot m_Instance;
  
  /** Creates a new IPivot. */
  private IPivot() {}

  public static IPivot getInstance() {
    if(m_Instance == null) {
      m_Instance = new IPivot();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (IPivotStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        iPivot.stop();

        break;

      case HOME:
      case INTAKING:

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
      case INTAKING:

        break;

      default:

        break;
    }

    if(!iPivot.checkSubsystem()) {
      setDesiredState(IPivotStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public IPivotStates getState() {
    return currentState;
  }

  public enum IPivotStates implements State {
    IDLE,
    BROKEN,
    HOME,
    INTAKING;
  }
}
