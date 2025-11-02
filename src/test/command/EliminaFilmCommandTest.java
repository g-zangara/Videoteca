package test.command;

import static org.junit.jupiter.api.Assertions.*;

import command.EliminaFilmCommand;
import model.Film;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import controller.FilmController;
import model.StatoVisione;

/**
 * Test unitari per la classe EliminaFilmCommand.
 * Verifica il corretto funzionamento delle operazioni di execute e undo.
 */
public class EliminaFilmCommandTest {

    private TestFilmController controller;
    private Film film;
    private EliminaFilmCommand command;

    /**
     * Classe interna per simulare il controller dei films.
     * Mantiene traccia delle operazioni effettuate per i test.
     */
    private static class TestFilmController extends FilmController {
        private Film filmEliminato = null;
        private boolean filmAggiunto = false;

        public TestFilmController() {
            super(null); // Il parametro view non è usato nei test
        }

        @Override
        public boolean eliminaFilmInterno(Film film) {
            this.filmEliminato = film;
            this.filmAggiunto = false;
            return true;
        }

        @Override
        public boolean aggiungiFilmInterno(Film film) {
            this.filmAggiunto = true;
            return true;
        }

        public boolean isFilmEliminato() {
            return filmEliminato != null;
        }

        public boolean isFilmAggiunto() {
            return filmAggiunto;
        }

        public Film getFilmEliminato() {
            return filmEliminato;
        }
    }

    @BeforeEach
    public void setUp() {
        controller = new TestFilmController();
        film = new Film("Test Film", "Test Regista", "1234", "Test Genere", 4, StatoVisione.VISTO);
        command = new EliminaFilmCommand(controller, film);
    }

    @Test
    public void testExecute() {
        // Verifica lo stato iniziale
        assertFalse(controller.isFilmEliminato());

        // Esegui il comando
        command.execute();

        // Verifica che il film sia stato eliminato
        assertTrue(controller.isFilmEliminato());
        assertSame(film, controller.getFilmEliminato());
    }

    @Test
    public void testUndo() {
        // Esegui il comando
        command.execute();

        // Verifica che il film sia stato eliminato
        assertTrue(controller.isFilmEliminato());

        // Annulla il comando
        command.undo();

        // Verifica che il film sia stato aggiunto nuovamente
        assertTrue(controller.isFilmAggiunto());
    }

    @Test
    public void testGetDescription() {
        // Verifica che la descrizione contenga il titolo e il regista del film
        String description = command.getDescription();

        assertTrue(description.contains("Test Film"));
        assertTrue(description.contains("Test Regista"));
        assertTrue(description.contains("Eliminazione film"));
    }
}