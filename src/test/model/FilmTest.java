package test.model;

import model.Film;
import model.StatoVisione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe Film.
 * Verifica il corretto funzionamento dei metodi di Film.
 */
public class FilmTest {

    private Film film;

    @BeforeEach
    public void setUp() {
        film = new Film("The Grand Budapest Hotel", "Wes Anderson", "2014", "commedia", 5, StatoVisione.VISTO);
    }

    @Test
    public void testCostruttore() {
        assertEquals("The Grand Budapest Hotel", film.getTitolo());
        assertEquals("Wes Anderson", film.getRegista());
        assertEquals("2014", film.getAnnoUscita());
        assertEquals("commedia", film.getGenere());
        assertEquals(5, film.getValutazione());
        assertEquals(StatoVisione.VISTO, film.getStatoVisione());
    }

    @Test
    public void testCostruttoreVuoto() {
        Film filmVuoto = new Film();
        assertEquals("", filmVuoto.getTitolo());
        assertEquals("", filmVuoto.getRegista());
        assertEquals("", filmVuoto.getAnnoUscita());
        assertEquals("", filmVuoto.getGenere());
        assertEquals(0, filmVuoto.getValutazione());
        assertEquals(StatoVisione.DA_VEDERE, filmVuoto.getStatoVisione());
    }

    @Test
    public void testSetters() {
        film.setTitolo("Nuovo Titolo");
        film.setRegista("Nuovo Regista");
        film.setAnnoUscita("1233");
        film.setGenere("Nuovo Genere");
        film.setValutazione(3);
        film.setStatoVisione(StatoVisione.IN_VISIONE);

        assertEquals("Nuovo Titolo", film.getTitolo());
        assertEquals("Nuovo Regista", film.getRegista());
        assertEquals("1233", film.getAnnoUscita());
        assertEquals("Nuovo Genere", film.getGenere());
        assertEquals(3, film.getValutazione());
        assertEquals(StatoVisione.IN_VISIONE, film.getStatoVisione());
    }

    @Test
    public void testGetValutazioneAsString() {
        assertEquals("5", film.getValutazioneAsString());

        film.setValutazione(0);
        assertEquals("Da valutare", film.getValutazioneAsString());
    }

    @Test
    public void testGetStatoVisioneAsString() {
        assertEquals("Visto", film.getStatoVisioneAsString());

        film.setStatoVisione(StatoVisione.IN_VISIONE);
        assertEquals("In visione", film.getStatoVisioneAsString());

        film.setStatoVisione(StatoVisione.DA_VEDERE);
        assertEquals("Da vedere", film.getStatoVisioneAsString());
    }

    @Test
    public void testCostruttoreConCampiNonValidi() {
        assertThrows(IllegalArgumentException.class, () ->
                new Film("", "Regista", "123-456", "Genere", 3, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", null, "123", "Genere", 3, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", "Regista", "ABC", "Genere", 3, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", "Regista", "1234", "", 3, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", "Regista", "1234", "Genere", -1, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", "", "1234", "Genere", 6, StatoVisione.VISTO)
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Film("Titolo", "Regista", "1234", "Genere", 3, null)
        );
    }

    @Test
    public void testSettersConValoriValidi() {
        assertDoesNotThrow(() -> film.setTitolo("Nuovo titolo"));
        assertDoesNotThrow(() -> film.setRegista("Nuovo regista"));
        assertDoesNotThrow(() -> film.setAnnoUscita("9783"));
        assertDoesNotThrow(() -> film.setGenere("Commedia"));
        assertDoesNotThrow(() -> film.setValutazione(5));
        assertDoesNotThrow(() -> film.setValutazione(0)); // accettabile come "da valutare"
        assertDoesNotThrow(() -> film.setStatoVisione(StatoVisione.IN_VISIONE));
    }

    @Test
    public void testEquals() {
        Film filmCopy = new Film("The Grand Budapest Hotel", "Wes Anderson", "2014", "commedia", 5, StatoVisione.VISTO);
        Film differentFilm = new Film("Different Title", "Different Director", "2020", "drama", 4, StatoVisione.DA_VEDERE);

        assertEquals(film, filmCopy);
        assertNotEquals(film, differentFilm);
        assertNotEquals(film, null);
        assertNotEquals(film, "Some String");
    }

    @Test
    public void testToString() {
        String expected = "Film{titolo='The Grand Budapest Hotel', regista='Wes Anderson', annoUscita='2014', genere='commedia', valutazione=5, statoVisione=VISTO}";
        assertEquals(expected, film.toString());
    }

}
