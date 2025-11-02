package test.command;

import static org.junit.jupiter.api.Assertions.*;

import model.Film;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import command.AggiungiFilmCommand;
import controller.FilmController;
import model.StatoVisione;

/**
 * Test unitari per la classe AggiungiFilmCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class AggiungiFilmCommandTest {

    private TestFilmController controller;
    private AggiungiFilmCommand command;

    /**
     * Classe interna per simulare il controller dei films.
     * Mantiene traccia delle operazioni effettuate per i test.
     */
    private static class TestFilmController extends FilmController {
        private Film filmAggiunto = null;
        private Film filmEliminato = null;

        public TestFilmController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean aggiungiFilmInterno(Film film) {
            this.filmAggiunto = film;
            return true;
        }

        @Override
        public boolean eliminaFilmInterno(Film film) {
            this.filmEliminato = film;
            return true;
        }

        public boolean isFilmAggiunto() {
            return filmAggiunto != null;
        }

        public boolean isFilmEliminato() {
            return filmEliminato != null;
        }

        public Film getFilmAggiunto() {
            return filmAggiunto;
        }

        public Film getFilmEliminato() {
            return filmEliminato;
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new TestFilmController();
        command = new AggiungiFilmCommand(
                controller,
                "Nuovo Film",
                "Nuovo Regista",
                "1234",
                "Nuovo Genere",
                4,
                StatoVisione.DA_VEDERE
        );
    }

    @Test
    public void testExecute() {
        // Verifica lo stato iniziale
        assertFalse(controller.isFilmAggiunto());

        // Esegui il comando
        command.execute();

        // Verifica che il film sia stato aggiunto
        assertTrue(controller.isFilmAggiunto());

        // Verifica che i dati del film aggiunto siano corretti
        Film filmAggiunto = controller.getFilmAggiunto();
        assertNotNull(filmAggiunto);
        assertEquals("Nuovo Film", filmAggiunto.getTitolo());
        assertEquals("Nuovo Regista", filmAggiunto.getRegista());
        assertEquals("1234", filmAggiunto.getAnnoUscita());
        assertEquals("Nuovo Genere", filmAggiunto.getGenere());
        assertEquals(4, filmAggiunto.getValutazione());
        assertEquals(StatoVisione.DA_VEDERE, filmAggiunto.getStatoVisione());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che il film sia stato aggiunto
        assertTrue(controller.isFilmAggiunto());
        Film filmAggiunto = controller.getFilmAggiunto();

        // Annulla il comando
        command.undo();

        // Verifica che il film sia stato eliminato
        assertTrue(controller.isFilmEliminato());

        // Verifica che il film eliminato sia lo stesso che era stato aggiunto
        assertSame(filmAggiunto, controller.getFilmEliminato());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione contenga il titolo e il regista del film
        String description = command.getDescription();

        assertTrue(description.contains("Nuovo Film"));
        assertTrue(description.contains("Nuovo Regista"));
        assertTrue(description.contains("Aggiunta film"));
    }
}