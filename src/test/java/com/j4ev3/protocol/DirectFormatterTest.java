package com.j4ev3.protocol;

import com.j4ev3.app.OutputPort;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

//https://netbeans.org/kb/docs/java/junit-intro_ru.html
//http://devcolibri.com/864
public class DirectFormatterTest {

    /**
     * Test of setGlobal method, of class DirectFormatter.
     * @throws java.io.IOException
     */
    @Test
    public void testSetGlobal() throws IOException {
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.setGlobal(0);
        df.setGlobal(1023);
    }
    
    /**
     * Test of setGlobal method, of class DirectFormatter.
     * @throws java.io.IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetGlobalException() throws IOException {
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.setGlobal(-1);
        df.setGlobal(1024);
        df.setGlobal((int) 2222222222l);
    }

    /**
     * Test of setLocal method, of class DirectFormatter.
     * @throws java.io.IOException
     */
    @Test
    public void testSetLocal() throws IOException {
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.setLocal(0);
        df.setLocal(63);
    }
    
    /**
     * Test of setLocal method, of class DirectFormatter.
     * @throws java.io.IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLocalException() throws IOException {
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.OUTPUT_POWER, DirectCommand.NO_REPLY);
        df.setLocal(-1);
        df.setLocal(64);
        df.setLocal((int) 2222222222l);
    }

    /**
     * Test of toByteArray method, of class DirectFormatter.
     * @throws java.io.IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        assertTrue(Arrays.equals(
                OutputPort.move(0, 1, 100), 
                new byte[] {13, 0, 0, 0, -128, 0, 0, -92, 0, 1, -127, 100, -90, 0, 1})
        );
        assertTrue(Arrays.equals(
                OutputPort.stepMovement(3, 15, 100, 1000), 
                new byte[] {18, 0, 0, 0, -128, 0, 0, -84, 3, 15, -127, 100, 0, -125, -24, 3, 0, 0, 0, 1})
        );
        assertTrue(Arrays.equals(
                OutputPort.stop(2, 13),
                new byte[] {9, 0, 0, 0, -128, 0, 0, -92, 2, 13, 0})
        );
        //byte[] mas = OutputPort.stepMovement(3, 15, 100, 1000);
        //String str = Arrays.toString(mas);
    }
    
}
