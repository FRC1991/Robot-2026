// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

public class S_Turret extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private TalonFX motor;

  private PIDController angleController;
  
  private static S_Turret m_Instance;
  
  /** Creates a new S_Turret. */
  private S_Turret() {
    motor = new TalonFX(CANConstants.TURRET_ID);

    angleController = new PIDController(0.01, 0, 0);

    initialized = true;
  }

  public static S_Turret getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Turret();
    }

    return m_Instance;
  }

  public void set(double setpoint) {
    motor.set(Utils.normalize(angleController.calculate(S_Swerve.getInstance().getTurretEncoder().getPosition(), setpoint)));
  }

  @Override
  public void stop() {
    motor.stopMotor();
  }

  @Override
  public boolean getInitialized() {
    return initialized;
  }

  @Override
  public boolean checkSubsystem() {
    status = getInitialized();

    return status;
  }
}
