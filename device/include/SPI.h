#ifndef SPI_H_
#define SPI_H_

//// PB2 - DATA  (Master Output Slave Input)
//// PB1 - CLOCK (SPI Bus Serial Clock)
//// PB0 - CHIP SELECT (Slave Select Input)

#include <avr/io.h>

/* SPI Pins */
#define SPI_DDR      DDRB
#define SPI_MOSI_BIT PB2
#define SPI_SCK_BIT  PB1
#define SPI_CS_BIT   PB0

/* SPI Mode */
#define SPI_MODE0   (0<<CPHA)
#define SPI_MODE1   (1<<CPHA)
#define SPI_MODE2   (2<<CPHA) // SPI mode which should be used CPOL = 1 CPHA = 0
#define SPI_MODE3   (3<<CPHA)

/* SPI order */
#define LSB_ORDER  (1<<DORD)
#define MSB_ORDER  (0<<DORD) // MSB first is used for default

/* SPI clock division */
#define CLOCKDIV4  (0<<SPR0)
#define CLOCKDIV16 (1<<SPR0)
#define CLOCKDIV64 (2<<SPR0)

/* Initialize SPI */
void SPI_init();

/* Send byte over SPI */
void SPI_send(uint8_t data);

/* Get byte from SPI */
uint8_t spi_read_byte(void);

#endif /* SPI_H_ */
