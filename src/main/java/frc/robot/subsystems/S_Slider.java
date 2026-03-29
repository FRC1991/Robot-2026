// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConstants;
import frc.robot.Constants.SliderConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;
import frc.utils.Utils.ElasticUtil;

public class S_Slider extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax motor;
  private SparkClosedLoopController posController;

  private double shakeSetpoint;
  
  private static S_Slider m_Instance;
  
  /** Creates a new S_Slider. */
  private S_Slider() {
    motor = new SparkMax(CANConstants.SLIDER_ID, MotorType.kBrushless);

    SparkMaxConfig motorConfig = new SparkMaxConfig();

    motorConfig.smartCurrentLimit(20).idleMode(IdleMode.kCoast)
      .closedLoop.p(0.04).i(0).d(0);

    motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    posController = motor.getClosedLoopController();

    motor.getEncoder().setPosition(0);

    ElasticUtil.putDouble("Slider Position", motor.getEncoder()::getPosition);

    initialized = true;
  }

  public static S_Slider getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Slider();
    }

    return m_Instance;
  }

  public void set(double setpoint) {
    posController.setSetpoint(setpoint, ControlType.kPosition);
  }

  public void shake() {
    if(posController.isAtSetpoint()) {
      if(shakeSetpoint == 0) {
        shakeSetpoint = -20;
      } else {
        shakeSetpoint = 0;
      }
    }

    posController.setSetpoint(shakeSetpoint, ControlType.kPosition);
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
