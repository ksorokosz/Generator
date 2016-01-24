package rs232;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import data.Data;
import data.BCDPrecisionException;

/**
 * Represent FTDI module
 *
 */
public class FTDI implements UART 
{
	/**
	 * Default constructor
	 */
	public FTDI() 
	{
	}
	
	/** 
	 * Connect to Serial Port (e.g FTDI module)
	 */
	public boolean connect(CommPortIdentifier port, int speed) throws PortInUseException 
	{
		this.speed = speed;
		serialPort = (SerialPort) port.open("FTDI", 2000);
		try 
		{
			outputStream = serialPort.getOutputStream();
			inputStream = serialPort.getInputStream();
			
			serialPort.setSerialPortParams(this.speed, 
				       SerialPort.DATABITS_8, 
				       SerialPort.STOPBITS_1, 
				       SerialPort.PARITY_NONE);
			serialPort.notifyOnOutputEmpty(true);
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (UnsupportedCommOperationException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Send whole buffer - this will be the task which will be run periodically
	 * @throws InterruptedException 
	 */
	public void sendBuffer( Vector<data.Data> dataToSend, Long duration ) throws IOException, BCDPrecisionException, InterruptedException
	{
		Iterator<data.Data> iDataToSend = dataToSend.iterator();

		/* Duration as BCD */
		Long durationBCD = Long.parseLong(duration.toString(),16);
		
		/* Send start byte */
		outputStream.write(0xFF);
	
		/* Send duration */
		outputStream.write(Data.intToByteArray(durationBCD));
		
		/* For each element */
		while (iDataToSend.hasNext())
		{
			/* Get current data */
			data.Data current = iDataToSend.next();
		
			/* Send single data */
			send(current);
			
			/* Wait as it was specified by the user */
			 Thread.sleep(5);
		}
		
		/* Send finish byte */
		outputStream.write(0xAA);
	}
	
	/**
	 * Send data to Serial Port (e.g FTDI module)
	 */
	public void send(Data aData) throws IOException, BCDPrecisionException
	{
		try 
		{
			/* Get frequency as BCD */
			Long frequencyBCD = aData.getDataAsBCD();
			
			/* Log */
			System.out.println("Sending: " + frequencyBCD + ". It is 0x" + Long.toHexString(frequencyBCD) + " in BCD.");
			
			/* Send frequency */
			outputStream.write(Data.intToByteArray(frequencyBCD));
		} 
		catch (BCDPrecisionException e) 
		{
			System.err.println("Impossible to send: " + e.getValue() + " using current precision. Try to send: " + e.getPossibleValue());
			
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * Receive data from Serial Port (e.g FTDI module)
	 */
	public String receive() throws IOException
	{
		byte[] receive = new byte[1];
		
		if(inputStream.available() <= 0)
			return new String("No data available");
		else
		{
			inputStream.read(receive);
			
			String temp = Integer.toString(Data.byteArraytoInt(receive),16);
			return temp;
		}
	}

	/** 
	 * Close connection
	 */
	public void close() 
	{
		if(serialPort!=null)
			serialPort.close();
	}
	
	/**
	 * Sets serial port speed parameter
	 * @param speed - serial port speed
	 */
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	/**
	 * Transmission speed
	 */
	private int speed = 9600;
	
	/**
	 * Stream which is use to write to serial port
	 */
	private OutputStream outputStream;
	
	/**
	 * Stream which is use to read data from serial port
	 */
	private InputStream inputStream;
	
	/**
	 * Serial port where data is written
	 */
	private SerialPort serialPort;
}
