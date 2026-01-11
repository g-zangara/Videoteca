import view.VideotecaView;

/**
 * Classe principale dell'applicazione di gestione di una videoteca personale.
 * Avvia l'applicazione creando l'interfaccia utente.
 */
public class Videoteca {

    /**
     * Metodo principale che avvia l'applicazione.
     */
    public static void main(String[] args) {
        // Utilizza SwingUtilities.invokeLater per garantire che l'interfaccia utente
        // venga creata nel thread di eventi Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Crea e visualizza l'interfaccia utente
            VideotecaView view = new VideotecaView();
            view.setVisible(true);
        });
    }
}
