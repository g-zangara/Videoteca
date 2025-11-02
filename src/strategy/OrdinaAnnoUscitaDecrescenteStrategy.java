package strategy;

import model.Film;
import java.util.Comparator;
import java.util.List;

/**
 * Strategia di ordinamento dei film per anno di uscita in ordine decrescente.
 * Implementa l'interfaccia OrdinatoreFilmStrategy utilizzando il Comparator.
 */
public class OrdinaAnnoUscitaDecrescenteStrategy implements OrdinatoreFilmStrategy{

    /**
     * Ordina una lista di film per anno di uscita in ordine decrescente.
     *
     * @param films Lista di film da ordinare
     */
    @Override
    public void ordina(List<Film> films) {
        films.sort(Comparator.comparing(
                Film::getAnnoUscita,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));
    }
}
