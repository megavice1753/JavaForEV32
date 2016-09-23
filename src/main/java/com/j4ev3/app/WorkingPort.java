package com.j4ev3.app;

import com.j4ev3.protocol.DirectCommand;
import com.j4ev3.protocol.DirectFormatter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public abstract class WorkingPort extends Port {
    public WorkingPort(byte t, byte m, byte p, byte l, Brick br) {
        super(t, m, p, l, br);
    }
    
    /**
     *
     * @param mode : -1 Don’t change mode, otherwise according documentation
     * @return
     * @throws IOException
     */
    public float readValue(int mode) throws IOException {
        if (mode < -1 || mode > 1) {
            throw new IOException("Unrecognized mode value: " + mode + " must be between -1 and 1");
        }
        int nvalue = 1;//Number of return values
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.INPUT_DEVICE, DirectCommand.REPLY);
        byte READY_SI = 0x1D;
        df.addByteCode(READY_SI);
        df.setGlobal(4 * nvalue);//nvalue * 4 bytes float
        df.setLocal(0);
        df.LC0(getLayer());//layer
        df.LC0(getPort());//port
        df.LC0(0); //Specify device type (0 = Don’t change type)
        df.LC0(mode); //Device mode [0-7] (-1 = Don’t change mode)
        df.LC0(nvalue); //Number of return values
        df.addGlobalIndex((byte)0x00);//need to change device type and select mode type
        byte[] req = df.toByteArray();
        brick.getCommunicator().write(req, 0);
        byte[] reply = brick.getCommunicator().read(0,0);
        float[] result = new float[1];
        for (int i = 0; i < 1; ++i) {
                byte[] data = Arrays.copyOfRange(reply, 5 + 4 * i, 9 + 4 * i);
                result[i] = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return result[0];
    }
}
