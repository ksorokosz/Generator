package rs232;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import gnu.io.*;

/**
 * Represent serial ports - include only static methods
 * @author kamil
 * 
 */
public class COMPorts 
{	
	/**
	 * Returns available serial ports
	 * @return Available serial ports
	 */
	public static Object[] getAvaliableSerialPorts() 
	{
		HashSet<CommPortIdentifier> h = getAvailableSerialPorts();
		String aSerialPortsName[] = new String[h.size()];
		Iterator<CommPortIdentifier> iter = h.iterator();
		
		int index = 0;
		while(iter.hasNext())
		{
			String portName = iter.next().getName();
			aSerialPortsName[h.size()-1-index] = portName;
			index++;
		}
		return aSerialPortsName;
	}
	/**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    @SuppressWarnings({ "rawtypes" })
	private static HashSet<CommPortIdentifier> getAvailableSerialPorts() 
    {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) 
        {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) 
            {
            	case CommPortIdentifier.PORT_SERIAL:
                try 
                {
                    CommPort thePort = com.open("CommUtil", 50);
                    thePort.close();
                    h.add(com);
                } 
                catch (PortInUseException e) 
                {
                    System.out.println("Port, "  + com.getName() + ", is in use.");
                }
                catch (Exception e) 
                {
                    System.err.println("Failed to open port " +  com.getName());
                    e.printStackTrace();
                }
            }
        }
        return h;
    }

}
