package org.usfirst.frc.team2412.robot;

public class TestCommand extends Command2 {
	private double duration;
	
	private long startuptime;
	public TestCommand(double _duration) {
		this.duration = _duration;
	}
	
	/**
	 * Called when the command first starts.
	 */
	public void initialize() {
		this.startuptime = System.nanoTime();
		System.out.println("Preparing for testing...");
	}
	
	/**
	 * Determines if the command is finished.
	 */
	protected boolean isFinished() {
		System.out.println(startuptime);
		long deltaTime = System.nanoTime() - startuptime; 
		return deltaTime < 0 || deltaTime > duration; 
	}
	
	/**
	 * Called periodically when the command is running.
	 */
	public void execute() {
		System.out.println("Testing in progress...");
	}
	
	/**
	 * Called when the command ends.
	 */
	public void end() {
		System.out.println("Testing complete!");
	}
}
