package org.usfirst.frc.team2412.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class for managing AutonomousCommands and SmartDashboard.
 */
public class AutonomousCommandManager {
	/* Autonomous peg selecting stuff*/
	final String leftPeg = "Left Peg";
	final String centerPeg = "Center Peg";
	final String rightPeg = "Right Peg";
	String pegSelected;
	SendableChooser<String> pegChooser = new SendableChooser<>();
	
	final String visionOnlyAuto = "Vision only";
	final String encodersAuto = "Encoders";
	String autoSelected;

	SendableChooser<String> autoChooser = new SendableChooser<>();
	
	public static String selectedCommand;
	
	/**
	 * This constructor is called by the Robot class during robotInit().
	 */
	public AutonomousCommandManager() {
		//Setup SmartDashboard.
		pegChooser.addDefault("Center Peg", centerPeg);
		pegChooser.addObject("Left Peg", leftPeg);
		pegChooser.addObject("Right Peg", rightPeg);
		SmartDashboard.putData("Peg choices", pegChooser);
		
		autoChooser.addDefault("Encoders!", encodersAuto);
		autoChooser.addObject("Vision Tracking", visionOnlyAuto);
		SmartDashboard.putData("Auto choices", autoChooser);
	}
	
	/**
	 * This method is called by the Robot class in autonomousInit().
	 */
	public void autonomousInit() {
		pegSelected = pegChooser.getSelected();
		autoSelected = autoChooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Peg selected: " + pegSelected);
		System.out.println("Auto selected: " + autoSelected);
	}
	
	/**
	 * This method is called by the Robot class in autonomousPeriodic().
	 */
	public void autonomousPeriodic() {
		switch (pegSelected) {
		case centerPeg:
			// Put custom auto code here
			break;
		case leftPeg:
		default:
			// Put default auto code here
			break;
		}
	}
}
