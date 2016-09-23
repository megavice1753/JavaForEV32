package com.j4ev3.app;

import com.j4ev3.protocol.DirectFormatter;
import com.j4ev3.protocol.DirectCommand;
import java.io.IOException;

public abstract class OutputPort extends WorkingPort {
    public OutputPort(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    public static byte convertPort(byte port) {
        return (byte)(0x01 << (port & 0x03));
    }
    
    private static void checkParams(int layer, int ports) throws IOException {
        if (layer < 0 || layer > 3) {
            throw new IOException("Unrecognized layer value: " + layer + " must be between 0 and 3");
        }
        if (ports < 0x00 || ports > 0x0F) {
            throw new IOException("Unrecognized ports value: " + ports + " must be between 0x00 and 0x0F");
        }
    }
    
    public static byte[] move(int layer, int ports, int power) throws IOException {
        checkParams(layer, ports);
        if (power < -100 || power > 100) {
            throw new IOException("Unrecognized power value: " + power + " must be between -100 and 100");
        }
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.LC0(layer);
        byte all = (byte)ports;
        df.LC0(all);
        df.LC1(power);
        df.addByteCode(DirectCommand.Command.OUTPUT_START.toByteCode());
        df.LC0(layer);
        df.LC0(all);
        return df.toByteArray();
    }
    
    public static byte[] stop(int layer, int ports) throws IOException {
        checkParams(layer, ports);
        byte all = (byte)ports;
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.LC0(layer);
        df.LC0(all);
        df.LC0(0);
        return df.toByteArray();
    }
    
    public static byte[] setPolarity(int layer, int ports, int polarity) throws IOException {
        checkParams(layer, ports);
        if (polarity < -1 || polarity > 1) {
            throw new IOException("Unrecognized layer value: " + layer + " must be between -1 and 1");
        }
        byte all = (byte)ports;
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POLARITY, DirectCommand.NO_REPLY);
        df.LC0(layer);
        df.LC0(all);
        df.LC0(polarity);
        return df.toByteArray();
    }
    
    public static byte[] clearCount(int layer, int ports) throws IOException {
        checkParams(layer, ports);
        byte all = (byte)ports;
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_CLR_COUNT, DirectCommand.NO_REPLY);
        df.LC0(layer);
        df.LC0(all);
        return df.toByteArray();
    }
    
    public static byte[] stepMovement(int layer, int ports, int power, int angle) throws IOException {
        checkParams(layer, ports);
        if (power < -100 || power > 100) {
            throw new IOException("Unrecognized power value: " + power + " must be between -100 and 100");
        }
        if (angle < 0) {
            throw new IOException("Unrecognized angle value: " + angle + " must be over  or equal 0");
        }
        byte all = (byte)ports;
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_STEP_POWER, DirectCommand.NO_REPLY);
        df.LC0(layer);
        df.LC0(all);
        df.LC1(power);
        df.LC0(0);
        df.LC4(angle);
        df.LC0(0);
        df.LC0(1);//break 1 - firmly fixed the rotation after the execution, not firmly 
        return df.toByteArray();
    }  
    
}
