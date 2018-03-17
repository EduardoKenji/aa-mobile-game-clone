package com.aatest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by Mario on 16/03/2018.
 */
public class MyApplication extends Game {

    AssetManager assetManager;
    float proportionX, proportionY;
    GlyphLayout glyphLayout;
    String gameState;

    @Override
    public void create() {
        proportionX = 1080f/(float) Gdx.graphics.getWidth();
        proportionY = 1794f/(float)Gdx.graphics.getHeight();
        assetManager = new AssetManager();
        glyphLayout = new GlyphLayout();
        gameState = "not started";
        setScreen(new LoadingScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public float getProportionX() {
        return proportionX;
    }

    public float getProportionY() {
        return proportionY;
    }

    public GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }
}
