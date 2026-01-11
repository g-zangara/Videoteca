package test.model;

import model.StatoVisione;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per l'enumerazione StatoVisione.
 * Verifica il corretto funzionamento dei metodi dell'enumerazione.
 */
public class StatoVisioneTest {

    @Test
    public void testGetDescrizione() {
        assertEquals("Da vedere", StatoVisione.DA_VEDERE.getDescrizione());
        assertEquals("In visione", StatoVisione.IN_VISIONE.getDescrizione());
        assertEquals("Visto", StatoVisione.VISTO.getDescrizione());
    }

    @Test
    public void testFromString() {
        assertEquals(StatoVisione.DA_VEDERE, StatoVisione.fromString("Da vedere"));
        assertEquals(StatoVisione.IN_VISIONE, StatoVisione.fromString("In visione"));
        assertEquals(StatoVisione.VISTO, StatoVisione.fromString("Visto"));

        // Test di sensibilità al maiuscolo/minuscolo
        assertEquals(StatoVisione.DA_VEDERE, StatoVisione.fromString("da vedere"));
        assertEquals(StatoVisione.IN_VISIONE, StatoVisione.fromString("IN VISIONE"));
        assertEquals(StatoVisione.VISTO, StatoVisione.fromString("vIsTO"));
    }

    @Test
    public void testFromStringInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            StatoVisione.fromString("Stato non valido");
        });

        String expectedMessage = "Stato visione non valido: Stato non valido";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}