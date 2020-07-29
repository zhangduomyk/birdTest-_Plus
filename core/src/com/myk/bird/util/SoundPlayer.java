package com.myk.bird.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {
    private boolean sound = true;

    public SoundPlayer(boolean sound){
        this.sound = sound;
    }

    public void playSound(String soundName){
        if(!AssetsUtil.getAssetManager().isLoaded(soundName)) return;
        if (!sound){
            Gdx.app.error("myk","未获取音频权限");
            return;
        }

        Sound sound = AssetsUtil.getAssetManager().get(soundName);
        sound.play();
    }
}
