#include "Main.h"
#include "UART.h"
#include "SPI.h"
#include "AD9833.h"

/* Initialize AD9833 as off */
void AD9833_init()
{
	AD9833_FSYNC_DDR |= (1 << AD9833_FSYNC_BIT);
	AD9833_FSYNC_HI();

	/* Delay after setting FSYNC */
	_delay_us(10);

	/* Reset word sequence */
	main_reset_word_sequence( ad9833_words, AD9833_WORD_SEQUENCE );

	/* Initialize word sequence */
	AD9833_init_square( &ad9833_words[AD9833_CONTROL_WORD], 0, 0 );

	/* Send LSB */
	AD9833_set_freq0( &ad9833_words[AD9833_LSB_WORD], DEFAULT_FREQUENCY, 0 );

	/* Send MSB */
	AD9833_set_freq0( &ad9833_words[AD9833_MSB_WORD], DEFAULT_FREQUENCY, 1 );

	/* Send phase - always 0 */
	AD9833_set_phase0( &ad9833_words[AD9833_PHASE_WORD], 0 );

	/* Finish - set reset to 0 */
	AD9833_finish( &ad9833_words[AD9833_FINISH_WORD], ad9833_words[AD9833_CONTROL_WORD] );
}

/* Reset word sequence */
void main_reset_word_sequence( uint16_t* word_sequence, uint32_t length )
{
	uint32_t counter = 0;
	for ( ; counter < length; counter++ )
		word_sequence[ counter ] = 0;
}

/* Get delay from buffer - received from UART */
uint32_t main_get_delay( unsigned char* delay_buffer, uint32_t length )
{
	uint32_t counter = 0;
	uint32_t value   = 0;

	/* For each element from buffer */
	for(; counter < length; counter++ )
	{
		/* Get component */
		uint32_t component_lsb = (delay_buffer[ length - ( counter + 1 )  ] & BCD_LSB_MASK) >> 0;
		uint32_t component_msb = (delay_buffer[ length - ( counter + 1 ) ] & BCD_MSB_MASK) >> 4;
		uint32_t base          = 1;
		uint32_t base_counter  = 0;

		/* Calculate BCD base */
		for ( ; base_counter < counter; base_counter++ )
			base *= 100;

		/* Value */
		value += component_lsb * ( base );
		value += component_msb * ( 10 * base );
	}

	return value;
}

/* Get frequency from buffer - received from UART */
double main_get_frequency( unsigned char* frequency_buffer, uint32_t length )
{
	uint32_t counter = 0;
	double value     = 0;

	/* For each element from buffer */
	for(; counter < length; counter++ )
	{
		/* Get component */
		double component_lsb  = (frequency_buffer[ length - ( counter + 1 )  ] & BCD_LSB_MASK) >> 0;
		double component_msb  = (frequency_buffer[ length - ( counter + 1 ) ] & BCD_MSB_MASK) >> 4;
		double base           = 1;
		uint32_t base_counter = 0;

		/* Calculate BCD base */
		for ( ; base_counter < counter; base_counter++ )
			base *= 100;

		/* Value */
		value += component_lsb * ( base );
		value += component_msb * ( 10 * base );
	}

	return value;
}

/* Create AD9833 word sequence - square signal */
void main_create_word_sequence_ad9833( uint16_t* word_sequence, uint32_t length, double frequency )
{
	/* Do nothing if length is smaller than 5 */
	if ( length < AD9833_WORD_SEQUENCE )
		return;

	/* Initialize word sequence */
	AD9833_init_square( &word_sequence[AD9833_CONTROL_WORD], 0, 0 );

	/* Send LSB */
	AD9833_set_freq0( &word_sequence[AD9833_LSB_WORD], frequency, 0 );

	/* Send MSB */
	AD9833_set_freq0( &word_sequence[AD9833_MSB_WORD], frequency, 1 );

	/* Send phase - always 0 */
	AD9833_set_phase0( &word_sequence[AD9833_PHASE_WORD], 0 );

	/* Finish - set reset to 0 */
	AD9833_finish( &word_sequence[AD9833_FINISH_WORD], word_sequence[AD9833_CONTROL_WORD]);
}

/* Set single word to ad9833 via SPI */
void main_send_word_ad9833_spi( uint16_t word, uint8_t msborder )
{
	switch(msborder)
	{
		default:
		case 1:
			/* Send MSB - if MSBORDER is used in SPI initialization */
			SPI_send((uint8_t)(word>>8));

			/* Send LSB*/
			SPI_send((uint8_t)word); break;

		case 0:
			/* Send LSB - if MSBORDER is NOT used in SPI initialization */
			SPI_send((uint8_t)word);

			/* Send MSB*/
			SPI_send((uint8_t)(word>>8)); break;
	}
}

/* Send word sequence to AD9833 - SPI */
void main_send_word_sequence_ad9833_spi( uint16_t* word_sequence, uint32_t length )
{
	uint32_t counter = 0;
	uint8_t msborder = 1;

	/* Set FSYNC to low - ready to send */
	AD9833_FSYNC_LO();
	_delay_us(5);

	/* Send word sequence */
	for ( ; counter < length; counter++ )
	{
		/* Send single word via SPI */
		main_send_word_ad9833_spi( word_sequence[ counter ], msborder );
	}

	_delay_us(5);

	/* Set FSYNC to high - bytes were send */
	AD9833_FSYNC_HI();
}

/* Send word sequence to UART - for testing purposes */
void main_send_word_sequence_ad9833_uart( uint16_t* word_sequence, uint32_t length )
{
	// For testing purposes
	// --------------------

	uint32_t counter = 0;
	unsigned char buffer[ AD9833_WORD_SEQUENCE * 2 ];

	/* Create buffer to send - USART */
	for (; counter < length; counter++ )
	{
		buffer[ counter * 2 ] = ( word_sequence[ counter ] & 0xFF00 ) >> 8;
		buffer[ counter * 2 + 1 ] = ( word_sequence[ counter ] & 0x00FF ) >> 0;
	}

	// --------------------
	USART_sendbuffer( buffer, AD9833_WORD_SEQUENCE * 2 );
}

/* Echo program - for testing purposes */
int echo(void)
{
	/* USART initialization */
	USART_init();

	/* SPI initialization */
	SPI_init();

	/* AD9833 initialization as off */
	AD9833_init();

	/* Send word sequence to AD9833 via SPI */
	main_send_word_sequence_ad9833_uart( ad9833_words, AD9833_WORD_SEQUENCE );

	/* Infinite loop */
	for(;;)
	{
		uint32_t counter = 0;
		uint32_t delay   = 0;

		/* If all bytes were received */
		if ( data_ready != SIG_DATA_VALID ) continue;

		USART_sendbyte(reception_delay[0]);
		USART_sendbyte(reception_delay[1]);

		delay = main_get_delay( reception_delay, DELAY_BYTES );

		/* For each frequency */
		for ( ; counter < ( received_bytes - DELAY_BYTES ) / FREQUENCY_BYTES; counter++ )
		{
			double frequency;

			/* Reset word sequence */
			main_reset_word_sequence( ad9833_words, AD9833_WORD_SEQUENCE );

			/* Get received frequency */
			frequency = main_get_frequency( frequency_buffer[counter], FREQUENCY_BYTES );

			/* Create word sequence for AD9833 */
			main_create_word_sequence_ad9833( ad9833_words, AD9833_WORD_SEQUENCE, frequency );

			/* Send word sequence to AD9833 */
			main_send_word_sequence_ad9833_uart( ad9833_words, AD9833_WORD_SEQUENCE );

			/* Delay - the same for each frequency */
			delay_ms( delay );
		}
	}

	return 0;
}

/* Application */
int app()
{
	USART_init();

	/* SPI initialization */
	SPI_init();

	/* AD9833 initialization as off */
	AD9833_init();

	/* Send word sequence to AD9833 via SPI */
	main_send_word_sequence_ad9833_spi( ad9833_words, AD9833_WORD_SEQUENCE );

	/* Infinite loop */
	for(;;)
	{
		uint32_t counter = 0;
		uint32_t delay   = 0;

		/* If all bytes were received */
		if ( data_ready != SIG_DATA_VALID ) continue;

		delay = main_get_delay( reception_delay, DELAY_BYTES );

		/* For each frequency */
		for ( ; counter < ( received_bytes - DELAY_BYTES ) / FREQUENCY_BYTES; counter++ )
		{
			double frequency;

			/* Reset word sequence */
			main_reset_word_sequence( ad9833_words, AD9833_WORD_SEQUENCE );

			/* Get received frequency */
			frequency = main_get_frequency( frequency_buffer[counter], FREQUENCY_BYTES );

			/* Create word sequence for AD9833 */
			main_create_word_sequence_ad9833( ad9833_words, AD9833_WORD_SEQUENCE, frequency );

			/* Send word sequence to AD9833 */
			main_send_word_sequence_ad9833_spi( ad9833_words, AD9833_WORD_SEQUENCE );

			/* Delay - the same for each frequency */
			delay_ms( delay );
		}
	}
	return 0;
}

int main(void)
{
	return app();
}

