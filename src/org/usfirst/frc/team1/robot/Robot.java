/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;                                                            //importing all of the classes to make the robot function
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.Console;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;

public class Robot extends IterativeRobot {
	
	VictorSP lMotor = new VictorSP(1);
	VictorSP rMotor = new VictorSP(0);
	VictorSP throwMotors = new VictorSP(2);
	
	Victor feed = new Victor(9);
	
	Joystick left = new Joystick(0);
	
	Timer timer = new Timer();
	
	boolean normalMode = false;
	boolean qbMode = false;
	boolean speedMode = false;
	boolean shuttleMode = false;
	boolean threeConeMode = false;
	boolean strengthMode = false;
	
	int mode = 0;
	int count = 0;
	
	@Override
	public void robotInit() {
		CameraServer server = CameraServer.getInstance();
		UsbCamera cam = server.startAutomaticCapture();
		cam.setExposureAuto();
		cam.setWhiteBalanceAuto();
		cam.setBrightness(80);
		cam.setResolution(640, 480);
		cam.setFPS(10);
		timer.start();
	}

	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {
		
	}
	
	@Override
	public void teleopPeriodic() {
		
		double leftTrigger = left.getRawAxis(2);
		double rightTrigger = left.getRawAxis(3);
		double lJoystickX = left.getRawAxis(0);
		double rJoystickY = -left.getRawAxis(5);
		double rJoystickX = left.getRawAxis(4);
		double rightSpeed = 0;
		double leftSpeed = 0;

		if (left.getRawButton(7)) {
			mode -= 1;
			timer.delay(.5);
			count = 0;
		}
		if (left.getRawButton(8)) {
			mode += 1;
			timer.delay(.5);
			count = 0;
		}
		
		switch (mode) {
			case 0 :
				if (count < 1) {
					System.out.println("\n\nNormal Mode Active\n\n");
				}
				normalMode = true;
				qbMode = false;
				speedMode = false;
				shuttleMode = false;
				threeConeMode = false;
				strengthMode = false;
				count ++;
				break;
			case 1 :
				if (count < 1) {
					System.out.println("\n\nQB Throwing Mode Active\n\n");
				}
				qbMode = true;
				normalMode = false;
				speedMode = false;
				shuttleMode = false;
				threeConeMode = false;
				strengthMode = false;
				count ++;
				break;
			case 2 :
				if (count < 1) {
					System.out.println("\n\nSpeed Test Mode Active\n\n");
				}
				speedMode = true;
				normalMode = false;
				qbMode = false;
				shuttleMode = false;
				threeConeMode = false;
				strengthMode = false;
				count ++;
				break;
			case 3 :
				if (count < 1) {
					System.out.println("\n\n60 Foot Shuttle Test Mode Active\n\n");
				}
				shuttleMode = true;
				normalMode = false;
				qbMode = false;
				speedMode = false;
				threeConeMode = false;
				strengthMode = false;
				count ++;
				break;
			case 4 :
				if (count < 1) {
					System.out.println("\n\n3 Cone Drill Test Mode Active\n\n");
				}
				threeConeMode = true;
				normalMode = false;
				qbMode = false;
				speedMode = false;
				shuttleMode = false;
				strengthMode = false;
				count ++;
				break;
			case 5 :
				if (count < 1) {
					System.out.println("\n\nStrength Test Mode Active\n\n");
				}
				strengthMode = true;
				normalMode = false;
				qbMode = false;
				speedMode = false;
				shuttleMode = false;
				threeConeMode = false;
				count ++;
				break;
		}
		
		if (normalMode) {
			
			if (rightTrigger > 0.05) {
				rightSpeed = -rightTrigger;
				leftSpeed = rightTrigger;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = leftTrigger;
				leftSpeed = -leftTrigger;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed *= lJoystickX + 1;	
			}
			else if (lJoystickX < -0.1) {
				leftSpeed *= lJoystickX + 1;
			}
			if (rJoystickX > 0.1) {
				rightSpeed = rJoystickX;
				leftSpeed = rJoystickX;
			}
			else if (rJoystickX < -0.1) {
				rightSpeed = rJoystickX;
				leftSpeed = rJoystickX;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
			
			
		}
		else if (qbMode) {
			
			if (left.getRawButton(1)) {
				feed.set(-0.5);
				timer.delay(.7);
				feed.set(0);
				throwMotors.set(-.4);
				timer.delay(.25);
				throwMotors.set(-.15);
				timer.delay(.5);
				throwMotors.set(0);
			}
			if(left.getRawButton(2)) {
				throwMotors.set(0.75);
				timer.delay(1);
				feed.set(0.5);
				timer.delay(0.6);
				feed.set(0);
				timer.delay(1);
				throwMotors.set(0);
			}
			if(left.getRawButton(3)) {
				throwMotors.set(0.56);
				timer.delay(1);
				feed.set(0.5);
				timer.delay(0.6);
				feed.set(0);
				timer.delay(1);
				throwMotors.set(0);
			}
			if (left.getRawButton(4)) {
				throwMotors.set(0.95);
				timer.delay(1);
				feed.set(0.5);
				timer.delay(0.6);
				feed.set(0);
				timer.delay(1);
				throwMotors.set(0);
			}
			if (left.getRawButton(6)) {
				feed.set(-0.5);
				timer.delay(.8);
				feed.set(0);
			}
			if (left.getRawButton(5)) {
				feed.set(0.5);
				timer.delay(.5);
				feed.set(0);
			}
			if (left.getRawButton((9))) {
				throwMotors.set(.1);
				timer.delay(.15);
				throwMotors.set(-.1);
				timer.delay(.15);
				throwMotors.set(0);
			}
			if (left.getRawButton(10)) {
				throwMotors.set(.2);
				timer.delay(.1);
				throwMotors.set(-.2);
				timer.delay(.15);
				throwMotors.set(0);
			}
			/*if (rightTrigger > 0.05) {
				rightSpeed = rightTrigger/2;
				leftSpeed = -rightTrigger/2;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = -leftTrigger/2;
				leftSpeed = leftTrigger/2;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed *= lJoystickX + 1;	
			}
			else if (lJoystickX < -0.1) {
				leftSpeed *= lJoystickX + 1;
			}*/
			if (rJoystickX > 0.1) {
				rightSpeed = rJoystickX/6;
				leftSpeed = rJoystickX/6;
			}
			else if (rJoystickX < -0.1) {
				rightSpeed = rJoystickX/6;
				leftSpeed = rJoystickX/6;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
		}
		else if (speedMode) {
			if (rightTrigger > 0.05) {
				rightSpeed = rightTrigger;
				leftSpeed = -rightTrigger;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = -leftTrigger;
				leftSpeed = leftTrigger;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed *= lJoystickX + 1;	
			}
			else if (lJoystickX < -0.1) {
				leftSpeed *= lJoystickX + 1;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
		}
		else if (shuttleMode) {
			if (rightTrigger > 0.05) {
				rightSpeed = rightTrigger/2;
				leftSpeed = -rightTrigger/2;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = -leftTrigger/2;
				leftSpeed = leftTrigger/2;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed *= lJoystickX + 1;
			}
			else if (lJoystickX < -0.1) {
				leftSpeed *= lJoystickX + 1;
			}
			if (rJoystickX > 0.05) {
				rightSpeed = -rJoystickX/4;
				leftSpeed = -rJoystickX/4;
			}
			else if (rJoystickX < -0.05) {
				rightSpeed = -rJoystickX/4;
				leftSpeed = -rJoystickX/4;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
		}
		else if (threeConeMode) {
			if (rightTrigger > 0.05) {
				rightSpeed = rightTrigger/1.5;
				leftSpeed = -rightTrigger/1.5;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = -leftTrigger/1.5;
				leftSpeed = leftTrigger/1.5;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed += (lJoystickX - 0.25);
			}
			else if (lJoystickX < -0.1) {
				leftSpeed += (-lJoystickX - 0.25);
			}
			if (rJoystickX > 0.1) {
				rightSpeed = -rJoystickX/2;
				leftSpeed = -rJoystickX/2;
			}
			else if (rJoystickX < -0.1) {
				rightSpeed = -rJoystickX/2;
				leftSpeed = -rJoystickX/2;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
		}
		else if (strengthMode) {
			if (rightTrigger > 0.05 && left.getRawButton(1) && left.getRawButton(5)) {
				rightSpeed = rightTrigger/2;
				leftSpeed = -rightTrigger/2;
			}
			else if (rightTrigger > 0.05 && left.getRawButton(1)) {
				rightSpeed = rightTrigger * 0.75;
				leftSpeed = -rightTrigger * 0.75;
			}
			else if (rightTrigger > 0.05) {
				rightSpeed = rightTrigger;
				leftSpeed = -rightTrigger;
			}
			else if (leftTrigger > 0.05) {
				rightSpeed = -leftTrigger;
				leftSpeed = leftTrigger;
			}
			if (lJoystickX > 0.1) {
				lJoystickX *= -1;
				rightSpeed *= lJoystickX + 1;	
			}
			else if (lJoystickX < -0.1) {
				leftSpeed *= lJoystickX + 1;
			}
			if (rJoystickX > 0.1) {
				rightSpeed = -rJoystickX/2;
				leftSpeed = -rJoystickX/2;
			}
			else if (rJoystickX < -0.1) {
				rightSpeed = -rJoystickX/2;
				leftSpeed = -rJoystickX/2;
			}
			lMotor.set(leftSpeed);
			rMotor.set(rightSpeed);
		}
		
	}
	
	@Override
	public void testPeriodic() {
	}
	
}