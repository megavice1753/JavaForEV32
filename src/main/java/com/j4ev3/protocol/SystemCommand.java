package com.j4ev3.protocol;

public final class SystemCommand extends BasicCommand {
    public static final byte SUCCESS = 0x00;
    public static final byte UNKNOWN_HANDLE = 0x01;
    public static final byte HANDLE_NOT_READY = 0x02;
    public static final byte CORRUPT_FILE = 0x03;
    public static final byte NO_HANDLES_AVAILABLE = 0x04;
    public static final byte NO_PERMISSION = 0x05;
    public static final byte ILLEGAL_PATH = 0x06;
    public static final byte FILE_EXITS = 0x07;
    public static final byte END_OF_FILE = 0x08;
    public static final byte SIZE_ERROR = 0x09;
    public static final byte UNKNOWN_ERROR = 0x0A;
    public static final byte ILLEGAL_FILENAME = 0x0B;
    public static final byte ILLEGAL_CONNECTION = 0x0C;
    
    
    public static final int MAX_FILE_CHUNK = 65000;
    
    private final Command cmd;
    
    public SystemCommand(Command c, CommandType ct) {
        super(ct);
        cmd = c;
    }
    
    @Override
    public byte getOperand() {
        return cmd.toByteCode();
    }
    
    public enum Command {
        BEGIN_DOWNLOAD((byte)0x92),
        CONTINUE_DOWNLOAD((byte)0x93),
        BEGIN_UPLOAD((byte)0x94),
        CONTINUE_UPLOAD((byte)0x95),
        BEGIN_GETFILE((byte)0x96),
        CONTINUE_GETFILE((byte)0x97),
        CLOSE_FILEHANDLE((byte)0x98),
        LIST_FILES((byte)0x99),
        CONTINUE_LIST_FILES((byte)0x9A),
        CREATE_DIR((byte)0x9B),
        DELETE_FILE((byte)0x9C),
        LIST_OPEN_HANDLES((byte)0x9D),
        WRITEMAILBOX((byte)0x9E),
        BLUETOOTHPIN((byte)0x9F),
        ENTERFWUPDATE((byte)0xA0);
        
        private final byte code;
        
        private Command(byte b) {
            code = b;
        }
        
        public byte toByteCode() {
            return code;
        }
    }
}
