package com.aatest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Mario on 16/03/2018.
 */
public class LoadingScreen implements Screen {

    MyApplication game;
    ShapeRenderer shapeRenderer;

    LoadingScreen(MyApplication game) {
        shapeRenderer = new ShapeRenderer();
        this.game = game;
    }

    @Override
    public void show() {

        FileHandleResolver resolver = new InternalFileHandleResolver();
        game.getAssetManager().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        game.getAssetManager().setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter fontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter.fontFileName = "font.ttf";
        fontLoaderParameter.fontParameters.size = (int)(50/game.getProportionX());
        fontLoaderParameter.fontParameters.borderWidth = 3f/game.getProportionX();
        fontLoaderParameter.fontParameters.borderColor = Color.BLACK;
        game.getAssetManager().load("font.ttf", BitmapFont.class, fontLoaderParameter);

        fontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter.fontFileName = "smallerCircleFont.ttf";
        fontLoaderParameter.fontParameters.size = (int)(40/game.getProportionX());
        game.getAssetManager().load("smallerCircleFont.ttf", BitmapFont.class, fontLoaderParameter);

        fontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontLoaderParameter.fontFileName = "biggerCircleFont.ttf";
        fontLoaderParameter.fontParameters.size = (int)(110/game.getProportionX());
        game.getAssetManager().load("biggerCircleFont.ttf", BitmapFont.class, fontLoaderParameter);
    }

    @Override
    public void render(float delta) {
        if(game.getAssetManager().update()) {
            game.setScreen(new Core(game, "angularSpeed 0.9 radius 460 circleNum 14 acceleration 0.12 variableRadius -140 30 2 invert fuzzy 4.5 1.5 0.7"));
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(Gdx.graphics.getWidth()/2 - 300, (Gdx.graphics.getHeight() / 2) - 50, 600*game.getAssetManager().getProgress(), 100);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 1, 1);
            shapeRenderer.rect(Gdx.graphics.getWidth()/2 - 300, (Gdx.graphics.getHeight() / 2) - 50, 600, 100);
            shapeRenderer.rect(Gdx.graphics.getWidth()/2 - 301, (Gdx.graphics.getHeight() / 2) - 51, 602, 102);
            shapeRenderer.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
