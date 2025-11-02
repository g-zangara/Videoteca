package strategy;

import model.Film;
import java.util.List;

/**
 * Interfaccia che definisce la strategia di ordinamento dei films.
 * Implementa il pattern Strategy per l'ordinamento flessibile.
 */
public interface OrdinatoreFilmStrategy {
    
    /**
     * Ordina una lista di films secondo una specifica strategia.
     * 
     * @param films Lista di films da ordinare
     */
    void ordina(List<Film> films);
}
