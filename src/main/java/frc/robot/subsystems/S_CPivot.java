// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

public class S_CPivot extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private TalonFX motor;
  
  private static S_CPivot m_Instance;
  
  /** Creates a new S_CPivot. */
  private S_CPivot() {
    motor = new TalonFX(99995);

    initialized = true;
  }

  public static S_CPivot getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_CPivot();
    }

    return m_Instance;
  }

  public void set(double speed) {
    motor.set(Utils.normalize(speed));
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
