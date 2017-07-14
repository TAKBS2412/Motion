package org.usfirst.frc.team2412.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class that represents a stage in Autonomous.
 */
public class AutonomousStage {
	private SendableChooser<String> chooser;
	private HashMap<String, Command2> hm;

	/**
	 * Creates an empty AutonomousStage instance.
	 * @param _stage The stage number that this instance represents.
	 */
	public AutonomousStage() {
		this.chooser = new SendableChooser<>();
		this.hm = new HashMap<String, Command2>();
	}

	/**
	 * Adds an autonomous command.
	 * @param commandName The name of the autonomous command to add.
	 * @param command The command object to be added.
	 */
	public void addCommand(String commandName, Command2 command) {
		chooser.addObject(commandName, commandName);
		hm.put(commandName, command);
	}
	
	/**
	 * Adds a default autonomous command.
	 * @param commandName The name of the autonomous command to add.
	 * @param command The command object to be added.
	 */
	public void addDefaultCommand(String commandName, Command2 command) {
		chooser.addDefault(commandName, commandName);
		hm.put(commandName, command);
	}

	/**
	 * Gets the currently selected autonomous command.
	 */
	public Command2 getSelected() {
		return hm.get(chooser.getSelected());
	}

	/**
	 * Sends the SendableChooser to the SmartDashboard.
	 * @param key The key to send.
	 */
	public void sendCommands(String key) {
		SmartDashboard.putData(key, chooser);
	}
}