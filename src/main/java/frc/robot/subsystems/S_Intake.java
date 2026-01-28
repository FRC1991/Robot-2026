// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

public class S_Intake extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax motor;
  
  private static S_Intake m_Instance;
  
  /** Creates a new S_Intake. */
  private S_Intake() {
    motor = new SparkMax(99997, MotorType.kBrushless);

    initialized = true;
  }

  public static S_Intake getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Intake();
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
