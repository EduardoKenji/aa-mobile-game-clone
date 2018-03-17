package com.aatest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by Mario on 16/03/2018.
 */
public class MyApplication extends Game {

    AssetManager assetManager;
    static float proportionX, proportionY;
    GlyphLayout glyphLayout;
    String gameState;
    Preferences myPreferences;
    int loseCount, winCount;

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
        myPreferences.putInteger("loseCount", loseCount);
        myPreferences.flush();
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
        myPreferences.putInteger("winCount", winCount);
        myPreferences.flush();
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    @Override
    public void create() {
        myPreferences = Gdx.app.getPreferences("My Preferences");
        loseCount = myPreferences.getInteger("loseCount", 0);
        winCount = myPreferences.getInteger("winCount", 0);
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
