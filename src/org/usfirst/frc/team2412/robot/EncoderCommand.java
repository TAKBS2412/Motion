package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

public class EncoderCommand extends Command2 {
	
	private CANTalon talon;
	private CANTalon slaves[];
	private boolean reverseSensor;
	private boolean started = false;
	private final double encodertocmconv = 0.0239534386;
	private final double targetPositionRotations;
	StringBuilder _sb = new StringBuilder();
	int _loops = 0;
	public EncoderCommand(CANTalon talon, CANTalon[] _slaves, double _targetPositionRotations, boolean _reverseSensor) {
		this.talon = talon;
		//Copy CANTalon array.
		slaves = new CANTalon[_slaves.length];
		for(int i = 0; i < _slaves.length; i++) {
			slaves[i] = _slaves[i];
			System.out.println("Slave #" + i + ": " +  slaves[i].getControlMode());
		}
		this.targetPositionRotations = _targetPositionRotations;
		this.reverseSensor = _reverseSensor;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void start() {
		for(CANTalon slave : slaves) {
			slave.changeControlMode(CANTalon.TalonControlMode.Follower);
			slave.set(talon.getDeviceID());
		}
		
		int absolutePosition = talon.getPulseWidthPosition() & 0xFFF; /* mask out the bottom12 bits, we don't care about the wrap arounds */
        /* use the low level API to set the quad encoder signal */
        talon.setEncPosition(absolutePosition);
        
        /* choose the sensor and sensor direction */
        talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        talon.reverseSensor(this.reverseSensor);
        talon.configEncoderCodesPerRev(2048); // if using FeedbackDevice.QuadEncoder

        /* set the peak and nominal outputs, 12V means full */
        talon.configNominalOutputVoltage(+0f, -0f);
        talon.configPeakOutputVoltage(+12f, -12f);
        /* set the allowable closed-loop error,
         * Closed-Loop output will be neutral within this range.
         * See Table in Section 17.2.1 for native units per rotation. 
         */
        talon.setAllowableClosedLoopErr(0); /* always servo */
        /* set closed loop gains in slot0 */
        talon.setProfile(0);
        talon.setF(0.0);
        talon.setP(0.1);
        talon.setI(0.0); 
        talon.setD(0.0);    
	}
	
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		return Math.abs(targetPositionRotations - talon.getPosition()) < 0.01;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		double motorOutput = talon.getOutputVoltage() / talon.getBusVoltage();
    	/* prepare line to print */
		_sb.append("\tout:");
		_sb.append(motorOutput);
        _sb.append("\tpos:");
        _sb.append(talon.getPosition() );
        
        /* on button1 press enter closed-loop mode on target position */
        if(!started) {
        	System.out.println("Started");
        	/* Position mode - button just pressed */
        	talon.changeControlMode(TalonControlMode.Position);
        	talon.set(0.5 * 50); /* 50 rotations in either direction */
        	talon.enable();
        	started = true;
        }
        
        if( talon.getControlMode() == TalonControlMode.Position) {
        	/* append more signals to print when in speed mode. */
        	_sb.append("\terrNative:");
        	_sb.append(talon.getClosedLoopError());
        	_sb.append("\ttrg:");
        	_sb.append(targetPositionRotations);
        }
        /* print every ten loops, printing too much too fast is generally bad for performance */ 
        if(++_loops >= 10) {
        	_loops = 0;
        	System.out.println(_sb.toString());
        }
        _sb.setLength(0);
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
	}
	
	//Gets the encoder's position value in cm.
	public double getPositionCm() {
		return talon.getPosition() * encodertocmconv;
	}
	
}
