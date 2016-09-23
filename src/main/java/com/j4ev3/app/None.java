package com.j4ev3.app;

public class None extends Port {
    public None(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    @Override
    public String getName() {
        return "None";
    }
}
