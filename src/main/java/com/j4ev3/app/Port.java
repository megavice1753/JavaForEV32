package com.j4ev3.app;

import static com.j4ev3.protocol.BasicByteCodeFormatter.*;

public abstract class Port {
    private final byte type;
    private final byte mode;
    private final byte port;
    private final byte layer;
    
    protected final Brick brick;
    
    public Port(byte t, byte m, byte p, byte l, Brick br) {
        type = t;
        mode = m;
        port = p;
        layer = l;
        brick = br;
    }
    
    public byte getType() {
        return type;
    }
    
    public byte getMode() {
        return mode;
    }
    
    public byte getPort() {
        return port;
    }
    
    public byte getLayer() {
        return layer;
    }
    
    public abstract String getName();
    
    public static String getPortAsString(byte port) {
        String name = null;
        switch (port) {
            case PORT1 : {
                name = "PORT1";
                break;
            }
            case PORT2 : {
                name = "PORT2";
                break;
            }
            case PORT3 : {
                name = "PORT3";
                break;
            }
            case PORT4 : {
                name = "PORT4";
                break;
            }
            case PORTA : {
                name = "PORTA";
                break;
            }
            case PORTB : {
                name = "PORTB";
                break;
            }
            case PORTC : {
                name = "PORTC";
                break;
            }
            case PORTD : {
                name = "PORTD";
            }
        }
        return name;
    }
}
