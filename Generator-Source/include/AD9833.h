
#ifndef AD9833_H_
#define AD9833_H_

#include <stdint.h>
#include <math.h>

/* Pi */
#define PI 3.14159

/* AD9833 clock */
#define AD9833_CLK 25000000.0

/* AD9833 frequency coefficient */
#define FREQUENCY_COEFFICIENT ( (1UL << 28) / AD9833_CLK )

/* AD9833 phase coefficient */
#define PHASE_COEFFICIENT ( 4096 / ( 2 * PI ) ) 

/* Baud prescale */
#define BAUD_PRESCALE (((F_CPU / (USART_BAUDRATE * 16UL))) - 1)

/* Frequency register - 14 bits, MSB */
#define FREQUENCY_REGISTER_MSB 0x0FFFC000

/* Frequency register - 14 bits, LSB */
#define FREQUENCY_REGISTER_LSB 0x00003FFF

/* Phase register - 12 bits */
#define PHASE_REGISTER 0x0FFF

/* Reserved bits - value: 0 */
#define RESERVED_BITS 0x0215

/* Bit mask */
#define BIT_MASK 0x01

/* Define 2 byte word position */
#define D15 15
#define D14 14
#define D13 13
#define D12 12
#define D11 11
#define D10 10
#define D09 9
#define D08 8
#define D07 7
#define D06 6
#define D05 5
#define D04 4
#define D03 3
#define D02 2
#define D01 1
#define D00 0

/* Init Ad9833 as off: D15 D14 - 0 0,
 *                     D7 - 1, (Internal clock disabled)
 *                     D6 - 1 (DAC off)
 *
 */
void AD9833_init_off( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg );

/* Sinusoidal: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg
 *             D10 - phasereg
 *             rest - 0
 */
void AD9833_init_sinusoidal( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg );

/* Triangle: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg
 *             D10 - phasereg
 *             D1 - 1
 *             rest - 0
 */
void AD9833_init_triangle( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg );

/* Square: D15 D14 - 0 0,
 *             D13 - 1 (always MSB LSB),
 *             D8 - 1 (reset),
 *             D11 - freqreg
 *             D10 - phasereg
 *             D6 - 1 (DAC off)
 *             D5 - 1 (MSB to DAC)
 *             rest - 0
 */
void AD9833_init_square( uint16_t* AD9833_word, uint8_t freqreg, uint8_t phasereg );

/* Load into FREQ0 D15 D14 - 0 1, rest - frequency (msb or lsb) */
void AD9833_set_freq0( uint16_t* AD9833_word, double frequency, uint8_t msb );

/* Load into FREQ1 D15 D14 - 1 0, rest - frequency (msb or lsb) */
void AD9833_set_freq1( uint16_t* AD9833_word, double frequency, uint8_t msb  );

/* Load into PHASE0 D15 D14 D13 D12 - 1 1 0 X, rest - phase */
void AD9833_set_phase0( uint16_t* AD9833_word, double phase );

/* Load into PHASE1 D15 D14 D13 D12 - 1 1 1 X, rest - phase */
void AD9833_set_phase1( uint16_t* AD9833_word, double phase );

/* Load finish word: D15 D14 - 0 0, D13 - 1, rest - 0 (especially reset) */
void AD9833_finish( uint16_t* AD9833_word, uint16_t control_word );

#endif /* AD9833_H_ */
