// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.Constants.ModuleConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.handlers.CheckableSubsystem;
import frc.utils.Utils;

/** Add your docs here. */
public class SwerveModule implements CheckableSubsystem {
    private boolean initialized = false, status = false;

    private TalonFX driveMotor;
    private SparkMax turningMotor;

    private AnalogEncoder m_turningEncoder;

    private double m_chassisAngularOffset = 0;
    private SwerveModuleState m_desiredState = new SwerveModuleState(0.0, new Rotation2d());

    private PIDController azimuth;

    public SwerveModule(int drivingCANId, int turningCANId, int encoderChannel, double chassisAngularOffset) {
        driveMotor = new TalonFX(drivingCANId);
        turningMotor = new SparkMax(turningCANId, MotorType.kBrushless);

        azimuth = new PIDController(0.008, 0, 0);
        azimuth.enableContinuousInput(-180, 180);
        azimuth.setTolerance(1);

        m_turningEncoder = new AnalogEncoder(encoderChannel);
        // m_turningEncoder = new AnalogEncoder(encoderChannel, (2 * Math.PI), chassisAngularOffset);

        TalonFXConfiguration drivingConfig = new TalonFXConfiguration();
        SparkMaxConfig turningConfig = new SparkMaxConfig();

        double drivingFactor = ModuleConstants.WHEEL_DIAMETER_METERS * Math.PI / ModuleConstants.DRIVING_MOTOR_REDUCTION;

        drivingConfig.TorqueCurrent.PeakForwardTorqueCurrent = 80.0;
        drivingConfig.TorqueCurrent.PeakReverseTorqueCurrent = -80.0;
        drivingConfig.ClosedLoopRamps.TorqueClosedLoopRampPeriod = 0.02;
        drivingConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        drivingConfig.Feedback.SensorToMechanismRatio = drivingFactor;

        drivingConfig.Slot0.kP = 0.05;
        drivingConfig.Slot0.kI = 0;
        drivingConfig.Slot0.kD = 0;

        turningConfig
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40);

        driveMotor.getConfigurator().apply(drivingConfig, 0.1);
        driveMotor.optimizeBusUtilization(0, 0.1);

        turningMotor.configure(turningConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        m_chassisAngularOffset = chassisAngularOffset;
        m_desiredState.angle = new Rotation2d(getEncoderRadians());
        
        driveMotor.setPosition(0);

        initialized = true;
    }

    public SwerveModuleState getState() {
        driveMotor.getVelocity().refresh();

        return new SwerveModuleState(driveMotor.getVelocity().getValueAsDouble(),
            new Rotation2d((m_turningEncoder.get() * 2 * Math.PI) - m_chassisAngularOffset));
    }

    public double getEncoderDegrees() {
        return getEncoderRadians() * (180 / Math.PI);
    }

    public double getEncoderRadians() {
        return MathUtil.angleModulus(m_turningEncoder.get() * 2 * Math.PI + m_chassisAngularOffset);
        // return MathUtil.angleModulus(m_turningEncoder.get());
    }

    public SwerveModulePosition getPosition() {
        driveMotor.getPosition().refresh();

        return new SwerveModulePosition(driveMotor.getPosition().getValueAsDouble(),
            new Rotation2d(getEncoderRadians() - m_chassisAngularOffset));
    }

    public void setDesiredState(SwerveModuleState desiredState) {
        SwerveModuleState correctedDesiredState = new SwerveModuleState();

        correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
        correctedDesiredState.angle = desiredState.angle;

        correctedDesiredState.optimize(new Rotation2d(getEncoderRadians()));

        driveMotor.set(Utils.normalize(correctedDesiredState.speedMetersPerSecond / SwerveConstants.MAX_SPEED_METERS_PER_SECOND));

        double error = azimuth.calculate(getEncoderDegrees(), correctedDesiredState.angle.getDegrees());

        if(Math.abs(error) < 0.02) {
            error = 0;
        }

        turningMotor.set(-Utils.normalize(error));

        m_desiredState = correctedDesiredState;
    }

    public void resetDriveEncoder() {
        driveMotor.setPosition(0);
    }

    public AbsoluteEncoder getAbsoluteEncoder() {
        return turningMotor.getAbsoluteEncoder();
    }

    @Override
    public void stop() {
        driveMotor.stopMotor();
        turningMotor.stopMotor();
    }

    @Override
    public boolean checkSubsystem() {
        status = initialized;

        return status;
    }

    @Override
    public boolean getInitialized() {
        return initialized;
    }
}
