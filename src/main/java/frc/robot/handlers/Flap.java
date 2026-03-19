// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.S_Flap;
import frc.robot.subsystems.S_Swerve;

public class Flap extends SubsystemBase implements StateSubsystem {
  private FlapStates desiredState, currentState = FlapStates.IDLE;
  private S_Flap flap = S_Flap.getInstance();

  private InterpolatingDoubleTreeMap angleMap;

  private static Flap m_Instance;

  /** Creates a new Flap. */
  private Flap() {
    // TODO: FIND VALUES AND ADD WITH .put()
    angleMap = new InterpolatingDoubleTreeMap();
  }

  public static Flap getInstance() {
    if(m_Instance == null) {
      m_Instance = new Flap();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (FlapStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        flap.stop();

        break;

      case MOVING:

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

      case MOVING:
        flap.set(angleMap.get(S_Swerve.getInstance().getDistToHub()));

        break;

      default:

        break;
    }

    if(!flap.checkSubsystem()) {
      setDesiredState(FlapStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public FlapStates getState() {
    return currentState;
  }

  public enum FlapStates implements State {
    IDLE,
    BROKEN,
    MOVING;
  }
}
