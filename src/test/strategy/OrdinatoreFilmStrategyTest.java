package test.strategy;

import model.Film;
import model.StatoVisione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test unitari per le classi che implementano OrdinatoreFilmStrategy.
 * Verifica il corretto funzionamento delle strategie di ordinamento.
 */
public class OrdinatoreFilmStrategyTest {

    private List<Film> films;
    private Film film1, film2, film3, film4;

    @BeforeEach
    public void setUp() {
        films = new ArrayList<>();

        film1 = new Film("Il Padrino", "Francis Ford Coppola", "1972", "dramma", 5, StatoVisione.VISTO);
        film2 = new Film("Interstellar", "Christopher Nolan", "2014", "fantascienza", 3, StatoVisione.DA_VEDERE);
        film3 = new Film("Nuovo Cinema Paradiso", "Giuseppe Tornatore", "1988", "dramma", 1, StatoVisione.VISTO);
        film4 = new Film("The Social Network", "David Fincher", "2010", "biografico", 4, StatoVisione.IN_VISIONE);

        // Aggiunta dei film in ordine casuale
        films.add(film1);
        films.add(film3);
        films.add(film2);
        films.add(film4);
    }

    @Test
    public void testOrdinaTitoloAZStrategy() {
        // Ordina per titolo A-Z
        OrdinatoreFilmStrategy strategy = new OrdinaTitoloAZStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film1, films.get(0)); // "Il Padrino"
        assertEquals(film2, films.get(1)); // "Interstellar"
        assertEquals(film3, films.get(2)); // "Nuovo Cinema Paradiso"
        assertEquals(film4, films.get(3)); // "The Social Network"
    }

    @Test
    public void testOrdinaTitoloZAStrategy() {
        // Ordina per titolo Z-A
        OrdinatoreFilmStrategy strategy = new OrdinaTitoloZAStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film1, films.get(3)); // "Il Padrino"
        assertEquals(film2, films.get(2)); // "Interstellar"
        assertEquals(film3, films.get(1)); // "Nuovo Cinema Paradiso"
        assertEquals(film4, films.get(0)); // "The Social Network"
    }

    @Test
    public void testOrdinaRegistaAZStrategy() {
        // Ordina per regista A-Z
        OrdinatoreFilmStrategy strategy = new OrdinaRegistaAZStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film2, films.get(0)); // "Christopher Nolan"
        assertEquals(film4, films.get(1)); // "David Fincher"
        assertEquals(film1, films.get(2)); // "Francis Ford Coppola"
        assertEquals(film3, films.get(3)); // "Giuseppe Tornatore"
    }

    @Test
    public void testOrdinaRegistaZAStrategy() {
        // Ordina per regista Z-A
        OrdinatoreFilmStrategy strategy = new OrdinaRegistaZAStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film2, films.get(3)); // "Christopher Nolan"
        assertEquals(film4, films.get(2)); // "David Fincher"
        assertEquals(film1, films.get(1)); // "Francis Ford Coppola"
        assertEquals(film3, films.get(0)); // "Giuseppe Tornatore"
    }

    @Test
    public void testOrdinaValutazioneAscStrategy() {
        // Ordina per valutazione ascendente
        OrdinatoreFilmStrategy strategy = new OrdinaValutazioneAscStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film3, films.get(0)); // 1
        assertEquals(film2, films.get(1)); // 3
        assertEquals(film4, films.get(2)); // 4
        assertEquals(film1, films.get(3)); // 5
    }

    @Test
    public void testOrdinaValutazioneDescStrategy() {
        // Ordina per valutazione discendente
        OrdinatoreFilmStrategy strategy = new OrdinaValutazioneDescStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film3, films.get(3)); // 1
        assertEquals(film2, films.get(2)); // 3
        assertEquals(film4, films.get(1)); // 4
        assertEquals(film1, films.get(0)); // 5
    }

    @Test
    public void testOrdinaAnnoUscitaASCStrategy() {
        // Ordina per valutazione discendente
        OrdinatoreFilmStrategy strategy = new OrdinaAnnoUscitaCrescenteStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film1, films.get(0)); // 1972
        assertEquals(film3, films.get(1)); // 1988
        assertEquals(film4, films.get(2)); // 2010
        assertEquals(film2, films.get(3)); // 2014
    }

    @Test
    public void testOrdinaAnnoUscitaDESCStrategy() {
        // Ordina per valutazione discendente
        OrdinatoreFilmStrategy strategy = new OrdinaAnnoUscitaDecrescenteStrategy();
        strategy.ordina(films);

        // Verifica l'ordine
        assertEquals(film1, films.get(3)); // 1972
        assertEquals(film3, films.get(2)); // 1988
        assertEquals(film4, films.get(1)); // 2010
        assertEquals(film2, films.get(0)); // 2014
    }


    @Test
    public void testCaseSensitivity() {
        // Crea films con titoli che differiscono solo per maiuscole/minuscole
        List<Film> filmsCase = new ArrayList<>();
        Film filmA = new Film("aaa", "Regista1", "1234", "Genere", 1, StatoVisione.VISTO);
        Film filmB = new Film("AAA", "Regista2", "5644", "Genere", 2, StatoVisione.VISTO);
        filmsCase.add(filmA);
        filmsCase.add(filmB);

        // Test ordinamento titolo A-Z (case insensitive)
        OrdinatoreFilmStrategy strategyTitolo = new OrdinaTitoloAZStrategy();
        strategyTitolo.ordina(filmsCase);

        // L'ordine dovrebbe dipendere dalla posizione originale, dato che i titoli sono uguali ignorando il caso
        // Verifichiamo solo che non siano stati modificati in modo imprevisto
        assertEquals(2, filmsCase.size());
        assertTrue(filmsCase.contains(filmA));
        assertTrue(filmsCase.contains(filmB));

        // Test ordinamento regista A-Z (case insensitive)
        filmsCase.clear();
        Film filmC = new Film("Titolo1", "regista", "2133", "Genere", 1, StatoVisione.VISTO);
        Film filmD = new Film("Titolo2", "REGISTA", "5454", "Genere", 2, StatoVisione.VISTO);
        filmsCase.add(filmC);
        filmsCase.add(filmD);

        OrdinatoreFilmStrategy strategyRegista = new OrdinaRegistaAZStrategy();
        strategyRegista.ordina(filmsCase);

        // Come sopra, verifichiamo solo che non ci siano stati problemi
        assertEquals(2, filmsCase.size());
        assertTrue(filmsCase.contains(filmC));
        assertTrue(filmsCase.contains(filmD));
    }

}
