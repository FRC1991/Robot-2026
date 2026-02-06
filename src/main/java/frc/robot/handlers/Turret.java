// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_Turret;

public class Turret extends SubsystemBase implements StateSubsystem {
  private TurretStates desiredState, currentState = TurretStates.IDLE;
  private S_Turret turret = S_Turret.getInstance();

  private static Turret m_Instance;
  
  /** Creates a new Turret. */
  private Turret() {}

  public static Turret getInstance() {
    if(m_Instance == null) {
      m_Instance = new Turret();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (TurretStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        turret.stop();

        break;

      case SHOOTING:
      case PASSING:

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
      case SHOOTING:
      case PASSING:

        break;

      default:

        break;
    }

    if(!turret.checkSubsystem()) {
      setDesiredState(TurretStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public TurretStates getState() {
    return currentState;
  }

  public enum TurretStates implements State {
    IDLE,
    BROKEN,
    SHOOTING,
    PASSING;
  }
}
