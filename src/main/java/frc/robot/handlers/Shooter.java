// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.S_Shooter;

public class Shooter extends SubsystemBase implements StateSubsystem {
  private ShooterStates desiredState, currentState = ShooterStates.IDLE;
  private S_Shooter shooter = S_Shooter.getInstance();

  private static Shooter m_Instance;

  private double indexSpeed = 0;
  
  /** Creates a new Shooter. */
  private Shooter() {}

  public static Shooter getInstance() {
    if(m_Instance == null) {
      m_Instance = new Shooter();
    }

    return m_Instance;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (ShooterStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
      case BROKEN:
        CommandScheduler.getInstance().cancelAll();
        indexSpeed = 0;
        shooter.stop();

        break;

      case SHOOTING:
        CommandScheduler.getInstance().schedule(
          new WaitCommand(0.5)
            .andThen(new InstantCommand(() -> indexSpeed = ShooterConstants.INDEXER_SPEED))
            .andThen(new WaitCommand(0.25))
            .andThen(new InstantCommand(() -> indexSpeed = 0)).repeatedly()
        );

        // CommandScheduler.getInstance().schedule(
        //   new WaitCommand(0.25)
        //     .andThen(new InstantCommand(() -> indexSpeed = ShooterConstants.INDEXER_SPEED))
        // );

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

      case SHOOTING:
        shooter.set(ShooterConstants.SHOOTER_SPEED, indexSpeed);

        break;

      default:

        break;
    }

    if(!shooter.checkSubsystem()) {
      setDesiredState(ShooterStates.BROKEN);
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public ShooterStates getState() {
    return currentState;
  }

  public enum ShooterStates implements State {
    IDLE,
    BROKEN,
    SHOOTING;
  }
}
