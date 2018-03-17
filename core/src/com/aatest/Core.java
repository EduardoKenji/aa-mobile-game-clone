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
	Text FPSText, TestText, TestText2, TestText3;
	Circle centerCircle;
	float timer;
	MyInput my_input;
	public static ArrayList<Circle> circleList;
	public static ArrayList<Circle> touchableCircles;
	public static ArrayList<Circle> movingTouchableCircles;
	public static ArrayList<Circle> spinningCircles;
	float touchableCirclesSpeed;
	float toleranceRadius;
	public static float angularSpeed;
	public static float modifiers_values[];
	float whiteColor[] = {1f, 1f, 1f, 1f}, redColor[] = {0.7f, 0.2f, 0.2f, 1f}, greenColor[] = {0.2f, 0.7f, 0.2f, 1}, currentColor[];

	MyApplication game;
	int i, j;
	int touchableCirclesAmount;

	float lowestRadius, highestRadius, radiusVarianceTick;
	int radiusVarianceMode = 0;

	float fuzzyTimer, normalDuration, fuzzyDuration;
	public static float fuzzySpeedVariance;
	static int fuzzyMode = 0;

	Core(MyApplication game, String modifiers) {
		this.game = game;
		font = game.getAssetManager().get("font.ttf", BitmapFont.class);
		smallerCircleFont = game.getAssetManager().get("smallerCircleFont.ttf", BitmapFont.class);
		biggerCircleFont = game.getAssetManager().get("biggerCircleFont.ttf", BitmapFont.class);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		FPSText = new Text(game.getGlyphLayout(), 10*game.proportionX, 10*game.proportionY, "FPS: "+Gdx.graphics.getFramesPerSecond(), "lowerLeft", font);
		TestText = new Text(game.getGlyphLayout(), 10*game.proportionX, 190*game.proportionY, "", "lowerLeft", font);
		TestText2 = new Text(game.getGlyphLayout(), 10*game.proportionX, 70*game.proportionY, "", "lowerLeft", font);
		TestText3 = new Text(game.getGlyphLayout(), 10*game.proportionX, 130*game.proportionY, "", "lowerLeft", font);
		centerCircle = new Circle(Gdx.graphics.getWidth()/2, 920*game.proportionY, 180 * game.proportionX, "1", biggerCircleFont, game.getGlyphLayout(), touchableCirclesSpeed, angularSpeed);
		my_input = new MyInput(game);

		modifiers_values = new float[2];
		String all_modifiers[] = modifiers.split(" ");
		for(i = 0; i < all_modifiers.length; i++) {
			if(all_modifiers[i].equals("acceleration")) {
				modifiers_values[0] = Float.parseFloat(all_modifiers[i+1]);
				i++;
				continue;
			}
			if(all_modifiers[i].equals("circleNum")) {
				touchableCirclesAmount = Integer.parseInt(all_modifiers[i + 1]);
				i++;
				continue;
			}
			if(all_modifiers[i].equals("variableRadius")) {
				lowestRadius = toleranceRadius + Float.parseFloat(all_modifiers[i+1]);
				highestRadius = toleranceRadius + Float.parseFloat(all_modifiers[i+2]);
				radiusVarianceTick = Float.parseFloat(all_modifiers[i + 3]);
				i+=3;
				continue;
			}
			if(all_modifiers[i].equals("radius")) {
				toleranceRadius = Float.parseFloat(all_modifiers[i + 1]) * game.getProportionX();
				i++;
				continue;
			}
			if(all_modifiers[i].equals("invert")) {
				modifiers_values[1] = 1;
				continue;
			}
			if(all_modifiers[i].equals("angularSpeed")) {
				angularSpeed = Float.parseFloat(all_modifiers[i + 1]);
				i++;
				continue;
			}
			if(all_modifiers[i].equals("fuzzy")) {
				normalDuration = Float.parseFloat(all_modifiers[i + 1]);
				fuzzyDuration = Float.parseFloat(all_modifiers[i + 2]);
				fuzzySpeedVariance = Float.parseFloat(all_modifiers[i + 3]);
				i+=3;
				continue;
			}
		}

		touchableCirclesSpeed = 10f * game.proportionY;

		circleList = new ArrayList<Circle>();
		touchableCircles = new ArrayList<Circle>();
		movingTouchableCircles = new ArrayList<Circle>();
		spinningCircles = new ArrayList<Circle>();

		for(i = 0; i < touchableCirclesAmount; i++) {
			touchableCircles.add(new Circle(Gdx.graphics.getWidth()/2, (270 - (i*90)) * game.proportionY , 39 * game.proportionX, (touchableCirclesAmount-i)+"",
					smallerCircleFont, game.getGlyphLayout(), touchableCirclesSpeed, angularSpeed));
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
					if (euclidianDistance(movingTouchableCircles.get(i).getX(), movingTouchableCircles.get(i).getY(), spinningCircles.get(j).getX(), spinningCircles.get(j).getY()) <=
							(movingTouchableCircles.get(i).getRadius() + spinningCircles.get(j).getRadius()) - (touchableCirclesSpeed)) {
						lostGame();
					}
				}
			}
		}

		if(radiusVarianceTick != 0 && spinningCircles.size() > 0 && game.gameState.equals("started")) {
			if(radiusVarianceMode == 0) {
				toleranceRadius += radiusVarianceTick;
				if(toleranceRadius >= highestRadius) {
					radiusVarianceMode = 1;
				}
			} else {
				toleranceRadius -= radiusVarianceTick;
				if (toleranceRadius <= lowestRadius) {
					radiusVarianceMode = 0;
				}
			}
		}

		if(fuzzySpeedVariance != 0 && spinningCircles.size() > 0 && game.gameState.equals("started")) {
			fuzzyTimer += delta;
			if(fuzzyMode == 0) {
				if(fuzzyTimer > normalDuration) {
					fuzzyMode = 1;
					for(i = 0; i < Core.circleList.size(); i++) {
						Core.circleList.get(i).setAngularSpeed(Core.circleList.get(i).getAngularSpeed()*(-1)*fuzzySpeedVariance);
					}
					fuzzyTimer = 0;
				}
			} else {
				if(fuzzyTimer > fuzzyDuration) {
					fuzzyMode = 0;
					for(i = 0; i < Core.circleList.size(); i++) {
						Core.circleList.get(i).setAngularSpeed((Core.circleList.get(i).getAngularSpeed()*(-1))/fuzzySpeedVariance);
					}
					fuzzyTimer = 0;
				}
			}
		}

		if(spinningCircles.size() > 0) {
			TestText.setText("Spd: "+spinningCircles.get(0).getAngularSpeed());
		}

		timer += delta;
		if(timer >= 0.3f) {
			FPSText.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
			timer = 0;
		}

		TestText2.setText("Radius: "+toleranceRadius);

		if(fuzzyMode == 0) {
			TestText3.setText("Mode: Normal");
		} else {
			TestText3.setText("Mode: Fuzzy");
		}


		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).update(delta, centerCircle, toleranceRadius);
		}

		for(i = 0; i < spinningCircles.size(); i++) {
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
		TestText2.draw(spriteBatch);
		TestText3.draw(spriteBatch);
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

	float euclidianDistance(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
	}

	public void winGame() {
		for(i = 0; i < circleList.size(); i++) {
			circleList.get(i).setVel(0);
			circleList.get(i).setTouched(false);
		}
		currentColor = greenColor;
		if(!game.gameState.equals("win")) {
			game.setWinCount(game.getWinCount() + 1);
		}
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
		if(!game.gameState.equals("lose")) {
			game.setLoseCount(game.getLoseCount()+1);
		}
		game.gameState = "lose";

	}

	public void updateTouchableBalls() {
		if(touchableCircles.size() > 0) {
			if ((270*game.proportionY) - touchableCircles.get(0).getY() < touchableCirclesSpeed) { // Remaining distance is smaller than circle "moving-up" speed
				for (i = 0; i < touchableCircles.size(); i++) {
					touchableCircles.get(i).setY(touchableCircles.get(i).getY() + ((270*game.proportionY) - touchableCircles.get(0).getY()));
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
