package ioio.examples.hello;

import java.util.Timer;
import java.util.TimerTask;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;

/**
 * this class handles all functionality of the robot's movement
 * @author �����
 */
public class MovmentSystem implements Stoppable{
	private static final float SHOLDER_LIM_UP = (float) 0.79;
	private static final float SHOLDER_LIM_DOWN = (float) 0.63;
	private static final float ELBOW_LIM_UP = (float) 0.11;
	private static final float ELBOW_LIM_DOWN = (float) 0.10;
	private static final float WRIST_LIM_UP = 36;
	private static final float WRIST_LIM_DOWN = 0;
	

	private ChassisFrame _chassis;
	private RoboticArmEdge _arm;
	private AnalogInput _wristPosition;
	private AnalogInput _sholderPosition;
	private AnalogInput _elbowPosition;
	private AnalogInput _distance;
	private Timer _stopTimer = new Timer("Stop Timer");
		
	

	
	public MovmentSystem(IOIO ioio, ChassisFrame chassis, RoboticArmEdge arm, int wristPositionPin, int sholderPositionPin, int elbowPositionPin, int distancePin) {
		_chassis = chassis;
		_arm = arm;
		
		try {
			_wristPosition = ioio.openAnalogInput(wristPositionPin);
			_sholderPosition = ioio.openAnalogInput(sholderPositionPin);
			_elbowPosition = ioio.openAnalogInput(elbowPositionPin);
			_distance = ioio.openAnalogInput(distancePin);
		} catch (ConnectionLostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * simple getter
	 * @return
	 */
	public RoboticArmEdge get_arm() {
		return _arm;
	}
	
	/**
	 * simple getter
	 * @return
	 */
	public ChassisFrame get_chassis() {
		return _chassis;
	}
	
	/**
	 * gets the elbow position by the potentiometer on the arm
	 * @return the elbow position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_elbowPosition() throws InterruptedException, ConnectionLostException {
		return _elbowPosition.read();
	}
	
	/**
	 * gets the shoulder position by the potentiometer on the arm
	 * @return the shoulder position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_sholderPosition() throws InterruptedException, ConnectionLostException {
		return _sholderPosition.read();
	}
	
	/**
	 * gets the wrist position by the potentiometer on the arm
	 * @return the wrist position
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_wristPosition() throws InterruptedException, ConnectionLostException {
		return _wristPosition.read();
	}
	
	/**
	 * gets the distance given by the distance sensor on the front of the robot
	 * @return returns the distance from the object in front of the rover
	 * @throws InterruptedException
	 * @throws ConnectionLostException
	 */
	public float get_distance() throws InterruptedException, ConnectionLostException {
		return _distance.read();
	}
	
	
	/**
	 * closes all relevant digital pins
	 */
	public void close() {
		_elbowPosition.close();
		_sholderPosition.close();
		_sholderPosition.close();
		_distance.close();
		_chassis.close();
		_arm.close();
		
	}
	
	
	/**
	 * move the shoulder in a certain degree
	 * @param degree the degree to move the arm, the sign of degree will determine the direction
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveSholder(double degree) throws ConnectionLostException, InterruptedException{
		
		double PositionToGet=degree*(RobotSettings.sholderMov);
		  while(get_sholderPosition()>PositionToGet){
			  _arm.sholderDown();
		  }
		  _arm.stop();
	}
	
	/**
	 * move the elbow in a certain degree
	 * @param degree the degree to move the arm, the sign of degree will determine the direction
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void moveElbow(double degree) throws ConnectionLostException, InterruptedException{
		double PositionToGet=degree*(RobotSettings.elbowMov);
		  while(get_elbowPosition()<PositionToGet){
			  _arm.elbowDown();
		  }
		  _arm.stop();
	}
	
	public void moveArm(double distance) throws ConnectionLostException, InterruptedException{
		double [] cube= new double [2];
		cube[0]=0;
		cube[1]=3;
		double [] D1_base={distance,18};
		double d1=9;
		double d2=11;
		double d3=4.5;
		double a3=(43.43)*(Math.PI/180);
		double [] D0=D1_base;
		double [] D3=cube;
		double b3=Math.sqrt(Math.pow(d2, 2)+Math.pow(d3, 2)-2*d2*d3*Math.cos(Math.PI-a3));

		double [] xx= new double [2];
		xx[0]=D0[0]-D3[0];
		xx[1]=D0[1]-D3[1];
		double b0 =Math.sqrt(Math.pow(xx[0], 2)+Math.pow(xx[1], 2));
		if(b0>d1+b3){
		this.moveForward(b0-(d1+b3)+3);
		this.moveArm(distance-(b0-(d1+b3)+3));
		return;
		}
		double beta0=Math.acos((Math.pow(b0, 2) - Math.pow(d1, 2)- Math.pow(b3, 2))/(-2*d1*b3));
		System.out.println(beta0);
		double gamma3=Math.asin(d3/(b3*Math.sin(Math.PI-a3)));
		double a2=Math.PI-beta0-gamma3;
		double beta3=Math.asin(b3/(b0*Math.sin(beta0)));
		double betax=Math.atan(xx[0]/xx[1]);
		double a1=Math.PI-betax-beta3;

		double a1_degrees=a1*(180/Math.PI);
		double a2_degrees=a2*(180/Math.PI);

		this.moveSholder(180-a1_degrees);
		this.moveElbow(180-a2_degrees);
		

	}
	
	/**
	 * moves the robot forwards x centimeters
	 * @param centimeters centimeters to move
	 * @throws ConnectionLostException
	 */
	public void moveForward(double centimeters) throws ConnectionLostException{
		long driveTime = (long) (RobotSettings.movmentSpeed / centimeters * 1000);
		_chassis.driveForward();
		_stopTimer.schedule(new StopMovment(_chassis), driveTime);
	}
	
	/**
	 * moves the robot backwards x centimeters
	 * @param centimeters centimeters to move
	 * @throws ConnectionLostException
	 */
	public void moveBackwards(double centimeters) throws ConnectionLostException{
		long driveTime = (long) (RobotSettings.movmentSpeed / centimeters * 1000);
		_chassis.driveBackwards();
		_stopTimer.schedule(new StopMovment(_chassis), driveTime);
	}

	@Override
	public void stop() throws ConnectionLostException {
		_arm.stop();
		_chassis.stop();
	}

	/**
	 * sets the drive speed of the rover
	 * @param speed the new speed value
	 * @throws ConnectionLostException
	 */
	public void setRoverSpeed(float speed) throws ConnectionLostException {
		_chassis.setSpeed(speed);
	}
	
	
	/**
	 * this classes implements the TimerTask abstract class
	 * the goal of this class is to stop a certain stoppable object 
	 * @author �����
	 *
	 */
	public class StopMovment extends TimerTask{
		private Stoppable _obj;
		public StopMovment(Stoppable obj) {
			_obj = obj;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("stoping...");
				_obj.stop();
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}// run()
	}// StopMovment
	
}
