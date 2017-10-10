package org.usfirst.frc.team2412.robot;

import java.util.ArrayList;

import org.usfirst.frc.team2412.robot.autonomous.AutonomousStage;
import org.usfirst.frc.team2412.robot.autonomous.Command2;
import org.usfirst.frc.team2412.robot.autonomous.DriveForTimeCommand;
import org.usfirst.frc.team2412.robot.autonomous.EncoderCommand;
import org.usfirst.frc.team2412.robot.autonomous.GyroCommand;
import org.usfirst.frc.team2412.robot.autonomous.MotionProfileCommand;
import org.usfirst.frc.team2412.robot.autonomous.PlaceGearCommand;
import org.usfirst.frc.team2412.robot.autonomous.VisionCommand;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static int SOLENOID_ID_UP_DOWN = 3, SOLENOID_ID_UP_DOWN_REVERSE = 6,
			SOLENOID_ID_OPEN_CLOSE = 1,
			SOLENOID_ID_OPEN_CLOSE_REVERSE = 4,
			SOLENOID_ID_OPEN_CLOSE_R = 2,
			SOLENOID_ID_OPEN_CLOSE_REVERSE_R = 5;
	
	public static DoubleSolenoid upDownGripper, openCloseGripperL, openCloseGripperR;
	
	private CANTalon[] allTalons = new CANTalon[4];
	
	private RobotDrive rd;
	
	private final double encodertocmconv = 0.0239534386;
	
	/**Autonomous selecting variables*/
	
	//Variables for selecting autonomous stages
	private int currentStage = 0;

	//Step 2 Commands.
	private MotionProfileCommand mpc;
	private EncoderCommand ec;
	private DriveForTimeCommand dftc;

	//Step 3 Commands.
	private GyroCommand gc;
	private VisionCommand vc;
	private DriveForTimeCommand dftc2;

	//Step 4 Commands.
	private VisionCommand vc2;
	private EncoderCommand ec2;
	private DriveForTimeCommand dftc3;
	
	private PlaceGearCommand pgc;
	
	private AutonomousStage as2;
	private AutonomousStage as3;
	private AutonomousStage as4;

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
		upDownGripper = new DoubleSolenoid(SOLENOID_ID_UP_DOWN, SOLENOID_ID_UP_DOWN_REVERSE);
		openCloseGripperL = new DoubleSolenoid(SOLENOID_ID_OPEN_CLOSE, SOLENOID_ID_OPEN_CLOSE_REVERSE);
		openCloseGripperR = new DoubleSolenoid(SOLENOID_ID_OPEN_CLOSE_R, SOLENOID_ID_OPEN_CLOSE_REVERSE_R);		
		
		pydashboardTable = NetworkTable.getTable("PyDashboard");
		visionTable = NetworkTable.getTable("datatable");

		allTalons[0] = new CANTalon(1);
		allTalons[1] = new CANTalon(9);
		
		allTalons[2] = new CANTalon(10);
		allTalons[3] = new CANTalon(5);
		
		/*
		allTalons[0] = new CANTalon(7);
		allTalons[1] = new CANTalon(6);
		
		allTalons[2] = new CANTalon(2);
		allTalons[3] = new CANTalon(3);
		*/
		//Make sure all of the Talons are in PercentVbus mode.
		for(CANTalon talon : allTalons) {
			talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		}
		
		rd = new RobotDrive(allTalons[0], allTalons[1], allTalons[2], allTalons[3]);
		rd.setSafetyEnabled(false);
		for(CANTalon talon : allTalons) {
			talon.enable();
		}
		//Setup Step2 Commands.
		CANTalon slaves[] = {allTalons[0], allTalons[1], allTalons[2]};

		mpc = new MotionProfileCommand(allTalons[3], slaves);
		ec = new EncoderCommand(allTalons[3], slaves, rd, 2.3, false);
		dftc = new DriveForTimeCommand(1, rd, 0.3d, 0.0d, 2.4E9);
		
		//Setup Step3 Commands.
		gc = new GyroCommand(new ADXRS450_Gyro(), allTalons[3], slaves, 0.2d, 60);
		vc = new VisionCommand(rd);
		dftc2 = new DriveForTimeCommand(2, rd, 0.0d, 0.3d, 0.3E9);

		//Setup Step4 Commands.
		vc2 = new VisionCommand(rd);
		ec2 = new EncoderCommand(allTalons[3], slaves, rd, 0.975, false);
		dftc3 = new DriveForTimeCommand(3, rd, 0.3d, 0.0d, 1.70E9);
		
		//End of autonomous commands
		pgc = new PlaceGearCommand(upDownGripper, openCloseGripperL, openCloseGripperR);
		
		//Setup autonomous stages
		as2 = new AutonomousStage();
		as2.addDefaultCommand("Motion Profiling", mpc);
		as2.addCommand("Encoders", ec);
		as2.addCommand("Time-Based", dftc);
		as2.sendCommands("Step2");
		
		as3 = new AutonomousStage();
		as3.addDefaultCommand("Gyroscope", gc);
		as3.addCommand("Vision Processing", vc);
		as3.addCommand("Time-Based", dftc2);
		as3.sendCommands("Step3");

		as4 = new AutonomousStage();
		as4.addDefaultCommand("Vision Processing", vc2);
		as4.addCommand("Encoders", ec2);
		as4.addCommand("Time-Based", dftc3);
		as4.sendCommands("Step4");
	}
	
	@Override
	public void teleopInit() {
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
		stages.add(as2);
		stages.add(as3);
		stages.add(as4);
		
		currentStage = 0;
		System.out.println(currentStage);
		selectedCommand = stages.get(currentStage).getSelected();
		if(selectedCommand != null) {
			selectedCommand.initialize();
			selectedCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if(currentStage > stages.size()) return;
		if(selectedCommand != null) {
			selectedCommand.execute();
			System.out.println(selectedCommand);
		}
			
		if(selectedCommand == null || selectedCommand.isFinished()) {
			//Current command is finished, move on to the next one.
			currentStage++;
			if(selectedCommand != null) {
				selectedCommand.end();
			}
			if(currentStage < stages.size()) {
				selectedCommand = stages.get(currentStage).getSelected();
				if(selectedCommand != null) {
					selectedCommand.initialize();
					selectedCommand.start();
				}
			} else if(currentStage == stages.size()) {
				selectedCommand = pgc;
				if(selectedCommand != null) {
					System.out.println("Starting...");
					selectedCommand.initialize();
					selectedCommand.start();
				}
			}
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
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
		if(selectedCommand != null)
			selectedCommand.end();
	}
}

