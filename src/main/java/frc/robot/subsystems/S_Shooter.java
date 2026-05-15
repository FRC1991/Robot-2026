// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;
import frc.utils.Utils.ElasticUtil;

public class S_Shooter extends SubsystemBase implements CheckableSubsystem {
  private boolean initialized = false, status = false;
  
  private SparkMax shootMotor1;
  private SparkClosedLoopController closedLoopOne;
    
  private SparkMax shootMotor2;
  private SparkClosedLoopController closedLoopTwo;

  private SparkMax indexMotor;
  
  private static S_Shooter m_Instance;
  
  /** Creates a new S_Shooter. */
  private S_Shooter() {
    shootMotor1 = new SparkMax(CANConstants.SHOOTER_ID_ONE, MotorType.kBrushless);
    shootMotor2 = new SparkMax(CANConstants.SHOOTER_ID_TWO, MotorType.kBrushless);

    closedLoopOne = shootMotor1.getClosedLoopController();
    closedLoopTwo = shootMotor2.getClosedLoopController();

    indexMotor = new SparkMax(CANConstants.INDEXER_ID, MotorType.kBrushless);

    SparkMaxConfig shootMotorConfig = new SparkMaxConfig();

    shootMotorConfig.smartCurrentLimit(30).idleMode(IdleMode.kCoast)
      .closedLoop.p(0.00005).i(0).d(0).allowedClosedLoopError(10, ClosedLoopSlot.kSlot0)
      .feedForward.kS(0.14).kV(0.002);
      // .closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      // .p(0.001).i(0).d(0)
      // .outputRange(-1, 1)
      // .feedForward.kV(10 / 5676);

    shootMotorConfig.encoder
      .positionConversionFactor(1)
      .velocityConversionFactor(1);

    shootMotor1.configure(shootMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    shootMotor2.configure(shootMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    indexMotor.configure(Constants.NEO_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    ElasticUtil.putDouble("Speed of Shooter 1", () -> shootMotor1.getEncoder().getVelocity());
    ElasticUtil.putDouble("Speed of Shooter 2", () -> shootMotor2.getEncoder().getVelocity());

    initialized = true;
  }

  public static S_Shooter getInstance() {
    if(m_Instance == null) {
      m_Instance = new S_Shooter();
    }

    return m_Instance;
  }

  public void set(double speedSetpoint1, double speedSetpoint2, double indexSpeed) {
    closedLoopOne.setSetpoint(speedSetpoint1 + 50, ControlType.kVelocity);
    closedLoopTwo.setSetpoint(speedSetpoint2 - 50, ControlType.kVelocity);

    // if(Math.abs(speedSetpoint1 - shootMotor1.getEncoder().getVelocity()) < 25 && Math.abs(speedSetpoint2 - shootMotor2.getEncoder().getVelocity()) < 25) {
    //   indexMotor.set(Utils.normalize(indexSpeed));
    // }
    
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
