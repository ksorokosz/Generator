package data;

import java.util.HashMap;


/**
 * Represent data sending by serial port (FTDI module)
 */
public class Data 
{
	/**
	 * Data constructor
	 * @param frequencyValue frequency value
	 * @param frequencyUnit frequency unit [Hz, kHz, MHz, GHz]
	 * @param durationValue duration value
	 * @param durationUnit duration unit [us, ms, s]
	 */
	public Data(Double frequencyValue, String frequencyUnit,
				Double durationValue, String durationUnit) 
	{
		this.frequencyUnit   = frequencyUnit;
		this.durationUnit    = durationUnit;
		this.frequencyValue  = Math.abs(frequencyValue);
		this.durationValue   = Math.abs(durationValue);
		
		this.frequencyUnitsMap = new HashMap<String, Long>();
		this.durationUnitsMap  = new HashMap<String, Long>();
		this.precisionMap      = new HashMap<Integer, Long>();
		
		this.frequencyUnitsMap.put("Hz",  1L);
		this.frequencyUnitsMap.put("kHz", 1000L);
		this.frequencyUnitsMap.put("MHz", 1000000L);
		this.frequencyUnitsMap.put("GHz", 1000000000L);

		this.durationUnitsMap.put("ms", 10L);
		this.durationUnitsMap.put("s" , 1000L);
		this.durationUnitsMap.put("min" , 60000L);
		
		this.precisionMap.put(4, 1L);     // 4 bytes (1 Hz   - 99 MHz)
		this.precisionMap.put(3, 100L);   // 3 bytes (100 Hz - 99 MHz)
		this.precisionMap.put(2, 10000L); // 2 bytes (10 kHz - 99 MHz)
	}
	
	/**
	 * Returns data ready to send to FTDI module in BCD
	 * @return Data ready to send to FTDI module in BCD
	 */
	public Long getDataAsBCD() throws BCDPrecisionException
	{
		/* Get frequency */
		Double dfrequency = this.getFrequencyInHz();
		
		/* Get frequency as Long */
		Long lfrequency = dfrequency.longValue();
		
		/* Precision */
		Long iprecision = this.precisionMap.get(BYTES_SEND);
		
		/* Calculate value outside precision */
		Long outsidePrecision = lfrequency % iprecision;
		
		/* Calculate value to code as BCD */
		Long lvalue = lfrequency - outsidePrecision;
		
		/* Code value as BCD */
		Long frequencyBCD = Long.parseLong(lvalue.toString(),16);	
		
		/* Throw precise exception */
		if( dfrequency - lfrequency != 0 || outsidePrecision != 0)
			throw new BCDPrecisionException( dfrequency, BYTES_SEND );
		
		/* Return BCD frequency */
		return frequencyBCD;
	}
	
	/**
	 * Returns frequency value
	 * @return frequency value
	 */
	public Double getFrequencyValue()
	{
		return frequencyValue;
	}
	
	/**
	 * Returns frequency unit
	 * @return frequency unit
	 */
	public String getFrequencyUnit()
	{
		return frequencyUnit;
	}
	
	/**
	 * Get frequency 
	 */
	public Double getFrequencyInHz()
	{
		return frequencyValue * frequencyUnitsMap.get(frequencyUnit);
	}
	
	/**
	 * Return duration value
	 * @return duration value
	 */
	public Double getDurationValue()
	{
		return durationValue;
	}
	
	/**
	 * Returns duration unit
	 * @return duration unit
	 */
	public String getDurationUnit()
	{
		return durationUnit;
	}

	/**
	 * Returns duration in milliseconds
	 * @return duration in milliseconds
	 */
	public Long getDurationInMs()
	{
		Double dValue = durationValue * durationUnitsMap.get(durationUnit);
		
		/* Return value */
		return dValue.longValue();
	}
	
	/**
	 * To string
	 */
	public String toString()
	{
		return new String(frequencyValue + " " + frequencyUnit + "\t" + durationValue + " " + durationUnit);
	}
	
	/**
	 * Integer to byte array
	 * @param value
	 * @return integer to byte
	 */
	public static byte[] intToByteArray(long value) 
	{
        byte[] b = new byte[BYTES_SEND];
        for (int i = 0; i < b.length; i++) 
        {
            int offset = (b.length - 1 - i) * 8 + (4 - BYTES_SEND) * 8; /* +16 and BYTES_SEND = 2 range from 10 kHz to 99MHz */
        										  				        /*  +8 and BYTES_SEND = 3 range from 100Hz to 99MHz */
            													        /*  +0 and BYTES_SEND = 4 range from 1Hz to 99MHz   */
            b[i] = (byte) ((value >> offset) & 0xFF);
        }
        return b;
    }
	
	/**
	 * Byte array to integer
	 * 
	 * @param b - byte array
	 * @return Integer - integer
	 */
	public static Integer byteArraytoInt(byte[] b) 
	{
		int value = 0;
		for (int i = 0; i < b.length; i++) 
        {
            value += Math.pow(256, b.length - i - 1) * (b[i]);
        }
        return value;
    }

	/**
	 * Frequency value
	 */
	private Double frequencyValue;
	/**
	 * Frequency unit
	 */
	private String frequencyUnit;
	/**
	 * Duration value
	 */
	private Double durationValue;
	/**
	 * Duration unit
	 */
	private String durationUnit;
	
	/**
	 * Number of bytes send to FTDI module
	 */
	public static int BYTES_SEND = 4;
	
	/**
	 * Hash map for frequency units
	 */
	HashMap<String, Long> frequencyUnitsMap;
	
	/**
	 * Hash map for duration units
	 */
	HashMap<String, Long> durationUnitsMap;
	
	/**
	 * Hash map for precision
	 */
	HashMap<Integer, Long> precisionMap;
}
