package com.j4ev3.app.fileSystem;

import java.io.IOException;

public class SendHandler implements ProcessHandler {
    private int total = 0;
    private int current = 0;
    
    @Override
    public void setTotal(int ttl) throws IOException {
        if (ttl > 0) {
            synchronized (this) {
                total = ttl;
                current = 0;
            }
        } else {
            throw new IOException("total size must be greater then 0");
        }
    }
    
    @Override
    public void setCurrent(int size) throws IOException {
        if (size < current) {
            throw new IOException("new size must be greater or equal then currentSize " + current);
        }
        if (size > total) {
            throw new IOException("new size must be less or equal then total size " + total);
        }
        synchronized (this) {
            current = size;
        }
    }
    
    @Override
    public int getCurrent() {
        synchronized (this) {
            return current;
        }
    }
    
    @Override
    public int getTotal() {
        synchronized (this) {
            return total;
        }
    }
}
