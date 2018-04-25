package com.hajau.onetwojump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Item {

	private float x = 0; // sprite coordinate x
	private float y = 0; // sprite coordinate y
	private float xSpeed = 0; // sprite x speed
	private float ySpeed = 0; // sprite y speed
	private boolean running = true;
	protected Bitmap bmp; // Bitmap
	protected GameView gameView;
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setSpeed(float xSpeed, float ySpeed) {
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	public void setSpeedX(float vx){
		this.xSpeed = vx;
	}
	public void setSpeedY(float vy){
		this.ySpeed = vy;
	}
	public float getSpeedX(){
		return this.xSpeed;
	}
	public float getSpeedY(){
		return this.ySpeed;
	}
	public int getWidth(){
		return bmp.getWidth();
	}
	public int getHeight(){
		return bmp.getWidth();
	}
	public int getRight(){
		return (int)x + bmp.getWidth();
	}
	public int getBottom(){
		return (int)y + bmp.getHeight();
	}
	public void setRunning(boolean run){
		this.running = run;
	}
	abstract protected void update();
	abstract protected void howToDraw(Canvas canvas);
	void draw(Canvas canvas){
		if(running)
			update();
		howToDraw(canvas);
	}
}
