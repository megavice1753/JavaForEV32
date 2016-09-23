package com.j4ev3.app;

import com.j4ev3.protocol.DirectFormatter;
import com.j4ev3.protocol.DirectCommand;
import java.io.IOException;

public class Panel {
    public static final byte OFF = 0x00;
    public static final byte GREEN = 0x01;
    public static final byte RED = 0x02;
    public static final byte ORANGE = 0x03;
    public static final byte GREEN_FLASHING = 0x04;
    public static final byte RED_FLASHING = 0x05;
    public static final byte ORANGE_FLASHING = 0x06;
    public static final byte GREEN_PULSE = 0x07;
    public static final byte RED_PULSE = 0x08;
    public static final byte ORANGE_PULSE = 0x09;
    
    private static final byte LED_CMD = 0x1B;
    
    private byte currentColor = OFF;
    
    public Panel() throws IOException {
        this(OFF);
    }
    
    public Panel(byte color) throws IOException {
        setColor(color);
    }
    
    public final void setColor(byte color) throws IOException {
        if (color < OFF || color > ORANGE_PULSE) {
            throw new IOException("Unrecognized color: " + color + " must be between 0x00 and 0x09");
        }
        currentColor = color;
    }
    
    public byte getColor() {
        return currentColor;
    }
    
    protected byte[] apply() throws IOException {
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.UI_WRITE, DirectCommand.NO_REPLY);
        df.addByteCode(LED_CMD);
        df.LC0(currentColor);
        return df.toByteArray();
    }
}
