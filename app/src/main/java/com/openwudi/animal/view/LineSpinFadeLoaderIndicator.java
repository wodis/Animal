package com.openwudi.animal.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Jack on 2015/10/24.
 * Email:81813780@qq.com
 */
public class LineSpinFadeLoaderIndicator extends Indicator {

    int[] alphas = new int[]{157, 133, 109, 85, 61, 37, 13, 37, 61, 85, 109, 133};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius = getWidth() / 12;
        for (int i = 0; i < 12; i++) {
            canvas.save();
            Point point = circleAt(getWidth(), getHeight(), getWidth() / 2.5f - radius, i * (Math.PI / 6));
            canvas.translate(point.x, point.y);
            canvas.rotate(i * 30);
            paint.setAlpha(alphas[i]);
            RectF rectF = new RectF(-radius, -radius / 2.2f, 2f * radius, radius / 2.2f);
            canvas.drawRoundRect(rectF, 5, 5, paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int[] delays = {0, 90, 180, 270, 360, 450, 540, 630, 720, 810, 900, 990, 1080};
        for (int i = 0; i < 12; i++) {
            final int index = i;
            ValueAnimator alphaAnim = ValueAnimator.ofInt(157, 13, 157);
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            addUpdateListener(alphaAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(alphaAnim);
        }
        return animators;
    }

    /**
     * 圆O的圆心为(a,b),半径为R,点A与到X轴的为角α.
     * 则点A的坐标为(a+R*cosα,b+R*sinα)
     *
     * @param width
     * @param height
     * @param radius
     * @param angle
     * @return
     */
    Point circleAt(int width, int height, float radius, double angle) {
        float x = (float) (width / 2 + radius * (Math.cos(angle)));
        float y = (float) (height / 2 + radius * (Math.sin(angle)));
        return new Point(x, y);
    }

    final class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
