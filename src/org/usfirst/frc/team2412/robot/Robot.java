package org.usfirst.frc.team2412.robot;

import java.util.ArrayList;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	CANTalon leftTalon;
	CANTalon leftTalon2;
	CANTalon rightTalon;
	CANTalon rightTalon2;
	
	public static RobotDrive rd;
	
	final double encodertocmconv = 0.0239534386;
	
	MotionProfiler profiler; //The MotionProfile logic.
	
	boolean motionProfileStarted;
	boolean motionProfileEnded;
	boolean turnStarted;
	
	/**Autonomous selecting variables*/
	
	//Variables for selecting autonomous mode.
	final String driveForward = "Drive Forward";
	final String leftPeg = "Left Peg";
	final String centerPeg = "Center Peg";
	final String rightPeg = "Right Peg";
	String pegSelected;
	SendableChooser<String> pegChooser = new SendableChooser<>();
	
	final String visionOnlyAuto = "Vision only";
	final String encodersAuto = "Encoders";
	final String timeBasedAuto = "Time Based";
	String autoSelected;
	
	//Variables for selecting autonomous stages
	int currentStage = 0;
	
	DriveForTimeCommand dftc;
	TestCommand testcmd;
	TestCommand1 testcmd1;
	TestCommand2 testcmd2;
	
	AutonomousStage as;
	AutonomousStage as1;
	
	ArrayList<AutonomousStage> stages;
	
	Command2 selectedCommand;
	
	//Gets the encoder's position value in cm.
	public double getPositionCm(CANTalon talon) {
		return talon.getPosition() * encodertocmconv;
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		leftTalon = new CANTalon(7);
		leftTalon2 = new CANTalon(6);
		
		rightTalon = new CANTalon(2);
		rightTalon2 = new CANTalon(3);
		
		rd = new RobotDrive(leftTalon, leftTalon2, rightTalon, rightTalon2);
		
		//Setup SmartDashboard.
		pegChooser.addObject("Drive Forward to Baseline (if selected, ignore stages 2 and 3)", driveForward);
		pegChooser.addDefault("Center Peg (if selected, ignore stage 2)", centerPeg);
		pegChooser.addObject("Left Peg", leftPeg);
		pegChooser.addObject("Right Peg", rightPeg);
		SmartDashboard.putData("Peg choices", pegChooser);
		
		//Setup autonomous stages.
		dftc = new DriveForTimeCommand(1, Robot.rd, 0.5d, 0.0d, 5E9);
		testcmd = new TestCommand(5E9);
		testcmd1 = new TestCommand1(4E9);
		testcmd2 = new TestCommand2(3E9);
		
		as = new AutonomousStage();
		as.addCommand("Time Based", dftc);
		as.addDefaultCommand("TEST", testcmd);
		as.sendCommands("Stage 1");
		
		as1 = new AutonomousStage();
		as1.addDefaultCommand("Test1", testcmd1);
		as1.addCommand("Test2", testcmd2);
		as1.sendCommands("Stage 2");
		
		profiler = new MotionProfiler(rightTalon);
	}
	
	@Override
	public void teleopInit() {
		rightTalon.setPosition(0); //Zero out the encoder in the beginning
		rightTalon.configEncoderCodesPerRev(2048);
		rightTalon.setF(0.2600);
		rightTalon.setP(0);
		rightTalon.setI(0);
		rightTalon.setD(0);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		stages = new ArrayList<AutonomousStage>();
		stages.add(as);
		stages.add(as1);
		
		currentStage = 0;
		
		selectedCommand = stages.get(currentStage).getSelected();
		selectedCommand.initialize();
		selectedCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if(currentStage >= stages.size()) return;
		
		Scheduler.getInstance().run();
		selectedCommand.execute();
		
		if(selectedCommand.isFinished()) {
			System.out.println("Finished");
			//Current command is finished, move on to the next one.
			currentStage++;
			selectedCommand.end();
			if(currentStage < stages.size()) {
				selectedCommand = stages.get(currentStage).getSelected();
				selectedCommand.initialize();
				selectedCommand.start();
			}
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		System.out.println(motionProfileStarted + ", " + motionProfileEnded + ", " + turnStarted);
		if(!motionProfileStarted && !turnStarted) {
			motionProfileStarted = true;
			rightTalon.changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
			profiler.startMotionProfile();
			System.out.println("Hello");
		}
		if(!motionProfileEnded) {
			profiler.control();
			rightTalon.changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
			CANTalon.SetValueMotionProfile setOutput = profiler.getSetValue();
			rightTalon.set(setOutput.value);
			if(profiler.activeIsLast()) {
				System.out.println("isLast: true");
			}
			motionProfileEnded = profiler.activeIsLast();
		}
		if(motionProfileEnded && !turnStarted) {
			System.out.println("Motion Profile ended, preparing to turn.");
			rightTalon.reverseOutput(false);
			rightTalon.changeControlMode(TalonControlMode.PercentVbus);
			rightTalon.set(0);
			profiler.reset();
			rightTalon2.changeControlMode(TalonControlMode.PercentVbus);
			leftTalon.changeControlMode(TalonControlMode.PercentVbus);
			leftTalon2.changeControlMode(TalonControlMode.PercentVbus);
			turnStarted = true;
		}
		if(turnStarted) {
//			System.out.println("Turning...");
//			rightTalon.set(0.3);
//			rightTalon2.set(0.3);
//			leftTalon.set(-0.3);
//			leftTalon2.set(-0.3);
//			rd.arcadeDrive(0.3d, 0.0d);
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	
//	// Pls work
//	@Override
//	public void disabledInit() {
//		
//	}
	@Override
	public void disabledInit() {
		setupMotionProfiling();
	}
	
	private void setupMotionProfiling() {
		rightTalon.reverseOutput(true); //Reverse the motor output.
		rightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		rightTalon.changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
		//Make talon2 follow rightTalon
		rightTalon2.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightTalon2.set(rightTalon.getDeviceID());
		
		//Make left talons follow rightTalon
		leftTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftTalon.set(rightTalon.getDeviceID());
		leftTalon2.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftTalon2.set(rightTalon.getDeviceID());
		motionProfileStarted = motionProfileEnded = turnStarted = false;
	}
}

