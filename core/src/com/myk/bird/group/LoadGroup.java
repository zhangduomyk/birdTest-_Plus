package com.myk.bird.group;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.myk.bird.MainGame;
import com.myk.bird.group.base.BaseGroup;
import com.myk.bird.res.Res;

public class LoadGroup extends BaseGroup {

    public LoadGroup(MainGame mainGame, Texture texture) {
        super(mainGame);
        init(texture);
    }

    private Image loadImage;

    private void init(Texture texture) {
        loadImage = new Image(texture);
        loadImage.setSize(Res.FIX_WORLD_WIDTH,Res.FIX_WORLD_HEIGHT);
        addActor(loadImage);
    }
}
