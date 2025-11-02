package model;

import java.util.Objects;

/**
 * Classe che rappresenta un film nella videoteca personale.
 * Attributi: titolo, regista, anno di uscita, genere, valutazione e stato di visione.
 */
public class Film {
    private String titolo;
    private String regista;
    private String annoUscita;
    private String genere;
    private int valutazione; // 0 = da valutare, 1..5 = stelle
    private StatoVisione statoVisione;

    /**
     * Costruttore completo.
     *
     * @param titolo        Titolo del film
     * @param regista       Regista del film
     * @param annoUscita    Anno di uscita (formato AAAA)
     * @param genere        Genere del film
     * @param valutazione   Valutazione (0..5)
     * @param statoVisione  Stato di visione (visto, in visione, da vedere)
     */
    public Film(String titolo, String regista, String annoUscita, String genere,
                int valutazione, StatoVisione statoVisione) {
        this.titolo = titolo;
        this.regista = regista;
        this.annoUscita = annoUscita;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoVisione = statoVisione;

        if (!FilmValidator.isValid(this)) {
            throw new IllegalArgumentException("I dati del film non sono validi.");
        }
    }

    /**
     * Costruttore vuoto necessario per serializzazione/deserializzazione.
     */
    public Film() {
        this.titolo = "";
        this.regista = "";
        this.annoUscita = "";
        this.genere = "";
        this.valutazione = 0; // Da valutare come default
        this.statoVisione = StatoVisione.DA_VEDERE;
    }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) {
        FilmValidator.validateTitolo(titolo);
        this.titolo = titolo;
    }

    public String getRegista() { return regista; }
    public void setRegista(String regista) {
        FilmValidator.validateRegista(regista);
        this.regista = regista;
    }

    public String getAnnoUscita() { return annoUscita; }
    public void setAnnoUscita(String annoUscita) {
        FilmValidator.validateAnnoUscita(annoUscita);
        this.annoUscita = annoUscita;
    }

    public String getGenere() { return genere; }
    public void setGenere(String genere) {
        FilmValidator.validateGenere(genere);
        this.genere = genere;
    }

    public int getValutazione() { return valutazione; }
    public void setValutazione(int valutazione) {
        FilmValidator.validateValutazione(valutazione);
        this.valutazione = valutazione;
    }

    public StatoVisione getStatoVisione() { return statoVisione; }
    public void setStatoVisione(StatoVisione statoVisione) {
        FilmValidator.validateStatoVisione(statoVisione);
        this.statoVisione = statoVisione;
    }


    /**
     * Restituisce la valutazione come stringa descrittiva.
     * @return "Da valutare" se 0, altrimenti il numero di stelle (1..5) come stringa
     */
    public String getValutazioneAsString() {
        return (valutazione == 0) ? "Da valutare" : String.valueOf(valutazione);
    }

    /**
     * Restituisce lo stato di visione come stringa (delegato all'enum).
     */
    public String getStatoVisioneAsString() {
        return statoVisione.getDescrizione();
    }

    /**
     * Verifica validità dell'istanza.
     */
    public boolean isValid() {
        return FilmValidator.isValid(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        // Identifichiamo un film tramite combinazione: titolo + regista + annoUscita
        return Objects.equals(titolo.toLowerCase(), film.titolo.toLowerCase())
                && Objects.equals(regista.toLowerCase(), film.regista.toLowerCase())
                && Objects.equals(annoUscita, film.annoUscita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo.toLowerCase(), regista.toLowerCase(), annoUscita);
    }

    @Override
    public String toString() {
        return "Film{" +
                "titolo='" + titolo + '\'' +
                ", regista='" + regista + '\'' +
                ", annoUscita='" + annoUscita + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", statoVisione=" + statoVisione +
                '}';
    }
}

/**
 * Utility di validazione per la classe Film.
 *
 */
final class FilmValidator {

    private FilmValidator() {}

    static void validateTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo non può essere vuoto.");
        }
    }

    static void validateRegista(String regista) {
        if (regista == null || regista.trim().isEmpty()) {
            throw new IllegalArgumentException("Il regista non può essere vuoto.");
        }
    }

    static void validateAnnoUscita(String annoUscita) {
        if (annoUscita == null || annoUscita.trim().isEmpty() || !annoUscita.matches("^[0-9]{4}$")) {
            throw new IllegalArgumentException("L'anno di uscita deve essere nel formato AAAA e non può essere vuoto.");
        }
    }

    static void validateGenere(String genere) {
        if (genere == null || genere.trim().isEmpty()) {
            throw new IllegalArgumentException("Il genere non può essere vuoto.");
        }
    }

    static void validateValutazione(int valutazione) {
        if (valutazione < 0 || valutazione > 5) {
            throw new IllegalArgumentException("La valutazione deve essere compresa tra 0 e 5.");
        }
    }

    static void validateStatoVisione(StatoVisione statoVisione) {
        if (statoVisione == null) {
            throw new IllegalArgumentException("Lo stato di visione non può essere nullo.");
        }
    }

    static boolean isValidTitolo(String titolo) {
        return titolo != null && !titolo.trim().isEmpty();
    }

    static boolean isValidRegista(String regista) {
        return regista != null && !regista.trim().isEmpty();
    }

    static boolean isValidAnnoUscita(String annoUscita) {
        return annoUscita != null && !annoUscita.trim().isEmpty()
                && annoUscita.matches("^[0-9]{4}$");
    }

    static boolean isValidGenere(String genere) {
        return genere != null && !genere.trim().isEmpty();
    }

    static boolean isValidValutazione(int valutazione) {
        return valutazione >= 0 && valutazione <= 5;
    }

    static boolean isValidStatoVisione(StatoVisione statoVisione) {
        return statoVisione != null;
    }

    /**
     * Verifica velidità di un Film.
     * @return boolean
     */
    static boolean isValid(Film f) {
        return isValidTitolo(f.getTitolo())
                && isValidRegista(f.getRegista())
                && isValidAnnoUscita(f.getAnnoUscita())
                && isValidGenere(f.getGenere())
                && isValidValutazione(f.getValutazione())
                && isValidStatoVisione(f.getStatoVisione());
    }
}
