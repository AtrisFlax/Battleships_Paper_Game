package com.liver_rus.Battleships.Client;

/**
 * Класс пар координат x, y. tag  попадание в клетку
 */

public class FieldCoord {
    private final int x, y;

    private boolean tag;

    FieldCoord(int x, int y) {
        this.x = x;
        this.y = y;
        this.tag = false;
    }

    FieldCoord(String str1, String str2) {
        x = Integer.parseInt(str1);
        y = Integer.parseInt(str2);
        tag = false;
    }

    boolean getTag() {
        return tag;
    }

    void setTag() {
        tag = true;
    }

    final int getX() {
        return x;
    }

    final int getY() {
        return y;
    }

    @Override
    public String toString() {
        return Integer.toString(x) + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldCoord that = (FieldCoord) o;

        return x != that.x || y != that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}

