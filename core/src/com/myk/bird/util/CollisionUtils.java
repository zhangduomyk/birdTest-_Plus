package com.myk.bird.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 碰撞检测工具
 * 
 * @xietansheng
 */
public class CollisionUtils {
	
	private static final Rectangle rect1 = new Rectangle();
	private static final Rectangle rect2 = new Rectangle();
	private static final Rectangle tempRect = new Rectangle();

	/**
	 * 判断两个演员是否碰撞
	 * 
	 * @param actor1
	 * @param actor2
	 * @param depth 碰撞深度, 两个演员的包围矩阵重叠部分的矩形宽高均超过 depth 才算碰撞
	 * @return
	 */
	public static synchronized boolean isCollision(Actor actor1, Actor actor2, float depth) {
        if (actor1 == null || actor2 == null) {
            return false;
        }
        
        // 获取 演员1 缩放后的包围矩阵
        rect1.setSize(
                actor1.getWidth() * actor1.getScaleX(),
                actor1.getHeight() * actor1.getScaleY()
        );
        rect1.setPosition(
                actor1.getX() - (actor1.getOriginX() * actor1.getScaleX() - actor1.getOriginX()),
                actor1.getY() - (actor1.getOriginY() * actor1.getScaleY() - actor1.getOriginY())
        );

        // 获取 演员2 缩放后的包围矩阵
        rect2.setSize(
                actor2.getWidth() * actor2.getScaleX(),
                actor2.getHeight() * actor2.getScaleY()
        );
        rect2.setPosition(
                actor2.getX() - (actor2.getOriginX() * actor2.getScaleX() - actor2.getOriginX()),
                actor2.getY() - (actor2.getOriginY() * actor2.getScaleY() - actor2.getOriginY())
        );

        return rect1.overlaps(rect2);
	}
	
}




















