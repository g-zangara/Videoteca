package test.controller;

import controller.GestoreVideoteca;
import model.Film;
import model.StatoVisione;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per la classe GestoreVideoteca.
 * Verifica il corretto funzionamento del Singleton e dei metodi di gestione dei films.
 */
public class GestoreVideotecaTest {

    private GestoreVideoteca gestore;
    private Film film1, film2, film3;

    @BeforeEach
    public void setUp() {
        gestore = GestoreVideoteca.getInstance();
        gestore.pulisciVideoteca();

        // Crea alcuni film di test
        film1 = new Film("Il Padrino", "Francis Ford Coppola", "1972", "dramma", 5, StatoVisione.VISTO);
        film2 = new Film("Interstellar", "Christopher Nolan", "2014", "fantascienza", 3, StatoVisione.DA_VEDERE);
        film3 = new Film("Nuovo Cinema Paradiso", "Giuseppe Tornatore", "1988", "dramma", 1, StatoVisione.VISTO);

        // Aggiungi i films al gestore
        gestore.aggiungiFilm(film1);
        gestore.aggiungiFilm(film2);
        gestore.aggiungiFilm(film3);
    }

    @AfterEach
    public void tearDown() {
        // Pulisce il gestore dopo ogni test
        gestore.pulisciVideoteca();
    }

    @Test
    public void testGetInstance() {
        // Verifica che l'istanza ottenuta sia un singleton
        GestoreVideoteca gestore1 = GestoreVideoteca.getInstance();
        GestoreVideoteca gestore2 = GestoreVideoteca.getInstance();

        assertSame(gestore1, gestore2);
        assertSame(gestore, gestore1);
    }

    @Test
    public void testAggiungiFilm() {
        // Svuota il gestore
        gestore.pulisciVideoteca();
        assertTrue(gestore.getFilms().isEmpty());

        // Aggiungi un film e verifica che sia stato aggiunto
        assertTrue(gestore.aggiungiFilm(film1));
        assertEquals(1, gestore.getFilms().size());
        assertTrue(gestore.getFilms().contains(film1));

        // Prova ad aggiungere lo stesso film e verifica che non sia stato aggiunto
        assertFalse(gestore.aggiungiFilm(film1));
        assertEquals(1, gestore.getFilms().size());

        // Prova ad aggiungere null e verifica che non sia stato aggiunto
        assertFalse(gestore.aggiungiFilm(null));
        assertEquals(1, gestore.getFilms().size());
    }

    @Test
    public void testModificaFilm() {
        Film filmModificato = new Film(film1.getTitolo(), film1.getRegista(), film1.getAnnoUscita(), "Genere Modificato", 3, StatoVisione.IN_VISIONE);

        // Modifica il film e verifica che sia stato modificato
        assertTrue(gestore.modificaFilm(film1, filmModificato));

        // Verifica che i dati siano stati aggiornati
        List<Film> films = gestore.getFilms();
        Film filmAggiornato = films.stream()
                .filter(film -> film.getAnnoUscita().equals(film1.getAnnoUscita()) &&
                        film.getTitolo().equals(film1.getTitolo()) &&
                        film.getRegista().equals(film1.getRegista()))
                .findFirst()
                .orElse(null);

        assertNotNull(filmAggiornato, "Il film modificato dovrebbe essere presente nella videoteca");
        assertEquals("Genere Modificato", filmAggiornato.getGenere());
        assertEquals(3, filmAggiornato.getValutazione());
        assertEquals(StatoVisione.IN_VISIONE, filmAggiornato.getStatoVisione());
    }

    @Test
    public void testModificaFilmInesistente() {
        // Crea un nuovo gestore e pulisci la videoteca
        GestoreVideoteca gestoreTest = GestoreVideoteca.getInstance();
        gestoreTest.pulisciVideoteca();

        // Aggiungo un film per assicurarmi che la videoteca non sia vuota
        Film filmEsistente = new Film("Film Test", "Regista Test", "9876", "Genere Test", 3, StatoVisione.VISTO);
        gestoreTest.aggiungiFilm(filmEsistente);

        // Verifica che la videoteca contenga esattamente un film
        assertEquals(1, gestoreTest.getFilms().size(), "La videoteca dovrebbe contenere esattamente un film");

        // Creo un film che sicuramente non esiste nella videoteca
        Film filmInesistente = new Film("Non Esiste", "Regista", "1234", "Genere", 1, StatoVisione.DA_VEDERE);
        Film nuovoFilm = new Film("Nuovo", "Regista Nuovo", "1234", "Genere Nuovo", 2, StatoVisione.VISTO);

        // Verifica che il film non esista prima del tentativo di modifica
        List<Film> filmsPrima = gestoreTest.getFilms();
        assertFalse(filmsPrima.contains(filmInesistente), "Il film inesistente non dovrebbe essere nella videoteca");

        // Prova a modificare il film inesistente
        boolean result = gestoreTest.modificaFilm(filmInesistente, nuovoFilm);
        assertFalse(result, "La modifica di un film inesistente dovrebbe fallire");

        // Verifica che la videoteca contenga ancora solo il film originale
        List<Film> filmsDopo = gestoreTest.getFilms();
        assertEquals(1, filmsDopo.size(), "La videoteca dovrebbe contenere ancora solo un film");
        assertTrue(filmsDopo.contains(filmEsistente), "La videoteca dovrebbe contenere ancora il film originale");
        assertFalse(filmsDopo.contains(nuovoFilm), "Il nuovo film non dovrebbe essere stato aggiunto");
    }

    @Test
    public void testEliminaFilm() {
        // Verifica il numero iniziale di films
        assertEquals(3, gestore.getFilms().size());

        // Elimina un film e verifica che sia stato eliminato
        assertTrue(gestore.eliminaFilm(film1));
        assertEquals(2, gestore.getFilms().size());
        assertFalse(gestore.getFilms().contains(film1));

        // Prova a eliminare un film già eliminato
        assertFalse(gestore.eliminaFilm(film1));
        assertEquals(2, gestore.getFilms().size());
    }

    @Test
    public void testGetFilms() {
        // Verifica che getFilms restituisca tutti i films aggiunti
        List<Film> films = gestore.getFilms();
        assertEquals(3, films.size());
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
        assertTrue(films.contains(film3));

        // Verifica che la lista restituita sia una copia (modificare la lista non deve modificare il gestore)
        films.remove(0);
        assertEquals(2, films.size());
        assertEquals(3, gestore.getFilms().size()); // Il gestore ha ancora 3 films
    }

    @Test
    public void testCercaPerTitolo() {
        // Cerca per titolo esatto
        List<Film> risultato = gestore.cercaPerTitolo("Il Padrino");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca per titolo parziale
        risultato = gestore.cercaPerTitolo("Padrino");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca per titolo case-insensitive
        risultato = gestore.cercaPerTitolo("padrino");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca titolo inesistente
        risultato = gestore.cercaPerTitolo("Titolo Inesistente");
        assertTrue(risultato.isEmpty());

        // Cerca con stringa vuota (deve restituire tutti i films)
        risultato = gestore.cercaPerTitolo("");
        assertEquals(3, risultato.size());

        // Cerca con null (deve restituire tutti i films)
        risultato = gestore.cercaPerTitolo(null);
        assertEquals(3, risultato.size());
    }

    @Test
    public void testCercaPerRegista() {
        // Cerca per regista esatto
        List<Film> risultato = gestore.cercaPerRegista("Francis Ford Coppola");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca per regista parziale
        risultato = gestore.cercaPerRegista("Francis");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca per regista case-insensitive
        risultato = gestore.cercaPerRegista("ford coppola");
        assertEquals(1, risultato.size());
        assertTrue(risultato.contains(film1));

        // Cerca regista inesistente
        risultato = gestore.cercaPerRegista("regista Inesistente");
        assertTrue(risultato.isEmpty());

        // Cerca con stringa vuota (deve restituire tutti i films)
        risultato = gestore.cercaPerRegista("");
        assertEquals(3, risultato.size());

        // Cerca con null (deve restituire tutti i films)
        risultato = gestore.cercaPerRegista(null);
        assertEquals(3, risultato.size());
    }

    @Test
    public void testOrdinaFilms() {
        List<Film> films = new ArrayList<>();
        // Aggiungo i films in un ordine specifico per il test
        films.add(film2); // Interstellar
        films.add(film3); // Nuovo Cinema Paradiso
        films.add(film1); // Il Padrino

        // Test ordinamento per titolo A-Z
        List<Film> ordinati = gestore.ordinaFilms(films, new OrdinaTitoloAZStrategy());
        // Verifica che i titoli siano in ordine alfabetico
        assertTrue(ordinati.get(0).getTitolo().compareTo(ordinati.get(1).getTitolo()) <= 0);
        assertTrue(ordinati.get(1).getTitolo().compareTo(ordinati.get(2).getTitolo()) <= 0);

        // Test ordinamento per titolo Z-A
        ordinati = gestore.ordinaFilms(films, new OrdinaTitoloZAStrategy());
        // Verifica che i titoli siano in ordine alfabetico inverso
        assertTrue(ordinati.get(0).getTitolo().compareTo(ordinati.get(1).getTitolo()) >= 0);
        assertTrue(ordinati.get(1).getTitolo().compareTo(ordinati.get(2).getTitolo()) >= 0);

        // Test ordinamento per regista A-Z
        ordinati = gestore.ordinaFilms(films, new OrdinaRegistaAZStrategy());
        // Verifica che i registi siano in ordine alfabetico
        assertTrue(ordinati.get(0).getRegista().compareTo(ordinati.get(1).getRegista()) <= 0);
        assertTrue(ordinati.get(1).getRegista().compareTo(ordinati.get(2).getRegista()) <= 0);

        // Test ordinamento per regista Z-A
        ordinati = gestore.ordinaFilms(films, new OrdinaRegistaZAStrategy());
        // Verifica che i registi siano in ordine alfabetico inverso
        assertTrue(ordinati.get(0).getRegista().compareTo(ordinati.get(1).getRegista()) >= 0);
        assertTrue(ordinati.get(1).getRegista().compareTo(ordinati.get(2).getRegista()) >= 0);

        // Test ordinamento per valutazione Ascendente
        ordinati = gestore.ordinaFilms(films, new OrdinaValutazioneAscStrategy());
        // Verifica che le valutazioni siano in ordine crescente
        assertTrue(ordinati.get(0).getValutazione() <= ordinati.get(1).getValutazione());
        assertTrue(ordinati.get(1).getValutazione() <= ordinati.get(2).getValutazione());

        // Test ordinamento per valutazione Discendente
        ordinati = gestore.ordinaFilms(films, new OrdinaValutazioneDescStrategy());
        // Verifica che le valutazioni siano in ordine decrescente
        assertTrue(ordinati.get(0).getValutazione() >= ordinati.get(1).getValutazione());
        assertTrue(ordinati.get(1).getValutazione() >= ordinati.get(2).getValutazione());

        //Test ordinamento per anno di uscita Ascendente
        ordinati = gestore.ordinaFilms(films, new OrdinaAnnoUscitaCrescenteStrategy());
        // Verifica che gli anni di uscita siano in ordine crescente
        assertTrue(ordinati.get(0).getAnnoUscita().compareTo(ordinati.get(1).getAnnoUscita()) <= 0);
        assertTrue(ordinati.get(1).getAnnoUscita().compareTo(ordinati.get(2).getAnnoUscita()) <= 0);

        //Test ordinamento per anno di uscita Discendente
        ordinati = gestore.ordinaFilms(films, new OrdinaAnnoUscitaDecrescenteStrategy());
        // Verifica che gli anni di uscita siano in ordine decrescente
        assertTrue(ordinati.get(0).getAnnoUscita().compareTo(ordinati.get(1).getAnnoUscita()) >= 0);
        assertTrue(ordinati.get(1).getAnnoUscita().compareTo(ordinati.get(2).getAnnoUscita()) >= 0);

        // Test ordinamento con strategia null
        ordinati = gestore.ordinaFilms(films, null);
        assertEquals(3, ordinati.size());
        // Verifica che tutti i films originali siano presenti
        assertTrue(ordinati.contains(film1));
        assertTrue(ordinati.contains(film2));
        assertTrue(ordinati.contains(film3));
    }

    @Test
    public void testGetGeneriUnici() {
        // Aggiungi un film con un genere duplicato
        Film film4 = new Film("Altro film", "Altro regista", "1234", "fantascienza", 3, StatoVisione.DA_VEDERE);
        gestore.aggiungiFilm(film4);

        // Verifica i generi unici
        List<String> generi = gestore.getGeneriUnici();
        assertEquals(2, generi.size());
        assertTrue(generi.contains("dramma"));
        assertTrue(generi.contains("fantascienza"));
    }

    @Test
    public void testGetRegistiUnici() {
        // Aggiungi un film con un regista duplicato
        Film film4 = new Film("Altro film di Nolan", "Christopher Nolan", "4531", "commedia", 3, StatoVisione.DA_VEDERE);
        gestore.aggiungiFilm(film4);

        // Verifica i registi unici
        List<String> registi = gestore.getRegistiUnici();
        assertEquals(3, registi.size());
        assertTrue(registi.contains("Francis Ford Coppola"));
        assertTrue(registi.contains("Christopher Nolan"));
        assertTrue(registi.contains("Giuseppe Tornatore"));
    }

    @Test
    public void testPulisciVideoteca() {
        // Verifica che ci siano films prima di pulire
        assertFalse(gestore.getFilms().isEmpty());

        // Pulisci la videoteca
        gestore.pulisciVideoteca();

        // Verifica che la videoteca sia vuota dopo aver pulito
        assertTrue(gestore.getFilms().isEmpty());
    }
}