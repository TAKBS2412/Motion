package org.usfirst.frc.team2412.robot.autonomous;

import java.util.HashMap;

import org.usfirst.frc.team2412.robot.Robot;

/**
 * A class that represents a stage in Autonomous.
 */
public class AutonomousStage {
	private HashMap<String, Command2> hm;
	private String key;
	
	/**
	 * Creates an empty AutonomousStage instance.
	 * @param _stage The stage number that this instance represents.
	 */
	public AutonomousStage() {
		this.hm = new HashMap<String, Command2>();
	}

	/**
	 * Adds an autonomous command.
	 * @param commandName The name of the autonomous command to add.
	 * @param command The command object to be added.
	 */
	public void addCommand(String commandName, Command2 command) {
		hm.put(commandName, command);
	}
	
	/**
	 * Adds a default autonomous command.
	 * @param commandName The name of the autonomous command to add.
	 * @param command The command object to be added.
	 */
	public void addDefaultCommand(String commandName, Command2 command) {
		hm.put(commandName, command);
	}

	/**
	 * Gets the currently selected autonomous command.
	 */
	public Command2 getSelected() {
		return hm.get(Robot.pydashboardTable.getString(key, ""));
	}

	/**
	 * Sends the SendableChooser to the SmartDashboard.
	 * @param key The key to send.
	 */
	public void sendCommands(String key) {
		this.key = key;
	}
}