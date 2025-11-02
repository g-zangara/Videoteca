package model;

/**
 * Classe che rappresenta lo stato del caricamento e salvataggio dei dati su file.
 * Contiene informazioni sul successo o fallimento dell'operazione
 * e un messaggio descrittivo.
 */
public class Status {

    private final boolean status;
    private final String message;

    public Status(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
