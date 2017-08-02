package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class GyroCommand extends Command2 {

	private Gyro gyro;
	private RobotDrive rd;
	private double turnSpeed;
	private double angleToTurn;
	public GyroCommand(Gyro _gyro, RobotDrive _rd, double _angle, double _angleToTurn) {
		this.gyro = _gyro;
		this.rd = _rd;
		this.turnSpeed = _angle;
		this.angleToTurn = _angleToTurn;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		gyro.reset();
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		if(gyro.getAngle() == 0) return false;
		if(Robot.step1.equals("Left Peg")) {
			return gyro.getAngle() > angleToTurn;
		} else if(Robot.step1.equals("Right Peg")) {
			return gyro.getAngle() < angleToTurn;
		}
		return true;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		rd.arcadeDrive(0.0, turnSpeed, false);
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
	}
}
