package controller;

import dao.CsvFilmDAO;
import dao.FilmDAO;
import model.Film;
import dao.JsonFilmDAO;
import strategy.OrdinatoreFilmStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementazione del pattern Singleton per la gestione centralizzata dei films.
 * Gestisce la collezione di films e le operazioni di ricerca, filtro e ordinamento.
 */
public class GestoreVideoteca {

    // Singleton instance
    private static GestoreVideoteca instance;

    // Attributi del gestore
    private List<Film> films;
    private final FilmDAO jsonDAO;
    private final FilmDAO csvDAO;

    /**
     * Costruttore privato per il pattern Singleton.
     * Inizializza le liste e gli oggetti DAO.
     */
    private GestoreVideoteca() {
        this.films = new ArrayList<>();
        this.jsonDAO = new JsonFilmDAO();
        this.csvDAO = new CsvFilmDAO();
    }

    /**
     * Ottiene l'istanza singleton del gestore videoteca.
     *
     * @return L'istanza singleton del GestoreVideoteca
     */
    public static synchronized GestoreVideoteca getInstance() {
        if (instance == null) {
            instance = new GestoreVideoteca();
        }
        return instance;
    }

    /**
     * Aggiunge un film alla collezione.
     *
     * @param film Film da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiFilm(Film film) {
        // Verifica che il film non sia già presente
        if (film != null && !films.contains(film)) {
            return films.add(film);
        }
        return false;
    }

    /**
     * Modifica un film esistente nella collezione.
     *
     * @param vecchioFilm Film da modificare
     * @param nuovoFilm Film con i nuovi dati
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean modificaFilm(Film vecchioFilm, Film nuovoFilm) {
        if(films.contains(nuovoFilm)) {
            //recupero il film già presente
            Film filmPresente = films.get(films.indexOf(nuovoFilm));
            if(!filmPresente.equals(vecchioFilm)) {
                throw new IllegalArgumentException("1 - Stai cercando di modificare un film in un altro già esistente.");
            }
            //posso solo modificare genere, valutazione e stato visione
            if(nuovoFilm.getGenere().equalsIgnoreCase(vecchioFilm.getGenere()) &&
               nuovoFilm.getValutazione() == vecchioFilm.getValutazione() &&
               nuovoFilm.getStatoVisione() == vecchioFilm.getStatoVisione()) {
                throw new IllegalArgumentException("2 - Non sono stati apportati cambiamenti al film.");
            }
        }
        int index = films.indexOf(vecchioFilm);
        if (index != -1) {
            films.set(index, nuovoFilm);
            return true;
        }
        return false;
    }

    /**
     * Elimina un film dalla collezione.
     *
     * @param film Film da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean eliminaFilm(Film film) {
        return films.remove(film);
    }

    /**
     * Ottiene la lista completa dei films.
     *
     * @return Lista dei films
     */
    public List<Film> getFilms() {
        return new ArrayList<>(films); // Restituisce una copia per evitare modifiche esterne
    }

    /**
     * Cerca films per titolo.
     *
     * @param titolo Titolo da cercare (match parziale, case-insensitive)
     * @return Lista di films che corrispondono alla ricerca
     */
    public List<Film> cercaPerTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            return getFilms();
        }

        String titoloLower = titolo.toLowerCase();
        return films.stream()
                .filter(film -> film.getTitolo().toLowerCase().contains(titoloLower))
                .collect(Collectors.toList());
    }

    /**
     * Cerca films per regista.
     *
     * @param regista Regista da cercare (match parziale, case-insensitive)
     * @return Lista di films che corrispondono alla ricerca
     */
    public List<Film> cercaPerRegista(String regista) {
        if (regista == null || regista.trim().isEmpty()) {
            return getFilms();
        }

        String registaLower = regista.toLowerCase();
        return films.stream()
                .filter(film -> film.getRegista().toLowerCase().contains(registaLower))
                .collect(Collectors.toList());
    }

    /**
     * Ordina i films secondo la strategia specificata.
     *
     * @param filmsDaOrdinare Lista di films da ordinare
     * @param strategy Strategia di ordinamento da applicare
     * @return Lista ordinata di films
     */
    public List<Film> ordinaFilms(List<Film> filmsDaOrdinare, OrdinatoreFilmStrategy strategy) {
        if (strategy == null) {
            return filmsDaOrdinare;
        }

        List<Film> result = new ArrayList<>(filmsDaOrdinare);
        strategy.ordina(result);
        return result;
    }

    /**
     * Carica films da un file JSON.
     *
     * @param percorsoFile Percorso del file JSON
     * @throws IOException In caso di errori durante la lettura del file
     */
    public void caricaFilmsDaJson(String percorsoFile) throws IOException {
        films = jsonDAO.caricaFilms(percorsoFile);
    }

    /**
     * Salva films in un file JSON.
     *
     * @param percorsoFile Percorso del file JSON
     * @throws IOException In caso di errori durante la scrittura del file
     */
    public void salvaFilmsInJson(String percorsoFile) throws IOException {
        jsonDAO.salvaFilms(films, percorsoFile);
    }

    /**
     * Carica films da un file CSV.
     *
     * @param percorsoFile Percorso del file CSV
     * @throws IOException In caso di errori durante la lettura del file
     */
    public void caricaFilmsDaCsv(String percorsoFile) throws IOException {
        films = csvDAO.caricaFilms(percorsoFile);
    }

    /**
     * Salva films in un file CSV.
     *
     * @param percorsoFile Percorso del file CSV
     * @throws IOException In caso di errori durante la scrittura del file
     */
    public void salvaFilmsInCsv(String percorsoFile) throws IOException {
        csvDAO.salvaFilms(films, percorsoFile);
    }

    /**
     * Ottiene tutti i generi unici presenti nella collezione di films.
     *
     * @return Lista di generi unici
     */
    public List<String> getGeneriUnici() {
        return films.stream()
                .map(Film::getGenere)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Ottiene tutti i registi unici presenti nella collezione di films.
     *
     * @return Lista di registi unici
     */
    public List<String> getRegistiUnici() {
        return films.stream()
                .map(Film::getRegista)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Ottiene tutti gli anni di uscita unici presenti nella collezione di films.
     *
     * @return Lista di anni di uscita unici
     */
    public List<String> getAnnoUscitaUnici() {
        return films.stream()
                .map(Film::getAnnoUscita)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Pulisce completamente la videoteca rimuovendo tutti i films.
     * Questa operazione non può essere annullata.
     */
    public void pulisciVideoteca() {
        films.clear();
    }
}
