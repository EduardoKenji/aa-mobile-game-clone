package com.aatest;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Mario on 17/03/2018.
 */
public class Text {

    String text;
    float x, y;
    float width, height;
    GlyphLayout glyphLayout;
    BitmapFont font;
    String position;

    Text(GlyphLayout glyphLayout, float x, float y, String text, String position, BitmapFont font) {
        this.glyphLayout = glyphLayout;
        this.x = x;
        this.y = y;
        this.font = font;
        this.text = text;
        this.position = position;
        glyphLayout.setText(font, text);
        this.width = glyphLayout.width;
        this.height = glyphLayout.height;
        if(position.equals("lowerLeft")) {
            this.y = y+height;
        } else if(position.equals("center")) {
            this.x = x-width/2;
            this.y = y+height/2;
        }
        glyphLayout.reset();

    }

    public void setText(String text) {
        this.text = text;
    }

    void draw(SpriteBatch spriteBatch) {
        font.draw(spriteBatch, text, x, y);
    }

    public void setY(float y) {
        this.y = y;
        if(position.equals("center")) {
            this.y = y+height/2;
        }
    }

    public void setX(float x) {
        this.x = x;
        if(position.equals("center")) {
            this.x = x-width/2;
        }
    }

    public float getY() {
        return y;
    }
}
