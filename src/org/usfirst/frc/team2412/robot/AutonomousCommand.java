package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.command.Command;

/*
 * This class represents a command to be run in autonomous, such as driving forward using motion profiling.
 */

public class AutonomousCommand extends Command {
	private boolean isRunning;
	private int commandStage;
	
	/** TODO Possibly change this constructor? */
	public AutonomousCommand(int _commandStage) {
		isRunning = false;
		this.commandStage = _commandStage;
	}

	/**
	 * Called periodically when the command is running.
	 */
	protected void runPeriodic() {

	}

	/**
	 * Called if this command should be running.
	 */
	protected boolean shouldBeRunning() {
		return commandStage == AutonomousCommandManager.currentStage;
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
		if(shouldBeRunning()) {
			if(isRunning) {
				if(isFinished()) {
					//Command's about to stop.
					AutonomousCommandManager.currentStage++; //Move on to the next stage
					end();
				} else {
					//Command's currently running.
					runPeriodic();
				}
			} else {
				//Command's about to start.
				initialize();
			}
		}
	}

	/**
	 * Determines if the command is finished.
	 * This method should be overriden by any subclasses.
	 */
	protected boolean isFinished() {
		return false;
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