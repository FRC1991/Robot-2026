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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConstants;
import frc.robot.Constants.SliderConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;
import frc.utils.Utils.ElasticUtil;

public class S_Slider extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax motor1, motor2;
  private SparkClosedLoopController posController1, posController2;
  
  private double shakeSetpoint;
  
  private static S_Slider m_Instance;
  
  /** Creates a new S_Slider. */
  private S_Slider() {
    motor1 = new SparkMax(CANConstants.SLIDER_ONE_ID, MotorType.kBrushed);
    motor2 = new SparkMax(CANConstants.SLIDER_TWO_ID, MotorType.kBrushed);

    SparkMaxConfig motorConfig = new SparkMaxConfig();

    motorConfig.smartCurrentLimit(30).idleMode(IdleMode.kCoast).inverted(true)
      .closedLoop.p(0.25).i(0).d(0);
      // .feedForward.kS(0.5);

    motor1.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    motor2.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    posController1 = motor1.getClosedLoopController();
    posController2 = motor2.getClosedLoopController();

    motor1.getEncoder().setPosition(0);
    motor2.getEncoder().setPosition(0);

    ElasticUtil.putDouble("Slider Position 1", motor1.getEncoder()::getPosition);
    ElasticUtil.putDouble("Slider Position 2", motor2.getEncoder()::getPosition);

    ElasticUtil.putDouble("SMotor 1 Speed", motor1.getEncoder()::getVelocity);
    ElasticUtil.putDouble("SMotor 2 Speed", motor2.getEncoder()::getVelocity);

    initialized = true;
  }

  public static S_Slider getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Slider();
    }

    return m_Instance;
  }

  public void set(boolean intaking) {
    posController1.setSetpoint(intaking ? SliderConstants.INTAKING_ONE_POS : 0, ControlType.kPosition);
    posController2.setSetpoint(intaking ? SliderConstants.INTAKING_TWO_POS : 0, ControlType.kPosition);

    // motor1.set(Utils.normalize(setpoint));
    // motor2.set(Utils.normalize(setpoint * 1.25));
  }

  public void set(double speed) {
    motor1.set(Utils.normalize(speed));
    motor2.set(Utils.normalize(speed));
  }

  public void shake() {
    if(posController1.isAtSetpoint()) {
      if(shakeSetpoint == 0) {
        shakeSetpoint = -20;
      } else {
        shakeSetpoint = 0;
      }
    }

    posController1.setSetpoint(shakeSetpoint, ControlType.kPosition);
  }

  @Override
  public void stop() {
    motor1.stopMotor();
    motor2.stopMotor();
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
