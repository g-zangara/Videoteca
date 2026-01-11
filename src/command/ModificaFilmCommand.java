package command;

import controller.FilmController;
import model.Film;
import model.StatoVisione;

/**
 * Comando per la modifica di un film esistente nella videoteca.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class ModificaFilmCommand implements Command {

    private final FilmController controller;
    private final Film filmOriginale;
    private final String nuovoTitolo;
    private final String nuovoRegista;
    private final String nuovoAnnoUscita;
    private final String nuovoGenere;
    private final int nuovaValutazione;
    private final StatoVisione nuovoStatoVisione;
    private Film filmModificato; // Riferimento al film modificato per l'operazione di undo

    /**
     * Costruttore che inizializza il comando con i dati del film da modificare.
     *
     * @param controller        Controller della videoteca
     * @param filmOriginale     Film originale da modificare
     * @param nuovoTitolo       Nuovo titolo
     * @param nuovoRegista      Nuovo regista
     * @param nuovoAnnoUscita   Nuovo anno di uscita
     * @param nuovoGenere       Nuovo genere
     * @param nuovaValutazione  Nuova valutazione
     * @param nuovoStatoVisione Nuovo stato visione
     */
    public ModificaFilmCommand(FilmController controller, Film filmOriginale,
                               String nuovoTitolo, String nuovoRegista,
                               String nuovoAnnoUscita, String nuovoGenere,
                               int nuovaValutazione, StatoVisione nuovoStatoVisione) {
        this.controller = controller;
        this.filmOriginale = filmOriginale;
        this.nuovoTitolo = nuovoTitolo;
        this.nuovoRegista = nuovoRegista;
        this.nuovoAnnoUscita = nuovoAnnoUscita;
        this.nuovoGenere = nuovoGenere;
        this.nuovaValutazione = nuovaValutazione;
        this.nuovoStatoVisione = nuovoStatoVisione;
    }

    /**
     * Esegue il comando modificando il film nella videoteca.
     * Crea una copia del film originale per supportare l'operazione di undo.
     */
    @Override
    public boolean execute() {
        filmModificato = new Film(nuovoTitolo, nuovoRegista, nuovoAnnoUscita, nuovoGenere,
                nuovaValutazione, nuovoStatoVisione);

        return controller.modificaFilmInterno(filmOriginale, filmModificato);
    }

    /**
     * Annulla il comando ripristinando i dati originali del film.
     */
    @Override
    public void undo() {
        if (filmModificato != null) {
            controller.modificaFilmInterno(filmModificato, filmOriginale);
        }
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Modifica film: " + filmOriginale.getTitolo() + " -> " + nuovoTitolo;
    }

}
