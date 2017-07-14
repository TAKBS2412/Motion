package org.usfirst.frc.team2412.robot;


/*
 * This class is a command that will read the currently selected command by the user and execute it.
 */

public class FudgeCommand extends Command2 {
	private AutonomousStage autoStage;
	private Command2 selectedCommand;
	/**
	 * Creates a FudgeCommand.
	 * @param autoStage The stage that this class will read the currently selected command from.
	 */
	public FudgeCommand(AutonomousStage _autoStage) {
		this.autoStage = _autoStage;
	}

	/**
	 * Called when the command first starts.
	 */
	protected void initialize() {
		//Start the currently selected command.
		selectedCommand = autoStage.getSelected();
		if(selectedCommand == null) {
			System.err.println("No command selected!");
		} else {
			selectedCommand.start();
		}
	}

	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		if(selectedCommand != null) return selectedCommand.isFinished();
		return true; //We've finished if there's nothing to do.
	}
}