package com.myk.bird.screen;

import com.badlogic.gdx.graphics.Texture;
import com.myk.bird.MainGame;
import com.myk.bird.group.LoadGroup;
import com.myk.bird.res.Res;
import com.myk.bird.screen.base.BaseScreen;
import com.myk.bird.util.AssetsUtil;

public class LoadScreen extends BaseScreen {

    private LoadGroup loadGroup;
    private Texture texture;
    private boolean isLoaded = true;

    public LoadScreen(MainGame mainGame){
        super(mainGame);
    }

    @Override
    public void show() {
//        获取加载等待界面图像
        AssetsUtil.getAssetManager().load("atlas/timg.gif",Texture.class);
        AssetsUtil.getAssetManager().finishLoading();
        texture = AssetsUtil.getAssetManager().get("atlas/timg.gif");
//        调用加载界面演员组展示界面
        loadGroup = new LoadGroup(getGame(),texture);
        loadGroup.setSize(Res.FIX_WORLD_WIDTH,Res.FIX_WORLD_HEIGHT);
//        添加演员到stage
        stage.addActor(loadGroup);
//        加载全部资源
        AssetsUtil.load();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
//        资源加载完毕跳转到游戏界面
        if (AssetsUtil.update()&&isLoaded==true){
            AssetsUtil.loadFinished();
            getGame().setGameScreen(new GameScreen(getGame()));
            getGame().setScreen(getGame().getGameScreen());
            isLoaded = false;

        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (texture!=null){
            texture.dispose();
        }
    }
}
