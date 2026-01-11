package test.dao;

import static org.junit.jupiter.api.Assertions.*;

import strategy.dao.FilmDAO;
import model.Film;
import model.StatoVisione;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import strategy.dao.JsonFilmDAO;
import strategy.dao.CsvFilmDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test unitari per le implementazioni di FilmDAO.
 * Verifica il corretto funzionamento delle operazioni di persistenza dei dati.
 */
public class FilmDAOTest {

    private FilmDAO jsonDAO;
    private FilmDAO csvDAO;
    private List<Film> filmsTest;
    private String jsonFilePath;
    private String csvFilePath;

    @BeforeEach
    public void setUp() {
        jsonDAO = new JsonFilmDAO();
        csvDAO = new CsvFilmDAO();

        // Crea una directory temporanea per i test
        File tempDir = new File("temp_test");
        tempDir.mkdir();

        // Prepara i percorsi dei file temporanei
        jsonFilePath = "temp_test/films_test.json";
        csvFilePath = "temp_test/films_test.csv";

        // Crea una lista di films di test
        filmsTest = new ArrayList<>();
        filmsTest.add(new Film("Il Padrino", "Francis Ford Coppola", "1972", "dramma", 5, StatoVisione.VISTO));
        filmsTest.add( new Film("Interstellar", "Christopher Nolan", "2014", "fantascienza", 3, StatoVisione.DA_VEDERE));
        filmsTest.add(new Film("The Social Network", "David Fincher", "2010", "biografico", 0, StatoVisione.IN_VISIONE));
    }

    @AfterEach
    public void tearDown() {
        // Elimina i file temporanei se esistono
        new File(jsonFilePath).delete();
        new File(csvFilePath).delete();
        new File("temp_test").delete();
    }

    @Test
    public void testJsonSalvaCaricaFilms() throws IOException {
        // Salva i films in formato JSON
        jsonDAO.salvaFilms(filmsTest, jsonFilePath);

        // Verifica che il file sia stato creato
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());
        assertTrue(jsonFile.length() > 0);

        // Carica i films salvati
        List<Film> filmsCaricati = jsonDAO.caricaFilms(jsonFilePath);

        // Verifica che i film caricati corrispondano a quelli salvati
        assertNotNull(filmsCaricati);
        assertEquals(filmsTest.size(), filmsCaricati.size());

        // Verifica i dati di ciascun film
        for (int i = 0; i < filmsTest.size(); i++) {
            Film filmOriginale = filmsTest.get(i);
            Film filmCaricato = filmsCaricati.get(i);

            assertEquals(filmOriginale.getTitolo(), filmCaricato.getTitolo());
            assertEquals(filmOriginale.getRegista(), filmCaricato.getRegista());
            assertEquals(filmOriginale.getAnnoUscita(), filmCaricato.getAnnoUscita());
            assertEquals(filmOriginale.getGenere(), filmCaricato.getGenere());
            assertEquals(filmOriginale.getValutazione(), filmCaricato.getValutazione());
            assertEquals(filmOriginale.getStatoVisione(), filmCaricato.getStatoVisione());
        }
    }

    @Test
    public void testCsvSalvaCaricaFilms() throws IOException {
        // Salva i films in formato CSV
        csvDAO.salvaFilms(filmsTest, csvFilePath);

        // Verifica che il file sia stato creato
        File csvFile = new File(csvFilePath);
        assertTrue(csvFile.exists());
        assertTrue(csvFile.length() > 0);

        // Carica i films salvati
        List<Film> filmsCaricati = csvDAO.caricaFilms(csvFilePath);

        // Verifica che i films caricati corrispondano a quelli salvati
        assertNotNull(filmsCaricati);
        assertEquals(filmsTest.size(), filmsCaricati.size());

        // Verifica i dati di ciascun film
        for (int i = 0; i < filmsTest.size(); i++) {
            Film filmOriginale = filmsTest.get(i);
            Film filmCaricato = filmsCaricati.get(i);

            assertEquals(filmOriginale.getTitolo(), filmCaricato.getTitolo());
            assertEquals(filmOriginale.getRegista(), filmCaricato.getRegista());
            assertEquals(filmOriginale.getAnnoUscita(), filmCaricato.getAnnoUscita());
            assertEquals(filmOriginale.getGenere(), filmCaricato.getGenere());
            assertEquals(filmOriginale.getValutazione(), filmCaricato.getValutazione());
            assertEquals(filmOriginale.getStatoVisione(), filmCaricato.getStatoVisione());
        }
    }

    @Test
    public void testJsonCaricaFileNonEsistente() {
        // Tenta di caricare da un file che non esiste
        String fileInesistente = "temp_test/file_inesistente.json";

        // Dovrebbe lanciare un'eccezione IOException
        assertThrows(IOException.class, () -> {
            jsonDAO.caricaFilms(fileInesistente);
        });
    }

    @Test
    public void testCsvCaricaFileNonEsistente() {
        // Tenta di caricare da un file che non esiste
        String fileInesistente = "temp_test/file_inesistente.csv";

        // Dovrebbe lanciare un'eccezione IOException
        assertThrows(IOException.class, () -> {
            csvDAO.caricaFilms(fileInesistente);
        });
    }

    @Test
    public void testJsonSalvaListaVuota() throws IOException {
        // Salva una lista vuota
        List<Film> listaVuota = new ArrayList<>();
        jsonDAO.salvaFilms(listaVuota, jsonFilePath);

        // Verifica che il file sia stato creato
        File jsonFile = new File(jsonFilePath);
        assertTrue(jsonFile.exists());

        // Carica la lista vuota
        List<Film> filmsCaricati = jsonDAO.caricaFilms(jsonFilePath);

        // Verifica che la lista caricata sia vuota
        assertNotNull(filmsCaricati);
        assertTrue(filmsCaricati.isEmpty());
    }

    @Test
    public void testCsvSalvaListaVuota() throws IOException {
        // Salva una lista vuota
        List<Film> listaVuota = new ArrayList<>();
        csvDAO.salvaFilms(listaVuota, csvFilePath);

        // Verifica che il file sia stato creato
        File csvFile = new File(csvFilePath);
        assertTrue(csvFile.exists());

        // Carica la lista vuota
        List<Film> filmsCaricati = csvDAO.caricaFilms(csvFilePath);

        // Verifica che la lista caricata sia vuota
        assertNotNull(filmsCaricati);
        assertTrue(filmsCaricati.isEmpty());
    }

    @Test
    public void testJsonCaricaFilmsDuplicati() throws IOException {
        // Crea una lista di film duplicati
        List<Film> filmsDuplicati = new ArrayList<>();
        filmsDuplicati.add(new Film("Film 1", "Regista 1", "1234", "Genere 1", 5, StatoVisione.VISTO));
        filmsDuplicati.add(new Film("Film 1", "Regista 1", "1234", "Genere 1", 5, StatoVisione.VISTO));

        // Salva i films in formato JSON
        jsonDAO.salvaFilms(filmsDuplicati, jsonFilePath);

        // Verifica che venga lanciata un'eccezione durante il caricamento
        IOException exception = assertThrows(IOException.class, () -> {
            jsonDAO.caricaFilms(jsonFilePath);
        });

        // Controlla che il messaggio dell'eccezione contenga informazioni sui duplicati
        String expectedMessage = "Impossibile caricare il file. Sono stati trovati films non validi:\n" +
                "Film #2 (Film 1): film già presente";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void testCsvCaricaFilmsDuplicati() throws IOException {
        // Crea una lista di films duplicati
        List<Film> filmsDuplicati = new ArrayList<>();
        filmsDuplicati.add(new Film("Film 1", "Regista 1", "1234", "Genere 1", 5, StatoVisione.VISTO));
        filmsDuplicati.add(new Film("Film 1", "Regista 1", "1234", "Genere 1", 5, StatoVisione.VISTO));

        // Salva i films in formato CSV
        csvDAO.salvaFilms(filmsDuplicati, csvFilePath);

        // Verifica che venga lanciata un'eccezione durante il caricamento
        IOException exception = assertThrows(IOException.class, () -> {
            csvDAO.caricaFilms(csvFilePath);
        });

        // Controlla che il messaggio dell'eccezione contenga informazioni sui duplicati
        String expectedMessage = "Impossibile caricare il file. Sono stati trovati films non validi:\n" +
                "Riga 3 (Film 1): film già presente";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

}
