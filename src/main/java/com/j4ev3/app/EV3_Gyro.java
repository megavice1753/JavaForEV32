package com.j4ev3.app;

public class EV3_Gyro extends InputPort {
    public EV3_Gyro(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    @Override
    public String getName() {
        return "EV3_Color";
    }
}
