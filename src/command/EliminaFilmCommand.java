package command;

import controller.FilmController;
import model.Film;

/**
 * Comando per l'eliminazione di un film dalla videoteca.
 * Implementa l'interfaccia Command per supportare le operazioni di undo/redo.
 */
public class EliminaFilmCommand implements Command {

    private final FilmController controller;
    private final Film film;

    /**
     * Costruttore che inizializza il comando con il film da eliminare.
     *
     * @param controller Controller della videoteca
     * @param film       Film da eliminare
     */
    public EliminaFilmCommand(FilmController controller, Film film) {
        this.controller = controller;
        this.film = film;
    }

    /**
     * Esegue il comando eliminando il film dalla videoteca.
     */
    @Override
    public boolean execute() {
        return controller.eliminaFilmInterno(film);
    }

    /**
     * Annulla il comando aggiungendo nuovamente il film alla videoteca.
     */
    @Override
    public void undo() {
        controller.aggiungiFilmInterno(film);
    }

    /**
     * Restituisce una descrizione del comando.
     *
     * @return Descrizione testuale del comando
     */
    @Override
    public String getDescription() {
        return "Eliminazione film: " + film.getTitolo() + " (" + film.getRegista() + ")";
    }

}