package com.j4ev3.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class BasicByteCodeFormatter {
    //see bytecodes.h
    private static final byte PRIMPAR_SHORT = 0x00;
    private static final byte PRIMPAR_LONG = (byte)0x80;
    private static final byte PRIMPAR_VALUE = 0x3F;
    private static final byte PRIMPAR_INDEX = 0x1F;
    private static final byte PRIMPAR_CONST = 0x00;
    private static final byte PRIMPAR_VARIABEL = 0x40;
    private static final byte PRIMPAR_GLOBAL = 0x20;
    private static final byte PRIMPAR_1_BYTE = 0x01;
    private static final byte PRIMPAR_2_BYTES = 0x02;
    private static final byte PRIMPAR_4_BYTES = 0x03;
    public static final byte PORT1 = 0x00;
    public static final byte PORT2 = 0x01;
    public static final byte PORT3 = 0x02;
    public static final byte PORT4 = 0x03;
    public static final byte PORTA = 0x10;
    public static final byte PORTB = 0x11;
    public static final byte PORTC = 0x12;
    public static final byte PORTD = 0x13;

    public static final byte EMPTY_PORT = 0x7E;
    public static final byte ERROR_PORT = 0x7F;

    public static final byte EV3_LARGE_MOTOR = 0x07;
    public static final byte EV3_LARGE_MOTOR_DEGREE = 0x00;
    public static final byte EV3_LARGE_MOTOR_ROTATION = 0x01;
    public static final byte EV3_LARGE_MOTOR_POWER = 0x02;

    public static final byte EV3_MEDIUM_MOTOR = 0x08;
    public static final byte EV3_MEDIUM_MOTOR_DEGREE = 0x00;
    public static final byte EV3_MEDIUM_MOTOR_ROTATION = 0x01;
    public static final byte EV3_MEDIUM_MOTOR_POWER = 0x02;

    public static final byte EV3_TOUCH = 0x10;
    public static final byte EV3_TOUCH_TOUCH = 0x00;
    public static final byte EV3_TOUCH_BUMP = 0x01;

    public static final byte EV3_COLOR = 0x1D;
    public static final byte EV3_COLOR_REFLECTED = 0x00;
    public static final byte EV3_COLOR_AMBIENT = 0x01;
    public static final byte EV3_COLOR_COLOR = 0x02;

    public static final byte EV3_IR = 0x21;
    public static final byte EV3_IR_PROXIMITY = 0x00;
    public static final byte EV3_IR_SEEKER = 0x01;
    public static final byte EV3_IR_REMOTE = 0x02;
    
    public static final byte EV3_GYRO = 0x20;
    public static final byte EV3_GYRO_ANG = 0x00;
    public static final byte EV3_GYRO_RATE = 0x01;

    protected final ByteArrayOutputStream mStream;
    protected final DataOutputStream mWriter;
    protected final BasicCommand cmd;
    protected final short messageCounter;

    protected BasicByteCodeFormatter(BasicCommand cd, short counter) throws IOException {
        mStream = new ByteArrayOutputStream();
        mWriter = new DataOutputStream(mStream);
        cmd = cd;
        messageCounter = counter;
        mWriter.write(cmd.getOperand());
    }

    public abstract byte[] toByteArray() throws IOException;

    public void LC0(int v) throws IOException {
        mWriter.writeByte(v & PRIMPAR_VALUE | PRIMPAR_SHORT | PRIMPAR_CONST);
    }

    public void LC1(int v) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_CONST | PRIMPAR_1_BYTE);
        mWriter.writeByte(v);
    }

    public void LC2(int v) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_CONST | PRIMPAR_2_BYTES);
        mWriter.writeByte(v);
        mWriter.writeByte(v >> 8);
    }

    public void LC4(int v) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_CONST | PRIMPAR_4_BYTES);
        mWriter.writeByte(v);
        mWriter.writeByte(v >> 8);
        mWriter.writeByte(v >> 16);
        mWriter.writeByte(v >> 24);
    }

    public void addString(String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        mWriter.write(bytes);
        mWriter.writeByte((byte)0x00);
    }

    public void addByteCode(byte opcode) throws IOException {
        mWriter.writeByte(opcode);
    }

    public void GV0(int i) throws IOException {
        mWriter.writeByte(i & PRIMPAR_INDEX | PRIMPAR_SHORT | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL);
    }

    public void GV1(int i) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_1_BYTE);
        mWriter.writeByte(i);
    }

    public void GV2(int i) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_2_BYTES);
        mWriter.writeByte(i);
        mWriter.writeByte(i >> 8);
    }

    public void GV4(int i) throws IOException {
        mWriter.writeByte(PRIMPAR_LONG  | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_4_BYTES);
        mWriter.writeByte(i);
        mWriter.writeByte(i >> 8);
        mWriter.writeByte(i >> 16);
        mWriter.writeByte(i >> 24);
    }
    
    public void addSize(int x) throws IOException {
        mWriter.writeByte(x);
        mWriter.writeByte(x >> 8);
        mWriter.writeByte(x >> 16);
        mWriter.writeByte(x >> 24);
    }
    
    public void addSize(short x) throws IOException {
        mWriter.writeByte(x);
        mWriter.writeByte(x >> 8);
    }
    
    public void addBytes(byte[] data, int offset, int length) throws IOException {
        mWriter.write(data, offset, length);
    }
}
