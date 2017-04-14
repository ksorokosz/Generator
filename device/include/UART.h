
#ifndef UART_H_
#define UART_H_

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

/* Data ready signal */
#define SIG_DATA_VALID 0x01

/* Data available */
#define SIG_DATA_AVAILABLE 0x02

/* Special bytes */

/* Start */
#define SIG_START_BYTE 0xFF

/* Finish */
#define SIG_FINISH_BYTE 0xAA

/* Number of bytes per frequency */
#define FREQUENCY_BYTES 4

/* Number of bytes per delay */
#define DELAY_BYTES 4

/* Number of frequencies in a sequence */
#define FREQUENCY_NUMBER 850

/* Define baud rate */
#define USART_BAUDRATE 9600

/* Baud prescale */
#define BAUD_PRESCALE (((F_CPU / (USART_BAUDRATE * 16UL))) - 1)

/* Default signal duration */
#define DEFAULT_DURATION_MS 180*1000UL

/* Default frequency */
#define DEFAULT_FREQUENCY 10000

/* Variable delay */
void delay_ms( uint32_t delay );

/**
 * USART Tx/Rx
 */

/* Value in register */
volatile unsigned char received_value;

/* Data ready signal */
volatile uint8_t data_ready;

/* Number of bytes received */
volatile uint32_t received_bytes;

/* Buffer for received data */
unsigned char frequency_buffer[FREQUENCY_NUMBER][ FREQUENCY_BYTES ];

/* Delay time */
unsigned char reception_delay[ DELAY_BYTES ];

/* USART initialization */
void USART_init(void);

/* Send buffer */
void USART_sendbuffer(unsigned char* usart_buffer, uint32_t usart_length);

/* Send byte */
void USART_sendbyte(unsigned char usart_byte);

/* Receive byte */
unsigned char USART_receivebyte(void);

#endif /* UART_H_ */
