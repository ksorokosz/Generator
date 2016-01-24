#include "SPI.h"

/* Initialize SPI */
void SPI_init()
{
	/* Initialize SPI Line*/
    SPI_DDR |= (1<<SPI_MOSI_BIT) | (1<<SPI_SCK_BIT) | (1<<SPI_CS_BIT);

    /* Configuration */
    /* CLOCKDIV4 - 2 MHz it means that 1 cycle it is 0.5 us */
    SPCR     = (1<<SPE) | (1<<MSTR) | SPI_MODE2 | MSB_ORDER | CLOCKDIV4;
}

/* Send byte over SPI using polling */
void SPI_send(uint8_t data)
{
    SPDR = data; // Set data register

    while(!(SPSR & (1<<SPIF))); // Wait ready to send
}

/* Get byte from SPI */
uint8_t SPI_read_byte(void)
{
    SPDR = 0x00;

    while(!(SPSR & (1<<SPIF))); // Wait for data
    return SPDR;                // Return data
}


