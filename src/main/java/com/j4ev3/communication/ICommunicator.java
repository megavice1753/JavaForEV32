package com.j4ev3.communication;

import java.io.IOException;

public interface ICommunicator {
	/**
	 * Open the connection between devices.
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException;
	
	/**
	 * Close the connection between devices.
	 */
	public void close();
	
	/**
	 * Write the byte array data to the device.
	 * 
	 * @param data
	 * @param timeout
	 *            Max waiting time for the end of this communication. [Unit: ms]
	 * @throws RuntimeException
         * @throws java.io.IOException
	 */
	public void write(byte[] data, int timeout) throws RuntimeException, IOException;
	
	/**
	 * Read the respose from the device.
	 * 
	 * @param length
	 *            The max length of response wanted to be read.
	 * @param timeout
	 *            Max waiting time for the end of this communication. [Unit: ms]
	 * @return
	 * @throws RuntimeException
         * @throws java.io.IOException
	 */
	public byte[] read(int length, int timeout) throws RuntimeException, IOException;
}
