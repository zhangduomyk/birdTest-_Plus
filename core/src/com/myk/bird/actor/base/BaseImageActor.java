package com.myk.bird.actor.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myk.bird.MainGame;
import com.myk.bird.actor.framework.ImageActor;

/**
 * 演员基类
 * 
 * @xietansheng
 */
public abstract class BaseImageActor extends ImageActor {

    private MainGame mainGame;

    public BaseImageActor(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public BaseImageActor(MainGame mainGame, TextureRegion region) {
        super(region);
        this.mainGame = mainGame;
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    public void setMainGame(MainGame mainGame) {
        this.mainGame = mainGame;
    }

}















