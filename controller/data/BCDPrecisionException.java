package data;


/** BCD value precision exception*/
public class BCDPrecisionException extends Exception 
{
	/**
	 * Create exception for value that couldn't be 
	 * represent as BCD with specified precision
	 * 
	 * @param value - user specified value
	 * @param precision - precision
	 */
	public BCDPrecisionException(Double value, Integer precision)
	{
		this.value     = value;
		this.precision = precision;	
		
		/* Get value as long */
		Long lvalue = value.longValue();
		
		/* Calculate value outside precision */
		Long outsidePrecision = lvalue % this.precision;
		
		/* Calculate value possible to code as BCD */
		this.possibleValue = lvalue - outsidePrecision;
	}
	
	/**
	 * Returns value that might be 
	 * @return No precise value
	 */
	public Double getValue()
	{
		return this.value;
	}
	
	public Long getPossibleValue()
	{
		return this.possibleValue;
	}
	
	/**
	 * Returns precision
	 * @return Precisions
	 */
	public Integer getPrecision()
	{
		return this.precision;
	}
	/**
	 * Serialized constant
	 */
	private static final long serialVersionUID = 8581396857698228669L;
	
	/**
	 * Frequency precision
	 */
	private Integer precision;
	
	/**
	 * User specified value 
	 */
	private Double value;
	
	/**
	 * Value possible to code as BCD
	 */
	private Long possibleValue;
}
