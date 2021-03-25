package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public boolean isAlive = true;
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC31";
    private static final String BODY_SIGN = "\uD83D\uDC3E";
    private static final String DEAD_SIGN = "☠";
    private Direction direction = Direction.LEFT;

    public int getLength() {
        return snakeParts.size();
    }

    public Snake(int x, int y) {
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    public void draw(Game game) {
        Color color = isAlive ? Color.BLACK : Color.RED;
        for (int i = 0; i < snakeParts.size(); i++) {
            if (i == 0)
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, isAlive ? HEAD_SIGN : DEAD_SIGN, color, 75);
            else
                game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, color, 55);
        }
    }

    public void setDirection(Direction direction) {
        if ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }
        if ((this.direction == Direction.UP || this.direction == Direction.DOWN) && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }
        if ((this.direction == Direction.DOWN && direction == Direction.UP) || (this.direction == Direction.UP && direction == Direction.DOWN)
                || (this.direction == Direction.LEFT && direction == Direction.RIGHT) || (this.direction == Direction.RIGHT && direction == Direction.LEFT)) {
            return;
        }
        
        this.direction = direction;
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (newHead.x >= SnakeGame.WIDTH || newHead.x < 0 || newHead.y < 0
                || newHead.y >= SnakeGame.HEIGHT) {
            isAlive = false;
            return;
        }

        if(checkCollision(newHead)) {
            isAlive = false;
            return;
        }

        snakeParts.add(0, newHead);

        if (newHead.x == apple.x && newHead.y == apple.y) {
                apple.isAlive = false;
            } else {
                removeTail();
              }
    }

    public GameObject createNewHead() {
        switch (direction) {
            case LEFT:
                return new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
            case DOWN:
                return new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
            case RIGHT:
                return new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
            case UP:
                return new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);

        }
        return null;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        for (GameObject go : snakeParts) {
            if (go.x == gameObject.x && go.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }
}
