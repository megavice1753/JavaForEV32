package com.j4ev3.protocol;

import java.io.IOException;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BasicByteCodeFormatterTest {
    
    @Test
    public void testLC0() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.LC0(31);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {31}));
        sf.mStream.reset();
        sf.LC0(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {0}));
        sf.mStream.reset();
        sf.LC0(-31);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {33}));
    }
    
    @Test
    public void testLC1() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.LC1(232);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-127, -24}));
        sf.mStream.reset();
        sf.LC1(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-127, 0}));
        sf.mStream.reset();
        sf.LC1(-255);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-127, 1}));
    }
    
    @Test
    public void testLC2() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.LC2(235);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-126, -21, 0}));
        sf.mStream.reset();
        sf.LC2(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-126, 0, 0}));
        sf.mStream.reset();
        sf.LC2(999);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-126, -25, 3}));
        sf.mStream.reset();
        sf.LC2(65535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-126, -1, -1}));
        sf.mStream.reset();
        sf.LC2(-65535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-126, 1, 0}));
    }
    
    @Test
    public void testLC4() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.LC4(235);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, -21, 0, 0, 0}));
        sf.mStream.reset();
        sf.LC4(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, 0, 0, 0, 0}));
        sf.mStream.reset();
        sf.LC4(999);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, -25, 3, 0, 0}));
        sf.mStream.reset();
        sf.LC4(65535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, -1, -1, 0, 0}));
        sf.mStream.reset();
        sf.LC4(2147483647);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, -1, -1, -1, 127}));
        sf.mStream.reset();
        sf.LC4(-2147483647);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-125, 1, 0, 0, -128}));
    }

    @Test
    public void testAddString() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        String str = "Hello world!";
        sf.addString(str);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33, 0}));
    }
    
    @Test
    public void testAddByteCode() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.addByteCode((byte)13);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {13}));
    }
    
    @Test 
    public void testGV0() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.GV0(31);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {127}));
        sf.mStream.reset();
        sf.GV0(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {96}));
        sf.mStream.reset();
        sf.GV0(-31);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {97}));
    }
    
    @Test 
    public void testGV1() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.GV1(31);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-31, 31}));
        sf.mStream.reset();
        sf.GV1(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-31, 0}));
        sf.mStream.reset();
        sf.GV1(-31);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-31, -31}));
        sf.mStream.reset();
        sf.GV1(255);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-31, -1}));
    }
    
    @Test 
    public void testGV2() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.GV2(31);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-30, 31, 0}));
        sf.mStream.reset();
        sf.GV2(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-30, 0, 0}));
        sf.mStream.reset();
        sf.GV2(-31);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-30, -31, -1}));
        sf.mStream.reset();
        sf.GV2(255);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-30, -1, 0}));
        sf.mStream.reset();
        sf.GV2(32535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-30, 23, 127}));
    }
    
    @Test 
    public void testGV4() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.GV4(31);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, 31, 0, 0, 0}));
        sf.mStream.reset();
        sf.GV4(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, 0, 0, 0, 0}));
        sf.mStream.reset();
        sf.GV4(-31);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, -31, -1, -1, -1}));
        sf.mStream.reset();
        sf.GV4(255);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, -1, 0, 0, 0}));
        sf.mStream.reset();
        sf.GV4(32535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, 23, 127, 0, 0}));
        sf.mStream.reset();
        sf.GV4(21556454);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-29, -26, -20, 72, 1}));
    }
    
    @Test
    public void testIntAddSize() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.addSize(2123345234);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {82, -83, -113, 126}));
        
        sf.mStream.reset();
        sf.addSize(-2123345234);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-82, 82, 112, -127}));
        
        sf.mStream.reset();
        sf.addSize(0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {0, 0, 0, 0}));
    }
    
    @Test
    public void testShrtAddSize() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.addSize((short)65535);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {-1, -1}));
        
        sf.mStream.reset();
        sf.addSize((short)-65535);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {1, 0}));
        
        sf.mStream.reset();
        sf.addSize((short)0);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {0, 0}));
    }
    
    @Test
    public void testAddBytes() throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.mStream.reset();
        sf.addBytes(new byte[] {1, 2, 3, 4, 5, 6, 7}, 0, 7);
        byte[] mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {1, 2, 3, 4, 5, 6, 7}));
        sf.mStream.reset();
        sf.addBytes(new byte[] {1, 2, 3, 4, 5, 6, 7}, 1, 5);
        mas = sf.mStream.toByteArray();
        assertTrue(Arrays.equals(mas, new byte[] {2, 3, 4, 5, 6}));
    }
}
