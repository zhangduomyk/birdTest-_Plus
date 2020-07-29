package com.myk.bird.group;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.myk.bird.MainGame;
import com.myk.bird.group.base.BaseGroup;
import com.myk.bird.res.Res;
import com.myk.bird.util.AssetsUtil;

public class OverGroup extends BaseGroup {
    public OverGroup(MainGame mainGame) {
        super(mainGame);
        init();
    }

    private Button button;
    private Sound restartSound;

    private void init() {
        button = new ImageButton(
                new TextureRegionDrawable(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 1)),
                new TextureRegionDrawable(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 2))
        );

        restartSound = AssetsUtil.getAssetManager().get(Res.Audios.AUDIO_RESTART, Sound.class);
        button.setX(Res.FIX_WORLD_WIDTH/2 - button.getWidth()/2);
        button.setY(Res.FIX_WORLD_HEIGHT/2 - button.getHeight()/2);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                restartSound.play();
                mainGame.getGameScreen().restartGame();
            }
        });


        addActor(button);
    }

    public Button getButton() {
        return button;
    }
}
