package com.aatest;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by Mario on 17/03/2018.
 */
public class MyInput implements InputProcessor {

    MyApplication game;

    MyInput(MyApplication game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(Core.touchableCircles.size() > 0 && !game.gameState.equals("failed")) {
            Core.touchableCircles.get(0).setTouched(true);
            Core.movingTouchableCircles.add(Core.touchableCircles.get(0));
            Core.touchableCircles.remove(0);
            Core.wrongPosition = true;
        }
        if(game.gameState.equals("lose") || game.gameState.equals("win")) {
            game.setScreen(new Core(game));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
