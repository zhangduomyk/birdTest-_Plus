package com.myk.bird.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myk.bird.MainGame;
import com.myk.bird.actor.base.BaseImageActor;
import com.myk.bird.res.Res;
import com.myk.bird.util.AssetsUtil;

/**
 * 地板
 * 
 * @xietansheng
 */
public class FloorActor extends BaseImageActor {

    /** 水平移动速度, px/s */
    private float moveVelocity;

    /** 地板纹理区域 */
    private TextureRegion region;

    /** 水平偏移量 */
    private float offerX;

    /** 地板是否在移动 */
    private boolean isMove;

    public FloorActor(MainGame mainGame) {
        super(mainGame);
        region = AssetsUtil.atlas.findRegion(Res.Atlas.IMAGE_GAME_FLOOR);
        setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
    }

    public float getMoveVelocity() {
        return moveVelocity;
    }

    public void setMoveVelocity(float moveVelocity) {
        this.moveVelocity = moveVelocity;
    }

    public boolean isMove() {
        return isMove;
    }

    public void setMove(boolean isMove) {
        this.isMove = isMove;
    }

    @Override
    public void act(float delta) {
         super.act(delta);
        if (isMove) {
            offerX += (delta * moveVelocity);
            offerX %= getWidth();
            if (offerX > 0) {
                offerX -= getWidth();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // 绘制两次以达到视觉上的循环移动效果
        batch.draw(region, getX() + offerX, getY(),Res.FIX_WORLD_WIDTH*2,getHeight());
        if (Math.abs(offerX) > 0.001F) {
            batch.draw(region, getX() + (getWidth() + offerX), getY(),Res.FIX_WORLD_WIDTH*2,getHeight());
        }
    }

}
