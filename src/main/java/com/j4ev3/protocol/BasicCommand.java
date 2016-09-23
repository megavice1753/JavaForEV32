package com.j4ev3.protocol;

public abstract class BasicCommand {
    private final CommandType type;
    public static final boolean REPLY = true;
    public static final boolean NO_REPLY = !REPLY;
    
    protected BasicCommand(CommandType cmd) {
        type = cmd;
    }
    
    public CommandType getCommandType() {
        return type;
    }
    
    public abstract byte getOperand();
    
    protected enum CommandType {
        DIRECT_COMMAND_REPLY((byte)0x00),
        DIRECT_COMMAND_NO_REPLY((byte)0x80),
        SYSTEM_COMMAND_REPLY((byte)0x01),
        SYSTEM_COMMAND_NO_REPLY((byte)0x81);
   
        private final byte code;
        
        private CommandType(byte b) {
            code = b;
        }
        
        public byte toByteCode() {
            return code;
        }
    }
}
