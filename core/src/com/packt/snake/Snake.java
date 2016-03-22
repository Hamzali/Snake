package com.packt.snake;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Snake {

    public Texture headTexture;
    public Texture bodyTexture;

    private static final int MOVEMENT = 20;
    private int x, y;
    private int xBeforeUpdate = 0, yBeforeUpdate = 0;

    // Enumeration for directions
    public enum Directions {
        RIGHT , LEFT, UP, DOWN
    }
    private Directions direction = Directions.UP;
    public boolean directionSet = false;

    // list for snake's body
    public Array<BodyPart> bodyParts = new Array<BodyPart>();


    public  Snake() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public int getMovement() {
        return MOVEMENT;
    }

    public boolean update(int HEIGHT, int WIDTH) {
        move();
        checkForOutOfBounds(HEIGHT, WIDTH);
        updateBodyPartsPosition();
        directionSet = false;
        return checkSnakeBodyCollison();
    }

    public void reset() {
        bodyParts.clear();
        x = 0;
        y = 0;
        direction = Directions.UP;
        xBeforeUpdate = 0;
        yBeforeUpdate = 0;
        directionSet = false;
        headTexture = new Texture(Gdx.files.internal("snakehead_up.png"));

    }

    public void draw(SpriteBatch batch) {
        batch.draw(headTexture, x, y);
        for (BodyPart bodyPart: bodyParts){
            bodyPart.draw(batch);
        }

    }

    public void checkForOutOfBounds(float HEIGHT, float WIDTH) {
        if(y > HEIGHT) {
            y = 0;
        }

        if(y < 0) {
            y = (int)HEIGHT - MOVEMENT;
        }

        if(x > WIDTH) {
            x = 0;
        }

        if(x < 0) {
            x = (int)WIDTH - MOVEMENT;
        }
    }

    public void move() {
        xBeforeUpdate = x;
        yBeforeUpdate = y;
        switch(direction) {
            case UP:
                y += MOVEMENT;
                break;
            case DOWN:
                y -= MOVEMENT;
                break;
            case RIGHT:
                x += MOVEMENT;
                break;
            case LEFT:
                x -= MOVEMENT;
                break;
        }
    }

    public boolean updateDirection(Directions newSnakeDirection) {
        if(!directionSet && direction != newSnakeDirection){
            directionSet = true;
            switch (direction) {
                case LEFT:
                    return updateIfNotOppositeDirection(newSnakeDirection, Directions.RIGHT);
                case RIGHT:
                    return updateIfNotOppositeDirection(newSnakeDirection, Directions.LEFT);
                case UP:
                    return updateIfNotOppositeDirection(newSnakeDirection, Directions.DOWN);
                case DOWN:
                    return updateIfNotOppositeDirection(newSnakeDirection, Directions.UP);
            }
        }

        return false;
    }

    private boolean updateIfNotOppositeDirection(Directions newSnakeDirection, Directions oppositeDirection){
        if (newSnakeDirection != oppositeDirection || bodyParts.size == 0) {
            direction = newSnakeDirection;
            return true;
        }else {
            return false;
        }
    }

    public class BodyPart {
        private int xBody, yBody;
        private Texture texture = bodyTexture;

        public BodyPart(Texture texture) {
            this.texture = texture;
        }

        public void updateBodyPosition(int x, int y) {
            xBody = x;
            yBody = y;
        }

        public void draw(SpriteBatch batch) {
            if (!(xBody == x && yBody == y)) batch.draw(texture, xBody, yBody);
        }
    }

    // Body collison
    public boolean checkSnakeBodyCollison() {
        for(BodyPart bodyPart : bodyParts){
            if(bodyPart.xBody == x && bodyPart.yBody == y){
                return true;
            }
        }

        return false;
    }

    public void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(xBeforeUpdate, yBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    public void addBodyPart() {
        BodyPart bodyPart = new BodyPart(bodyTexture);
        bodyPart.updateBodyPosition(x, y);
        bodyParts.insert(0, bodyPart);
    }
}
