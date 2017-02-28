package com.j4ev3.app;

import com.j4ev3.communication.ICommunicator;
import com.j4ev3.protocol.DirectCommand;
import com.j4ev3.protocol.DirectFormatter;
import static com.j4ev3.protocol.BasicByteCodeFormatter.*;
import java.io.IOException;
        
public class PortFactory {
    public static Port checkPort(int prt, Brick br) throws IOException {
        ICommunicator com = br.getCommunicator();
        if (com == null) {
            throw new IOException("Physical data transmission environment was not initialized");
        }
        if (prt < 0 || prt > 31) {
            throw new IOException("Unrecognized port value: " + prt + " must be between 0 and 31");
        }
        byte layer = (byte)((prt & 0x0C) >> 2);
        Port port = null;
        DirectFormatter df = new DirectFormatter(DirectCommand.Command.INPUT_DEVICE, DirectCommand.REPLY);
        df.setGlobal(2);//2 global variables
        df.setLocal(0);
        df.addByteCode((byte)0x05);//GET_TYPEMODE
        df.LC0(layer);//layer
        df.LC0(prt);//port
        df.GV0(0);//offset of 1 returned param type
        df.GV0(1);//offset of 2 returned param mode
        byte [] gettype1 = df.toByteArray();
        com.write(gettype1, 0);
        byte[] mas = com.read(0,0);
        if (mas.length < 7) {
            throw new IOException("Unrecognized response length");
        } else {
            switch (mas[5]) {
                case EV3_LARGE_MOTOR: {
                    port = new EV3_L_Motor(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case EV3_MEDIUM_MOTOR: {
                    port = new EV3_M_Motor(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case EV3_TOUCH: {
                    port = new EV3_Touch(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case EV3_COLOR: {
                    port = new EV3_Color(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case EV3_IR: {
                    port = new EV3_IR(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case EV3_GYRO: {
                    port = new EV3_Gyro(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                case ERROR_PORT: {
                    port = new Error(mas[5], mas[6], (byte)prt, (byte)layer, br);
                    break;
                }
                default: {
                    port = new None(mas[5], mas[6], (byte)prt, (byte)layer, br);
                }
            }
        }
        return port;
    }
}
