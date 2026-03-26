// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

public class S_Shooter extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax shootMotor1;
  private SparkMax shootMotor2;

  private SparkMax indexMotor;
  
  private static S_Shooter m_Instance;
  
  /** Creates a new S_Shooter. */
  private S_Shooter() {
    shootMotor1 = new SparkMax(CANConstants.SHOOTER_ID_ONE, MotorType.kBrushless);
    shootMotor2 = new SparkMax(CANConstants.SHOOTER_ID_TWO, MotorType.kBrushless);

    indexMotor = new SparkMax(CANConstants.INDEXER_ID, MotorType.kBrushless);

    indexMotor.configure(Constants.NEO_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    initialized = true;
  }

  public static S_Shooter getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Shooter();
    }

    return m_Instance;
  }

  public void set(double shootSpeed1, double shootSpeed2, double indexSpeed) {
    shootMotor1.set(Utils.normalize(shootSpeed1));
    shootMotor2.set(Utils.normalize(shootSpeed2));
    indexMotor.set(Utils.normalize(indexSpeed));
  }

  @Override
  public void stop() {
    shootMotor1.stopMotor();
    shootMotor2.stopMotor();
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
