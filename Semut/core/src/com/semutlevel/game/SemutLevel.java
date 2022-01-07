package com.semutlevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Camera;

public class SemutLevel implements Screen {
	private Stage mainStage;
	private AnimatedActor semut;
	private BaseActor cake;
	private BaseActor floor;
	private BaseActor winText;

	private float timeElapsed;
	private Label timeLabel;

	private Stage uiStage;

	private boolean win;

	private final int SPEED = 250;

	private final int mapHeight = 800;
	private final int mapWidth = 800;

	private final int viewWidth = 640;
	private final int viewHeight = 480;

	public Game game;

	public SemutLevel(Game g) {
		game = g;
		create();
	}

	public void create () {
		timeElapsed = 0;
		BitmapFont font = new BitmapFont();
		String text = "Time: 0";
		LabelStyle style = new LabelStyle(font, Color.NAVY);
		timeLabel = new Label(text, style);
		timeLabel.setFontScale(2);
		timeLabel.setPosition(500, 440);

		mainStage = new Stage();
		uiStage = new Stage();

		floor = new BaseActor();

		floor.setTexture(new Texture(Gdx.files.internal("tiles-800-800.png")));
		floor.setPosition(0, 0);
		mainStage.addActor(floor);

		semut = new AnimatedActor();

		TextureRegion[] frames = new TextureRegion[4];

		for (int n = 0; n < 4; n++) {
			Texture tex = new Texture(Gdx.files.internal("semut"+n+".png"));
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			frames[n] = new TextureRegion(tex);
		}

		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

		Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

		semut.setAnimation(anim);
		semut.setOrigin(semut.getWidth() / 2, semut.getHeight() / 2);
		semut.setPosition(20,20);
		mainStage.addActor(semut);

		cake = new BaseActor();
		cake.setTexture(new Texture(Gdx.files.internal("cake.png")));
		cake.setOrigin(cake.getWidth() / 2, cake.getHeight() / 2);
		cake.setPosition(400, 300);
		mainStage.addActor(cake);

		winText = new BaseActor();
		winText.setTexture(new Texture(Gdx.files.internal("winner.png")));
		winText.setPosition(170, 60);
		winText.setVisible(false);

		uiStage.addActor(winText);
		uiStage.addActor(timeLabel);

		win = false;
	}


	@Override
	public void show() {

	}

	public void render (float dt) {
		semut.velocityX = 0;
		semut.velocityY = 0;

		if (Gdx.input.isKeyPressed(Keys.M))
			game.setScreen(new SemutMenu(game));

		if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
			semut.velocityX -= SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))
			semut.velocityX += SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Keys.UP))
			semut.velocityY += SPEED;
		else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN))
			semut.velocityY -= SPEED;

		mainStage.act(dt);
		uiStage.act(dt);

		semut.setX(MathUtils.clamp(semut.getX(), 0, mapWidth - semut.getWidth()));
		semut.setY(MathUtils.clamp(semut.getY(), 0, mapHeight - semut.getHeight()));

		Rectangle cheeseRectangle
				= cake.getBoundingRectangle();
		Rectangle mouseRectangle
				= semut.getBoundingRectangle();
		if ( !win && cheeseRectangle.contains(mouseRectangle) ) {
			win = true;

			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0, 0, 1),
					Actions.fadeOut(1)
			);
			cake.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(
							Actions.sequence(
									Actions.color(new Color(1,0,0,1), 1),
									Actions.color(new Color(0,0,1,1), 1)
							)
					)
			);

			winText.addAction(fadeInColorCycleForever);

			if (Gdx.input.isKeyPressed(Keys.P))
				game.setScreen(new SemutLevel(game));
		}

		if (!win)
			timeElapsed += dt;
		timeLabel.setText("Time : " + (int)timeElapsed );

		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Camera cam = mainStage.getCamera();
		cam.position.set(semut.getX() + semut.getOriginX(),
				semut.getY() + semut.getOriginY(), 0);

		cam.position.x = MathUtils.clamp(cam.position.x, viewWidth / 2,
				mapWidth - viewWidth / 2);
		cam.position.y = MathUtils.clamp(cam.position.y, viewHeight / 2,
				mapHeight - viewHeight / 2);
		cam.update();

		mainStage.draw();
		uiStage.draw();

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

	public void dispose () {

	}
}
