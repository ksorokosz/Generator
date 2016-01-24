package rs232;

import java.io.IOException;
import java.util.Vector;

import data.Data;
import data.BCDPrecisionException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

/**
 * UART protocol interface
 * @author kamil
 *
 */
public interface UART 
{
	/**
	 * Connect to FTDI module
	 * @param port Port to connect
	 * @param speed Transmission speed
	 * @return connection status
	 * @throws PortInUseException 
	 *
	 */
	public boolean connect(CommPortIdentifier port, int speed) throws PortInUseException;
	/**
	 * Send buffer to FTDI module
	 * 
	 * @throws IOException
	 * @throws BCDPrecisionException
	 */
	public void sendBuffer( Vector<data.Data> dataToSend, Long duration ) throws IOException, BCDPrecisionException, InterruptedException;
	/**
	 * Send data to FTDI module
	 * @throws IOException 
	 * @param aData Data sending to FTDI module 
	 * @throws BCDPrecisionException
	 */
	public void send(Data aData) throws IOException, BCDPrecisionException ;
	/**
	 * Returns receive data from FTDI module - one byte in BCD
	 * @return Data (one byte in BCD) from FTDI module
	 * @throws IOException 
	 */
	public String receive() throws IOException;
	/**
	 * Close serial port
	 */
	public void close();
}
