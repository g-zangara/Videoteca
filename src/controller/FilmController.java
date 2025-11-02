package controller;

import model.Film;
import model.StatoVisione;
import model.Status;
import view.VideotecaView;
import strategy.*;
import command.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Controller per gestire l'interazione tra il modello (Film/GestoreVideoteca) e la vista (VideotecaView).
 * Implementa il pattern MVC.
 */
public class FilmController {

    private final GestoreVideoteca gestoreVideoteca;
    private final VideotecaView view;
    private final CommandManager commandManager;

    /**
     * Costruttore che inizializza il controller con il gestore videoteca e la vista.
     *
     * @param view Vista da associare al controller
     */
    public FilmController(VideotecaView view) {
        this.gestoreVideoteca = GestoreVideoteca.getInstance();
        this.view = view;
        this.commandManager = new CommandManager();
    }

    /**
     * Aggiunge un nuovo film alla videoteca usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando AggiungiFilm.
     *
     * @param titolo Titolo del film
     * @param regista Regista del film
     * @param annoUscita Anno di uscita del film
     * @param genere Genere del film
     * @param valutazione Valutazione del film
     * @param statoVisione Stato visione del film
     * @return true se l'inserimento è avvenuto con successo, false altrimenti
     */
    public boolean aggiungiFilm(String titolo, String regista, String annoUscita, String genere,
                                int valutazione, StatoVisione statoVisione) {
        Command comando = new AggiungiFilmCommand(this, titolo, regista, annoUscita, genere,
                valutazione, statoVisione);
        boolean result = commandManager.executeCommand(comando);
        if(result)
            aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Metodo interno per aggiungere un film senza creare un comando.
     * Questo metodo è chiamato dai comandi AggiungiFilmCommand e EliminaFilmCommand (undo).
     *
     * @param film Film da aggiungere
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean aggiungiFilmInterno(Film film) {
        boolean result = gestoreVideoteca.aggiungiFilm(film);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Modifica un film esistente nella videoteca usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando ModificaFilm.
     *
     * @param filmSelezionato Film da modificare
     * @param titolo Nuovo titolo
     * @param regista Nuovo regista
     * @param annoUscita Nuovo anno di uscita
     * @param genere Nuovo genere
     * @param valutazione Nuova valutazione
     * @param statoVisione Nuovo stato visione
     * @return true se la modifica è avvenuta con successo, false altrimenti
     */
    public boolean modificaFilm(Film filmSelezionato, String titolo, String regista, String annoUscita,
                                String genere, int valutazione, StatoVisione statoVisione) {
        if (filmSelezionato == null) {
            return false;
        }

        Command comando = new ModificaFilmCommand(this, filmSelezionato, titolo, regista, annoUscita,
                genere, valutazione, statoVisione);
        boolean result = commandManager.executeCommand(comando);
        if(result)
            aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Metodo interno per modificare un film senza creare un comando.
     * Questo metodo è chiamato dal comando ModificaFilmCommand.
     *
     * @param vecchioFilm Film da modificare
     * @param nuovoFilm Film con i nuovi dati
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean modificaFilmInterno(Film vecchioFilm, Film nuovoFilm) {
        boolean result = gestoreVideoteca.modificaFilm(vecchioFilm, nuovoFilm);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Elimina un film dalla videoteca usando il pattern Command.
     * Questo metodo è chiamato dalla vista e crea un comando EliminaFilm.
     *
     * @param film Film da eliminare
     * @return true se l'eliminazione è avvenuta con successo, false altrimenti
     */
    public boolean eliminaFilm(Film film) {
        if (film == null) {
            return false;
        }

        Command comando = new EliminaFilmCommand(this, film);
        commandManager.executeCommand(comando);
        aggiornaStatoPulsanti();
        return true;
    }

    /**
     * Metodo interno per eliminare un film senza creare un comando.
     * Questo metodo è chiamato dal comando EliminaFilmCommand.
     *
     * @param film Film da eliminare
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    public boolean eliminaFilmInterno(Film film) {
        boolean result = gestoreVideoteca.eliminaFilm(film);
        if (result) {
            aggiornaTabella();
        }
        return result;
    }

    /**
     * Esegue l'operazione di undo (annulla l'ultima operazione).
     *
     * @return true se l'undo è stato eseguito, false se non ci sono operazioni da annullare
     */
    public boolean undo() {
        boolean result = commandManager.undo();
        aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Esegue l'operazione di redo (ripristina l'ultima operazione annullata).
     *
     * @return true se il redo è stato eseguito, false se non ci sono operazioni da ripristinare
     */
    public boolean redo() {
        boolean result = commandManager.redo();
        aggiornaStatoPulsanti();
        return result;
    }

    /**
     * Verifica se è possibile eseguire un'operazione di undo.
     *
     * @return true se è possibile eseguire un undo, false altrimenti
     */
    public boolean canUndo() {
        return commandManager.canUndo();
    }

    /**
     * Verifica se è possibile eseguire un'operazione di redo.
     *
     * @return true se è possibile eseguire un redo, false altrimenti
     */
    public boolean canRedo() {
        return commandManager.canRedo();
    }

    /**
     * Ottiene la descrizione dell'ultima operazione eseguita (per tooltip undo).
     *
     * @return Descrizione dell'ultima operazione o null se non ci sono operazioni
     */
    public String getUndoDescription() {
        return commandManager.getUndoDescription();
    }

    /**
     * Ottiene la descrizione dell'ultima operazione annullata (per tooltip redo).
     *
     * @return Descrizione dell'ultima operazione annullata o null se non ci sono operazioni
     */
    public String getRedoDescription() {
        return commandManager.getRedoDescription();
    }

    /**
     * Aggiorna lo stato dei pulsanti undo/redo nella vista.
     */
    private void aggiornaStatoPulsanti() {
        view.aggiornaStatoPulsantiUndoRedo(canUndo(), canRedo(),
                getUndoDescription(), getRedoDescription());
    }

    /**
     * Carica la lista completa dei films e aggiorna la vista.
     */
    public void caricaFilms() {
        List<Film> films = gestoreVideoteca.getFilms();
        view.aggiornaTabella(films);
        view.aggiornaComboBoxGeneri(gestoreVideoteca.getGeneriUnici());
        view.aggiornaComboBoxRegisti(gestoreVideoteca.getRegistiUnici());
        view.aggiornaComboBoxAnnoUscita(gestoreVideoteca.getAnnoUscitaUnici());
        // Inizializza lo stato dei pulsanti undo/redo
        aggiornaStatoPulsanti();
    }

    /**
     * Cerca films in base ai criteri di ricerca specificati.
     *
     * @param testoCerca Testo di ricerca
     * @param tipoCerca Tipo di ricerca (titolo, regista)
     */
    private List<Film> cercaFilms(String testoCerca, String tipoCerca) {
        List<Film> risultato;

        if (testoCerca == null || testoCerca.trim().isEmpty()) {
            risultato = gestoreVideoteca.getFilms();
        } else {
            switch (tipoCerca) {
                case "Titolo":
                    risultato = gestoreVideoteca.cercaPerTitolo(testoCerca);
                    break;
                case "Regista":
                    risultato = gestoreVideoteca.cercaPerRegista(testoCerca);
                    break;
                default:
                    risultato = gestoreVideoteca.getFilms();
            }
        }

        // Applica i filtri selezionati
        risultato = applicaFiltri(risultato);

        // Applica l'ordinamento selezionato
        risultato = applicaOrdinamento(risultato);

        return risultato;
    }

    /**
     * Applica i filtri selezionati nella vista a una lista di films.
     * Supporta l'applicazione di filtri multipli in combinazione.
     *
     * @param filmsOriginali Lista di films da filtrare
     * @return Lista filtrata di films
     */
    private List<Film> applicaFiltri(List<Film> filmsOriginali) {
        List<Film> films = new ArrayList<>(filmsOriginali); // Clona la lista per non modificare l'originale

        // Filtro per genere
        String genereSelezionato = view.getGenereSelezionato();
        if (genereSelezionato != null && !genereSelezionato.equals("Tutti")) {
            films = films.stream()
                    .filter(film -> film.getGenere().equalsIgnoreCase(genereSelezionato))
                    .collect(Collectors.toList());
        }

        // Filtro per regista
        String registaSelezionato = view.getRegistaSelezionato();
        if (registaSelezionato != null && !registaSelezionato.equals("Tutti")) {
            films = films.stream()
                    .filter(film -> film.getRegista().toLowerCase().contains(registaSelezionato.toLowerCase()))
                    .collect(Collectors.toList());
        }

        //Filtro per anno di uscita
        String annoUscitaSelezionato = view.getAnnoUscitaSelezionato();
        if (annoUscitaSelezionato != null && !annoUscitaSelezionato.equals("Tutti")) {
            films = films.stream()
                    .filter(film -> film.getAnnoUscita().equals(annoUscitaSelezionato))
                    .collect(Collectors.toList());
        }

        // Filtro per stato di visione
        String statoVisioneSelezionato = view.getStatoVisioneSelezionato();
        if (statoVisioneSelezionato != null && !statoVisioneSelezionato.equals("Tutti")) {
            try {
                StatoVisione stato = StatoVisione.fromString(statoVisioneSelezionato);
                films = films.stream()
                        .filter(film -> film.getStatoVisione() == stato)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                // Ignora filtro non valido
                System.err.println("Filtro non valido: " + statoVisioneSelezionato);
            }
        }

        // Filtro per valutazione
        int valutazioneSelezionata = view.getValutazioneSelezionata();
        if (valutazioneSelezionata >= 0) {
            films = films.stream()
                    .filter(film -> film.getValutazione() == valutazioneSelezionata)
                    .collect(Collectors.toList());
        }

        return films;
    }

    /**
     * Applica l'ordinamento selezionato nella vista a una lista di films.
     *
     * @param films Lista di films da ordinare
     * @return Lista ordinata di films
     */
    private List<Film> applicaOrdinamento(List<Film> films) {
        String ordinamentoSelezionato = view.getOrdinamentoSelezionato();
        if (ordinamentoSelezionato != null) {
            OrdinatoreFilmStrategy strategy = null;

            switch (ordinamentoSelezionato) {
                case "Titolo (A-Z)":
                    strategy = new OrdinaTitoloAZStrategy();
                    break;
                case "Titolo (Z-A)":
                    strategy = new OrdinaTitoloZAStrategy();
                    break;
                case "Regista (A-Z)":
                    strategy = new OrdinaRegistaAZStrategy();
                    break;
                case "Regista (Z-A)":
                    strategy = new OrdinaRegistaZAStrategy();
                    break;
                case "Valutazione (1-5)":
                    strategy = new OrdinaValutazioneAscStrategy();
                    break;
                case "Valutazione (5-1)":
                    strategy = new OrdinaValutazioneDescStrategy();
                    break;
                case "Anno di Uscita (ASC)":
                    strategy = new OrdinaAnnoUscitaCrescenteStrategy();
                    break;
                case "Anno di Uscita (DESC)":
                    strategy = new OrdinaAnnoUscitaDecrescenteStrategy();
                    break;
                default:
                    // Nessun ordinamento selezionato
                    break;
            }

            if (strategy != null) {
                films = gestoreVideoteca.ordinaFilms(films, strategy);
            }
        }

        return films;
    }

    /**
     * Aggiorna la tabella nella vista con la lista filtrata e ordinata.
     */
    public void aggiornaTabella() {
        // Ottiene la lista dei films
        List<Film> films = gestoreVideoteca.getFilms();

        films = cercaFilms(view.getCampoCerca(), view.getCampoTipoCerca());

        // Applica i filtri
        films = applicaFiltri(films);

        // Applica l'ordinamento
        films = applicaOrdinamento(films);

        // Aggiorna la tabella nella vista
        view.aggiornaTabella(films);

        // Aggiorna le combo box di filtro
        view.aggiornaComboBoxGeneri(gestoreVideoteca.getGeneriUnici());
        view.aggiornaComboBoxRegisti(gestoreVideoteca.getRegistiUnici());
        view.aggiornaComboBoxAnnoUscita(gestoreVideoteca.getAnnoUscitaUnici());
    }

    /**
     * Salva la videoteca nel formato specificato.
     *
     * @param percorsoFile Percorso del file
     * @param formato Formato del file (JSON o CSV)
     */
    public Status salvaVideoteca(String percorsoFile, String formato) {
        try {
            if ("JSON".equalsIgnoreCase(formato)) {
                gestoreVideoteca.salvaFilmsInJson(percorsoFile);
            } else if ("CSV".equalsIgnoreCase(formato)) {
                gestoreVideoteca.salvaFilmsInCsv(percorsoFile);
            }
            return new Status(true, "Videoteca salvata con successo in " + formato + " nel file: " + percorsoFile);
        } catch (IOException e) {
            return new Status(false, "Errore durante il salvataggio della videoteca: " + e.getMessage());
        }
    }

    /**
     * Carica la videoteca dal formato specificato.
     *
     * @param percorsoFile Percorso del file
     * @param formato Formato del file (JSON o CSV)
     *
     * @return Status con esito e messaggio
     */
    public Status caricaVideoteca(String percorsoFile, String formato) {
        try {
            if ("JSON".equalsIgnoreCase(formato)) {
                gestoreVideoteca.caricaFilmsDaJson(percorsoFile);
            } else if ("CSV".equalsIgnoreCase(formato)) {
                gestoreVideoteca.caricaFilmsDaCsv(percorsoFile);
            }else {
                //IN TEORIA QUI NON DOVREBBE MAI ARRIVARCI
                System.err.println("Formato non supportato: " + formato);
                return new Status(false, "Formato non supportato: " + formato);
            }

            aggiornaTabella();

            // Quando si carica una nuova videoteca, si svuotano gli stack undo/redo
            commandManager.clearStacks();
            aggiornaStatoPulsanti();
            return new Status(true, "Videoteca caricata con successo dal file: " + percorsoFile);
        } catch (IOException e) {
            return new Status(false, "Errore durante il caricamento della videoteca: " + e.getMessage());
        }
    }

    /**
     * Pulisce la videoteca, rimuovendo tutti i films presenti.
     * Svuota anche gli stack di undo e redo.
     */
    public void pulisciVideoteca() {
        gestoreVideoteca.pulisciVideoteca();
        aggiornaTabella();

        // Svuota gli stack undo/redo quando si pulisce la videoteca
        commandManager.clearStacks();
        aggiornaStatoPulsanti();
    }

}
