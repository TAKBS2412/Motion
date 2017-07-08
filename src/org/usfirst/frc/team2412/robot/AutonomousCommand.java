package org.usfirst.frc.team2412.robot;

/*
 * This class represents a command to be run in autonomous, such as driving forward using motion profiling.
 */

public class AutonomousCommand {
	private boolean isRunning;
	
	/** TODO Possibly change this constructor? */
	public AutonomousCommand() {
		isRunning = false;
	}

	/**
	 * Called when the command first starts.
	 */
	protected void startInit() {
		isRunning = true;
	}

	/**
	 * Called when the command stops.
	 */
	protected void stopInit() {
		isRunning = false;
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
		return false;
	}

	/**
	 * Called by other classes that use AutonomousCommand.
	 * This method determines when the other methods should be called.
	 */
	public void run() {
		if(shouldBeRunning()) {
			if(isRunning) {
				runPeriodic();
			} else {
				startInit();
			}
		} else {
			if(isRunning) {
				stopInit();
			}
			//Don't do anything if the command has been stopped and doesn't need to be run.
		}
	}
}