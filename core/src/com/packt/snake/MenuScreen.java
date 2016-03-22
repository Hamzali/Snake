package com.packt.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen extends ScreenAdapter{

    SnakeGame game;

    private float HEIGHT;
    private float WIDTH;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont bitmapFont;


    public MenuScreen(SnakeGame game){
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(Gdx.graphics.getWidth() / 2,  Gdx.graphics.getHeight() / 2, 0);
        camera.update();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        HEIGHT = viewport.getWorldHeight();
        WIDTH = viewport.getWorldWidth();
    }

    @Override
    public void render(float delta) {

        clearScreen();
        draw();

    }

    public void draw() {
        batch.begin();

        layout.setText(bitmapFont, "MENU");
        bitmapFont.draw(batch, "MENU",
                (WIDTH - layout.width) / 2,
                (HEIGHT - layout.height) / 2);

        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 255);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
