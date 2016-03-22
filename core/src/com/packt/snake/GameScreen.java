package com.packt.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    SnakeGame game;

    private Viewport viewport;
    private Camera camera;

    // Textures and batch class for drawing
    private SpriteBatch batch;
    private Texture apple;
    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();
    private Score playerScore = new Score();

    private Snake snake = new Snake();

    // Points
    private static final int APPLE_POINT = 10;

    // Height and Width
    private float HEIGHT;
    private float WIDTH;

    // Game over string
    private static final String GAME_OVER_TEXT = "GAME OVER!... Tap space to Restart...ESC to Exit";

    // Time variables
    private static final float MOVE_TIME = 0.1f;
    private float timer = MOVE_TIME;


    // Enumeration for game states
    private enum STATE {
        IN_GAME, GAME_OVER, PAUSE
    }
    private STATE state = STATE.IN_GAME;


    // Apple stuff
    private boolean appleAvailable = false;
    private int appleX, appleY;

    // Shape renderer
    private ShapeRenderer shapeRenderer;

    public GameScreen(SnakeGame game){
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        HEIGHT = viewport.getWorldHeight();
        WIDTH = viewport.getWorldWidth();


        // For drawing directly on to the screen
        shapeRenderer = new ShapeRenderer();

        // Creates our artist spirited class for drawing
        batch = new SpriteBatch();

        // Creates the fonts default is Arial 15
        bitmapFont = new BitmapFont();

        // Loads images of the snake for drawing
        snake.headTexture = new Texture(Gdx.files.internal("snakehead_up.png"));
        snake.bodyTexture = new Texture(Gdx.files.internal("snakebody.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
    }

    @Override
    public void render(float delta) {
        // Runs the time and updates it.
        checkGameState();
        if(state == STATE.IN_GAME){
            queryInput();
            updateSnake(delta);
            checkAppleCollison();
            checkAndPlaceApple();
        }
        clearScreen();
        if(state == STATE.IN_GAME)drawGrid();
        draw();

    }

    private void clearScreen () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        if (appleAvailable){
            batch.draw(apple, appleX, appleY);
        }
        // Drawing the snake
        snake.draw(batch);

        // Drawing the score
        layout.setText(bitmapFont, "Score: " + playerScore.getScore());
        bitmapFont.draw(batch, "Score: " + playerScore.getScore() + " ",
                ( WIDTH - layout.width),
                (HEIGHT - layout.height));

        if(state == STATE.GAME_OVER){
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT,
                    ( WIDTH - layout.width) / 2,
                    (HEIGHT - layout.height) / 2);
        }
        batch.end();
    }

    // Function for slicing the background to grids
    private static final int GRID_CELL = 20;
    private void drawGrid() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < WIDTH; x += GRID_CELL){
            for(int y = 0; y < HEIGHT; y += GRID_CELL )
                shapeRenderer.rect(x, y, GRID_CELL, GRID_CELL);
        }
        shapeRenderer.end();
    }

    private void updateSnake(float delta) {
            timer -= delta;
            if(timer <= 0){
                timer = MOVE_TIME;
                if(snake.update((int)HEIGHT, (int)WIDTH))state = STATE.GAME_OVER;
            }
    }

    private void queryInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(snake.updateDirection(Snake.Directions.UP))
            snake.headTexture = new Texture(Gdx.files.internal("snakehead_up.png"));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(snake.updateDirection(Snake.Directions.DOWN))
            snake.headTexture = new Texture(Gdx.files.internal("snakehead_down.png"));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(snake.updateDirection(Snake.Directions.LEFT))
            snake.headTexture = new Texture(Gdx.files.internal("snakehead_left.png"));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(snake.updateDirection(Snake.Directions.RIGHT))
            snake.headTexture = new Texture(Gdx.files.internal("snakehead_right.png"));
        }
    }

    private void checkAndPlaceApple () {
        if(!appleAvailable) {
            do{
                // We slice the window to little boxes then randomly choose one box
                appleX = MathUtils.random((int)WIDTH / snake.getMovement() - 1) * snake.getMovement();
                appleY = MathUtils.random((int)HEIGHT / snake.getMovement() - 1) * snake.getMovement();
            }while(appleX == snake.getX() && appleY == snake.getY());// If it is on the snake then  we reproduce
            appleAvailable = true;
        }
    }

    private void checkAppleCollison () {
        if (appleAvailable && appleX == snake.getX() && appleY == snake.getY()) {
            appleAvailable = false;
            snake.addBodyPart();
            playerScore.addScore(APPLE_POINT);
        }
    }

    private void doRestart() {
        state = STATE.IN_GAME;
        appleAvailable = false;
        snake.reset();
        timer = MOVE_TIME;
        playerScore.resetScore();
    }

    private void checkGameState() {
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
           if (state == STATE.IN_GAME) {state = STATE.PAUSE;}
            else if(state == STATE.PAUSE) {state = STATE.IN_GAME;}
        }


        if(state == STATE.GAME_OVER || state == STATE.PAUSE) {
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){dispose();game.setScreen(game.menuScreen);}
            else if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) doRestart();
        }
    }
}
