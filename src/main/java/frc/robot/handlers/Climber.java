// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimberConstants;
import frc.robot.subsystems.S_Climber;

public class Climber extends SubsystemBase implements StateSubsystem {
  private ClimberStates desiredState, currentState = ClimberStates.IDLE;
  private S_Climber climber = S_Climber.getInstance();
  
  private static Climber m_Instance;
  
  /** Creates a new Climber. */
  private Climber() {}

  public static Climber getInstance() {
    if(m_Instance == null) {
      m_Instance = new Climber();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (ClimberStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        climber.stop();

        break;

      case HOME:
      case CLIMBING:
      case RETURNING:

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

        break;

      case CLIMBING:
        climber.set(ClimberConstants.CLIMBER_SPEED);

        break;

      case RETURNING:
        climber.set(-ClimberConstants.CLIMBER_SPEED);

        break;

      default:
      
        break;
    }

    if(!climber.checkSubsystem()) {
      setDesiredState(ClimberStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public ClimberStates getState() {
    return currentState;
  }

  public enum ClimberStates implements State {
    IDLE,
    BROKEN,
    HOME,
    CLIMBING,
    RETURNING;
  }
}
