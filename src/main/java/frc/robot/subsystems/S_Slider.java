// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;
import frc.utils.Utils.ElasticUtil;

public class S_Slider extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax motor;

  private PIDController posController;
  
  private static S_Slider m_Instance;
  
  /** Creates a new S_IPivot. */
  private S_Slider() {
    motor = new SparkMax(CANConstants.SLIDER_ID, MotorType.kBrushless);

    ElasticUtil.putDouble("Slider Position", motor.getEncoder()::getPosition);

    initialized = true;
  }

  public static S_Slider getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Slider();
    }

    return m_Instance;
  }

  public void set(double speed, double setpoint) {
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
