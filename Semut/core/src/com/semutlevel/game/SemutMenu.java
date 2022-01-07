package com.semutlevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class SemutMenu implements Screen
{
    private Stage uiStage;
    private Game game;

    public SemutMenu(Game g)
    {
        game = g;
        create();
    }

    public void create() {
        uiStage = new Stage();

        BaseActor background = new BaseActor();
        background.setTexture(new Texture(Gdx.files.internal("tiles-menu.png")));
        uiStage.addActor(background);

        BaseActor titleText = new BaseActor();
        titleText.setTexture(new Texture(Gdx.files.internal("semute-cake.png")));
        titleText.setPosition(20, 100);
        uiStage.addActor(titleText);

        BitmapFont font = new BitmapFont();
        String text = " Press P to play, M for main menu ";
        LabelStyle style = new LabelStyle(font, Color.YELLOW);
        Label instructions = new Label(text, style);
        instructions.setFontScale(2);
        instructions.setPosition(100, 50);

        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color(new Color(1,1,0,1), 0.5f),
                                Actions.delay(0.5f),
                                Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f)
                        )
                )
        );

        uiStage.addActor(instructions);
    }

    public void render(float dt) {
        if (Gdx.input.isKeyPressed(Keys.P))
            game.setScreen(new SemutLevel(game));

        uiStage.act(dt);

        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.draw();
    }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
