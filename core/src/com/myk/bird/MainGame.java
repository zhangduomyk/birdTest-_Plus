package com.myk.bird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myk.bird.res.Res;
import com.myk.bird.screen.GameScreen;
import com.myk.bird.screen.LoadScreen;
import com.myk.bird.util.AssetsUtil;
import com.myk.bird.util.SoundPlayer;

public class MainGame extends Game {

//	屏幕大小
	private float worldWidth;
	private float worldHeight;
//	加载界面
	private LoadScreen loadScreen;
//	游戏界面
	private GameScreen gameScreen;
//	音效播放实例
	private SoundPlayer soundPlayer;

	@Override
	public void create () {
//		初始化资源管理器
		AssetsUtil.init();
//		屏幕适配
		worldWidth = Res.FIX_WORLD_WIDTH;
		worldHeight = Gdx.graphics.getHeight() * worldWidth / Gdx.graphics.getWidth();
//		授予音效播放权限，正式使用需要获取设备权限并进行判定
		soundPlayer = new SoundPlayer(true);
//		创建加载界面实例并设置为当前界面
		loadScreen = new LoadScreen(this);
		setScreen(loadScreen);
	}

	@Override
	public void render () {
//		红色清屏
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		loadScreen.dispose();
		gameScreen.dispose();
		AssetsUtil.dispose();
	}

	public float getWorldWidth() {
		return worldWidth;
	}

	public float getWorldHeight() {
		return worldHeight;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}
}
