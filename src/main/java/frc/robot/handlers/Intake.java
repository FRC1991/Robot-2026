// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.S_Intake;

public class Intake extends SubsystemBase implements StateSubsystem {
  private IntakeStates desiredState, currentState = IntakeStates.IDLE;
  private S_Intake intake = S_Intake.getInstance();

  private static Intake m_Instance;
  
  /** Creates a new Intake. */
  private Intake() {}

  public static Intake getInstance() {
    if(m_Instance == null) {
      m_Instance = new Intake();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (IntakeStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        intake.stop();

        break;

      case INTAKING:
      case OUTTAKING:

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
      // case OUTTAKING:

        break;

      case INTAKING:
        intake.set(IntakeConstants.INTAKE_SPEED);

        break;

      default:
      
        break;
    }

    if(!intake.checkSubsystem()) {
      setDesiredState(IntakeStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public IntakeStates getState() {
    return currentState;
  }

  public enum IntakeStates implements State {
    IDLE,
    BROKEN,
    INTAKING,
    OUTTAKING;
  }
}
