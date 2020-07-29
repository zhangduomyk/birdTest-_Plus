package com.myk.bird.group.base;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.myk.bird.MainGame;

public class BaseGroup extends Group {
    protected MainGame mainGame;

    public BaseGroup(MainGame mainGame) {
        this.mainGame = mainGame;
    }

    public MainGame getMainGame() {
        return mainGame;
    }
}
