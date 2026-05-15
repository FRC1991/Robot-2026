// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

import java.util.function.BooleanSupplier;

import org.ejml.interfaces.decomposition.SingularValueDecomposition;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.handlers.Hopper.HopperStates;
import frc.robot.handlers.Intake.IntakeStates;
// // import frc.robot.handlers.Intake.IntakeStates;
import frc.robot.handlers.Shooter.ShooterStates;
import frc.robot.handlers.Slider.SliderStates;
import frc.robot.subsystems.S_Hopper;
import frc.robot.subsystems.S_Intake;
// // import frc.robot.handlers.Turret.TurretStates;
// import frc.robot.subsystems.S_Intake;
import frc.robot.subsystems.S_Shooter;
// import frc.robot.subsystems.S_Turret;
import frc.robot.subsystems.S_Slider;

public class Manager extends SubsystemBase implements CheckableSubsystem, StateSubsystem {
  private boolean initialized = false, status = false;

  private static Manager m_Instance;
  
  private ManagerStates desiredState, currentState = ManagerStates.IDLE;

  private S_Intake intake = S_Intake.getInstance();
  private S_Slider slider = S_Slider.getInstance();
  private S_Shooter shooter = S_Shooter.getInstance();
  private S_Hopper hopper = S_Hopper.getInstance();
  
  /** Creates a new Manager. */
  private Manager() {
    Intake.getInstance();
    Slider.getInstance();
    Shooter.getInstance();
    Hopper.getInstance();
    
    initialized = intake.getInitialized();
    initialized &= slider.getInitialized();
    initialized &= shooter.getInitialized();
    initialized &= hopper.getInitialized();
  }

  public static Manager getInstance() {
    if(m_Instance == null) {
      m_Instance = new Manager();
    }

    return m_Instance;
  }

  @Override
  public void stop() {
    intake.stop();
    slider.stop();
    shooter.stop();
    hopper.stop();
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }

  @Override
  public boolean checkSubsystem() {
    status = intake.checkSubsystem();
    status &= slider.checkSubsystem();
    status &= shooter.checkSubsystem();
    status &= hopper.checkSubsystem();

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
        Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        Slider.getInstance().setDesiredState(SliderStates.IDLE);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.IDLE);

        break;

      case DRIVING:
        Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        Slider.getInstance().setDesiredState(SliderStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.IDLE);

        break;

      case SHOOTING:
        Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        Slider.getInstance().setDesiredState(SliderStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.SHOOTING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case PASSING:
        Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        Slider.getInstance().setDesiredState(SliderStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.PASSING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case BACKSPINNING:
        Intake.getInstance().setDesiredState(IntakeStates.IDLE);
        Slider.getInstance().setDesiredState(SliderStates.HOME);
        Shooter.getInstance().setDesiredState(ShooterStates.BACKSPINNING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case SHAKING:
        Intake.getInstance().setDesiredState(IntakeStates.INTAKING);
        Slider.getInstance().setDesiredState(SliderStates.SHAKING);
        Shooter.getInstance().setDesiredState(ShooterStates.SHOOTING);
        Hopper.getInstance().setDesiredState(HopperStates.RUNNING);

        break;

      case INTAKING:
        Intake.getInstance().setDesiredState(IntakeStates.INTAKING);
        Slider.getInstance().setDesiredState(SliderStates.INTAKING);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.IDLE);

        break;

      case OUTTAKING:
        Intake.getInstance().setDesiredState(IntakeStates.OUTTAKING);
        Slider.getInstance().setDesiredState(SliderStates.INTAKING);
        Shooter.getInstance().setDesiredState(ShooterStates.IDLE);
        Hopper.getInstance().setDesiredState(HopperStates.OUTTAKING);

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
      case SHAKING:
      case OUTTAKING:
      
        break;

      default:
    
        break;
    }
  }

  public void takeInput(BooleanSupplier supplier, Command firstSection, Command secondSection){
    Timer timer = new Timer();
    if (timer.hasElapsed(2)){
      CommandScheduler.getInstance().cancel(firstSection);
      CommandScheduler.getInstance().schedule(secondSection);
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
    SHAKING,
    INTAKING,
    OUTTAKING;
  }
}
