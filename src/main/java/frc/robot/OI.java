// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OIConstants;

/** Add your docs here. */
public class OI {
    private OI() {}

    public static final CommandXboxController driverController = new CommandXboxController(OIConstants.kDriverControllerPort);
}
