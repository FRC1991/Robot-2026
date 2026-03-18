// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_Hopper;

public class Hopper extends SubsystemBase implements StateSubsystem {
  private HopperStates desiredState, currentState = HopperStates.IDLE;
  private S_Hopper hopper = S_Hopper.getInstance();

  private static Hopper m_Instance;
  
  /** Creates a new Hopper. */
  private Hopper() {}

  public static Hopper getInstance() {
    if(m_Instance == null) {
      m_Instance = new Hopper();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (HopperStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        hopper.stop();

        break;

      case RUNNING:

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

        break;
         
      case RUNNING:

        break;

      default:

        break;
    }

    if(!hopper.checkSubsystem()) {
      setDesiredState(HopperStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }
  
  public HopperStates getState() {
    return currentState;
  }

  public enum HopperStates implements State {
    IDLE,
    BROKEN,
    RUNNING;
  }
}
