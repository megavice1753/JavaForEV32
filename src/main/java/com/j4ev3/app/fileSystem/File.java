package com.j4ev3.app.fileSystem;

public class File extends Node {
    private int size = 0;
    protected File(String name, int size) {
        super(name);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", " + getSize() + " bytes";
    }
}
