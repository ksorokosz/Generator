#include "AD9833.h"


/* Init Ad9833 as off: D15 D14 - 0 0,
 *                     D7 - 1, (Internal clock disabled)
 *                     D6 - 1 (DAC off)
 *
 */
void AD9833_init_off( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg )
{
	(*AD9833_word) |= ( 0 << D15 );
	(*AD9833_word) |= ( 0 << D14 );
	(*AD9833_word) |= ( 1 << D13 );
	(*AD9833_word) |= ( 0 << D12 );
	(*AD9833_word) |= ( (freqreg & BIT_MASK) << D11 );
	(*AD9833_word) |= ( (phasereg & BIT_MASK) << D10 );
	(*AD9833_word) |= ( 0 << D09 );
	(*AD9833_word) |= ( 0 << D08 );
	(*AD9833_word) |= ( 1 << D07 );
	(*AD9833_word) |= ( 1 << D06 );
	(*AD9833_word) |= ( 0 << D05 );
	(*AD9833_word) |= ( 0 << D04 );
	(*AD9833_word) |= ( 0 << D03 );
	(*AD9833_word) |= ( 0 << D02 );
	(*AD9833_word) |= ( 0 << D01 );
	(*AD9833_word) |= ( 0 << D00 );
}

/* Sinusoidal: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg (0)
 *             D10 - phasereg (0)
 *             rest - 0
 */
void AD9833_init_sinusoidal( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg )
{
	(*AD9833_word) |= ( 0 << D15 );
	(*AD9833_word) |= ( 0 << D14 );
	(*AD9833_word) |= ( 1 << D13 );
	(*AD9833_word) |= ( 0 << D12 );
	(*AD9833_word) |= ( (freqreg & BIT_MASK) << D11 );
	(*AD9833_word) |= ( (phasereg & BIT_MASK) << D10 );
	(*AD9833_word) |= ( 0 << D09 );
	(*AD9833_word) |= ( 1 << D08 );
	(*AD9833_word) |= ( 0 << D07 );
	(*AD9833_word) |= ( 0 << D06 );
	(*AD9833_word) |= ( 0 << D05 );
	(*AD9833_word) |= ( 0 << D04 );
	(*AD9833_word) |= ( 0 << D03 );
	(*AD9833_word) |= ( 0 << D02 );
	(*AD9833_word) |= ( 0 << D01 );
	(*AD9833_word) |= ( 0 << D00 );
}

/* Triangle: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg
 *             D10 - phasereg
 *             D1 - 1
 *             rest - 0
 */
void AD9833_init_triangle( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg )
{
	(*AD9833_word) |= ( 0 << D15 );
	(*AD9833_word) |= ( 0 << D14 );
	(*AD9833_word) |= ( 1 << D13 );
	(*AD9833_word) |= ( 0 << D12 );
	(*AD9833_word) |= ( (freqreg & BIT_MASK) << D11 );
	(*AD9833_word) |= ( (phasereg & BIT_MASK) << D10 );
	(*AD9833_word) |= ( 0 << D09 );
	(*AD9833_word) |= ( 1 << D08 );
	(*AD9833_word) |= ( 0 << D07 );
	(*AD9833_word) |= ( 0 << D06 );
	(*AD9833_word) |= ( 0 << D05 );
	(*AD9833_word) |= ( 0 << D04 );
	(*AD9833_word) |= ( 0 << D03 );
	(*AD9833_word) |= ( 0 << D02 );
	(*AD9833_word) |= ( 1 << D01 );
	(*AD9833_word) |= ( 0 << D00 );
}

/* Square: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg
 *             D10 - phasereg
 *             D6 - 1 (DAC off)
 *             D5 - 1 (MSB or MSB/2 to DAC)
 *             D3 - 1 (MSB to DAC)
 *             rest - 0
 */
void AD9833_init_square( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg )
{
	(*AD9833_word) |= ( 0 << D15 );
	(*AD9833_word) |= ( 0 << D14 );
	(*AD9833_word) |= ( 1 << D13 );
	(*AD9833_word) |= ( 0 << D12 );
	(*AD9833_word) |= ( (freqreg & BIT_MASK) << D11 );
	(*AD9833_word) |= ( (phasereg & BIT_MASK) << D10 );
	(*AD9833_word) |= ( 0 << D09 );
	(*AD9833_word) |= ( 1 << D08 );
	(*AD9833_word) |= ( 0 << D07 );
	(*AD9833_word) |= ( 1 << D06 );
	(*AD9833_word) |= ( 1 << D05 );
	(*AD9833_word) |= ( 0 << D04 );
	(*AD9833_word) |= ( 1 << D03 );
	(*AD9833_word) |= ( 0 << D02 );
	(*AD9833_word) |= ( 0 << D01 );
	(*AD9833_word) |= ( 0 << D00 );
}

/* Load into FREQ0 D15 D14 - 0 1, rest - frequency (msb or lsb) f_MCLK / 2^28 × FREQREG */
void AD9833_set_freq0( uint16_t* AD9833_word, double frequency, uint8_t msb )
{
	/* Calculate frequency value */
	uint32_t frequency_register = ceil( frequency * FREQUENCY_COEFFICIENT );

	/* Set control bits */
	(*AD9833_word) |= ( 0 << D15 );
	(*AD9833_word) |= ( 1 << D14 );

	/* Get MSB or LSB */
	switch ( msb & BIT_MASK )
	{
	case 0: /* LSB */

		(*AD9833_word) |= ( frequency_register & FREQUENCY_REGISTER_LSB ); break;

	case 1: /* MSB */

		(*AD9833_word) |= ( ( frequency_register & FREQUENCY_REGISTER_MSB ) >> 14 ); break;
	}
}

/* Load into FREQ1 D15 D14 - 1 0, rest - frequency (msb or lsb) f_MCLK / 2^28 × FREQREG */
void AD9833_set_freq1( uint16_t* AD9833_word, double frequency, uint8_t msb  )
{
	/* Calculate frequency value */
	uint32_t frequency_register = round( frequency * FREQUENCY_COEFFICIENT );

	/* Set control bits */
	(*AD9833_word) |= ( 1 << D15 );
	(*AD9833_word) |= ( 0 << D14 );

	/* Get MSB or LSB */
	switch ( msb & BIT_MASK )
	{
	case 0: /* LSB */

		(*AD9833_word) |= ( frequency_register & FREQUENCY_REGISTER_LSB ); break;

	case 1: /* MSB */

		(*AD9833_word) |= ( ( frequency_register & FREQUENCY_REGISTER_MSB ) >> 14 ); break;
	}
}

/* Load into PHASE0 D15 D14 D13 D12 - 1 1 0 X, rest - phase */
void AD9833_set_phase0( uint16_t* AD9833_word, double phase )
{
	/* Calculate phase value */
	uint32_t phase_register = round( phase * PHASE_COEFFICIENT );

	/* Set control bits */
	(*AD9833_word) |= ( 1 << D15 );
	(*AD9833_word) |= ( 1 << D14 );
	(*AD9833_word) |= ( 0 << D13 );
	(*AD9833_word) |= ( 0 << D12 );

	/* Set phase */
	(*AD9833_word) |= ( phase_register & PHASE_REGISTER );
}

/* Load into PHASE1 D15 D14 D13 D12 - 1 1 1 X, rest - phase */
void AD9833_set_phase1( uint16_t* AD9833_word, double phase )
{
	/* Calculate phase value */
	uint32_t phase_register = round( phase * PHASE_COEFFICIENT );

	/* Set control bits */
	(*AD9833_word) |= ( 1 << D15 );
	(*AD9833_word) |= ( 1 << D14 );
	(*AD9833_word) |= ( 1 << D13 );
	(*AD9833_word) |= ( 0 << D12 );

	/* Set phase */
	(*AD9833_word) |= ( phase_register & PHASE_REGISTER );
}

/* Load finish word: D15 D14 - 0 0, D13 - 1, rest - 0 (especially reset) */
void AD9833_finish( uint16_t* AD9833_word, uint16_t control_word )
{
	(*AD9833_word)  = control_word;
	(*AD9833_word) &= ~( 1 << D08 );
}
