// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

public class S_Shooter extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private TalonFX shootMotor;
  private SparkMax indexMotor;
  
  private static S_Shooter m_Instance;
  
  /** Creates a new S_Shooter. */
  private S_Shooter() {
    shootMotor = new TalonFX(CANConstants.SHOOTER_ID);
    indexMotor = new SparkMax(CANConstants.INDEXER_ID, MotorType.kBrushless);

    TalonFXConfiguration shootMotorConfig = new TalonFXConfiguration();

    shootMotorConfig.TorqueCurrent.PeakForwardTorqueCurrent = 80.0;
    shootMotorConfig.TorqueCurrent.PeakReverseTorqueCurrent = -80.0;
    shootMotorConfig.Slot0.kP = 0.1;
    shootMotorConfig.Slot0.kI = 0;
    shootMotorConfig.Slot0.kD = 0;

    indexMotor.configure(Constants.NEO_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    initialized = true;
  }

  public static S_Shooter getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Shooter();
    }

    return m_Instance;
  }

  public void set(double shootSpeed, double indexSpeed) {
    shootMotor.set(Utils.normalize(shootSpeed));
    indexMotor.set(Utils.normalize(indexSpeed));

    System.out.println(shootMotor.getVelocity().getValueAsDouble());
  }

  @Override
  public void stop() {
    shootMotor.stopMotor();
    indexMotor.stopMotor();
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
