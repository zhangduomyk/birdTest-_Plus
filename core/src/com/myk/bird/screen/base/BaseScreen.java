package com.myk.bird.screen.base;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.myk.bird.MainGame;

public class BaseScreen extends ScreenAdapter {
    protected Stage stage;
    protected MainGame game;
    public BaseScreen(MainGame game){
        this.game = game;
        stage = new Stage(new StretchViewport(
                game.getWorldWidth(),
                game.getWorldHeight()
        ));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    public MainGame getGame() {
        return game;
    }
}
