// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.SliderConstants;
import frc.robot.subsystems.S_Slider;

public class Slider extends SubsystemBase implements StateSubsystem {
  private SliderStates desiredState, currentState = SliderStates.IDLE;
  private S_Slider slider = S_Slider.getInstance();

  private static Slider m_Instance;
  
  /** Creates a new IPivot. */
  private Slider() {}

  public static Slider getInstance() {
    if(m_Instance == null) {
      m_Instance = new Slider();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (SliderStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        slider.stop();

        break;

      case HOME:
        // slider.set(SliderConstants.HOME_POSITION);

        break;
      
      case INTAKING:
        // slider.set(SliderConstants.INTAKING_POSITION);

        break;

      case SHAKING:

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
      
      case HOME:
        slider.set(SliderConstants.HOME_POSITION);

        break;
      
      case INTAKING:
        slider.set(SliderConstants.INTAKING_POSITION);

        break;

      case SHAKING:  
        slider.shake();

        break;

      default:

        break;
    }

    if(!slider.checkSubsystem()) {
      setDesiredState(SliderStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public SliderStates getState() {
    return currentState;
  }

  public enum SliderStates implements State {
    IDLE,
    BROKEN,
    HOME,
    INTAKING,
    SHAKING;
  }
}
