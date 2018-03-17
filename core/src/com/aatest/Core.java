package com.aatest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Core implements Screen {

	public static boolean wrongPosition;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	BitmapFont font, smallerCircleFont, biggerCircleFont;
	Text FPSText, TestText;
	Circle centerCircle;
	float timer;
	MyInput my_input;
	ArrayList<Circle> circleList;
	public static ArrayList<Circle> touchableCircles;
	public static ArrayList<Circle> movingTouchableCircles;
	public static ArrayList<Circle> spinningCircles;
	float touchableCirclesSpeed;
	float toleranceRadius;
	float angularSpeed;
	float whiteColor[] = {1f, 1f, 1f, 1f}, redColor[] = {0.7f, 0.2f, 0.2f, 1f}, greenColor[] = {0.2f, 0.7f, 0.2f, 1}, currentColor[];

	MyApplication game;
	int i, j;
	int touchableCirclesAmount;

	Core(MyApplication game) {
		this.game = game;
		font = game.getAssetManager().get("font.ttf", BitmapFont.class);
		smallerCircleFont = game.getAssetManager().get("smallerCircleFont.ttf", BitmapFont.class);
		biggerCircleFont = game.getAssetManager().get("biggerCircleFont.ttf", BitmapFont.class);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		FPSText = new Text(game.getGlyphLayout(), 10*game.proportionX, 10*game.proportionY, "FPS: "+Gdx.graphics.getFramesPerSecond(), "lowerLeft", font);
		TestText = new Text(game.getGlyphLayout(), 10*game.proportionX, 70*game.proportionY, "", "lowerLeft", font);
		centerCircle = new Circle(Gdx.graphics.getWidth()/2, 920*game.proportionY, 180 * game.proportionX, "1", biggerCircleFont, game.getGlyphLayout(), touchableCirclesSpeed);
		my_input = new MyInput(game);

		touchableCirclesSpeed = 10f * game.proportionY;
		touchableCirclesAmount = 17;
		toleranceRadius = 460f * game.proportionX;
		angularSpeed = 0.9f;

		circleList = new ArrayList<Circle>();
		touchableCircles = new ArrayList<Circle>();
		movingTouchableCircles = new ArrayList<Circle>();
		spinningCircles = new ArrayList<Circle>();

		for(i = 0; i < touchableCirclesAmount; i++) {
			touchableCircles.add(new Circle(Gdx.graphics.getWidth()/2, (270 - (i*90)) * game.proportionY , 39 * game.proportionX, (touchableCirclesAmount-i)+"", smallerCircleFont, game.getGlyphLayout(), touchableCirclesSpeed));
		}

		circleList.add(centerCircle);
		circleList.addAll(touchableCircles);

		wrongPosition = false;
		currentColor = whiteColor;
	}

	@Override
	public void show() {
		game.gameState = "started";
		Gdx.input.setInputProcessor(my_input);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(currentColor[0], currentColor[1], currentColor[2], currentColor[3]);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		for(i = 0; i < movingTouchableCircles.size(); i++) {
			for(j = 0; j < spinningCircles.size(); j++) {
				if(movingTouchableCircles.size() > i) {
					if (distanciaEuclidiana(movingTouchableCircles.get(i).getX(), movingTouchableCircles.get(i).getY(), spinningCircles.get(j).getX(), spinningCircles.get(j).getY()) <=
							(movingTouchableCircles.get(i).getRadius() + spinningCircles.get(j).getRadius()) - (0.2f * game.proportionX)) {
						lostGame();
					}
				}
			}
		}

		timer += delta;
		if(timer >= 0.3f) {
			FPSText.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			timer = 0;
		}

		TestText.setText(touchableCircles.size() + " " + movingTouchableCircles.size() + " " + spinningCircles.size());

		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).update(delta, centerCircle, toleranceRadius);
		}

		for(i = 0; i < spinningCircles.size(); i++) {
			spinningCircles.get(i).angle += angularSpeed + (0.11*spinningCircles.size());
			if(spinningCircles.get(i).angle >= 360) spinningCircles.get(i).angle = 0;
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.line(centerCircle.getX(), centerCircle.getY(), spinningCircles.get(i).getX(), spinningCircles.get(i).getY());
			shapeRenderer.end();
		}

		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.BLACK);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).draw(shapeRenderer);
		}
		shapeRenderer.end();

		spriteBatch.begin();
		FPSText.draw(spriteBatch);
		TestText.draw(spriteBatch);
		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).drawText(spriteBatch);
		}
		spriteBatch.end();

		if(wrongPosition) {
			updateTouchableBalls();
		}

		if(movingTouchableCircles.size() == 0 && touchableCircles.size() == 0) {
			winGame();
		}
	}

	float distanciaEuclidiana(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
	}

	public void winGame() {
		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).setVel(0);
			circleList.get(i).setTouched(false);
		}
		currentColor = greenColor;
		game.gameState = "win";
	}

	public void lostGame() {
		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).setVel(0);
			circleList.get(i).setTouched(false);
			circleList.get(i).setSpinning(false);
		}
		wrongPosition = false;
		currentColor = redColor;
		game.gameState = "lose";
	}

	public void updateTouchableBalls() {
		if(touchableCircles.size() > 0) {
			if (270 - touchableCircles.get(0).getY() < touchableCirclesSpeed) { // Remaining distance is smaller than circle "moving-up" speed
				for (i = 0; i < touchableCircles.size(); i++) {
					touchableCircles.get(i).setY(touchableCircles.get(i).getY() + (270 - touchableCircles.get(0).getY()));
					touchableCircles.get(i).getCenterText().setY(touchableCircles.get(i).getY());
				}
				wrongPosition = false;
			} else {
				for (i = 0; i < touchableCircles.size(); i++) {
					touchableCircles.get(i).setY(touchableCircles.get(i).getY() + touchableCirclesSpeed);
					touchableCircles.get(i).getCenterText().setY(touchableCircles.get(i).getY());
				}
			}
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
