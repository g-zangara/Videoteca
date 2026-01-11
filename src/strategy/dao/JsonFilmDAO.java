package strategy.dao;

import model.Film;
import model.StatoVisione;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia FilmDAO per la gestione dei films in formato JSON.
 * Utilizza la codifica manuale del JSON per evitare dipendenze esterne.
 */
public class JsonFilmDAO implements FilmDAO {

    /**
     * Salva una lista di films in formato JSON.
     *
     * @param films        Lista di films da salvare
     * @param percorsoFile Percorso del file JSON in cui salvare i dati
     * @throws IOException In caso di errori durante la scrittura del file
     */
    @Override
    public void salvaFilms(List<Film> films, String percorsoFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorsoFile))) {
            writer.write("[\n");

            for (int i = 0; i < films.size(); i++) {
                Film film = films.get(i);
                writer.write("  {\n");
                writer.write("    \"titolo\": \"" + escapeJson(film.getTitolo()) + "\",\n");
                writer.write("    \"regista\": \"" + escapeJson(film.getRegista()) + "\",\n");
                writer.write("    \"annoUscita\": \"" + escapeJson(film.getAnnoUscita()) + "\",\n");
                writer.write("    \"genere\": \"" + escapeJson(film.getGenere()) + "\",\n");

                // Valutazione come numero, per retro-compatibilità
                writer.write("    \"valutazione\": " + film.getValutazione() + ",\n");

                writer.write("    \"statoVisione\": \"" + film.getStatoVisione().name() + "\"\n");
                writer.write("  }");

                // Aggiungi virgola se non è l'ultimo elemento
                if (i < films.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }

            writer.write("]");
        }
    }

    /**
     * Carica una lista di films da un file JSON.
     * Se anche un solo film non è valido, l'intera operazione fallisce.
     *
     * @param percorsoFile Percorso del file JSON da cui caricare i dati
     * @return Lista di films caricati dal file
     * @throws IOException In caso di errori durante la lettura del file o se ci sono films non validi
     */
    @Override
    public List<Film> caricaFilms(String percorsoFile) throws IOException {
        List<Film> films = new ArrayList<>();
        File file = new File(percorsoFile);

        // Verifica che il file abbia solo una estensione e che sia .json
        String nomeFile = file.getName();
        int ultimoPunto = nomeFile.lastIndexOf('.');
        if (ultimoPunto == -1 || !nomeFile.substring(ultimoPunto + 1).equalsIgnoreCase("json") ||
                nomeFile.substring(0, ultimoPunto).contains(".")) {
            System.err.println("Formato file non valido: " + percorsoFile);
            throw new IOException("Formato file non valido.\n Il file deve avere solo l'estensione .json senza estensioni multiple.");
        }

        // Se il file non esiste, restituisce una lista vuota
        if (!file.exists()) {
            System.err.println("File non trovato: " + percorsoFile);
            throw new IOException("File non trovato:\n" + percorsoFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line).append("\n");
            }

            // Parsing manuale del JSON
            String content = jsonContent.toString().trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();

                // Suddivide gli oggetti JSON
                List<String> jsonObjects = splitJsonObjects(content);

                List<String> errori = new ArrayList<>();
                int indice = 0;

                for (String jsonObject : jsonObjects) {
                    indice++;
                    Film film = parseJsonFilm(jsonObject);
                    if (film == null) {
                        // Film non valido
                        errori.add("Film #" + indice + ": formato JSON non valido");
                    } else if (!film.isValid()) {
                        // Film con dati incompleti o invalidi
                        errori.add("Film #" + indice + " (" +
                                (film.getTitolo().isEmpty() ? "titolo mancante" : film.getTitolo()) +
                                "): dati incompleti o non validi");
                    } else {
                        // Film valido, verifica duplicati
                        if (films.contains(film)) {
                            errori.add("Film #" + indice + " (" +
                                    (film.getTitolo().isEmpty() ? "titolo mancante" : film.getTitolo()) +
                                    "): film già presente");
                        } else {
                            films.add(film);
                        }
                    }
                }

                // Se ci sono errori, interrompi il caricamento e segnala
                if (!errori.isEmpty()) {
                    String messaggioErrore = "Impossibile caricare il file. Sono stati trovati films non validi:" +
                            "\n" + String.join("\n", errori);
                    throw new IOException(messaggioErrore);
                }
            }
        }

        return films;
    }

    /**
     * Suddivide una stringa contenente oggetti JSON in una lista di stringhe,
     * ciascuna rappresentante un singolo oggetto JSON.
     *
     * @param jsonContent Stringa contenente più oggetti JSON
     * @return Lista di stringhe, ciascuna rappresentante un oggetto JSON
     */
    private List<String> splitJsonObjects(String jsonContent) {
        List<String> objects = new ArrayList<>();
        int nesting = 0;
        StringBuilder currentObject = new StringBuilder();

        for (char c : jsonContent.toCharArray()) {
            if (c == '{') {
                nesting++;
            } else if (c == '}') {
                nesting--;
            }

            currentObject.append(c);

            if (nesting == 0 && !currentObject.toString().trim().isEmpty()) {
                // Rimozione di eventuali virgole finali
                String obj = currentObject.toString().trim();
                if (obj.endsWith(",")) {
                    obj = obj.substring(0, obj.length() - 1);
                }
                if (!obj.isEmpty()) {
                    objects.add(obj);
                }
                currentObject = new StringBuilder();
            }
        }

        return objects;
    }

    /**
     * Converte un oggetto JSON (come stringa) in un oggetto Film.
     * Include validazione degli input durante il parsing.
     *
     * @param jsonObject Stringa rappresentante un oggetto JSON
     * @return Oggetto Film costruito dai dati JSON, o null in caso di errore
     */
    private Film parseJsonFilm(String jsonObject) {
        jsonObject = jsonObject.trim();
        if (!jsonObject.startsWith("{") || !jsonObject.endsWith("}")) {
            return null;
        }

        // Rimuove le parentesi graffe
        jsonObject = jsonObject.substring(1, jsonObject.length() - 1).trim();

        String titolo = "";
        String regista = "";
        String annoUscita = "";
        String genere = "";
        int valutazione = 0; // Default a "da valutare"
        StatoVisione statoVisione = StatoVisione.DA_VEDERE; // Default a "da vedere"

        // Suddivide le coppie chiave-valore
        String[] pairs = jsonObject.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();

                // Rimuove virgolette per stringhe
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                switch (key) {
                    case "titolo":
                        titolo = value;
                        break;
                    case "regista":
                        regista = value;
                        break;
                    case "annoUscita":
                        annoUscita = value;
                        break;
                    case "genere":
                        genere = value;
                        break;
                    case "valutazione":
                        try {
                            if (value.equalsIgnoreCase("Da valutare") || value.equals("0")) {
                                valutazione = 0;
                            } else {
                                valutazione = Integer.parseInt(value);
                                if (valutazione < 0 || valutazione > 5) {
                                    System.err.println("La valutazione deve essere tra 0 e 5, trovato: " + valutazione);
                                    return null;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("La valutazione deve essere un numero intero tra 0 e 5 o 'Da valutare', trovato: " + value);
                            return null;
                        }
                        break;
                    case "statoVisione":
                        try {
                            statoVisione = StatoVisione.valueOf(value);
                        } catch (IllegalArgumentException e) {
                            // Prova a convertire usando la descrizione
                            try {
                                statoVisione = StatoVisione.fromString(value);
                            } catch (IllegalArgumentException ex) {
                                System.err.println("Stato visione non valido: " + value);
                                return null;
                            }
                        }
                        break;
                }
            }
        }

        Film film;
        try {
            film = new Film(titolo, regista, annoUscita, genere, valutazione, statoVisione);
        } catch (IllegalArgumentException e) {
            System.err.println("JSON film non valido: " + jsonObject);
            System.err.println("Errore nella creazione del film: " + e.getMessage());
            return null;
        }

        return film;
    }

    /**
     * Aggiunge escape characters per caratteri speciali nel JSON.
     *
     * @param text Testo da escapare
     * @return Testo escapato per JSON
     */
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
