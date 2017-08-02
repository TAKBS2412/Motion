package org.usfirst.frc.team2412.robot;

import java.util.ArrayList;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private CANTalon[] allTalons = new CANTalon[4];
	
	private RobotDrive rd;
	
	private final double encodertocmconv = 0.0239534386;
	
	private MotionProfiler profiler; //The MotionProfile logic.
	
	private boolean motionProfileStarted;
	private boolean motionProfileEnded;
	private boolean turnStarted;
	
	/**Autonomous selecting variables*/
	
	//Variables for selecting autonomous stages
	private int currentStage = 0;

	private DriveForTimeCommand dftc;
	private TestCommand testcmd;
	private TestCommand1 testcmd1;
	private TestCommand2 testcmd2;

	private AutonomousStage as;
	private AutonomousStage as1;

	private ArrayList<AutonomousStage> stages;
	 
	private Command2 selectedCommand;
	
	public static NetworkTable pydashboardTable;
	public static String step1;
	
	public static NetworkTable visionTable;
	
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
		pydashboardTable = NetworkTable.getTable("PyDashboard");
		visionTable = NetworkTable.getTable("datatable");
		
		allTalons[0] = new CANTalon(7);
		allTalons[1] = new CANTalon(6);
		
		allTalons[2] = new CANTalon(2);
		allTalons[3] = new CANTalon(3);
		
		//Make sure all of the Talons are in PercentVbus mode.
		for(CANTalon talon : allTalons) {
			talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		}
		
		rd = new RobotDrive(allTalons[0], allTalons[1], allTalons[2], allTalons[3]);
		
		//Setup autonomous stages.
		dftc = new DriveForTimeCommand(1, rd, 0.5d, 0.0d, 5E9);
		testcmd = new TestCommand(5E9);
		testcmd1 = new TestCommand1(4E9);
		testcmd2 = new TestCommand2(3E9);
		
		as = new AutonomousStage();
		as.addDefaultCommand("Time Based", dftc);
		as.addCommand("TEST", testcmd);
		as.sendCommands("Stage 1");
		
		as1 = new AutonomousStage();
		as1.addDefaultCommand("Test1", testcmd1);
		as1.addCommand("Test2", testcmd2);
		as1.sendCommands("Stage 2");
		
//		profiler = new MotionProfiler(allTalons[2]);
	}
	
	@Override
	public void teleopInit() {
		allTalons[2].setPosition(0); //Zero out the encoder in the beginning
		allTalons[2].configEncoderCodesPerRev(2048);
		allTalons[2].setF(0.2600);
		allTalons[2].setP(0);
		allTalons[2].setI(0);
		allTalons[2].setD(0);
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
		step1 = pydashboardTable.getString("Step1", "");
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
			allTalons[2].changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
			profiler.startMotionProfile();
			System.out.println("Hello");
		}
		if(!motionProfileEnded) {
			profiler.control();
			allTalons[2].changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
			CANTalon.SetValueMotionProfile setOutput = profiler.getSetValue();
			allTalons[2].set(setOutput.value);
			if(profiler.activeIsLast()) {
				System.out.println("isLast: true");
			}
			motionProfileEnded = profiler.activeIsLast();
		}
		if(motionProfileEnded && !turnStarted) {
			System.out.println("Motion Profile ended, preparing to turn.");
			allTalons[2].reverseOutput(false);
			allTalons[2].changeControlMode(TalonControlMode.PercentVbus);
			allTalons[2].set(0);
			profiler.reset();
			allTalons[3].changeControlMode(TalonControlMode.PercentVbus);
			allTalons[0].changeControlMode(TalonControlMode.PercentVbus);
			allTalons[1].changeControlMode(TalonControlMode.PercentVbus);
			turnStarted = true;
		}
		if(turnStarted) {
//			System.out.println("Turning...");
//			allTalons[2].set(0.3);
//			allTalons[3].set(0.3);
//			allTalons[0].set(-0.3);
//			allTalons[1].set(-0.3);
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
//		setupMotionProfiling();
	}
	
	private void setupMotionProfiling() {
		allTalons[2].reverseOutput(true); //Reverse the motor output.
		allTalons[2].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		allTalons[2].changeControlMode(TalonControlMode.MotionProfile); //Make Talon go into motion profiling mode.
		//Make talon2 follow allTalons[2]
		allTalons[3].changeControlMode(CANTalon.TalonControlMode.Follower);
		allTalons[3].set(allTalons[2].getDeviceID());
		
		//Make left talons follow allTalons[2]
		allTalons[0].changeControlMode(CANTalon.TalonControlMode.Follower);
		allTalons[0].set(allTalons[2].getDeviceID());
		allTalons[1].changeControlMode(CANTalon.TalonControlMode.Follower);
		allTalons[1].set(allTalons[2].getDeviceID());
		motionProfileStarted = motionProfileEnded = turnStarted = false;
	}
}

