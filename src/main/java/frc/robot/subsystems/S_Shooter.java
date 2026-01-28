// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class S_Shooter extends SubsystemBase {
  private SparkMax motor;
  
  private static S_Shooter m_Instance;
  
  /** Creates a new S_Shooter. */
  private S_Shooter() {
    motor = new SparkMax(99994, MotorType.kBrushless);
  }

  public static S_Shooter getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Shooter();
    }

    return m_Instance;
  }

  public void set(double speed) {
    motor.set(speed); // TODO: Normalize this
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
