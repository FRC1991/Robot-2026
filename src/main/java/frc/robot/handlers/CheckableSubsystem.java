// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.handlers;

/** Add your docs here. */
public interface CheckableSubsystem {
    /**
     * Should stop all of a subsystem's motors, servos, and/or pneumatic pistons.
     * Effectively a subsystem wide E-stop.
     */
    void stop();

    /**
     * Initialized should always represent if the constructor has run properly.
     * It can also have subsystem specific requirements, like a turret being aligned
     * to dead center before a match or a pivot needing to be level before a match.
     * 
     * @return Did the constructor successfully execute.
     */
    boolean getInitialized();

    /**
     * This check should ensure that any data about a subsystem
     * that can be determined in software is returning an acceptable
     * output. For example, a Pivot subsytem should check that the
     * motor's encoder or external encoder is inside an acceptable range.
     * 
     * @return Whether or not the subsystem okay to opperate. 
     */
    boolean checkSubsystem();
}
