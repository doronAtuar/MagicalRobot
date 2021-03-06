package ioio.examples.hello;

import ioio.lib.api.exception.ConnectionLostException;
/*
 * this class represents the functionality that the rovers chassis provides
 */


public class ChassisFrame {
	BigMotorDriver _front;
	BigMotorDriver _back;
    //sides
    private static final boolean TURN_LEFT = false;
    private static final boolean TURN_RIGHT = true;
    
	public ChassisFrame(BigMotorDriver front, BigMotorDriver back) {
		_front = front;
		_back = back;
	}
	
	
	public void driveForward() throws ConnectionLostException{
		_front.turnBothMotors(TURN_LEFT);
		_back.turnBothMotors(TURN_RIGHT);
	}

	public void driveBackwards() throws ConnectionLostException{
		_front.turnBothMotors(TURN_RIGHT);
		_back.turnBothMotors(TURN_LEFT);
	}
	
	public void turnLeft() throws ConnectionLostException{
		_front.turnBothMotorsOposite(TURN_RIGHT);
		_back.turnBothMotorsOposite(TURN_LEFT);
	}
	
	public void turnRight() throws ConnectionLostException{
		_front.turnBothMotorsOposite(TURN_LEFT);
		_back.turnBothMotorsOposite(TURN_RIGHT);
	}
	
	public void setSpeed(float speed) throws ConnectionLostException{
		_front.setMotorA_speed(speed);
		_back.setMotorA_speed(speed);
		_front.setMotorB_speed(speed);
		_back.setMotorB_speed(speed);
	}
	
	
	public void stop() throws ConnectionLostException {
		_front.stop();
		_back.stop();
	}
	
	public void close(){
		_back.close();
		_front.close();
	}
	
}
