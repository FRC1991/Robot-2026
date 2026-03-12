// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
// import frc.robot.handlers.Claw.ClawStates;
// import frc.robot.handlers.Climber.ClimberStates;
// // import frc.robot.handlers.IPivot.IPivotStates;
// // import frc.robot.handlers.Intake.IntakeStates;
import frc.robot.handlers.Shooter.ShooterStates;
// // import frc.robot.handlers.Turret.TurretStates;
// import frc.robot.subsystems.S_Claw;
// import frc.robot.subsystems.S_Climber;
// import frc.robot.subsystems.S_IPivot;
// import frc.robot.subsystems.S_Intake;
import frc.robot.subsystems.S_Shooter;
// import frc.robot.subsystems.S_Turret;

public class Manager extends SubsystemBase implements CheckableSubsystem, StateSubsystem {
  private boolean initialized = false, status = false;

  private static Manager m_Instance;
  
  private ManagerStates desiredState, currentState = ManagerStates.IDLE;

  // private S_Climber climber = S_Climber.getInstance();
  // private S_Claw claw = S_Claw.getInstance();
  // private S_Intake intake = S_Intake.getInstance();
  // private S_IPivot iPivot = S_IPivot.getInstance();
  private S_Shooter shooter = S_Shooter.getInstance();
  // private S_Turret turret = S_Turret.getInstance();
  
  /** Creates a new Manager. */
  private Manager() {
    // Climber.getInstance();
    // Claw.getInstance();
    // Intake.getInstance();
    // IPivot.getInstance();
    Shooter.getInstance();
    // Turret.getInstance();
    
    // initialized = climber.getInitialized();
    // initialized &= claw.getInitialized();
    // initialized &= intake.getInitialized();
    // initialized &= iPivot.getInitialized();
    initialized &= shooter.getInitialized();
    // initialized &= turret.getInitialized();
  }

  public static Manager getInstance() {
    if(m_Instance == null) {
      m_Instance = new Manager();
    }

    return m_Instance;
  }

  @Override
  public void stop() {
    // climber.stop();
    // claw.stop();
    // intake.stop();
    // iPivot.stop();
    shooter.stop();
    // turret.stop();
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }

  @Override
  public boolean checkSubsystem() {
    // status = climber.checkSubsystem();
    // status &= claw.checkSubsystem();
    // status &= intake.checkSubsystem();
    // status &= iPivot.checkSubsystem();
    status &= shooter.checkSubsystem();
    // status &= turret.checkSubsystem();

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
        // Climber.getInstance().setDesiredState(ClimberStates.IDLE);
        // Claw.getInstance().setDesiredState(ClawStates.IDLE);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.IDLE);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        // Turret.getInstance().setDesiredState(TurretStates.IDLE);

        break;

      case DRIVING:
        // Climber.getInstance().setDesiredState(ClimberStates.HOME);
        // Claw.getInstance().setDesiredState(ClawStates.HOME);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        // Turret.getInstance().setDesiredState(TurretStates.IDLE);

        break;

      case SHOOTING:
        // Climber.getInstance().setDesiredState(ClimberStates.HOME);
        // Claw.getInstance().setDesiredState(ClawStates.HOME);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.SHOOTING);
        // Turret.getInstance().setDesiredState(TurretStates.SHOOTING);

        break;

      case PASSING:
        // Climber.getInstance().setDesiredState(ClimberStates.HOME);
        // Claw.getInstance().setDesiredState(ClawStates.HOME);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.SHOOTING); // Subject to change
        // Turret.getInstance().setDesiredState(TurretStates.PASSING);

        break;

      // case INTAKING:
      //   Climber.getInstance().setDesiredState(ClimberStates.HOME);
      //   // Intake.getInstance().setDesiredState(IntakeStates.INTAKING);
      //   // IPivot.getInstance().setDesiredState(IPivotStates.INTAKING);
      //   Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
      //   // Turret.getInstance().setDesiredState(TurretStates.IDLE);

      //   break;

      // case OUTTAKING:
      //   Climber.getInstance().setDesiredState(ClimberStates.HOME);
      //   // Intake.getInstance().setDesiredState(IntakeStates.OUTTAKING);
      //   // IPivot.getInstance().setDesiredState(IPivotStates.INTAKING);
        // Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
      //   // Turret.getInstance().setDesiredState(TurretStates.IDLE);

      //   break;

      case CLIMBING:
        // Climber.getInstance().setDesiredState(ClimberStates.CLIMBING);
        // Claw.getInstance().setDesiredState(ClawStates.HOLDING);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        // Turret.getInstance().setDesiredState(TurretStates.IDLE);

        break;

      case LOWERING:
        // Climber.getInstance().setDesiredState(ClimberStates.RETURNING);
        // Claw.getInstance().setDesiredState(ClawStates.HOLDING);
        // Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        // IPivot.getInstance().setDesiredState(IPivotStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        // Turret.getInstance().setDesiredState(TurretStates.IDLE);

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
      // case INTAKING:
      // case OUTTAKING:
      case CLIMBING:
      case LOWERING:
      
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
    // INTAKING,
    // OUTTAKING,
    CLIMBING,
    LOWERING;
  }
}
