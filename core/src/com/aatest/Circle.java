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
    boolean Touched;
    boolean Spinning;
    float angle;
    float angularSpeed;
    int i;

    Circle(float x, float y, float radius, String text, BitmapFont font, GlyphLayout glyphLayout, float vel, float angularSpeed) {
        this.x = x;
        this.y = y;
        this.vel = vel;
        this.radius = radius;
        this.angularSpeed = angularSpeed;
        Touched = false;
        Spinning = false;
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
        if(Touched) {
            if(centerCircle.getY()-toleranceRadius-y < vel) {
                y=y+(centerCircle.getY()-toleranceRadius-y);
                centerText.setY(y);
                Touched = false;
                Core.spinningCircles.add(this);
                Core.movingTouchableCircles.remove(this);
                Spinning = true;
                if(Core.modifiers_values[0] > 0) {
                    for(i = 0; i < Core.circleList.size(); i++) {
                        if(Core.circleList.get(i).getAngularSpeed() < 0) {
                            if(Core.fuzzyMode == 1) {
                                Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()-(Core.modifiers_values[0]*Core.fuzzySpeedVariance));
                            } else {
                                Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()-Core.modifiers_values[0]);
                            }
                        } else {
                            if(Core.fuzzyMode == 1) {
                                Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()+(Core.modifiers_values[0]*Core.fuzzySpeedVariance));
                            } else {
                                Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()+Core.modifiers_values[0]);
                            }

                        }
                    }
                }
                if(Core.modifiers_values[1] == 1) {
                    for(i = 0; i < Core.circleList.size(); i++) {
                        Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()*(-1));
                    }
                }

            } else {
                y=y+vel;
                centerText.setY(y);

            }
        }

        if(Spinning) {
            angle += angularSpeed;
            if(angle >= 360) angle = 0;
            y = centerCircle.getY()+((float)(Math.sin(Math.toRadians(angle)))*toleranceRadius);
            x = centerCircle.getX()+((float) (Math.cos(Math.toRadians(angle)))*toleranceRadius);
            centerText.setY(y);
            centerText.setX(x);
        }
    }

    public float getAngularSpeed() {
        return angularSpeed;
    }

    public float getRadius() {
        return radius;
    }

    public void setSpinning(boolean isSpinning) {
        this.Spinning = isSpinning;
    }

    public void setVel(float vel) {
        this.vel = vel;
    }

    public void setTouched(boolean isTouched) {
        this.Touched = isTouched;
    }

    public void setAngularSpeed(float angularSpeed) {
        this.angularSpeed = angularSpeed;
    }
}
