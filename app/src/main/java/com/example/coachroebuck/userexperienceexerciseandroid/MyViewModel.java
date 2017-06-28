package com.example.coachroebuck.userexperienceexerciseandroid;

class MyViewModel {

    private boolean isExpanded;
    private int backgroundColor;
    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;
    private int width = 0;
    private int height = 0;
    private int position = 0;

    MyViewModel(final boolean isExpanded,
                final int backgroundColor) {

        this.isExpanded = isExpanded;
        this.backgroundColor = backgroundColor;
    }

    boolean isExpanded() {
        return isExpanded;
    }

    void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    int getBackgroundColor() {
        return this.backgroundColor;
    }

    int getLeft() {
        return left;
    }

    void setLeft(int left) {
        this.left = left;
    }

    int getTop() {
        return top;
    }

    void setTop(int top) {
        this.top = top;
    }

    int getRight() {
        return right;
    }

    void setRight(int right) {
        this.right = right;
    }

    int getBottom() {
        return bottom;
    }

    void setBottom(int bottom) {
        this.bottom = bottom;
    }

    void setWidth(final int width) {
        this.width = width;
    }

    int getWidth() {
        return this.width;
    }

    void setHeight(int height) {
        this.height = height;
    }

    int getHeight() {
        return height;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
