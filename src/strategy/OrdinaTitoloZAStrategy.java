package strategy;

import model.Film;

import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei films per titolo in ordine alfabetico inverso (Z-A).
 * Implementa l'interfaccia OrdinatoreFilmStrategy utilizzando il Comparator.
 */
public class OrdinaTitoloZAStrategy implements OrdinatoreFilmStrategy {

    /**
     * Ordina una lista di films per titolo in ordine alfabetico inverso (Z-A).
     *
     * @param films Lista di films da ordinare
     */
    @Override
    public void ordina(List<Film> films) {
        films.sort(Comparator.comparing(Film::getTitolo, String.CASE_INSENSITIVE_ORDER).reversed());
    }
}
