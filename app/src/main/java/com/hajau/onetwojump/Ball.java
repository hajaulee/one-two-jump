package com.hajau.onetwojump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Ball extends Item{
	private Matrix m;
	private float angularSpeed;
	private float t = 0;
	private float giatoc = 4;
	public Ball(GameView gameView, Bitmap bmp, float y, float w) {
		this.bmp = bmp;
		this.gameView = gameView;
		m = new Matrix();
		setPos(500, y);
		setAngularSpeed(w);
	}
	
	public void setAngularSpeed(float w){
		this.angularSpeed = w;
	}
	public float getAngularSpeed(){
		return this.angularSpeed;
	}
	@Override
	public void setPos(float x, float y){
		super.setPos(x, y);
		m.setTranslate(x, y);
		m.postRotate( angularSpeed += gameView.getGameSpeed(), getX() + getWidth()/2, getY() + getHeight()/2);
		if(angularSpeed < 360 && angularSpeed + gameView.getGameSpeed() >  360 )
			angularSpeed = 0;
	}
	public void jump(){
		float newY = getY() - getSpeedY() * t + giatoc * t * t;
		float ballInlandY = gameView.longland.getY() - this.getHeight() + 10;
		boolean flags = false;
		if(getY() < ballInlandY + 15 && newY > ballInlandY){			
			for(Land l: gameView.land){
				if(this.getRight() > l.getX() && this.getX() < l.getRight() )
					flags = true;
			}
			if(this.getRight() > gameView.longland.getX() && this.getX() < gameView.longland.getRight() )
				flags = true;
		}
		if(flags){
			setPos(getX(), (float) ballInlandY);
			setSpeedY(0);
			t = 0;
		}else{
			setPos(getX(), (float) (getY() - getSpeedY() * t + giatoc * t * t));
		}			
	}
	public boolean setJump(float v, float g){
		if(getBottom() > gameView.longland.getY() - 4 &&
				getBottom() < gameView.longland.getY() + 11){
			this.giatoc = g;
			this.t = 0;
			setSpeedY(v);
			setY(getY() - 1);
			MainActivity.playJump(gameView.sender);
			return true;
		}
		return false;
	}
	public boolean setJumpByDis(int dis){
		float nearestLandX = GameView.WIDTH;
		for(Land l : gameView.land){
			float distance = l.getX() - this.getX();
			if(distance > 100 && distance < nearestLandX)
				nearestLandX = distance;
		}
		Log.d("onTouch", "[Want:" + dis + "]  |  [Real Distance:" + nearestLandX + "]");
		if(dis == 200){			
			float spY = (float)(4 * nearestLandX / 75.38);
			if(nearestLandX < 250)
				return setJump(spY, 4);
			else
				return setJump((float)10.61, 4);
		}else{			
			float spY = (float)(1.1 * nearestLandX / 75.3);
			if(nearestLandX >= 250)
				return setJump(spY, (float) 1.1 );
			else
				return setJump((float)3.9, (float) 1.1);
		}
		
	}
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		t+= 0.1 * gameView.getGameSpeed()  / 5 ;
			jump();
			if(getBottom() > gameView.getHeight()){
				setPos(getX(), gameView.getHeight() - getHeight());
				gameView.lost = true;
				MainActivity.playBones();
			}
				
	}

	@Override
	protected void howToDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(gameView.lost)
			canvas.drawBitmap(bmp, getX(), getY(), null);
		else
			canvas.drawBitmap(bmp , m, null);
	}

}
