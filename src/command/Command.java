package command;

/**
 * Interfaccia Command che definisce le operazioni base del pattern Command.
 * Ogni comando concreto deve implementare i metodi execute() e undo().
 */
public interface Command {

    /**
     * Esegue il comando.
     */
    boolean execute();

    /**
     * Annulla l'esecuzione del comando.
     */
    void undo();

    /**
     * Restituisce una descrizione del comando eseguito.
     *
     * @return Descrizione testuale del comando
     */
    String getDescription();

}
