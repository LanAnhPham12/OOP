package Game.model;

import java.awt.Color;

import Game.controller.Board;

public class ShapeFactory extends Shape {
    private final Board board;

    public ShapeFactory(Board board) {
        this.board = board;
    }

    public Shape createShape(int index) {
        Color[] colors = { Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"),
                Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc") };

        switch (index) {
            case 0:
                return new Shape(new int[][] { { 1, 1, 1, 1 } }, board, colors[0]);
            case 1:
                return new Shape(new int[][] { { 1, 1, 1 }, { 0, 1, 0 } }, board, colors[1]);
            case 2:
                return new Shape(new int[][] { { 1, 1, 1 }, { 1, 0, 0 } }, board, colors[2]);
            case 3:
                return new Shape(new int[][] { { 1, 1, 1 }, { 0, 0, 1 } }, board, colors[3]);
            case 4:
                return new Shape(new int[][] { { 0, 1, 1 }, { 1, 1, 0 } }, board, colors[4]);
            case 5:
                return new Shape(new int[][] { { 1, 1, 0 }, { 0, 1, 1 } }, board, colors[5]);
            case 6:
                return new Shape(new int[][] { { 1, 1 }, { 1, 1 } }, board, colors[6]);
            default:
                throw new IllegalArgumentException("Invalid index for creating shape");
        }
    }

}
