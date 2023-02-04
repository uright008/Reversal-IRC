package cn.stars.starx.module.impl;

public interface Draggable {

    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void setPosition(int x, int y, int width, int height);

    void readPosition();
}
