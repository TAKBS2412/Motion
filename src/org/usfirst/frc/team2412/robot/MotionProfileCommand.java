package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class MotionProfileCommand extends Command2 {

	private CANTalon master;
	private CANTalon[] slaves;
	private MotionProfiler profiler;
	
	/**
	 * 
	 * @param _master The CANTalon to be driven using motion profiling.
	 * @param _slaves The CANTalons that will be slaves to _master.
	 */
	public MotionProfileCommand(CANTalon _master, CANTalon[] _slaves) {
		this.master = _master;
		System.out.println("Master: " + master.getControlMode());
		//Copy CANTalon array.
		slaves = new CANTalon[_slaves.length];
		for(int i = 0; i < _slaves.length; i++) {
			slaves[i] = _slaves[i];
			System.out.println("Slave #" + i + ": " +  slaves[i].getControlMode());
		}
		
		profiler = new MotionProfiler(master);
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		setupMotionProfiling();
		
		master.setPosition(0); //Zero out the encoder in the beginning
		master.configEncoderCodesPerRev(2048);
		master.setF(0.2600);
		master.setP(0);
		master.setI(0);
		master.setD(0);
		
		System.out.println("init");
	}
	
	/**
	 * Called when the Command starts.
	 */
	public void start() {
		super.start();
		profiler.startMotionProfile();
		
		System.out.println("start");
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		return profiler.getSetValue() == CANTalon.SetValueMotionProfile.Hold;
//		return profiler.activeIsLast();
	}
	
	//TODO add execute(), look over https://github.com/TAKBS2412/Motion/blob/master/src/org/usfirst/frc/team2412/robot/Robot.java, add anything else that's needed.
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
//		master.set(0.3);
		profiler.control();
		master.changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
		CANTalon.SetValueMotionProfile setOutput = profiler.getSetValue();
		master.set(setOutput.value);
	}
	
	private void setupMotionProfiling() {
		master.reverseOutput(true); //Reverse the motor output.
		master.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//		master.changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
		
		//Make all talons follow master
		for(CANTalon talon : slaves) {
			talon.changeControlMode(CANTalon.TalonControlMode.Follower);
			talon.set(master.getDeviceID());
		}
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		System.out.println("End");
		master.reverseOutput(false);
		master.changeControlMode(TalonControlMode.PercentVbus);
		master.set(0);
		profiler.reset();
		//Don't make all talons follow master
		for(CANTalon talon : slaves) {
			talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		}
	}
}
