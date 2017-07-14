package org.usfirst.frc.team2412.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class for managing AutonomousCommands and SmartDashboard.
 */
public class AutonomousCommandManager {
	/* Autonomous peg selecting stuff*/
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

//	SendableChooser<String> autoChooser = new SendableChooser<>();
	
	public static String selectedCommand;
	
	public static int currentStage = 0;
	
	DriveForTimeCommand dftc;
	TestCommand testcmd;
	TestCommand1 testcmd1;
	TestCommand2 testcmd2;
	
	AutonomousStage as;
	AutonomousStage as1;
	
	AutonomousCommand ac;
	ArrayList<AutonomousStage> stages;
	
	Command2 selectedCommand2;
	/**
	 * This constructor is called by the Robot class during robotInit().
	 */
	public AutonomousCommandManager() {
		//Setup SmartDashboard.
		pegChooser.addObject("Drive Forward to Baseline (if selected, ignore stages 2 and 3)", driveForward);
		pegChooser.addDefault("Center Peg (if selected, ignore stage 2)", centerPeg);
		pegChooser.addObject("Left Peg", leftPeg);
		pegChooser.addObject("Right Peg", rightPeg);
		SmartDashboard.putData("Peg choices", pegChooser);
		
//		autoChooser.addDefault("Encoders!", encodersAuto);
//		autoChooser.addObject("Vision Tracking", visionOnlyAuto);
//		autoChooser.addObject("Time Based", timeBasedAuto);
//		SmartDashboard.putData("Auto choices", autoChooser);
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
	}
	
	/**
	 * This method is called by the Robot class in autonomousInit().
	 */
	public void autonomousInit() {
		stages = new ArrayList<AutonomousStage>();
		stages.add(as);
		stages.add(as1);
//		ac = new AutonomousCommand(stages);
//		ac.start();
		
		pegSelected = pegChooser.getSelected();
//		autoSelected = autoChooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
//		System.out.println("Peg selected: " + pegSelected);
//		System.out.println("Auto selected: " + autoSelected);
		
		currentStage = 0;
		
//		dftc = new DriveForTimeCommand(1, Robot.rd, 0.5d, 0.0d, 1E9);
		
		selectedCommand2 = stages.get(currentStage).getSelected();
		selectedCommand2.initialize();
		selectedCommand2.start();
	}
	
	/**
	 * This method is called by the Robot class in autonomousPeriodic().
	 */
	public void autonomousPeriodic() {
		if(currentStage >= stages.size()) return;
		Scheduler.getInstance().run();
		selectedCommand2.execute();
//		System.out.println(currentStage);
//		dftc.execute();
//		System.out.println(selectedCommand2);
		if(selectedCommand2.isFinished()) {
			System.out.println("Finished");
			//Current command is finished, move on to the next one.
			currentStage++;
			selectedCommand2.end();
			if(currentStage < stages.size()) {
				selectedCommand2 = stages.get(currentStage).getSelected();
				selectedCommand2.initialize();
				selectedCommand2.start();
			}
		}
	}
}
