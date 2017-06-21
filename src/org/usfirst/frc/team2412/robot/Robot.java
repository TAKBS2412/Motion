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
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	CANTalon leftTalon;
	CANTalon leftTalon2;
	CANTalon rightTalon;
	CANTalon rightTalon2;
	
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
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		leftTalon = new CANTalon(7);
		leftTalon2 = new CANTalon(6);
		
		rightTalon = new CANTalon(2);
		rightTalon2 = new CANTalon(3);
		
		rightTalon.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		rightTalon.reverseSensor(true); //Reverse the sensor
	
		//Make talon2 follow rightTalon
		rightTalon2.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightTalon2.set(rightTalon.getDeviceID());
		
		//Make left talons follow rightTalon
		leftTalon.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftTalon.set(rightTalon.getDeviceID());
		leftTalon2.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftTalon2.set(rightTalon.getDeviceID());
	}
	
	@Override
	public void teleopInit() {
		rightTalon.setPosition(0); //Zero out the encoder in the beginning
		rightTalon.configEncoderCodesPerRev(1);
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
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
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
		rightTalon.set(0.1); //Hopefully this will set talon2 as well.
//		talon2.set(0.1);
		if(getPositionCm(rightTalon) > 10) {
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

