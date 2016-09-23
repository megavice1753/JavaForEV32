package com.j4ev3.app;

import com.j4ev3.app.fileSystem.FileSystem;
import com.j4ev3.communication.ICommunicator;
import static com.j4ev3.protocol.BasicByteCodeFormatter.*;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Brick {
    private final ICommunicator connector;
    private final TreeMap<Byte, Port> ports = new TreeMap();
    private byte layer = 0;
    private Panel panel;
    private FileSystem fs;
    public Brick (ICommunicator c) throws IOException {
        if (c == null) {
            throw new IOException("Physical data transmission environment was not initialized");
        }
        connector = c;
        fs = new FileSystem(this);
        panel = new Panel();
        panelColor(Panel.OFF);
    }
    
    public Brick (ICommunicator c, int _layer) throws IOException {
        this(c);
        if (_layer < 0 || _layer > 3) {
            throw new IOException("Unrecognized layer value: " + _layer + " must between 0 and 3");
        }
        layer = (byte)_layer;
    }
    
    public ICommunicator getCommunicator() {
        return connector;
    }
    
    public FileSystem getFileSystem() {
        return fs;
    }
    
    public final void panelColor(int color) throws IOException {
        panel.setColor((byte)color);
        byte[] req = panel.apply();
        connector.write(req, 0);
    }
 
    public void checkAllPorts() throws IOException {
        checkPortN(PORT1);
        checkPortN(PORT2);
        checkPortN(PORT3);
        checkPortN(PORT4);
        checkPortN(PORTA);
        checkPortN(PORTB);
        checkPortN(PORTC);
        checkPortN(PORTD);
    }
    
    public Port checkPortN(int port) throws IOException {
        if (port < PORT1 || (port > PORT4 && port < PORTA) || port > PORTD) {
            throw new IOException("Unrecognized port value: " + port + " must be between PORT1 and PORT4 or between PORTA and PORTD");
        }
        Port p = PortFactory.checkPort(port + layer * 4, this);
        ports.put((byte)port, p);
        return p;
    }
    
    public byte[] getOutputPorts() {
        byte[] mas = new byte[ports.size()];
        int i = 0;
        for (Map.Entry<Byte, Port> entr : ports.entrySet()) {
            Port p = entr.getValue();
            if (p instanceof OutputPort) {
                mas[i++] = p.getPort();
            }
        }
        byte[] result = new byte[i];
        for (int j = 0; j < i; ++j) {
            result[j] = mas[j];
        }
        return result;
    }
    
    public WorkingPort getWorkingPort(int port) throws IOException {
        Port prt = getPort(port);
        if (prt instanceof WorkingPort) {
            return (WorkingPort)prt;
        } else {
            throw new IOException(Port.getPortAsString((byte)port) + " is currently " + prt.getName());
        }
    }
    
    public Port getPort(int port) throws IOException {
        if (port < PORT1 || (port > PORT4 && port < PORTA) || port > PORTD) {
            throw new IOException("Unrecognized port value: " + port + " must be between PORT1 and PORT4 or between PORTA and PORTD");
        }
        Port prt = ports.get((byte)port);
        if (prt == null) {
            throw new IOException(Port.getPortAsString((byte)port) + " was not initialized");
        }
        return prt;
    }
    
    private byte convolution (byte[] ports) throws IOException {
        byte all = 0;
        for (byte port : ports) {
            if (port < PORTA || port > PORTD) {
                throw new IOException("Unrecognized port value: " + port + " must be between PORTA and PORTD");
            }
            Port prt = this.ports.get(port);
            if (prt == null) {
                throw new IOException(Port.getPortAsString((byte)port) + " was not initialized");
            }
            if (prt instanceof OutputPort) {
                if (port == PORTA) {
                    all |= 0x01;
                } else if (port == PORTB) {
                    all |= 0x02;
                } else if (port == PORTC) {
                    all |= 0x04;
                } else if (port == PORTD) {
                    all |= 0x08;
                }
            } else {
                throw new IOException(Port.getPortAsString((byte)port) + " is currently " + prt.getName());
            }
        }
        return all;
    }
    
    public void movePortN(int port, int power) throws IOException {
        _movePorts(OutputPort.convertPort((byte)port), power);
    }
    
    private void _movePorts(int ports, int power) throws IOException {
        byte[] req = OutputPort.move(layer, ports, power);
        connector.write(req, 0);
    }
    
    public void moveAllPorts(int power) throws IOException {
        _movePorts(0x0F, power);
    }
    
    public void movePorts(byte[] ports, int power) throws IOException {
        byte all = convolution(ports);
        _movePorts(all, power);
    }
    
    public void stopPortN(int port) throws IOException {
        _stopPorts(OutputPort.convertPort((byte)port));
    }
    
    private void _stopPorts(int ports) throws IOException {
        byte[] req = OutputPort.stop(layer, ports);
        connector.write(req, 0);
    }
    
    public void stopAllPorts() throws IOException {
        _stopPorts(0x0F);
    }
    
    public void stopPorts(byte[] ports) throws IOException {
        byte all = convolution(ports);
        _stopPorts(all);
    }
    
    public void polarityN(int port, int polarity) throws IOException {
        _polarityPorts(OutputPort.convertPort((byte)port), polarity);
    }
    
    private void _polarityPorts(int ports, int polarity) throws IOException {
        byte[] req = OutputPort.setPolarity(layer, ports, polarity);
        connector.write(req, 0);
    }
    
    public void polarityAllPorts(int polarity) throws IOException {
        _polarityPorts(0x0F, polarity);  
    }
    
    public void polarityPorts(byte[] ports, int polarity) throws IOException {
        byte all = convolution(ports);
        _polarityPorts(all, polarity);
    }
    
    public void clearCountN(int port) throws IOException {
        _clearCountPorts(OutputPort.convertPort((byte)port));
    }
    
    private void _clearCountPorts(int ports) throws IOException {
        byte[] req = OutputPort.clearCount(layer, ports);
        connector.write(req, 0);
    }
    
    public void clearCountAllPorts() throws IOException {
        _clearCountPorts(0x0F);
    }
    
    public void clearCountPorts(byte[] ports) throws IOException {
        byte all = convolution(ports);
        _clearCountPorts(all);
    }
    
    private void _stepMovement(int ports, int speed, int angle) throws IOException {
        byte[] req = OutputPort.stepMovement(layer, ports, speed, angle);
        connector.write(req, 0);
    }
    
    public void stepMovementN(int port, int speed, int angle) throws IOException {
        _stepMovement(OutputPort.convertPort((byte)port), speed, angle);
    }
    
    public void stepMovementAll(int port, int speed, int angle) throws IOException {
        _stepMovement(0x0F, speed, angle);
    }
    
    public void stepMovementPorts(byte[] ports, int speed, int angle) throws IOException {
        byte all = convolution(ports);
        _stepMovement(all, speed, angle);
    }
 
    
    
}
