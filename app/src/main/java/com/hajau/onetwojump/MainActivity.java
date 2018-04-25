package com.hajau.onetwojump;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity {
	public static int x = 0;
	public static int y = 0;
	public GameView gameView = null;;
	public static MediaPlayer jump;
	public static MediaPlayer click;
	public static MediaPlayer bones;
	public static MediaPlayer portal;
	public static MediaPlayer soundtrack;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//GameView.setgameSceen(GameView.GAME_LOGO);
		jump = MediaPlayer.create(this, R.raw.jump);
		bones = MediaPlayer.create(this, R.raw.bones);
		click = MediaPlayer.create(this, R.raw.click);
		portal = MediaPlayer.create(this, R.raw.portal);
		soundtrack = MediaPlayer.create(this, R.raw.soundtrack);
		gameView = new GameView(this);
		gameView.setOnTouchListener(touch);
		setContentView(gameView);
		context = this;
	}
	public void onBackPressed(){
	     // do something here and don't write super.onBackPressed()
		switch (GameView.gameScreen) {
			case GameView.GAME_LOGO:
			case GameView.GAME_MENU:
				soundtrack.stop();
				this.finish();
				break;
			case GameView.GAME_SCORE:
			case GameView.GAME_OPTIONS:
				gameView.loadMenu();
				GameView.setgameSceen(GameView.GAME_MENU);
				break;
			case GameView.GAME_PLAY:
				if(gameView.lost){
					gameView.loadMenu();
					GameView.setgameSceen(GameView.GAME_MENU);					
				}
				break;
			
		}
	}
	public static void playJump(Context context){
		if(jump.isPlaying()){
			jump.stop();
			jump.release();
			jump = MediaPlayer.create(context, R.raw.jump);
		}
		if(GameView.soundOn)
			jump.start();
	}
	public static void playClick(){
		if(GameView.soundOn)
			click.start();
	}
	public static void playPortal(){
		if(portal.isPlaying()){
			portal.stop();
			portal.release();
			portal = MediaPlayer.create(context, R.raw.portal);
			
		}
		if(GameView.soundOn)
			portal.start();
	}
	public static void playBones(){
		if(GameView.soundOn)
			bones.start();
	}
	public static void playMusic(Context context, int vol){		
		if(soundtrack.isPlaying()){
				soundtrack.stop();
				soundtrack.release();
				soundtrack = MediaPlayer.create(context, R.raw.soundtrack);
				soundtrack.setLooping(true);
		}
		if(GameView.musicOn){
			int maxVolume = 50;
			float log1=(float)(Math.log(maxVolume-vol)/Math.log(maxVolume));
			soundtrack.setVolume(1 - log1, 1 - log1);
			soundtrack.start();
		}
	}
	OnTouchListener touch = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent e) {
			
			// TODO Auto-generated method stub
			x = (int)e.getX();
			y = (int)e.getY();
			if(gameView.isPlayScreen()){
				if(gameView.firstPlay){
					gameView.setGameRunning(true);
					gameView.firstPlay = false;
				}
				if(x > GameView.WIDTH /2)
					gameView.ball.setJumpByDis(400);
				else
					gameView.ball.setJumpByDis(200);
				if(gameView.lost){
					if(gameView.retryButton.isClicked(x, y)){
						gameView.resetGamePlay();
					}
					if(gameView.menuButton.isClicked(x, y)){
						GameView.backgroundColor = Color.WHITE;
						gameView.loadMenu();
						GameView.setgameSceen(GameView.GAME_MENU);
					}
				}
			}
			if(gameView.isLogoScreen()){
				gameView.loadMenu();
				GameView.setgameSceen(GameView.GAME_MENU);
			}
			if(gameView.isMenuScreen()){
				if(gameView.playButton.isClicked(x, y)){
					GameView.setgameSceen(GameView.GAME_PLAY);
					playMusic(MainActivity.this, 17);
				}
				if(gameView.optionButton.isClicked(x, y)){	
					GameView.backgroundColor = Color.WHITE;
					gameView.loadOptionsMenu();
					GameView.setgameSceen(GameView.GAME_OPTIONS);
				}
				if(gameView.scoreButton.isClicked(x, y)){
					gameView.loadScoreMenu();
					GameView.setgameSceen(GameView.GAME_SCORE);
				}
			}
			
			if(gameView.isScoreScreen()){
				if(gameView.backButton.isClicked(x, y)){
					gameView.loadMenu();
					GameView.setgameSceen(GameView.GAME_MENU);
				}
			}
			if(gameView.isOptionsScreen()){
				if(gameView.backButton.isClicked(x, y)){
					gameView.loadMenu();
					GameView.setgameSceen(GameView.GAME_MENU);
				}
				if(gameView.soundButton.isClicked(x, y)){
					GameView.soundOn = !GameView.soundOn;
					gameView.changeSoundStatus();
					playClick();
					gameView.setFileContentToDataDir("rina.Ishihara", GameView.musicOn + "\n" + GameView.soundOn);
				}
				if(gameView.musicButton.isClicked(x, y)){
					GameView.musicOn = !GameView.musicOn;
					gameView.changeMusicStatus();
					gameView.setFileContentToDataDir("rina.Ishihara", GameView.musicOn + "\n" + GameView.soundOn);
				}
			}
			return false;
		}
	};
}
