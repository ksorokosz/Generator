/*
 * Main.h
 *
 *  Created on: 10-03-2015
 *      Author: kamil
 */

#ifndef MAIN_H_
#define MAIN_H_

#include <stdlib.h>
#include <stdio.h>

#include "UART.h"
#include "SPI.h"
#include "AD9833.h"

/* Masks which will be used to decode BCD data received from UART */
#define BCD_LSB_MASK 0x0F
#define BCD_MSB_MASK 0xF0

/* AD9833 word sequence IDs */
#define AD9833_CONTROL_WORD 0
#define AD9833_LSB_WORD 1
#define AD9833_MSB_WORD 2
#define AD9833_PHASE_WORD 3
#define AD9833_FINISH_WORD 4
#define AD9833_WORD_SEQUENCE 5

/* AD9833 ports */

/* AD9833 FSYNC port */
#define AD9833_FSYNC_PORT PORTB
#define AD9833_FSYNC_DDR  DDRB
#define AD9833_FSYNC_BIT  PB0

/* Set FSYNC port to high or low */
#define AD9833_FSYNC_HI() AD9833_FSYNC_PORT |=  (1 << AD9833_FSYNC_BIT)
#define AD9833_FSYNC_LO() AD9833_FSYNC_PORT &= ~(1 << AD9833_FSYNC_BIT)


/* Reset word sequence */
void main_reset_word_sequence( uint16_t* word_sequence, uint32_t length );

/* Get frequency from buffer - received from UART */
double main_get_frequency( unsigned char* frequency_buffer, uint32_t length );

/* Create AD9833 word sequence - square signal */
void main_create_word_sequence_ad9833( uint16_t* word_sequence, uint32_t length, double frequency );

/* Set single word to ad9833 via SPI, msborder - find SPI init */
void main_send_word_ad9833_spi( uint16_t word, uint8_t msborder );

/* Send word sequence to AD9833 - SPI */
void main_send_word_sequence_ad9833_spi( uint16_t* word_sequence, uint32_t length );

/* Send word sequence to UART - for testing purposes */
void main_send_word_sequence_ad9833_uart( uint16_t* word_sequence, uint32_t length );

/* AD9833 buffer */
uint16_t ad9833_words[ AD9833_WORD_SEQUENCE ] = { 0, 0, 0, 0, 0 };

#endif /* MAIN_H_ */
