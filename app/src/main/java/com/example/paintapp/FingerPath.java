package com.example.paintapp;

import android.graphics.Path;

public class FingerPath {
    private int color;
    private int strokeWidth;
    private Path path;

    public FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public Path getPath() {
        return path;
    }
}
