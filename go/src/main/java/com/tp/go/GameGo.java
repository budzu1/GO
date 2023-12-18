package com.tp.go;

public class GameGo {

    private Player player1;
    private Player player2;
    private Checker checker;
    private Board board;

    public GameGo(Player player1, Checker checker, int sizeOfBoard) {
        this.player1 = player1;
        this.checker = checker;
        board = new Board(sizeOfBoard);
    }
}
