package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    private int GOAL = 28;
    private int score;
    private int stage = 1;

    @Override
    public void initialize() {
       setScreenSize(WIDTH, HEIGHT);
       createGame();
    }

    private void createGame() {
        if (stage == 1) GOAL = 23;
        if (stage == 2) GOAL = 25;
        if (stage == 3) GOAL = 21;
        score = 0;
        setScore(score);
        snake = new Snake(WIDTH/2, HEIGHT/2);
        createNewApple();
        isGameStopped = false;
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            score = snake.getLength();
            setScore(score);
            createNewApple();
            if (stage == 1) turnDelay -= 5;
            if (stage == 2) turnDelay -= 10;
            if (stage == 3) turnDelay -= 15;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive) {
            gameOver();
        }
        if (snake.getLength() > GOAL) {
            win();
        }
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch(key) {
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case SPACE:
                if (isGameStopped) createGame();
                break;
        }
    }

    private void createNewApple() {
        Apple tempApple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        while (snake.checkCollision(tempApple)) {
            tempApple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        }
        apple = tempApple;
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "GAME OVER", Color.WHITE, 75);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        if (stage == 1) {
            showMessageDialog(Color.BLACK, "STAGE 1 COMPLETE", Color.WHITE, 50);
            stage = 2;
            return;
        }
        if (stage == 2) {
            showMessageDialog(Color.BLACK, "STAGE 2 COMPLETE", Color.WHITE, 50);
            stage = 3;
            return;
        }
        if (stage == 3) {
            showMessageDialog(Color.BLACK, "YOU WIN", Color.WHITE, 75);
            stage = 1;
        }
    }
}
