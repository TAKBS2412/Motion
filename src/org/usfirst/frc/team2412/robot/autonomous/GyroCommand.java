package org.usfirst.frc.team2412.robot.autonomous;

import org.usfirst.frc.team2412.robot.Robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;

public class GyroCommand extends Command2 {

	private GyroBase gyro;
	private RobotDrive rd;
	private double turnSpeed;
	private double angleToTurn;
	private PIDController turncontrol;
	
	private CANTalon master;
	private CANTalon[] slaves;
	
	/**PID constants**/
	private double P = 0.009;
	private double I = 0;
	private double D = 0;
	
	private int times = 0;
	
	private int direction = 1;
	private double error = 0;
	
	
	public GyroCommand(GyroBase _gyro, CANTalon _master, CANTalon[] _slaves, double _angle, double _angleToTurn) {
		this.gyro = _gyro;
		this.turnSpeed = _angle;
		this.angleToTurn = _angleToTurn;
		
		this.master = _master;
		//Copy CANTalon array.
		slaves = new CANTalon[_slaves.length];
		for(int i = 0; i < _slaves.length; i++) {
			slaves[i] = _slaves[i];
		}
		
		this.turncontrol = new PIDController(P, I, D, gyro, master);
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		gyro.reset();
		for(CANTalon talon : slaves) {
			talon.changeControlMode(CANTalon.TalonControlMode.Follower);
			talon.set(master.getDeviceID());
		}
		if(Robot.step1.equals("Left Peg")) {
			direction = 1;
		} else if(Robot.step1.equals("Right Peg")) {
			direction = -1;
		}
		this.angleToTurn = direction * Math.abs(angleToTurn);
		turncontrol.setSetpoint(angleToTurn);
		turncontrol.setPercentTolerance(10);
		turncontrol.enable();
	}
	
	/**
	 * Determines if the command is finished.
	 */
	public boolean isFinished() {
		return Math.abs(turncontrol.getError()) < 3;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		System.out.println("GyroCommand!");
		System.out.println("Angle: " + gyro.getAngle());
		master.enable();
		for(CANTalon talon : slaves) {
			talon.enable();
		}
		System.out.println("Enabled: " + master.isEnabled());
		/**
		times++;
		error = (angleToTurn - Math.abs(gyro.getAngle()));
//		if(times == 10) {
			System.out.println("Angle: " + gyro.getAngle());
			System.out.println("Error: " + error);
			times = 0;
//		}
		rd.arcadeDrive(0.0, 0.7*error/angleToTurn, false);
		*/
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		turncontrol.disable();
		//rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
		master.changeControlMode(TalonControlMode.PercentVbus);
		master.set(0);
		//Don't make all talons follow master
		for(CANTalon talon : slaves) {
			talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		}
	}
}
