package test.command;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import command.Command;
import command.CommandManager;

/**
 * Test unitari per la classe CommandManager.
 * Verifica il corretto funzionamento delle operazioni di undo e redo.
 */
public class CommandManagerTest {

    private CommandManager commandManager;
    private TestCommand command1;
    private TestCommand command2;

    /**
     * Classe interna di test che implementa l'interfaccia Command.
     * Utilizzata per testare il CommandManager.
     */
    private static class TestCommand implements Command {
        private boolean executed;
        private final String description;

        public TestCommand(String description) {
            this.description = description;
            this.executed = false;
        }

        @Override
        public boolean execute() {
            executed = true;
            return true;
        }

        @Override
        public void undo() {
            executed = false;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public boolean isExecuted() {
            return executed;
        }
    }

    @BeforeEach
    public void setUp() {
        commandManager = new CommandManager();
        command1 = new TestCommand("Comando di test 1");
        command2 = new TestCommand("Comando di test 2");
    }

    @Test
    public void testExecuteCommand() {
        commandManager.executeCommand(command1);
        assertTrue(command1.isExecuted());
        assertTrue(commandManager.canUndo());
        assertFalse(commandManager.canRedo());
        assertEquals(command1.getDescription(), commandManager.getUndoDescription());
    }

    @Test
    public void testUndo() {
        // Inizialmente non ci sono comandi da annullare
        assertFalse(commandManager.canUndo());
        assertFalse(commandManager.undo());

        // Esegui un comando e poi annullalo
        commandManager.executeCommand(command1);
        assertTrue(command1.isExecuted());
        assertTrue(commandManager.canUndo());
        assertTrue(commandManager.undo());
        assertFalse(command1.isExecuted());

        // Dopo l'undo, dovrebbe essere possibile eseguire redo
        assertFalse(commandManager.canUndo());
        assertTrue(commandManager.canRedo());
        assertEquals(command1.getDescription(), commandManager.getRedoDescription());
    }

    @Test
    public void testRedo() {
        // Inizialmente non ci sono comandi da ripristinare
        assertFalse(commandManager.canRedo());
        assertFalse(commandManager.redo());

        // Esegui un comando, annullalo e poi ripristinalo
        commandManager.executeCommand(command1);
        assertTrue(command1.isExecuted());
        assertTrue(commandManager.undo());
        assertFalse(command1.isExecuted());
        assertTrue(commandManager.redo());
        assertTrue(command1.isExecuted());

        // Dopo il redo, dovrebbe essere possibile eseguire undo ma non redo
        assertTrue(commandManager.canUndo());
        assertFalse(commandManager.canRedo());
    }

    @Test
    public void testMultipleUndoRedo() {
        // Esegui due comandi
        commandManager.executeCommand(command1);
        commandManager.executeCommand(command2);

        // Verifica che entrambi siano stati eseguiti
        assertTrue(command1.isExecuted());
        assertTrue(command2.isExecuted());

        // Annulla il comando più recente (command2)
        assertTrue(commandManager.undo());
        assertTrue(command1.isExecuted());
        assertFalse(command2.isExecuted());

        // Annulla anche il primo comando
        assertTrue(commandManager.undo());
        assertFalse(command1.isExecuted());
        assertFalse(command2.isExecuted());

        // Non ci sono più comandi da annullare
        assertFalse(commandManager.undo());

        // Ripristina i comandi nell'ordine inverso
        assertTrue(commandManager.redo()); // Ripristina command1
        assertTrue(command1.isExecuted());
        assertFalse(command2.isExecuted());

        assertTrue(commandManager.redo()); // Ripristina command2
        assertTrue(command1.isExecuted());
        assertTrue(command2.isExecuted());

        // Non ci sono più comandi da ripristinare
        assertFalse(commandManager.redo());
    }

    @Test
    public void testClearStacksAfterNewCommand() {
        // Esegui un comando e annullalo
        commandManager.executeCommand(command1);
        assertTrue(commandManager.undo());

        // Verifica che sia possibile fare redo
        assertTrue(commandManager.canRedo());

        // Esegui un nuovo comando
        commandManager.executeCommand(command2);

        // Lo stack di redo dovrebbe essere vuoto
        assertFalse(commandManager.canRedo());
        assertNull(commandManager.getRedoDescription());
    }

    @Test
    public void testGetDescriptions() {
        // Inizialmente non ci sono descrizioni
        assertNull(commandManager.getUndoDescription());
        assertNull(commandManager.getRedoDescription());

        // Esegui un comando
        commandManager.executeCommand(command1);
        assertEquals(command1.getDescription(), commandManager.getUndoDescription());

        // Annulla il comando
        commandManager.undo();
        assertNull(commandManager.getUndoDescription());
        assertEquals(command1.getDescription(), commandManager.getRedoDescription());

        // Ripristina il comando
        commandManager.redo();
        assertEquals(command1.getDescription(), commandManager.getUndoDescription());
        assertNull(commandManager.getRedoDescription());
    }

    @Test
    public void testClearStacks() {
        // Esegui due comandi
        commandManager.executeCommand(command1);
        commandManager.executeCommand(command2);

        // Annulla un comando
        commandManager.undo();

        // Verifica che ci siano comandi negli stack
        assertTrue(commandManager.canUndo());
        assertTrue(commandManager.canRedo());

        // Pulisci gli stack
        commandManager.clearStacks();

        // Verifica che gli stack siano vuoti
        assertFalse(commandManager.canUndo());
        assertFalse(commandManager.canRedo());
        assertNull(commandManager.getUndoDescription());
        assertNull(commandManager.getRedoDescription());
    }
}