package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.command.Command;

/*
 * This class represents a command to be run in autonomous, such as driving forward using motion profiling.
 */

public class AutonomousCommand extends Command {
	private boolean isRunning;
	private String commandName;
	
	/** TODO Possibly change this constructor? */
	public AutonomousCommand(String _commandName) {
		isRunning = false;
		this.commandName = _commandName;
	}

	/**
	 * Called periodically when the command is running.
	 */
	protected void runPeriodic() {

	}

	/**
	 * Called if this command should be running.
	 */
	protected boolean shouldBeRunning(String selectedCommandName) {
		return commandName.equals(selectedCommandName);
	}

	/**
	 * Called when the command first starts.
	 */
	protected void initialize() {
		isRunning = true;
	}

	/**
	 * Called by other classes that use AutonomousCommand.
	 * This method determines when the other methods should be called.
	 */
	protected void execute() {
		if(shouldBeRunning(AutonomousCommandManager.selectedCommand)) {
			if(!isFinished()) {
				runPeriodic();
			} else {
				initialize();
			}
		} else {
			if(!isFinished()) {
				end();
			}
			//Don't do anything if the command has been stopped and doesn't need to be run.
		}
	}

	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		return isRunning;
	}

	/**
	 * Called when the command ends.
	 */
	protected void end() {
		isRunning = false;
	}


	/**
	 * Called if the command has been interrupted.
	 */

	protected void interrupted() {
		//Do nothing...for now.
	}
}