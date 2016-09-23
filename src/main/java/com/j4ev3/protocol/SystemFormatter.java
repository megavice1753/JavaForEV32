package com.j4ev3.protocol;

import com.j4ev3.protocol.BasicCommand.CommandType;
import com.j4ev3.protocol.SystemCommand.Command;
import java.io.IOException;

/**
    Byte 0 – 1: Command size, Little Endian. Command size not including these 2 bytes
    Byte 2 – 3: Message counter, Little Endian. Forth running counter
    Byte 4: Command type. See defines above:
    Byte 5: System Command. See the definitions below:
    Byte 6 – n: Depends on the System Command given in byte 5.
 */
public class SystemFormatter extends BasicByteCodeFormatter {
    
    public SystemFormatter(Command cd, boolean reply, short messageCounter) throws IOException {
        super(createCommand(cd, reply), messageCounter);
    }
    
    public SystemFormatter(Command cd, boolean reply) throws IOException {
        this(cd, reply, (short)0);
    }
    
    private static SystemCommand createCommand(Command cd, boolean reply) {
        SystemCommand sc;
        if (reply == BasicCommand.REPLY) {
            sc = new SystemCommand(cd, CommandType.SYSTEM_COMMAND_REPLY);
        } else {
            sc = new SystemCommand(cd, CommandType.SYSTEM_COMMAND_NO_REPLY);
        }
        return sc;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        int bodyLength = mStream.size();
        int HEAD_SIZE = 5;
        byte[] request = new byte[bodyLength + HEAD_SIZE];
        bodyLength += HEAD_SIZE - 2;//excluding first two bytes (HEAD_SIZE - 2)
        request[0] = (byte)(bodyLength & 0xFF);
        request[1] = (byte)((bodyLength >>> 8) & 0xFF);
        request[2] = (byte)(messageCounter & 0xFF);
        request[3] = (byte)((messageCounter >>> 8) & 0xFF);
        request[4] = cmd.getCommandType().toByteCode();
        System.arraycopy(mStream.toByteArray(), 0, request, HEAD_SIZE, mStream.size());
        return request;
    }
}
