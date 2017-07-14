package org.usfirst.frc.team2412.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/*
 * This class represents a group of commands to be run in autonomous.
 */

public class AutonomousCommand extends CommandGroup {
	
	/**
	 * Creates an AutonomousCommand from an ArrayList of AutonomousStage objects.
	 * Assumes that commands have already been added to each AutonomousStage (and have been sent to the SmartDashboard).
	 */
	public AutonomousCommand(ArrayList<AutonomousStage> stages) {
		for(AutonomousStage stage : stages) {
			Command selectedCommand = stage.getSelected();
			if(selectedCommand == null) {
				System.err.println("No stage was selected!");
				continue;
			}
			addSequential(selectedCommand);
		}
	}
	
	
}