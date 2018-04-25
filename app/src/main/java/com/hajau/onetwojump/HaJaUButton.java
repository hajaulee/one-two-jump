package com.hajau.onetwojump;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class HaJaUButton extends Item {
	private int dx;
	private int dy;
	private float t = 0;
	private boolean normalButton = true;
	public HaJaUButton(Bitmap bmp, int x, int y, int dx, int dy) {
		this.bmp = bmp;
		setPos(x, y);
		this.dx = dx;
		this.dy = dy;
	}
	public HaJaUButton(Bitmap bmp, int x, int y, int dx, int dy, boolean nb) {
		this.bmp = bmp;
		setPos(x, y);
		this.dx = dx;
		this.dy = dy;
		this.normalButton = nb;
	}
	@Override
	protected void update() {
		// TODO Auto-generated method stub
		t++;
		int mspx = dx > getX() ? 1 : -1;
		int mspy = dy > getY() ? 1 : -1;
		float newX = (float) (getX() + mspx * (0.4 * t + 0.1 * t * t));
		float newY = (float) (getY() + mspy * (0.4 * t + 0.1 * t * t));
		setPos(mspx * newX < mspx * dx ? newX: dx, getY());
		setPos(getX(), mspy * newY < mspy * dy ? newY: dy);
	}

	public boolean isClicked(int x, int y) {
		if (x > getX() && x < getRight() && y > getY() && y < getBottom()){
			if(normalButton)
				MainActivity.playClick();
			return true;
		}
		return false;

	}

	@Override
	protected void howToDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, getX(), getY(), null);
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

}
