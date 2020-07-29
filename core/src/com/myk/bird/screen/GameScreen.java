package com.myk.bird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.myk.bird.MainGame;
import com.myk.bird.actor.BarActor;
import com.myk.bird.actor.BirdActor;
import com.myk.bird.actor.FloorActor;
import com.myk.bird.actor.framework.ImageActor;
import com.myk.bird.group.OverGroup;
import com.myk.bird.res.Res;
import com.myk.bird.screen.base.BaseScreen;
import com.myk.bird.util.AssetsUtil;
import com.myk.bird.util.CollisionUtils;
import com.myk.bird.util.GameState;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen extends BaseScreen {

    public GameScreen(MainGame mainGame){
        super(mainGame);
        init();
    }

    //    背景
    private ImageActor bgImage;
    //    地板
    private FloorActor floorImage;
    //    点击提示
    private ImageActor tapTipImage;
    //    准备提示
    private ImageActor getReadyActor;
    //    鸟
    private BirdActor birdActor;
    //    分
    private int score;
    //    分数显示
    private Label scoreLable;
    //    水管集合
    private CopyOnWriteArrayList<BarActor> barImageList = new CopyOnWriteArrayList<>();
    //    对象池
    private Pool<BarActor> barImagePool;
    //    初始鸟高度
    private float birdStartPositionY;
    //    下水管最小值
    private float minDownBarTopY;
    //    下水管最大值
    private float maxDownBarTopY;
    //   距离下次生成水管的时间累加器
    private float generateBarTimeCounter;
    //  游戏状态
    private GameState gameState;
    //    暂停按钮
    private Button button;
//    结束界面
    private OverGroup overGroup;


    public void init() {
//        用户输入设置到当前stage
        Gdx.input.setInputProcessor(stage);

        bgImage = new ImageActor(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_BG));
        bgImage.setSize(getGame().getWorldWidth(),getGame().getWorldHeight());
        bgImage.setPosition(stage.getWidth()/2,stage.getHeight()/2,Align.center);
        stage.addActor(bgImage);

        floorImage = new FloorActor(getGame());
        floorImage.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        floorImage.setCenterX(0);
        floorImage.setTopY(
                Math.min(
                        floorImage.getHeight(),
                        stage.getHeight() * (floorImage.getHeight() / getGame().getWorldHeight() / 2)
                )
        );
        floorImage.setWidth(1280);
        stage.addActor(floorImage);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = AssetsUtil.bitmapFont;
        scoreLable = new Label("" + score, style);
        stage.addActor(scoreLable);

        getReadyActor = new ImageActor(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_READY));
        getReadyActor.setCenterX(stage.getWidth() / 2);
        getReadyActor.setTopY(stage.getHeight() - 182);
        stage.addActor(getReadyActor);

        tapTipImage = new ImageActor(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_TAP_TIP));
        tapTipImage.setCenterX(stage.getWidth() / 2);
        tapTipImage.setCenterY((getReadyActor.getY() + floorImage.getTopY()) / 2);
        stage.addActor(tapTipImage);

        birdActor = new BirdActor(getGame());
        birdActor.setX(tapTipImage.getX()-20);
        birdActor.setY(tapTipImage.getY()+80);
        birdStartPositionY = birdActor.getY();
        birdActor.setScale(1.2f);
        birdActor.setOrigin(Align.center);
        stage.addActor(birdActor);

        floorImage.setZIndex(birdActor.getZIndex());
        scoreLable.setZIndex(stage.getRoot().getChildren().size-1);

        barImagePool = Pools.get(BarActor.class,10);

        float barHeight = 400;
        float maxRegion = 300;
        minDownBarTopY = Math.max(floorImage.getTopY() + 40, stage.getHeight() - barHeight - Res.Physics.BAR_INTERVAL);
        maxDownBarTopY = Math.min(minDownBarTopY + maxRegion, stage.getHeight() - Res.Physics.BAR_INTERVAL - 60);
        maxDownBarTopY = Math.min(maxDownBarTopY, floorImage.getTopY() + barHeight);
        button = new ImageButton(
                new TextureRegionDrawable(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 1)),
                new TextureRegionDrawable(AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 2))
        );

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (gameState == GameState.fly){
                    gamePause();
                }else if (gameState == GameState.pause){
                    gameResume();
                }

            }
        });
        stage.addActor(button);

        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (gameState == GameState.ready){
                    startGame();
                    game.getSoundPlayer().playSound(Res.Audios.AUDIO_TOUCH);
                    birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);
                }else if (gameState == GameState.fly){
                    if (birdActor.getY()<stage.getHeight()){
                        birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);
                        getGame().getSoundPlayer().playSound(Res.Audios.AUDIO_TOUCH);
                    }
                }
            }
        });

        ready();
    }

    /**
     * 游戏就绪状态
     */
    public void ready() {
        gameState = GameState.ready;

        // 设置小鸟初始Y轴坐标
        birdActor.setY(birdStartPositionY);
        // 刷新小鸟的显示帧和旋转角度
        birdActor.refreshFrameAndRotation(gameState);

        // 地板停止移动
        floorImage.setMove(false);

        // 清空水管
        for (BarActor barActor : barImageList) {
            // 从舞台中移除水管
            stage.getRoot().removeActor(barActor);
        }
        // 从集合中移除水管
        barImageList.clear();

        // 设置点击提示和准备提示可见
        tapTipImage.setVisible(true);
        getReadyActor.setVisible(true);

        // 分数清零
        score = 0;
        scoreLable.setText(score);
        // 更新分数后重新水平居中
        scoreLable.setX(stage.getWidth() / 2);

        button.setVisible(true);
    }

    /**
     * 游戏开始状态
     */
    private void startGame() {
        gameState = GameState.fly;
        birdActor.refreshFrameAndRotation(gameState);
        floorImage.setMove(true);
        tapTipImage.setVisible(false);
        getReadyActor.setVisible(false);
        generateBarTimeCounter = 0.0F;
    }

    /**
     * 触碰水管
     */
    private void collisionBar() {
        gameState = GameState.die;

        // 所有水管停止移动
        for (BarActor barActor : barImageList) {
            if (barActor.isMove()) {
                barActor.setMove(false);
            }
        }
        floorImage.setMove(false);
    }

    /**
     * 触碰地板
     */
    private void gameOver() {
        gameState = GameState.gameOver;
        birdActor.refreshFrameAndRotation(gameState);
        floorImage.setMove(false);
        for (BarActor barActor : barImageList) {
            if (barActor.isMove()) {
                barActor.setMove(false);
            }
        }
        button.setVisible(false);
        // 显示游戏结束舞台
        showOver(score);
    }

    /**
     * 暂停
     */
    private void gamePause(){
        gameState = GameState.pause;
        birdActor.refreshFrameAndRotation(gameState);
        for (BarActor barActor : barImageList) {
            if (barActor.isMove()) {
                barActor.setMove(false);
            }
        }
        floorImage.setMove(false);

    }

    /**
     * 继续
     */
    private void gameResume(){
        gameState = GameState.resume;
        for (BarActor barActor : barImageList) {
            if (!barActor.isMove()) {
                barActor.setMove(true);
            }
        }
        floorImage.setMove(true);
        gameState = GameState.fly;
        birdActor.refreshFrameAndRotation(gameState);
    }

    /**
     * 初始化水管
     */
    private void generateBar(){
//        随机生成缺口位置
        float downBarY = MathUtils.random(minDownBarTopY,maxDownBarTopY);
//        搞个下水管先
        BarActor downBarActor = barImagePool.obtain();
        downBarActor.setMainGame(getGame());
        downBarActor.setUpBar(false);
        downBarActor.setX(stage.getWidth());
        downBarActor.setTopY(downBarY);
        downBarActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);

//        让它跑起来
        downBarActor.setMove(true);
        stage.addActor(downBarActor);

        barImageList.add(downBarActor);
        downBarActor.setZIndex(birdActor.getZIndex());

//        再搞个上水管
        BarActor upBarActor = barImagePool.obtain();
        upBarActor.setMainGame(getGame());
        upBarActor.setUpBar(true);
        upBarActor.setX(stage.getWidth());
        upBarActor.setY(downBarActor.getTopY() + Res.Physics.BAR_INTERVAL);
        upBarActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        upBarActor.setMove(true);
        stage.addActor(upBarActor);
        barImageList.add(upBarActor);
        upBarActor.setZIndex(birdActor.getZIndex());
    }

    /**
     * 逻辑检测
     */
    private void check(){
//        飞行状态
        if (gameState == GameState.fly){
//            遍历水管
            for (BarActor barActor : barImageList){
//                水管碰撞检测
                if (CollisionUtils.isCollision(birdActor,barActor,Res.Physics.DEPTH)){
                    collisionBar();
                    getGame().getSoundPlayer().playSound(Res.Audios.AUDIO_HIT);
                    break;
                }
//                通过检测
                if (barActor.isUpBar()&&!barActor.isPassByBird()&&birdActor.getX()>barActor.getRightX()){
                    score += 1;
                    scoreLable.setText(score);
                    barActor.setPassByBird(true);
                    getGame().getSoundPlayer().playSound(Res.Audios.AUDIO_SCORE);
                }
            }
        }

//        移除越界水管
        for (BarActor barActor : barImageList){
            if (barActor.getRightX()<0){
                barImageList.remove(barActor);
                barImagePool.free(barActor);
            }
        }

//        地板碰撞检测
        if (CollisionUtils.isCollision(birdActor,floorImage,Res.Physics.DEPTH)){
            gameOver();
            getGame().getSoundPlayer().playSound(Res.Audios.AUDIO_DIE);
        }
    }


    @Override
    public void dispose() {
        super.dispose();
        barImageList.clear();
    }


    //    重新开始
    public void restartGame() {
        overGroup.getButton().setVisible(false);
        ready();
    }

    public void showOver(int currScore){
        overGroup = new OverGroup(getGame());
        stage.addActor(overGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (gameState == GameState.fly || gameState == GameState.die){
            check();
        }

        if (gameState == GameState.fly) {
            generateBarTimeCounter += delta;
            if (generateBarTimeCounter >= Res.Physics.GENERATE_BAR_TIME_INTERVAL) {
                generateBar();
                generateBarTimeCounter = 0;
            }
        }
    }

}
