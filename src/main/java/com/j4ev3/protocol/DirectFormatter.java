package com.j4ev3.protocol;

import com.j4ev3.protocol.BasicCommand.CommandType;
import com.j4ev3.protocol.DirectCommand.Command;
import java.io.IOException;


/**
    Byte 0 – 1: Command size, Little Endian. Command size not including these 2 bytes
    Byte 2 – 3: Message counter, Little Endian. Forth running counter
    Byte 4: Command type. See defines above
    Byte 5 - 6: Reservation (allocation) of global and local variables using a compressed format (globals reserved in byte 5 and the 2 lsb of byte 6, locals reserved in the upper 6 bits of byte 6) – see below:
    Byte 7 – n: Byte codes as a single command or compound commands (I.e. more commands composed as a small program)
    Locals = “l” and Globals = “g”
    Byte 6: Byte 5:
    Bit no: 76543210 76543210
    Var size: llllllgg gggggggg
    gg gggggggg Global vars reservation 0 – (210 – 1) 0..1023 bytes
 */
public final class DirectFormatter extends BasicByteCodeFormatter {
    private byte local = 0;
    private short global = 0;
    
    public DirectFormatter(Command cd, boolean reply, short messageCounter) throws IOException {
        super(createCommand(cd, reply), messageCounter);
    }
    
    public DirectFormatter(Command cd, boolean reply) throws IOException {
        this(cd, reply, (short)0);
    }
    
    public void setGlobal(int g) {
        if (g >= 1024 || g < 0) throw new IllegalArgumentException("Global buffer must be less than 1024 bytes");
        global = (short)g;
    }
    
    public void setLocal(int l) {
        if (l >= 64 || l < 0) throw new IllegalArgumentException("Local buffer must be less than 64 bytes");
        local = (byte)l;
    }
    
    private static DirectCommand createCommand(Command cd, boolean reply) {
        DirectCommand dc;
        if (reply == BasicCommand.REPLY) {
            dc = new DirectCommand(cd, CommandType.DIRECT_COMMAND_REPLY);
        } else {
            dc = new DirectCommand(cd, CommandType.DIRECT_COMMAND_NO_REPLY);
        }
        return dc;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        int bodyLength = mStream.size();
        int HEAD_SIZE = 7;
        byte[] request = new byte[bodyLength + HEAD_SIZE];
        bodyLength += HEAD_SIZE - 2;//excluding first two bytes (HEAD_SIZE - 2)
        request[0] = (byte)(bodyLength & 0xFF);
        request[1] = (byte)((bodyLength >>> 8) & 0xFF);
        request[2] = (byte)(messageCounter & 0xFF);
        request[3] = (byte)((messageCounter >>> 8) & 0xFF);
        request[4] = cmd.getCommandType().toByteCode();
        request[5] = (byte)(global & 0xFF);
        request[6] = (byte)((local << 2) | ((global >>> 8) & 0x03));
        System.arraycopy(mStream.toByteArray(), 0, request, HEAD_SIZE, mStream.size());
        return request;
    }
}
