package strategy;

import model.Film;
import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei films per valutazione in ordine decrescente (da 5 a 1).
 * Implementa l'interfaccia OrdinatoreFilmStrategy utilizzando il Comparator.
 */
public class OrdinaValutazioneDescStrategy implements OrdinatoreFilmStrategy {
    
    /**
     * Ordina una lista di films per valutazione in ordine decrescente (da 5 a 1).
     * 
     * @param films Lista di films da ordinare
     */
    @Override
    public void ordina(List<Film> films) {
        films.sort(Comparator.comparingInt(Film::getValutazione).reversed());
    }
}
