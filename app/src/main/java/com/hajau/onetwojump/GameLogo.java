package com.hajau.onetwojump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameLogo extends Item{
	private float t = 0;
	private float v = 0;
	private int count = 4;
	public GameLogo(Bitmap bmp){
		this.bmp = bmp;
		setPos((GameView.WIDTH - bmp.getWidth()) / 2, -500);
	}
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		t+= 0.1;
		float newY = getY() - v * t + 4 * t * t;
		if(newY  > (GameView.HEIGHT - this.getHeight())/ 2 ){
			t = 0;
			setY((GameView.HEIGHT - this.getHeight())/ 2);
			if(count >= 0){
				v =  4 * (count--);
			}
			else{
				GameView.setgameSceen(GameView.GAME_MENU);
				this.setRunning(false);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
			setY(newY );
	}

	@Override
	protected void howToDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, getX(), getY(), null);
	}

}
