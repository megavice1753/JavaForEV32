package com.j4ev3.app.fileSystem;

import com.j4ev3.communication.ICommunicator;
import com.j4ev3.protocol.SystemCommand;
import com.j4ev3.protocol.SystemFormatter;
import com.j4ev3.app.Brick;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileSystem {
    private final Brick brick;
    private final ICommunicator connector;
    public FileSystem(Brick br) {
        brick = br;
        connector = br.getCommunicator();
    }
    
    /**
     * <p>See lego ev3 firmware developer kit, Folder structure within firmware, page 102;</p>
     * <p>See lego ev3 communication developer kit, Downloading data to the EV3 programmable brick, page 7;</p>
     * @param inputFileName name of local file e.g. "d:/1.jpg"
     * @param outputFileName brick full filename e.g. "../prjs/SD_Card/test/1.jpg"
     * @param timeout between each iteration of transmission, msec
     * @param part portion of bytes in each iteration of transmission. default 10000, must be between 1 and SystemCommand.MAX_FILE_CHUNK
     * @param sh sh is handler which counts progress in transmission, may be null
     * @throws IOException
     */
    public void sendFile(String inputFileName, String outputFileName, int timeout, int part, ProcessHandler sh) throws IOException {
        java.io.File f = new java.io.File(inputFileName);
        try (FileInputStream fos = new FileInputStream(f)) {
            int fileSize = fos.available();
            byte[] data = new byte[fileSize];
            fos.read(data);
            sendFile(data, outputFileName, timeout, part, sh);
        }
    }
    
    /**
     * <p>See lego ev3 firmware developer kit, Folder structure within firmware, page 102;</p>
     * <p>See lego ev3 communication developer kit, Downloading data to the EV3 programmable brick, page 7;</p>
     * @param data byte[] body of local file 
     * @param outputFileName brick full filename e.g. "../prjs/SD_Card/test/1.jpg"
     * @param timeout between each iteration of transmission, msec
     * @param part portion of bytes in each iteration of transmission. default 10000, must be between 1 and SystemCommand.MAX_FILE_CHUNK
     * @param sh sh is handler which counts progress in transmission, may be null
     * @throws IOException
     */
    public void sendFile(byte[] data, String outputFileName, int timeout, int part, ProcessHandler sh) throws IOException {
        int fileSize = data.length;
        if (sh != null) {
            sh.setTotal(fileSize);
        }
        int chunk = 10000;//SystemCommand.MAX_FILE_CHUNK
        if (part > 0 && part <= SystemCommand.MAX_FILE_CHUNK) {
            chunk = part;
        }
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_DOWNLOAD, SystemCommand.REPLY);
        sf.addSize(fileSize);
        sf.addString(outputFileName);
        byte[] bytecode = sf.toByteArray();
        connector.write(bytecode, timeout);
        bytecode = connector.read(0, 0);
        if (bytecode[6] != SystemCommand.SUCCESS) {
            throw new IOException("Could not begin file save: " + bytecode[6]);
        }
        byte handler = bytecode[7];
        int sizeSent = 0;
        while (sizeSent < data.length) {
            sf = new SystemFormatter(SystemCommand.Command.CONTINUE_DOWNLOAD, SystemCommand.REPLY);
            sf.addByteCode(handler);
            int sizeToSend = Math.min(chunk, data.length - sizeSent);
            sf.addBytes(data, sizeSent, sizeToSend);
            bytecode = sf.toByteArray();
            connector.write(bytecode, timeout);
            bytecode = connector.read(0, 0);
            handler = bytecode[7];
            sizeSent += sizeToSend;
            if (sh != null) {
                sh.setCurrent(sizeSent);
            }
            if (bytecode[6] != SystemCommand.SUCCESS && bytecode[6] != SystemCommand.END_OF_FILE) {
                throw new IOException("Error saving file: " + bytecode[6]);
            }
        }
    }
    
    /**
     *
     * @param fileName ev3 full filename
     * @param timeout timeout in msec
     * @param part strongly recommended set about 1000 bytes
     * @return byte[] byte of file
     * @throws IOException
     */
    public  byte[] getFile(String fileName, int timeout, int part) throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.BEGIN_GETFILE, SystemCommand.REPLY);
        short chunk = 1000;//SystemCommand.MAX_FILE_CHUNK
        if (part > 0 && part <= SystemCommand.MAX_FILE_CHUNK) {
            chunk = (short)part;
        }
        sf.addSize(chunk);
        sf.addString(fileName);
        byte[] bytecode = sf.toByteArray();
        connector.write(bytecode, timeout);
        bytecode = connector.read(0, 0);
        if (bytecode[6] != SystemCommand.SUCCESS && bytecode[6] != SystemCommand.END_OF_FILE) {
            throw new IOException("Could not begin upload file: " + bytecode[6]);
        }
        int totalSize = ((bytecode[10] & 0xFF) << 24) + ((bytecode[9] & 0xFF) << 16) + ((bytecode[8] & 0xFF) << 8) + (bytecode[7] & 0xFF);
        byte handler = bytecode[11];
        byte[] bfile = new byte[totalSize];
        int sizeRead = bytecode.length - 12;
        chunk = (short)sizeRead;
        System.arraycopy(bytecode, 12, bfile, 0, sizeRead);
        while (sizeRead < totalSize) {
            sf = new SystemFormatter(SystemCommand.Command.CONTINUE_GETFILE, SystemCommand.REPLY);
            sf.addByteCode(handler);
            int newRead = Math.min(chunk, totalSize - sizeRead);
            sf.addSize(newRead);
            bytecode = sf.toByteArray();
            connector.write(bytecode, timeout);
            bytecode = connector.read(0, 0);
            if (bytecode[6] != SystemCommand.SUCCESS && bytecode[6] != SystemCommand.END_OF_FILE) {
                throw new IOException("Error CONTINUE_UPLOAD: " + bytecode[6]);
            }
            handler = bytecode[11];
            newRead = bytecode.length - 12;
            System.arraycopy(bytecode, 12, bfile, sizeRead, newRead);
            sizeRead += newRead;
        }
        return bfile;
    }
    
    /**
     *
     * @param path
     * @param timeout
     * @return  
        The new line delimited list is formatted as:

        If it is a file:
        32 chars (hex) of MD5SUM + space + 8 chars (hex) of filesize + space + filename + new line

        If it is a folder:
        foldername + / + new line
     * @throws IOException
     */
    public String readFolder(String path, int timeout) throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.LIST_FILES, SystemCommand.REPLY);
        short chunk = 100;
        sf.addSize(chunk);
        sf.addString(path);
        byte[] data = sf.toByteArray();
        connector.write(data, timeout);
        data = connector.read(0, 0);
        if (data[6] != SystemCommand.SUCCESS && data[6] != SystemCommand.END_OF_FILE) {
            throw new IOException("Could not begin fileList read: " + data[6]);
        }
        int totalSize = ((data[10] & 0xFF) << 24) + ((data[9] & 0xFF) << 16) + ((data[8] & 0xFF) << 8) + (data[7] & 0xFF);
        byte handler = data[11];
        byte[] bstring = new byte[totalSize];
        int sizeRead = Math.min(chunk, totalSize);
        System.arraycopy(data, 12, bstring, 0, sizeRead);
        while (sizeRead < totalSize) {
            sf = new SystemFormatter(SystemCommand.Command.CONTINUE_LIST_FILES, SystemCommand.REPLY);
            sf.addByteCode(handler);
            int newRead = Math.min(chunk, totalSize - sizeRead);
            sf.addSize(newRead);
            data = sf.toByteArray();
            connector.write(data, timeout);
            data = connector.read(0, 0);
            if (data[6] != SystemCommand.SUCCESS && data[6] != SystemCommand.END_OF_FILE) {
                throw new IOException("Error reading fileList: " + data[6]);
            }
            handler = data[7];
            System.arraycopy(data, 8, bstring, sizeRead, newRead);
            sizeRead += newRead;
        }
        String str = new String(bstring, "UTF-8");
        return str;
    }
    
    public ArrayList<Node> getFolderNodes(String path, int timeout) throws IOException {
        String struct = readFolder(path, timeout);
        String[] mas = struct.split("\n");
        ArrayList<Node> res = new ArrayList(); 
        for (String str : mas) {
            if (str.equals("../")) {
                res.add(new Node("../"));
            } else if (str.equals("./")) {
            } else if (str.contains("/")) {
                res.add(new Folder(str));
            } else {
                String[] params = str.split(" ");
                String name = params[2];
                int size = Integer.parseInt(params[1], 16);
                res.add(new com.j4ev3.app.fileSystem.File(name, size));
            }
        }
        return res;
    }
    
    public boolean createDir(String name) throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.CREATE_DIR, SystemCommand.REPLY);
        sf.addString(name);
        byte[] req = sf.toByteArray();
        connector.write(req, 0);
        byte[] resp = connector.read(0, 0);
        return resp[6] == SystemCommand.SUCCESS;
    }
    
    public boolean deleteFile(String name) throws IOException {
        SystemFormatter sf = new SystemFormatter(SystemCommand.Command.DELETE_FILE, SystemCommand.REPLY);
        sf.addString(name);
        byte[] req = sf.toByteArray();
        connector.write(req, 0);
        byte[] resp = connector.read(0, 0);
        return resp[6] == SystemCommand.SUCCESS;
    }
}
