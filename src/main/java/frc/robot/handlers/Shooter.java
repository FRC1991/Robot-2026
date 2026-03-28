// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.S_Shooter;
import frc.robot.subsystems.S_Swerve;
import frc.utils.Utils.ElasticUtil;

public class Shooter extends SubsystemBase implements StateSubsystem {
  private ShooterStates desiredState, currentState = ShooterStates.IDLE;
  private S_Shooter shooter = S_Shooter.getInstance();

  private InterpolatingDoubleTreeMap shootMap1;
  private InterpolatingDoubleTreeMap shootMap2;

  private static Shooter m_Instance;

  private double indexSpeed = 0;
  
  /** Creates a new Shooter. */
  private Shooter() {
    ElasticUtil.putDouble("Index Speed", () -> indexSpeed);

    // TODO: FIND VALUES AND ADD WITH .put()
    shootMap1 = new InterpolatingDoubleTreeMap();

    shootMap2 = new InterpolatingDoubleTreeMap();
  }

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
      case PASSING:
      case BACKSPINNING:
        CommandScheduler.getInstance().schedule(
          new WaitCommand(0.5)
            .andThen(new InstantCommand(() -> indexSpeed = ShooterConstants.INDEXER_SPEED))
            .andThen(new WaitCommand(0.25))
            .andThen(new InstantCommand(() -> indexSpeed = 0)).repeatedly()
        );

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
        // shooter.set(shootMap1.get(S_Swerve.getInstance().getDistToHub()), shootMap2.get(S_Swerve.getInstance().getDistToHub()), indexSpeed);
        shooter.set(ShooterConstants.SHOOTER_SPEED, -ShooterConstants.SHOOTER_SPEED, indexSpeed);

        break;

      case PASSING:
        // shooter.set(ShooterConstants.SHOOTER_SPEED, -ShooterConstants.SHOOTER_SPEED, indexSpeed);
        shooter.set(0, -ShooterConstants.PASSING_SPEED, indexSpeed);

        break;

      case BACKSPINNING:
        shooter.set(0.5, -0.3, indexSpeed);

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
    SHOOTING,
    PASSING,
    BACKSPINNING;
  }
}
