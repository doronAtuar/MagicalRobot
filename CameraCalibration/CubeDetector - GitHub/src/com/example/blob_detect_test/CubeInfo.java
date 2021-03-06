package com.example.blob_detect_test;

import java.util.ArrayList;

public class CubeInfo {
	
	private static CubeInfo instance = null;
	private double horizontalLocation;
	private double distance;
	private Color color;
	private boolean found;
	
	
	protected CubeInfo(){
		this.horizontalLocation = -100;
		this.distance = -1;
		found = false;
	}
	
	public void setHorizontalLocation(double location){
		this.horizontalLocation = location;
	}
	
	public void setDistance(double dist){
		this.distance = dist;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setFound(boolean found){
		this.found = found;
	}
	
	public boolean getFound(){
		return this.found;
	}
	
	public double getHorizontalLocation(){
		return this.horizontalLocation;
	}
	
	public double getDistance(){
		return this.distance;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public static CubeInfo getInstance(){
		if (instance == null){
			instance = new CubeInfo();
		} 
		return instance;
	}
	
	
}
