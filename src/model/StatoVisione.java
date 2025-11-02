package model;

/**
 * Enumerazione che rappresenta i possibili stati di visione di un film.
 * Definisce i tre stati possibili: visto, in visione, da vedere.
 */
public enum StatoVisione {
    DA_VEDERE("Da vedere"),
    IN_VISIONE("In visione"),
    VISTO("Visto");

    private final String descrizione;

    /**
     * Costruttore dell'enumerazione.
     *
     * @param descrizione Descrizione testuale dello stato di visione
     */
    StatoVisione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Ottiene la descrizione testuale dello stato di visione.
     *
     * @return Stringa rappresentante lo stato visione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Converte una stringa nella corrispondente enumerazione StatoVisione.
     *
     * @param descrizione Stringa da convertire
     * @return StatoVisione corrispondente
     * @throws IllegalArgumentException se la stringa non corrisponde a nessuno stato valido
     */
    public static StatoVisione fromString(String descrizione) {
        for (StatoVisione stato : StatoVisione.values()) {
            if (stato.descrizione.equalsIgnoreCase(descrizione)) {
                return stato;
            }
        }
        throw new IllegalArgumentException("Stato visione non valido: " + descrizione);
    }
}
