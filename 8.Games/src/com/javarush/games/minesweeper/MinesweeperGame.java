package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private  boolean isGameStopped;
    private  int countClosedTiles = SIDE * SIDE;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if(!isGameStopped) {
            openTile(x, y);
        } else restart();
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                setCellValue(x, y, "");
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }


    private void countMineNeighbors() {
        GameObject currentCell;
        List<GameObject> listNeighbors = new ArrayList<>();
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                currentCell = gameField[y][x];
                if (!currentCell.isMine) {
                    listNeighbors = getNeighbors(currentCell);
                    for (GameObject neighbor : listNeighbors) {
                        if (neighbor.isMine) {
                            currentCell.countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void openTile(int x, int y) {
        GameObject cell = gameField[y][x];
        if (isGameStopped || cell.isFlag || cell.isOpen) {
            return;
        }
        cell.isOpen = true;
        countClosedTiles--;
        if (countClosedTiles == countMinesOnField && !cell.isMine) {
            setCellColor(x, y, Color.GREEN);
            win();
            return;
        }
        List<GameObject> listOfNeighbors;
        setCellColor(x, y, Color.GREEN);
        if (cell.isMine) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else if (cell.countMineNeighbors == 0){
            setCellValue(x, y, "");
            listOfNeighbors = getNeighbors(cell);
            score += 5;
            setScore(score);
            for (GameObject gameObject : listOfNeighbors) {
                if (!gameObject.isOpen) {
                    openTile(gameObject.x, gameObject.y);
                }
            }
        } else {
            setCellNumber(x, y, cell.countMineNeighbors);
            score += 5;
            setScore(score);
        }
    }


    private void markTile(int x, int y) {
        GameObject cell = gameField[y][x];
        if (cell.isOpen || (countFlags == 0 && !cell.isFlag)) {
            return;
        }
        if (!cell.isFlag) {
            cell.isFlag = true;
            countFlags--;
            setCellColor(x, y, Color.YELLOW);
            setCellValue(x, y, FLAG);
        } else {
            cell.isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.ORANGE);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Game Over", Color.WHITE, 50);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "YOU WIN!", Color.BLACK, 50);
    }

    private void restart() {
        isGameStopped = false;
        countMinesOnField = 0;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        createGame();
    }

}