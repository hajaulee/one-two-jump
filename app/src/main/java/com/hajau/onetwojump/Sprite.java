package com.hajau.onetwojump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite extends Item{
	
	
	public Sprite(Bitmap bmp) {
		this.bmp = bmp;
		setPos(10,10);
		setSpeed(25,25);
	}

	public Sprite(Bitmap bmp, float x, float y) {
		this.bmp = bmp;
		setPos(x,y);
		setSpeed(25,25);
	}
	
	public Sprite(Bitmap bmp, float x, float y, float vx, float vy) {
		this.bmp = bmp;
		setPos(x,y);
		setSpeed(vx,vy);
	}
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		// boundaries collision for east / west
		if (getX() > GameView.WIDTH - bmp.getWidth() - getSpeedX()) {
			setSpeedX(-getSpeedX());
		}
		if (getX() + getSpeedX() < 0) {
			setSpeedX(15);
		}
		setX(getX() + getSpeedX());

		// boundaries collision for north /south
		if (getY() > GameView.HEIGHT - bmp.getHeight() - getSpeedY()) {
			setSpeedY(-getSpeedY());	        
		}
		if (getY() + getSpeedY() < 0) {
			setSpeedY(15);
		}
		setY(getY() + getSpeedY());
	}

	@Override
	protected void howToDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, getX(), getY(), null);
	}
}
