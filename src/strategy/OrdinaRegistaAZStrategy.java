package strategy;

import model.Film;

import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei films per regista in ordine alfabetico (A-Z).
 * Implementa l'interfaccia OrdinatoreFilmStrategy utilizzando il Comparator.
 */
public class OrdinaRegistaAZStrategy implements OrdinatoreFilmStrategy {

    /**
     * Ordina una lista di films per regista in ordine alfabetico (A-Z).
     *
     * @param films Lista di films da ordinare
     */
    @Override
    public void ordina(List<Film> films) {
        films.sort(Comparator.comparing(Film::getRegista, String.CASE_INSENSITIVE_ORDER));
    }
}
