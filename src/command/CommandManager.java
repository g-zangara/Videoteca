package command;

import java.util.Stack;

/**
 * Gestore dei comandi che implementa funzionalità di undo e redo.
 * Mantiene due stack: uno per le operazioni da annullare (undo) e uno per quelle da ripristinare (redo).
 */
public class CommandManager {

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    /**
     * Costruttore del gestore comandi.
     * Inizializza gli stack di undo e redo.
     */
    public CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Esegue un comando e lo aggiunge allo stack di undo.
     * Quando viene eseguito un nuovo comando, lo stack di redo viene svuotato.
     *
     * @param command Comando da eseguire
     */
    public boolean executeCommand(Command command) {
        boolean result = command.execute();
        if(result){
            undoStack.push(command);
            redoStack.clear();
            return true;
        }
        return false;
    }

    /**
     * Annulla l'ultimo comando eseguito.
     * Sposta il comando dallo stack di undo a quello di redo.
     *
     * @return true se l'operazione di undo è stata eseguita, false se non ci sono comandi da annullare
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }

        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
        return true;
    }

    /**
     * Ripristina l'ultimo comando annullato.
     * Sposta il comando dallo stack di redo a quello di undo.
     *
     * @return true se l'operazione di redo è stata eseguita, false se non ci sono comandi da ripristinare
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            return false;
        }

        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
        return true;
    }

    /**
     * Verifica se è possibile eseguire un undo.
     *
     * @return true se ci sono comandi nello stack di undo, false altrimenti
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Verifica se è possibile eseguire un redo.
     *
     * @return true se ci sono comandi nello stack di redo, false altrimenti
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Ottiene la descrizione dell'ultimo comando eseguito.
     *
     * @return Descrizione dell'ultimo comando o null se non ci sono comandi
     */
    public String getUndoDescription() {
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.peek().getDescription();
    }

    /**
     * Ottiene la descrizione dell'ultimo comando annullato.
     *
     * @return Descrizione dell'ultimo comando annullato o null se non ci sono comandi
     */
    public String getRedoDescription() {
        if (redoStack.isEmpty()) {
            return null;
        }
        return redoStack.peek().getDescription();
    }

    /**
     * Svuota gli stack di undo e redo.
     * Usato quando vengono eseguite operazioni che rendono obsoleti i comandi precedenti,
     * come "Pulisci Videoteca".
     */
    public void clearStacks() {
        undoStack.clear();
        redoStack.clear();
    }

}
