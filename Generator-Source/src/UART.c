#include "UART.h"

void delay_ms( uint32_t delay )
{
	while ( delay-- )
	{
		_delay_ms(1);
	}
}

/** Interruption handler for register 1 */
ISR(USART1_RX_vect, ISR_BLOCK)
{
	/* Receive value */
	received_value = USART_receivebyte();

	switch (received_value)
	{
	case SIG_START_BYTE:
		data_ready           = SIG_DATA_AVAILABLE;
		received_bytes       = 0;
		break;

	case SIG_FINISH_BYTE:
		data_ready           = SIG_DATA_VALID;
		break;

	}

	/* Only BCD code is taken into account */
	if ( ( received_value & 0x0F ) > 0x09 ||
	     ( ( received_value & 0xF0 ) >> 4 ) > 0x09 ||
	       data_ready != SIG_DATA_AVAILABLE )
		return;

	/* Frequency reception */
	if ( received_bytes >= DELAY_BYTES )
	{
		/* Store in a buffer */
		frequency_buffer[ ( received_bytes - DELAY_BYTES ) / FREQUENCY_BYTES ][ ( received_bytes - DELAY_BYTES ) % FREQUENCY_BYTES ] = received_value;
	}
	else /* Time reception */
	{
		reception_delay[ received_bytes % DELAY_BYTES ] = received_value;
	}

	/* Increment bytes number and frequencies number */
	received_bytes++;

	/* All possible frequencies were stored */
	if ( received_bytes >= FREQUENCY_NUMBER * FREQUENCY_BYTES + DELAY_BYTES )
	{
		data_ready = SIG_DATA_VALID;
	}
}

/* USART initialization */
void USART_init(void)
{
	received_bytes       = 0;
	received_value       = 0;
	data_ready           = 0;

	/* Set baud rate */
	UBRR1L = BAUD_PRESCALE;
	UBRR1H = (BAUD_PRESCALE >> 8);

	/* Load upper 8-bits into the high byte of the UBRR register
	   Default frame format is 8 data bits, no parity, 1 stop bit
	   to change use UCSRC, see AVR datasheet */

	UCSR1C  = ( 0 << USBS1  ) |
			  ( 1 << UCSZ10 ) |
			  ( 1 << UCSZ11 );   /* 8bit data format */

	/* Enable receiver and transmitter */
	UCSR1B  = ((1<<TXEN1) | (1<<RXEN1));

	/* Enable interrupts complete at register 1 */
	UCSR1B |= ( 1 << RXCIE1 );

	/* Enable global interrupts */
	sei();
}

/* Send buffer */
void USART_sendbuffer(unsigned char* usart_buffer, uint32_t usart_length)
{
	/* Index */
	uint32_t index = 0;

	/* Send each element from buffer */
	for ( index = 0; index < usart_length; index++ )
	{
		/* Send single byte */
		USART_sendbyte( usart_buffer[index] );
	}
}

/* Send byte */
void USART_sendbyte(unsigned char usart_byte)
{
	/* Wait until byte might be sent */
	while((UCSR1A & (1 << UDRE1)) == 0) {}

	/* Transmit data */
	UDR1 = usart_byte;
}

/* Receive byte */
unsigned char USART_receivebyte(void)
{
	return UDR1;
}
