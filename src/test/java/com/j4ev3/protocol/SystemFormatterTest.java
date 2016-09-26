package com.j4ev3.protocol;

import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SystemFormatterTest {
    /**
     * Test of toByteArray method, of class SystemFormatter.
     * @throws java.io.IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.addSize(100000);
        sf.addString("hello world file name");
        byte[] bytecode = sf.toByteArray();
        assertTrue(Arrays.equals(bytecode,
                new byte[] {30, 0, 0, 0, 1, -110, -96, -122, 1, 0, 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 32, 102, 105, 108, 101, 32, 110, 97, 109, 101, 0})
        );
        
        sf = new SystemFormatter(SystemCommand.Command.BEGIN_GETFILE, SystemCommand.REPLY);
        short chunk = 1000;//SystemCommand.MAX_FILE_CHUNK
        sf.addSize(100000);
        sf.addString("hello world file name");
        bytecode = sf.toByteArray();
        assertTrue(Arrays.equals(bytecode,
                new byte[] {30, 0, 0, 0, 1, -106, -96, -122, 1, 0, 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 32, 102, 105, 108, 101, 32, 110, 97, 109, 101, 0})
        );
        
        sf = new SystemFormatter(SystemCommand.Command.CONTINUE_LIST_FILES, SystemCommand.REPLY);
        sf.addByteCode((byte)4);
        sf.addSize(2000);
        bytecode = sf.toByteArray();
        assertTrue(Arrays.equals(bytecode,
                new byte[] {9, 0, 0, 0, 1, -102, 4, -48, 7, 0, 0})
        );
        //String str = Arrays.toString(bytecode);
    }
}
