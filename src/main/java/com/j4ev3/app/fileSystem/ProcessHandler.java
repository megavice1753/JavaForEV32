package com.j4ev3.app.fileSystem;

import java.io.IOException;

public interface ProcessHandler {
    public void setTotal(int ttl) throws IOException;
    
    public void setCurrent(int size) throws IOException;
    
    public int getCurrent();
    
    public int getTotal();
}
