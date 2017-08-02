package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;

public class EncoderCommand extends Command2 {
	
	private CANTalon talon;
	private RobotDrive rd;
	private double speed;
	private double distanceToTravel; //The distance to travel, in cm.
	private boolean reverseSensor;
	private final double encodertocmconv = 0.0239534386;
	public EncoderCommand(CANTalon _talon, RobotDrive _rd, double _speed, double _distanceToTravel, boolean _reverseSensor) {
		this.talon = _talon;
		this.rd = _rd;
		this.speed = _speed;
		this.distanceToTravel = _distanceToTravel;
		this.reverseSensor = _reverseSensor;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		talon.reverseSensor(this.reverseSensor);
		talon.setPosition(0); //Zero out the encoder.
		talon.configEncoderCodesPerRev(2048);
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		return getPositionCm() > distanceToTravel;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		rd.arcadeDrive(speed, 0.0, false);
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
	}
	
	//Gets the encoder's position value in cm.
	public double getPositionCm() {
		return talon.getPosition() * encodertocmconv;
	}
	
}
