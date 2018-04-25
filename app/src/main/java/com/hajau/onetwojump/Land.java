package com.hajau.onetwojump;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Land extends Item{
	private boolean repeatable = false;
	private Random r = new Random();
	private int index;
	public Land(GameView gameView, Bitmap bmp, float x, float y, float vx, boolean repeat, int index){
		this.gameView = gameView;
		this.bmp = bmp;
		this.index = index;
		this.repeatable = repeat;
		setPos(x,y);
		setSpeed(vx, 0);
	}
	public int getIndex(){
		return index;
	}
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		if(getX() < gameView.ball.getX() &&
				getX() + getSpeedX() > gameView.ball.getX()){
			if((gameView.scores++ % 2 == 0 && gameView.scores < 20) ||
				(gameView.scores % 5 == 0 && gameView.scores > 20)	)
				gameView.changeGameSpeed((float) (gameView.getGameSpeed() + 0.5));
			if(gameView.scores % 22 == 0){
				MainActivity.playPortal();
				GameView.backgroundColor = Color.BLUE;
			}
			else if(gameView.scores % 11 == 0){
				MainActivity.playPortal();
				GameView.backgroundColor = Color.RED;
			}
			if(gameView.scores > 10 && (gameView.scores - 3) % 11 == 0){
				MainActivity.playPortal();
				GameView.backgroundColor = Color.WHITE;
			}
		}
		setX(getX() - getSpeedX());
		if(repeatable && getRight() < 0){
			setX(gameView.land[gameView.currentLastLandIndex].getRight()+
					+ 200 + 200 * r.nextInt(2));
			gameView.currentLastLandIndex = this.index;
		}
	}

	@Override
	protected void howToDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, getX(), getY(), null);
	}


}
