package com.example.venaj.slovnifotbal;

public class Gamer {

    private String name;
    private boolean anotherWord, computerTurn, inGame;

    public Gamer(String name){
        this.name = name;
        anotherWord = true;
        computerTurn = true;
        inGame = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComputerTurn() {
        return computerTurn;
    }

    public void setComputerTurn(boolean computerTurn) {
        this.computerTurn = computerTurn;
    }

    public boolean isAnotherWord() {
        return anotherWord;
    }

    public void setAnotherWord(boolean anotherWord) {
        this.anotherWord = anotherWord;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
