package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.RobotDrive;

public class DriveForTimeCommand extends AutonomousCommand {

	private RobotDrive rd;
	private double move;
	private double turn;
	private long duration;
	
	private long startuptime;
	public DriveForTimeCommand(int _commandStage, RobotDrive _rd, double _move, double _turn, long _duration) {
		super(_commandStage);
		this.rd = _rd;
		this.move = _move;
		this.turn = _turn;
		this.duration = _duration;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		super.initialize();
		this.startuptime = System.nanoTime();
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		long deltaTime = System.nanoTime() - startuptime; 
		return deltaTime < 0 || deltaTime > duration; 
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	protected void runPeriodic() {
		rd.arcadeDrive(move, turn, false);
	}
}