package com.hajau.onetwojump;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Cloneable{
	private SurfaceHolder holder;
	public GameLoopThread gameLoopThread;
	public Context sender;
	public static int WIDTH;
	public static int HEIGHT;
	public static int backgroundColor = Color.WHITE;
	public static boolean soundOn = false;
	public static boolean musicOn = false;
	public static final int GAME_PLAY = 0;
	public static final int GAME_MENU = 1;
	public static final int GAME_SCORE = 2;
	public static final int GAME_LOGO = 3;
	public static final int GAME_OPTIONS = 4;
	public static int gameScreen = GAME_LOGO;
	
	public Ball ball;
	
	public HaJaUButton retryButton;
	public HaJaUButton menuButton;
	public HaJaUButton playButton;
	public HaJaUButton scoreButton;
	public HaJaUButton optionButton;
	public HaJaUButton backButton;
	public HaJaUButton musicButton;
	public HaJaUButton soundButton;
	public Land score_bg;
	public Land longland;
	public Land[] land = new Land[10];
	public GameLogo logo;
	
	private Paint paint;
	private Paint p1;
	private Paint p2;
	private Bitmap note;
	
	public int currentLastLandIndex = land.length - 1;
	public int nextLand = 0;
	public int scores = 0;
	public boolean firstPlay = true;
	private float gameSpeed = 5;
	private int hightscore = 0;
	public boolean lost = false;
	private String dateHightScore = "";
	public GameView(Context context) {
		super(context);
		sender = context;
		gameLoopThread = new GameLoopThread(this);

		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread = new GameLoopThread(GameView.this);
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}
		});
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		// set value for WIDTH HEIGHT
		WIDTH = this.getWidth();
		HEIGHT = this.getHeight();
		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);
		paint.setTextSize(160);
		
		p1 = new Paint();
		p1.setStyle(Paint.Style.FILL);
		p1.setAntiAlias(true);
		p1.setARGB(255, 255, 0, 0);
		p1.setTextSize(200);
		p1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		p1.setTextAlign(Align.CENTER);
		
		p2 = new Paint();
		p2.setStyle(Paint.Style.FILL);
		p2.setAntiAlias(true);
		p2.setARGB(100, 0, 255, 0);
		p2.setTextSize(40);
		p2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		p2.setTextAlign(Align.CENTER);
		
		getSavedMusicSound("rina.Ishihara");
		loadGameLogo();
		loadMenu();
		resetGamePlay();
		loadScoreMenu();
		loadOptionsMenu();
		hightscore = Integer.parseInt(getSavedHightScore("tokuda.shigeo"));

		setGameRunning(false);
	}
	public void loadGameLogo(){
		Bitmap logoImage = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		logo = new GameLogo(logoImage);
	}
	public void loadScoreMenu(){
		Bitmap score_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.score_bg);
		Bitmap backImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);
		float scale = (float) (1.0 * score_bgImage.getWidth() /  WIDTH);
		score_bgImage = Bitmap.createScaledBitmap(score_bgImage, WIDTH, (int) (scale * score_bgImage.getHeight()), false);
		score_bg = new Land(this, score_bgImage, 0, HEIGHT - score_bgImage.getHeight(), 0, false, 0);
		note = BitmapFactory.decodeResource(getResources(), R.drawable.note);
		backButton = new HaJaUButton(backImage, (WIDTH - backImage.getWidth()) / 2, HEIGHT - backImage.getHeight() - 20, 20,
				HEIGHT -backImage.getHeight() - 20);
	
	}
	public void loadOptionsMenu(){
		Bitmap score_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.score_bg);
		Bitmap backImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);
		
		float scale = (float) (1.0 * score_bgImage.getWidth() /  WIDTH);
		score_bgImage = Bitmap.createScaledBitmap(score_bgImage, WIDTH, (int) (scale * score_bgImage.getHeight()), false);
		score_bg = new Land(this, score_bgImage, 0, HEIGHT - score_bgImage.getHeight(), 0, false, 0);
		backButton = new HaJaUButton(backImage, (WIDTH - backImage.getWidth()) / 2, HEIGHT - backImage.getHeight() - 20, 20,
				HEIGHT -backImage.getHeight() - 20);
		changeMusicStatus();
		changeSoundStatus();
	}
	public void changeMusicStatus(){
		Bitmap music_onImage = BitmapFactory.decodeResource(getResources(), R.drawable.music_on);
		Bitmap music_offImage = BitmapFactory.decodeResource(getResources(), R.drawable.music_off);
		Bitmap musicImage = musicOn?music_onImage:music_offImage;
		musicButton = new HaJaUButton(musicImage,  (WIDTH - musicImage.getWidth()) /2, HEIGHT,
					(WIDTH - musicImage.getWidth()) /2 , 100);

		MainActivity.playMusic(sender, 40);
	}
	public void changeSoundStatus(){
		Bitmap sound_onImage = BitmapFactory.decodeResource(getResources(), R.drawable.sound_on);
		Bitmap sound_offImage = BitmapFactory.decodeResource(getResources(), R.drawable.sound_off);
		Bitmap soundImage = soundOn?sound_onImage:sound_offImage;
		soundButton = new HaJaUButton(soundImage,  (WIDTH - soundImage.getWidth()) /2, HEIGHT + 500,
				(WIDTH - soundImage.getWidth()) /2 , 250, false);
	
	}
	public void loadMenu(){
		Bitmap playImage = BitmapFactory.decodeResource(getResources(), R.drawable.play);
		Bitmap scoreImage = BitmapFactory.decodeResource(getResources(), R.drawable.scores);
		Bitmap optionImage = BitmapFactory.decodeResource(getResources(), R.drawable.options);

		playButton = new HaJaUButton(playImage, WIDTH, 100,
				(WIDTH - playImage.getWidth()) /2 , 100);
		scoreButton = new HaJaUButton(scoreImage, - scoreImage.getWidth(), 250,
				(WIDTH - scoreImage.getWidth()) /2 , 250);
		optionButton = new HaJaUButton(optionImage, (WIDTH - optionImage.getWidth()) /2, HEIGHT,
				(WIDTH - optionImage.getWidth()) /2 , 400);
	}
	public void changeGameSpeed(float speed){
		this.setGameSpeed(speed);
		for(Land l: land)
			l.setSpeedX(gameSpeed);
	}
	public void resetGamePlay() {
		lost = false;
		scores = 0;
		currentLastLandIndex = land.length - 1;
		nextLand = 0;
		backgroundColor = Color.WHITE;
		Bitmap ballImage = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
		Bitmap longlandImage = BitmapFactory.decodeResource(getResources(), R.drawable.longland);
		Bitmap landImage = BitmapFactory.decodeResource(getResources(), R.drawable.land);
		Bitmap retryImage = BitmapFactory.decodeResource(getResources(), R.drawable.retry);
		Bitmap menuImage = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
		
		Random ran = new Random();
		longland = new Land(this, longlandImage, 490, HEIGHT - 200, gameSpeed, false, 0);
		land[0] =  new Land(this, landImage, longland.getRight() + 400,
				HEIGHT - 200, gameSpeed, true, 0);
		for (int i = 1; i < land.length; i++)
			land[i] = new Land(this, landImage, longland.getRight() + 200 * (ran.nextInt(2) + i + 1),
					HEIGHT - 200, gameSpeed, true, i);

		ball = new Ball(this, ballImage, longland.getY() - 2 * ballImage.getHeight(), gameSpeed);
		retryButton = new HaJaUButton(retryImage, (WIDTH - retryImage.getWidth()) / 2, HEIGHT - retryImage.getHeight() - 20,
				WIDTH - retryImage.getWidth() - 20, HEIGHT - retryImage.getHeight() - 20);
		menuButton = new HaJaUButton(menuImage, (WIDTH - menuImage.getWidth()) / 2, HEIGHT - menuImage.getHeight() - 20, 20,
				HEIGHT - menuImage.getHeight() - 20);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(backgroundColor);
			switch (gameScreen) {
				case GAME_LOGO:
					this.drawGameLogo(canvas);
					break;
				case GAME_MENU:
					this.drawGameMenu(canvas);
					break;
				case GAME_SCORE:
					this.drawGameScore(canvas);
					break;
				case GAME_PLAY:
					this.drawGamePlay(canvas);
					break;
				case GAME_OPTIONS:
					this.drawGameOptions(canvas);
					break;
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void drawGamePlay(Canvas canvas){
		canvas.drawText("" + scores, WIDTH / 2, 150, paint);
		if(firstPlay){
			Paint x = new Paint();
			x.setColor(Color.GREEN);
			canvas.drawRect(0, 0, WIDTH /2 , HEIGHT, x);
			x.setColor(Color.YELLOW);
			canvas.drawRect(WIDTH /2 , 0, WIDTH, HEIGHT, x);
			canvas.drawText("Nhảy", WIDTH * 3/ 4, HEIGHT / 2 - 60, paint);
			canvas.drawText("Nhảy", WIDTH * 1/ 4, HEIGHT / 2 - 60, paint);
			canvas.drawText("gần", WIDTH * 1/4, HEIGHT / 2 + 150, paint);
			canvas.drawText("xa", WIDTH * 3 / 4, HEIGHT / 2 + 150, paint);
		}
		if (longland.getRight() > 0)
			longland.draw(canvas);
		for (Land l : land)
			l.draw(canvas);
		ball.draw(canvas);
		if (lost) {
			this.setGameSpeed(5);
			if(scores > hightscore){
				hightscore = scores;
				SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
				Date d = new Date();
				dateHightScore =  dt.format(d);
				setFileContentToDataDir("tokuda.shigeo", hightscore + "\n" + dateHightScore);
			}
			setGameRunning(false);
			menuButton.draw(canvas);
			retryButton.draw(canvas);
		}
	}
	public void setGameRunning(boolean run){
		longland.setRunning(run);
		ball.setRunning(run);
		for (Land l : land)
			l.setRunning(run);
	}
	public static void setgameSceen(int screen){
		gameScreen = screen;
	}
	public boolean isMenuScreen(){
		return (gameScreen == GAME_MENU);
	}
	public boolean isScoreScreen(){
		return (gameScreen == GAME_SCORE);
	}
	public boolean isPlayScreen(){
		return (gameScreen == GAME_PLAY);
	}
	public boolean isOptionsScreen(){
		return (gameScreen == GAME_OPTIONS);
	}
	public boolean isLogoScreen(){
		return (gameScreen == GAME_LOGO);
	}
	private void drawGameLogo(Canvas canvas){
		logo.draw(canvas);
	}
	private void drawGameOptions(Canvas canvas){
		score_bg.draw(canvas);
		backButton.draw(canvas);
		musicButton.draw(canvas);
		soundButton.draw(canvas);
	}
	private void drawGameMenu(Canvas canvas){
		playButton.draw(canvas);
		scoreButton.draw(canvas);
		optionButton.draw(canvas);
	}
	@SuppressLint("SimpleDateFormat")
	private void drawGameScore(Canvas canvas){
		canvas.drawBitmap(note, (WIDTH - note.getWidth()) / 2, 50, null);
		score_bg.draw(canvas);
		backButton.draw(canvas);
		canvas.drawText("" + hightscore , (WIDTH / 2), 50 + note.getHeight() / 2, p1);
		canvas.drawText(dateHightScore, WIDTH / 2, note.getHeight() + 30 , p2);
	}
	public boolean setFileContentToDataDir(String fileName, String content) {
		File file = new File(sender.getApplicationInfo().dataDir + "/" + fileName);
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(file));
			br.write(content);
			br.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public void getSavedMusicSound(String fileName) {
		File file = new File(sender.getApplicationInfo().dataDir + "/" + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String mu = br.readLine();
			String so = br.readLine();
			musicOn = mu != null && mu.equals("false")?false: true;
			soundOn = so != null && so.equals("false")?false: true;
			br.close();
		} catch (Exception e) {
			musicOn = true;
			soundOn = true;
		}
	}
	public String getSavedHightScore(String fileName) {
		File file = new File(sender.getApplicationInfo().dataDir + "/" + fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = br.readLine();
			dateHightScore = br.readLine();
			if(dateHightScore == null)
				dateHightScore = "  ";
			br.close();
			return s;
		} catch (Exception e) {
			return "0";
		}
	}

	public float getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(float gameSpeed) {
		this.gameSpeed = gameSpeed;
	}
	 protected Object clone() throws CloneNotSupportedException {
	        return super.clone();
	 }
}
