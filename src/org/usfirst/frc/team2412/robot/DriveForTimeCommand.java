package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Command;

public class DriveForTimeCommand extends Command {

	private RobotDrive rd;
	private double move;
	private double turn;
	private double duration;
	
	private long startuptime;
	public DriveForTimeCommand(int _commandStage, RobotDrive _rd, double _move, double _turn, double _duration) {
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
	
	/**
	 * Called when the command ends.
	 */
	protected void end() {
		rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
	}
}
