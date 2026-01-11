package command;

import controller.FilmController;
import model.Film;
import model.StatoVisione;

/**
 * Comando per l'aggiunta di un nuovo film alla videoteca.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class AggiungiFilmCommand implements Command {

    private final FilmController controller;
    private final String titolo;
    private final String regista;
    private final String annoUscita;
    private final String genere;
    private final int valutazione;
    private final StatoVisione statoVisione;
    private Film filmAggiunto; // Riferimento al film aggiunto per l'operazione di undo

    /**
     * Costruttore che inizializza il comando con i dati del film da aggiungere.
     *
     * @param controller   Controller della videoteca
     * @param titolo       Titolo del film
     * @param regista      Regista del film
     * @param annoUscita   Anno di uscita del film
     * @param genere       Genere del film
     * @param valutazione  Valutazione del film
     * @param statoVisione Stato visione del film
     */
    public AggiungiFilmCommand(FilmController controller, String titolo, String regista,
                               String annoUscita, String genere, int valutazione,
                               StatoVisione statoVisione) {
        this.controller = controller;
        this.titolo = titolo;
        this.regista = regista;
        this.annoUscita = annoUscita;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoVisione = statoVisione;
    }

    /**
     * Esegue il comando aggiungendo il film alla videoteca.
     * Memorizza un riferimento al film aggiunto per supportare l'operazione di undo.
     */
    @Override
    public boolean execute() {
        // Crea un nuovo film con i dati forniti
        filmAggiunto = new Film(titolo, regista, annoUscita, genere, valutazione, statoVisione);

        // Aggiungi il film tramite il controller
        return controller.aggiungiFilmInterno(filmAggiunto);
    }

    /**
     * Annulla il comando eliminando il film aggiunto.
     */
    @Override
    public void undo() {
        if (filmAggiunto != null) {
            controller.eliminaFilmInterno(filmAggiunto);
        }
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Aggiunta film: " + titolo + " (" + regista + ")";
    }

}
