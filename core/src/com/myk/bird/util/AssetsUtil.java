package com.myk.bird.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.myk.bird.res.Res;

public class AssetsUtil {
    private static AssetManager assetManager;

    public static TextureAtlas atlas;
    public static BitmapFont bitmapFont;
    public static void init(){
        assetManager = new AssetManager();
    }
    public static void load(){
        //		加载资源
        assetManager.load(Res.Atlas.ATLAS_PATH, TextureAtlas.class);
        assetManager.load(Res.Audios.AUDIO_DIE, Sound.class);
        assetManager.load(Res.Audios.AUDIO_HIT, Sound.class);
        assetManager.load(Res.Audios.AUDIO_TOUCH, Sound.class);
        assetManager.load(Res.Audios.AUDIO_RESTART, Sound.class);
        assetManager.load(Res.Audios.AUDIO_SCORE, Sound.class);
        assetManager.load(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);
    }

    public static boolean loadFinished(){
//        加载图集和文字集
        atlas = assetManager.get(Res.Atlas.ATLAS_PATH);
        bitmapFont = assetManager.get(Res.FPS_BITMAP_FONT_PATH);
        return true;
    }

    /**
     * 等待资源加载完毕
     * @return
     */
    public static boolean update(){
        return assetManager.update();
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

//    资源管理器需要手动释放
    public static void dispose(){
        assetManager.dispose();
        assetManager = null;
        atlas = null;
        bitmapFont = null;
    }
}
