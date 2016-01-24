package data;

import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Repeat sequence task. It will be run periodically. 
 */
public class RepeatableSequence extends TimerTask 
{
	/**
	 * Constructor
	 *  
	 * @param aFTDI - FTDI instance
	 */
	public RepeatableSequence(rs232.FTDI aFTDI)
	{
		this.aFTDI    = aFTDI;
		this.sequence = new Vector<data.Data>();
		this.period   = 0L;
	}
	
	public void setDuration(Long duration)
	{
		this.period = duration;
	}
	
	/**
	 * Add data to send
	 * @param data - data that will be send
	 */
	public void addToSend(data.Data data)
	{
		/* Update sequence */
		sequence.add(data);
	}
	
	/**
	 * Clear send buffer
	 */
	public void clearSendBuffer()
	{
		this.period = 0L;
		sequence.clear();
	}
	
	/**
	 * Returns task period
	 * @return task period
	 */
	public Long getTaskPeriodMs()
	{
		return this.period;
	}
	
	@Override
    public void run() 
    {
        try 
        {
        	/* Send whole buffer */
			aFTDI.sendBuffer(sequence, this.period);
		} 
        catch (IOException e) 
		{
			e.printStackTrace();
		} 
        catch (BCDPrecisionException e) 
        {
			e.printStackTrace();
		} 
        catch (InterruptedException e) 
        {
			e.printStackTrace();
		}
    }
	
	/**
	 * FTDI instance
	 */
	private rs232.FTDI aFTDI;
	
	/**
	 * Period in ms
	 */
	private Long period;
	
	/**
	 * Data to send as sequence
	 */
	Vector<data.Data> sequence;
}
