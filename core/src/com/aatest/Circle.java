package com.aatest;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Mario on 17/03/2018.
 */
public class Circle {
    float x, y, radius;
    float vel;
    Text centerText;
    boolean isTouched;
    boolean isSpinning;
    float angle;

    Circle(float x, float y, float radius, String text, BitmapFont font, GlyphLayout glyphLayout, float vel) {
        this.x = x;
        this.y = y;
        this.vel = vel;
        this.radius = radius;
        isTouched = false;
        isSpinning = false;
        angle = 270f;
        centerText = new Text(glyphLayout, x+(1), y, text, "center", font);
    }

    void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(x, y, radius);
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    void drawText(SpriteBatch spriteBatch) {
        centerText.draw(spriteBatch);
    }

    public Text getCenterText() {
        return centerText;
    }

    void update(float delta, Circle centerCircle, float toleranceRadius) {
        if(isTouched) {
            if(centerCircle.getY()-toleranceRadius-y < vel) {
                y=y+(centerCircle.getY()-toleranceRadius-y);
                centerText.setY(y);
                isTouched = false;
                Core.spinningCircles.add(this);
                Core.movingTouchableCircles.remove(this);
                isSpinning = true;
            } else {
                y=y+vel;
                centerText.setY(y);

            }
        }

        if(isSpinning) {
            y = centerCircle.getY()+((float)(Math.sin(Math.toRadians(angle)))*toleranceRadius);
            x = centerCircle.getX()+((float) (Math.cos(Math.toRadians(angle)))*toleranceRadius);
            centerText.setY(y);
            centerText.setX(x);
        }
    }

    public float getRadius() {
        return radius;
    }

    public void setSpinning(boolean isSpinning) {
        this.isSpinning = isSpinning;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    public void setTouched(boolean isTouched) {
        this.isTouched = isTouched;
    }
}
