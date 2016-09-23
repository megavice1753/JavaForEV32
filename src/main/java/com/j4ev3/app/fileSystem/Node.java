package com.j4ev3.app.fileSystem;

public class Node {
    private String name;

    protected Node(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
}
