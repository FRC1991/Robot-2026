// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.handlers.Hopper;
import frc.robot.handlers.Intake;
import frc.robot.handlers.Manager;
import frc.robot.handlers.Swerve;
import frc.robot.handlers.Swerve.SwerveStates;
import frc.robot.subsystems.S_Swerve;
import frc.utils.Utils.ElasticUtil;
import frc.robot.handlers.Manager.ManagerStates;
import frc.robot.handlers.Shooter;
import frc.robot.handlers.Slider;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private boolean useShootAuto = true;
  
  public RobotContainer() {
    configureBindings();
    configureElastic();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    Manager.getInstance().bindState(OI.auxController.rightBumper(), ManagerStates.SHOOTING, ManagerStates.DRIVING);

    Manager.getInstance().bindState(OI.auxController.leftBumper(), ManagerStates.PASSING, ManagerStates.DRIVING);

    Manager.getInstance().bindState(OI.auxController.y(), ManagerStates.BACKSPINNING, ManagerStates.DRIVING);

    Manager.getInstance().bindState(OI.auxController.x(), ManagerStates.INTAKING, ManagerStates.DRIVING);
    
    Manager.getInstance().bindState(OI.auxController.b(), ManagerStates.OUTTAKING, ManagerStates.DRIVING);

    Swerve.getInstance().bindState(OI.driverController.a(), SwerveStates.AIMING, SwerveStates.DRIVING);

    Swerve.getInstance().bindState(OI.driverController.x(), SwerveStates.LOCKED, SwerveStates.DRIVING);

    OI.driverController.leftBumper()
      .onTrue(new InstantCommand(() -> S_Swerve.getInstance().setHeading(0), Swerve.getInstance()));
  }

  private void configureElastic() {
    WebServer.start(5800, Filesystem.getDeployDirectory().getPath());

    ElasticUtil.putString("Manager State", () -> Manager.getInstance().getState().toString());
    ElasticUtil.putString("Shooter State", () -> Shooter.getInstance().getState().toString());
    ElasticUtil.putString("Hopper State", () -> Hopper.getInstance().getState().toString());
    ElasticUtil.putString("Intake State", () -> Intake.getInstance().getState().toString());
    ElasticUtil.putString("Slider State", () -> Slider.getInstance().getState().toString());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    if(useShootAuto) {
      return Commands.sequence(
        new WaitCommand(2.0),
        new InstantCommand(() -> Manager.getInstance().setDesiredState(ManagerStates.SHOOTING)),
        new WaitCommand(15.5),
        new InstantCommand(() -> Manager.getInstance().setDesiredState(ManagerStates.DRIVING))
      );
    }

    return Commands.print("No auto configured");
  }
}
