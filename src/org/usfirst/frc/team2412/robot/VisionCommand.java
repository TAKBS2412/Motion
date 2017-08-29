package org.usfirst.frc.team2412.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;

public class VisionCommand extends Command2 {
	
	private RobotDrive rd;
	private CANTalon[] talons;
	//Variables for detecting whether targets weren't found three times in a row.
	private boolean targetsFoundLast;
	private boolean targetsFoundSecondLast;
	public VisionCommand(CANTalon[] _talons) {
		this.talons = _talons;
	}
	
	/**
	 * Called when the command starts.
	 */
	public void start() {
		rd = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
//		return Robot.visionTable.getBoolean("pegclose", false);
		return Math.random() > 0.7;
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		//Turn if the robot isn't lined up with the peg
		boolean targetsFound = Robot.visionTable.getBoolean("targetsFound", false);
		if(targetsFound || targetsFoundLast || targetsFoundSecondLast) {
			double angle = Robot.visionTable.getNumber("angle", -1);
			double distance = Robot.visionTable.getNumber("distance", -1);
			System.out.println("Angle: " + angle);
			System.out.println("Distance: " + distance);
			double angleToTurn = Math.min(angle, 1.0d);
			if(Math.abs(angle) < 0.03) {
				angleToTurn = 0d;
			} else {
				double visionDirection = Math.signum(angle);
				angleToTurn = 0.2*visionDirection;
			}
			rd.arcadeDrive(0.3d, angleToTurn, false);
			
		} else { //Targets haven't been found for three times in a row.
			//System.out.println("No targets found!");
		}
		//Update targetsFoundLast and targetsFoundSeconLast
		targetsFoundSecondLast = targetsFoundLast;
		targetsFoundLast = targetsFound;
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		rd.arcadeDrive(0.0d, 0.0d, false); //Stop driving
	}
}
