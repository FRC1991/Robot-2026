// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class S_Climber extends SubsystemBase {
  private TalonFX motor1;
  private TalonFX motor2;
  
  private static S_Climber m_Instance;
  
  /** Creates a new S_Climber. */
  private S_Climber() {
    motor1 = new TalonFX(99999);
    motor2 = new TalonFX(99998);
  }

  public static S_Climber getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Climber();
    }

    return m_Instance;
  }

  public void set(double speed) {
    motor1.set(speed); // TODO: Normalize this
    motor2.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
