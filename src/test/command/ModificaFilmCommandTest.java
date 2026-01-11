package test.command;

import command.ModificaFilmCommand;
import controller.FilmController;
import model.Film;
import model.StatoVisione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe ModificaFilmCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class ModificaFilmCommandTest {

    private TestFilmController controller;
    private Film filmOriginale;
    private ModificaFilmCommand command;

    /**
     * Classe controller personalizzata per i test.
     * Tiene traccia dei films modificati per verificare il comportamento del comando.
     */
    private static class TestFilmController extends FilmController {
        private Film vecchioFilm;
        private Film nuovoFilm;
        private boolean modificaEseguita = false;

        public TestFilmController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean modificaFilmInterno(Film vecchioFilm, Film nuovoFilm) {
            this.vecchioFilm = vecchioFilm;
            this.nuovoFilm = nuovoFilm;
            this.modificaEseguita = true;
            return true;
        }

        public Film getVecchioFilm() {
            return vecchioFilm;
        }

        public Film getNuovoFilm() {
            return nuovoFilm;
        }

        public boolean isModificaEseguita() {
            return modificaEseguita;
        }

        // Reset dello stato per i test
        public void reset() {
            this.vecchioFilm = null;
            this.nuovoFilm = null;
            this.modificaEseguita = false;
        }
    }

    @BeforeEach
    public void setUp() {
        // Crea un controller per il test
        controller = new TestFilmController();

        // Crea un film originale di test
        filmOriginale = new Film(
                "Titolo Originale",
                "Regista Originale",
                "1234",
                "Genere Originale",
                3,
                StatoVisione.VISTO
        );

        // Crea il comando di modifica
        command = new ModificaFilmCommand(
                controller,
                filmOriginale,
                "Titolo Modificato",
                "Regista Modificato",
                "1234",
                "Genere Modificato",
                5,
                StatoVisione.IN_VISIONE
        );
    }

    @Test
    public void testExecute() {
        // Esegui il comando
        command.execute();

        // Verifica che la modifica sia stata eseguita
        assertTrue(controller.isModificaEseguita());

        // Verifica i parametri passati al controller
        assertEquals(filmOriginale, controller.getVecchioFilm());

        Film filmModificato = controller.getNuovoFilm();
        assertNotNull(filmModificato);
        assertEquals("Titolo Modificato", filmModificato.getTitolo());
        assertEquals("Regista Modificato", filmModificato.getRegista());
        assertEquals("1234", filmModificato.getAnnoUscita());
        assertEquals("Genere Modificato", filmModificato.getGenere());
        assertEquals(5, filmModificato.getValutazione());
        assertEquals(StatoVisione.IN_VISIONE, filmModificato.getStatoVisione());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che la modifica sia stata eseguita
        assertTrue(controller.isModificaEseguita());

        // Reset del controller per verificare l'undo
        controller.reset();

        // Annulla il comando
        command.undo();

        // Verifica che sia stata chiamata la modifica inversa
        assertTrue(controller.isModificaEseguita());

        // Verifica che il film originale e il film modificato siano stati scambiati
        // Durante l'undo, il film modificato diventa vecchio e l'originale diventa nuovo
        Film filmOriginaleRipristinato = controller.getNuovoFilm();
        assertNotNull(filmOriginaleRipristinato);
        assertEquals("Titolo Originale", filmOriginaleRipristinato.getTitolo());
        assertEquals("Regista Originale", filmOriginaleRipristinato.getRegista());
        assertEquals("1234", filmOriginaleRipristinato.getAnnoUscita());
        assertEquals("Genere Originale", filmOriginaleRipristinato.getGenere());
        assertEquals(3, filmOriginaleRipristinato.getValutazione());
        assertEquals(StatoVisione.VISTO, filmOriginaleRipristinato.getStatoVisione());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione del comando contenga i titoli dei films
        String description = command.getDescription();

        assertTrue(description.contains("Titolo Originale"));
        assertTrue(description.contains("Titolo Modificato"));
        assertTrue(description.contains("Modifica film"));
    }
}