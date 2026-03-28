// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.handlers.Hopper.HopperStates;
// // import frc.robot.handlers.Intake.IntakeStates;
import frc.robot.handlers.Shooter.ShooterStates;
import frc.robot.subsystems.S_Hopper;
// // import frc.robot.handlers.Turret.TurretStates;
// import frc.robot.subsystems.S_Intake;
import frc.robot.subsystems.S_Shooter;
// import frc.robot.subsystems.S_Turret;

public class Manager extends SubsystemBase implements CheckableSubsystem, StateSubsystem {
  private boolean initialized = false, status = false;

  private static Manager m_Instance;
  
  private ManagerStates desiredState, currentState = ManagerStates.IDLE;

  // private S_Intake intake = S_Intake.getInstance();
  private S_Shooter shooter = S_Shooter.getInstance();
  private S_Hopper hopper = S_Hopper.getInstance();
  
  /** Creates a new Manager. */
  private Manager() {
    // Intake.getInstance();
    Shooter.getInstance();
    Hopper.getInstance();
    
    // initialized &= intake.getInitialized();
    initialized = shooter.getInitialized();
  }

  public static Manager getInstance() {
    if(m_Instance == null) {
      m_Instance = new Manager();
    }

    return m_Instance;
  }

  @Override
  public void stop() {
    // intake.stop();
    shooter.stop();
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }

  @Override
  public boolean checkSubsystem() {
    // status &= intake.checkSubsystem();
    status = shooter.checkSubsystem();

    return status;
  }

  @Override
  public void setDesiredState(State state) {
    if(desiredState != state) {
      desiredState = (ManagerStates) state;
      handleStateTransition();
    }
  }

  @Override
  public void handleStateTransition() {
    switch(desiredState) {
      case IDLE:
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.IDLE);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.IDLE);

        break;

      case DRIVING:
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.IDLE);

        break;

      case SHOOTING:
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.SHOOTING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case PASSING:
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.PASSING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case BACKSPINNING:
        Shooter.getInstance().setDesiredState(ShooterStates.BACKSPINNING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

      case INTAKING:
        // Intake.getInstance().setDesiredState(IntakeStates.INTAKING);
        // IPivot.getInstance().setDesiredState(IPivotStates.INTAKING);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);

        break;

      case OUTTAKING:
        // Intake.getInstance().setDesiredState(IntakeStates.OUTTAKING);
        // IPivot.getInstance().setDesiredState(IPivotStates.INTAKING);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);

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
        setDesiredState(ManagerStates.DRIVING);

        break;

      case DRIVING:
      case SHOOTING:
      case PASSING:
      case BACKSPINNING:
      case INTAKING:
      case OUTTAKING:
      
        break;

      default:
    
        break;
    }
  }

  @Override
  public void periodic() {
    update();
  }

  public ManagerStates getState() {
    return currentState;
  }

  public Trigger bindState(Trigger button, ManagerStates onTrue, ManagerStates onFalse) {
    return button
      .onTrue(new InstantCommand(() -> setDesiredState(onTrue), this))
      .onFalse(new InstantCommand(() -> setDesiredState(onFalse), this));
  }

  public enum ManagerStates implements State {
    IDLE,
    DRIVING,
    SHOOTING,
    PASSING,
    BACKSPINNING,
    INTAKING,
    OUTTAKING;
  }
}
