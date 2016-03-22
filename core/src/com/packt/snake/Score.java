package com.packt.snake;


public class Score {

    private int score;
    private String name;

    public Score(int score, String name){
        this.score = score;
        this.name = name;
    }
    public Score() {
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

    public void addScore(int point) {
        score += point;
    }

    @Override
    public String toString() {
        return "name" + score;
    }
}
