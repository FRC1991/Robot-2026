// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.utils.Utils;

public class S_Intake extends SubsystemBase {
  private SparkMax motor;
  
  private static S_Intake m_Instance;
  
  /** Creates a new S_Intake. */
  private S_Intake() {
    motor = new SparkMax(99997, MotorType.kBrushless);
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
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
