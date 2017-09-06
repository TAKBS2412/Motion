package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.RobotDrive;

public class EncoderCommand extends Command2 {
	
	private CANTalon talon;
	private CANTalon slaves[];
	private RobotDrive rd;
	private boolean reverseSensor;
	private final double encodertocmconv = 0.0239534386;
	private final double targetPositionRotations;
	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	public EncoderCommand(CANTalon talon, CANTalon[] _slaves, RobotDrive _rd, double _targetPositionRotations, boolean _reverseSensor) {
		this.talon = talon;
		//Copy CANTalon array.
		slaves = new CANTalon[_slaves.length];
		for(int i = 0; i < _slaves.length; i++) {
			slaves[i] = _slaves[i];
			System.out.println("Slave #" + i + ": " +  slaves[i].getControlMode());
		}
		this.targetPositionRotations = _targetPositionRotations;
		this.reverseSensor = _reverseSensor;
		this.rd = _rd;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void start() {
//		for(CANTalon slave : _slaves) {
//			slave.changeControlMode(CANTalon.TalonControlMode.Follower);
//			slave.set(_talon.getDeviceID());
//		}

		//Make sure all of the Talons are in PercentVbus mode.
		for(CANTalon talon : slaves) {
			talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			talon.enable();
		}
		talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talon.enable();
		
		int absolutePosition = talon.getPulseWidthPosition() & 0xFFF; /* mask out the bottom12 bits, we don't care about the wrap arounds */
		/* use the low level API to set the quad encoder signal */
        talon.setPosition(absolutePosition);
        
        /* choose the sensor and sensor direction */
        talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        talon.reverseSensor(this.reverseSensor);
        talon.configEncoderCodesPerRev(2048); // if using FeedbackDevice.QuadEncoder

        /* set the peak and nominal outputs, 12V means full */
        talon.configNominalOutputVoltage(+0f, -0f);
        talon.configPeakOutputVoltage(+12f, -12f);   
	}
	
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		return talon.getPosition() > targetPositionRotations;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		System.out.println(talon.getPosition());
		System.out.println("Driving...");
		rd.drive(0.5, 0.0);
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		System.out.println("Stopped");
		rd.drive(0.0, 0.0);
	}
	
	//Gets the encoder's position value in cm.
	public double getPositionCm() {
		return talon.getPosition() * encodertocmconv;
	}
	
}
