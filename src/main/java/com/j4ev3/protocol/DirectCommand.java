package com.j4ev3.protocol;

public final class DirectCommand extends BasicCommand {
    private final Command cmd;
    
    public DirectCommand(Command c, CommandType ct) {
        super(ct);
        cmd = c;
    }
    
    @Override
    public byte getOperand() {
        return cmd.toByteCode();
    }
    
    public enum Command {
        OUTPUT_SET_TYPE((byte)0xA1),
        OUTPUT_RESET((byte)0xA2),
        OUTPUT_STOP((byte)0xA3),
        OUTPUT_POWER((byte)0xA4),
        OUTPUT_SPEED((byte)0xA5),
        OUTPUT_START((byte)0xA6),
        OUTPUT_POLARITY((byte)0xA7),
        OUTPUT_READ((byte)0xA8),
        OUTPUT_TEST((byte)0xA9),
        OUTPUT_READY((byte)0xAA),
        OUTPUT_STEP_POWER((byte)0xAC),
        OUTPUT_TIME_POWER((byte)0xAD),
        OUTPUT_STEP_SPEED((byte)0xAE),
        OUTPUT_TIME_SPEED((byte)0xAF),
        OUTPUT_STEP_SYNC((byte)0xB0),
        OUTPUT_TIME_SYNC((byte)0xB1),
        OUTPUT_CLR_COUNT((byte)0xB2),
        OUTPUT_GET_COUNT((byte)0xB3),
        OUTPUT_PRG_STOP((byte)0xB4),
        DEVICE_LIST((byte)0x98),
        INPUT_DEVICE((byte)0x99),
        UI_WRITE((byte)0x82);
        
        private final byte code;
        
        private Command(byte b) {
            code = b;
        }
        
        public byte toByteCode() {
            return code;
        }
    }
}
