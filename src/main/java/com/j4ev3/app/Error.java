package com.j4ev3.app;

public class Error extends Port {
    public Error(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    @Override
    public String getName() {
        return "Error";
    }
}
