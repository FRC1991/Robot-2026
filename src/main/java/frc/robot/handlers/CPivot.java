// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_CPivot;

public class CPivot extends SubsystemBase implements StateSubsystem {
  private CPivotStates desiredState, currentState = CPivotStates.IDLE;
  private S_CPivot cPivot = S_CPivot.getInstance();

  private static CPivot m_Instance;
  
  /** Creates a new CPivot. */
  private CPivot() {}

  public static CPivot getInstance() {
    if(m_Instance == null) {
      m_Instance = new CPivot();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (CPivotStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        cPivot.stop();

        break;

      case HOME:
      case CLIMBING:

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
      case CLIMBING:

        break;

      default:
      
        break;
    }

    if(!cPivot.checkSubsystem()) {
      setDesiredState(CPivotStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public CPivotStates getState() {
    return currentState;
  }

  public enum CPivotStates implements State {
    IDLE,
    BROKEN,
    HOME,
    CLIMBING;
  }
}
