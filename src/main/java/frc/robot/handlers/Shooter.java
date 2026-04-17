// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.S_Shooter;
import frc.robot.subsystems.S_Swerve;
import frc.utils.LimelightHelpers;
import frc.utils.LimelightHelpers.RawFiducial;
import frc.utils.Utils.ElasticUtil;

public class Shooter extends SubsystemBase implements StateSubsystem {
  private ShooterStates desiredState, currentState = ShooterStates.IDLE;
  private S_Shooter shooter = S_Shooter.getInstance();

  private InterpolatingDoubleTreeMap shootMap1;
  private InterpolatingDoubleTreeMap shootMap2;

  private static Shooter m_Instance;

  private double indexSpeed = 0;

  private double shootSpeed1 = 1625, shootSpeed2 = 1625;
  
  /** Creates a new Shooter. */
  private Shooter() {
    ElasticUtil.putDouble("Shoot One", () -> shootSpeed1, (double speed) -> shootSpeed1 = speed);
    ElasticUtil.putDouble("Shoot Two", () -> shootSpeed2, (double speed) -> shootSpeed2 = speed);

    ElasticUtil.putDouble("Index Speed", () -> indexSpeed);

    ElasticUtil.putDouble("Distance", this::getAverageTagDist);
    ElasticUtil.putDouble("TX", () -> LimelightHelpers.getTX(Constants.LIMELIGHT_NAME));
    ElasticUtil.putDouble("TY", () -> LimelightHelpers.getTY(Constants.LIMELIGHT_NAME));

    ElasticUtil.putDouble("Tag Count", () -> LimelightHelpers.getTargetCount(Constants.LIMELIGHT_NAME));

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
        // if(DriverStation.isAutonomous()) {
        //   shooter.set(ShooterConstants.SHOOTER_SPEED * 0.75, -ShooterConstants.SHOOTER_SPEED * 0.75, ShooterConstants.INDEXER_SPEED);
        // } else {
        //   shooter.set(ShooterConstants.SHOOTER_SPEED, -ShooterConstants.SHOOTER_SPEED, ShooterConstants.INDEXER_SPEED);

        // }

        shooter.set(shootSpeed1, -shootSpeed2, ShooterConstants.INDEXER_SPEED);

        break;

      case PASSING:
        // shooter.set(ShooterConstants.SHOOTER_SPEED, -ShooterConstants.SHOOTER_SPEED, indexSpeed);
        shooter.set(0, -ShooterConstants.PASSING_SPEED, ShooterConstants.INDEXER_SPEED);

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

  private double getAverageTagDist() {
    RawFiducial[] aprilTags = LimelightHelpers.getRawFiducials(Constants.LIMELIGHT_NAME);

    double sum = 0;

    for(RawFiducial tag : aprilTags) {
      sum += tag.distToRobot;
    }

    if(aprilTags.length == 0) {
      return -15;
    }

    return sum / aprilTags.length;
  }

  public void incSpeed(double speed) {
    shootSpeed1 += speed;
    shootSpeed2 += speed;
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
