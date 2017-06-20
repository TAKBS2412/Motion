package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.IterativeRobot;
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
	
	CANTalon talon;
	CANTalon talon2;
	
	final double encodertocmconv = 0.0239534386;
	
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
		pegChooser.addDefault("Center Peg", centerPeg);
		pegChooser.addObject("Left Peg", leftPeg);
		pegChooser.addObject("Right Peg", rightPeg);
		SmartDashboard.putData("Peg choices", pegChooser);
		
		autoChooser.addDefault("Encoders!", encodersAuto);
		autoChooser.addObject("Vision Tracking", visionOnlyAuto);
		SmartDashboard.putData("Auto choices", autoChooser);
		
		talon = new CANTalon(2);
		talon2 = new CANTalon(3);
		talon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		talon.reverseSensor(true); //Reverse the sensor
	}
	
	@Override
	public void teleopInit() {
		talon.setPosition(0); //Zero out the encoder in the beginning
		talon.configEncoderCodesPerRev(1);
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
		pegSelected = pegChooser.getSelected();
		autoSelected = autoChooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Peg selected: " + pegSelected);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
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

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
//		talon.set(0.1);
//		talon2.set(0.1);
		if(getPositionCm(talon) > 10) {
			System.out.println("STOP");
		} else {
			System.out.println("GO");
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
}

