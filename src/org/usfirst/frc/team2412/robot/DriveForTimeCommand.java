package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;

public class DriveForTimeCommand extends Command2 {

	private RobotDrive rd;
	private CANTalon[] talons;
	private double move;
	private double turn;
	private double duration;
	
	private long startuptime;
	public DriveForTimeCommand(int _commandStage, CANTalon[] _talons, double _move, double _turn, double _duration) {
		this.talons = _talons;
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
	 * Called when the command starts.
	 */
	public void start() {
		rd = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
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
	public void execute() {
		System.out.println(rd);
		rd.arcadeDrive(move, turn, false);
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
	}
}
