package dao;

import model.Film;

import java.util.List;
import java.io.IOException;

/**
 * Interfaccia DAO (Data Access Object) per la gestione della persistenza dei films.
 * Definisce le operazioni di lettura/scrittura per salvare e caricare films.
 */
public interface FilmDAO {

    /**
     * Salva una lista di films su un file.
     *
     * @param films Lista di films da salvare
     * @param percorsoFile Percorso del file in cui salvare i dati
     * @throws IOException In caso di errori durante la scrittura del file
     */
    void salvaFilms(List<Film> films, String percorsoFile) throws IOException;

    /**
     * Carica una lista di films da un file.
     *
     * @param percorsoFile Percorso del file da cui caricare i dati
     * @return Lista di films caricati dal file
     * @throws IOException In caso di errori durante la lettura del file
     */
    List<Film> caricaFilms(String percorsoFile) throws IOException;
}
