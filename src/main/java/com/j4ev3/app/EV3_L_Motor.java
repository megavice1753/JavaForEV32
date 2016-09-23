package com.j4ev3.app;

public class EV3_L_Motor extends OutputPort {
    public EV3_L_Motor(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    @Override
    public String getName() {
        return "EV3_L_Motor";
    }
}
