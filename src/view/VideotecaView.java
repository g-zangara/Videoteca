package view;

import controller.FilmController;
import model.Film;
import model.StatoVisione;
import model.Status;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * Classe che implementa l'interfaccia grafica Swing per la gestione della videoteca.
 * Rappresenta la vista nel pattern MVC.
 */
public class VideotecaView extends JFrame {

    // Controller
    private final FilmController controller;

    // Componenti UI
    private JTable tabellaFilms;
    private DefaultTableModel modelloTabella;
    private JTextField campoCerca;
    private JComboBox<String> comboTipoCerca;
    private JComboBox<String> comboGenere;
    private JComboBox<String> comboRegista;
    private JComboBox<String> comboAnnoUscita;
    private JComboBox<String> comboStatoVisione;
    private JComboBox<String> comboValutazione;
    private JComboBox<String> comboOrdinamento;
    private JButton btnAggiungi, btnModifica, btnElimina;
    private JButton btnCerca, btnResetFiltri;
    private JButton btnSalvaJSON, btnSalvaCSV, btnCaricaJSON, btnCaricaCSV;
    private JButton btnPulisciVideoteca;
    private JButton btnInfo;
    private JButton btnUndo, btnRedo;

    /**
     * Costruttore che inizializza la vista.
     */
    public VideotecaView() {
        super("Gestione Videoteca Personale");

        // Inizializza il controller
        controller = new FilmController(this);

        // Inizializza i componenti UI
        initUI();

        // Carica i films iniziali
        controller.caricaFilms();
    }

    /**
     * Inizializza l'interfaccia utente.
     */
    private void initUI() {
        // Impostazioni base della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(1150, 600));
        setLocationRelativeTo(null);

        // Layout principale
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Pannello superiore per ricerca e filtri con scrollbar
        JPanel panelSuperiore = creaPanelSuperiore();

        // Pannello centrale per la tabella dei films
        JPanel panelCentrale = creaPanelCentrale();

        // Pannello inferiore per i pulsanti delle operazioni
        JPanel panelInferiore = creaPanelInferiore();

        add(panelSuperiore, BorderLayout.NORTH);
        add(panelCentrale, BorderLayout.CENTER);
        add(panelInferiore, BorderLayout.SOUTH);
    }

    private JPanel creaPanelSuperiore() {
        JPanel panelSuperiore = new JPanel(new BorderLayout(10, 10));

        // Pannello di ricerca
        JPanel panelRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRicerca.setBorder(BorderFactory.createTitledBorder("Ricerca"));

        campoCerca = new JTextField(15);
        comboTipoCerca = new JComboBox<>(new String[]{"Titolo", "Regista"}); //possibilità di aggiungere anno uscita in futuro
        btnCerca = new JButton("Cerca");
        btnCerca.addActionListener(e -> controller.aggiornaTabella());

        panelRicerca.add(new JLabel("Cerca per:"));
        panelRicerca.add(comboTipoCerca);
        panelRicerca.add(campoCerca);
        panelRicerca.add(btnCerca);

        // Pannello dei filtri
        JPanel panelFiltri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltri.setBorder(BorderFactory.createTitledBorder("Filtri"));

        comboGenere = new JComboBox<>();
        comboGenere.addItem("Tutti");
        comboGenere.addActionListener(e -> controller.aggiornaTabella());

        comboRegista = new JComboBox<>();
        comboRegista.addItem("Tutti");
        comboRegista.addActionListener(e -> controller.aggiornaTabella());

        comboAnnoUscita = new JComboBox<>();
        comboAnnoUscita.addItem("Tutti");
        comboAnnoUscita.addActionListener(e -> controller.aggiornaTabella());

        comboStatoVisione = new JComboBox<>(new String[]{"Tutti", "Visto", "In visione", "Da vedere"});
        comboStatoVisione.addActionListener(e -> controller.aggiornaTabella());

        comboValutazione = new JComboBox<>(new String[]{"Tutti", "Da valutare", "1", "2", "3", "4", "5"});
        comboValutazione.setSelectedIndex(0);
        comboValutazione.addActionListener(e -> controller.aggiornaTabella());

        btnResetFiltri = new JButton("Reset Filtri");
        btnResetFiltri.addActionListener(e -> resetFiltri());

        panelFiltri.add(new JLabel("Genere:"));
        panelFiltri.add(comboGenere);
        panelFiltri.add(new JLabel("Regista:"));
        panelFiltri.add(comboRegista);
        panelFiltri.add(new JLabel("Anno di Uscita:"));
        panelFiltri.add(comboAnnoUscita);
        panelFiltri.add(new JLabel("Stato:"));
        panelFiltri.add(comboStatoVisione);
        panelFiltri.add(new JLabel("Valutazione:"));
        panelFiltri.add(comboValutazione);
        panelFiltri.add(btnResetFiltri);

        // Pannello di ordinamento
        JPanel panelOrdinamento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOrdinamento.setBorder(BorderFactory.createTitledBorder("Ordinamento"));

        comboOrdinamento = new JComboBox<>(new String[]{
                "Predefinito", "Titolo (A-Z)", "Titolo (Z-A)",
                "Regista (A-Z)", "Regista (Z-A)", "Anno di Uscita (ASC)", "Anno di Uscita (DESC)",
                "Valutazione (1-5)", "Valutazione (5-1)"
        });
        comboOrdinamento.addActionListener(e -> controller.aggiornaTabella());

        panelOrdinamento.add(new JLabel("Ordina per:"));
        panelOrdinamento.add(comboOrdinamento);

        // Pannello che contiene filtri e ordinamento con scrollbar
        JPanel panelFiltriOrdinamento = new JPanel();
        panelFiltriOrdinamento.setLayout(new BoxLayout(panelFiltriOrdinamento, BoxLayout.Y_AXIS));
        panelFiltriOrdinamento.add(panelFiltri);
        panelFiltriOrdinamento.add(panelOrdinamento);

        // Pannello superiore con scrollbar
        JScrollPane scrollPanelSuperiore = new JScrollPane(panelFiltriOrdinamento);
        scrollPanelSuperiore.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanelSuperiore.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanelSuperiore.setBorder(null);

        // Aggiungi i pannelli al pannello superiore
        panelSuperiore.add(panelRicerca, BorderLayout.NORTH);
        panelSuperiore.add(scrollPanelSuperiore, BorderLayout.CENTER);

        return panelSuperiore;
    }

    private JPanel creaPanelCentrale() {
        JPanel panelCentrale = new JPanel(new BorderLayout());

        // Crea la tabella con il modello dati
        String[] colonne = {"Titolo", "Regista", "Anno di Uscita", "Genere", "Valutazione", "Stato Visione"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non permette la modifica diretta nella tabella
            }
        };

        tabellaFilms = new JTable(modelloTabella);
        tabellaFilms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaFilms.getTableHeader().setReorderingAllowed(false);

        // Imposta le dimensioni preferite delle colonne
        tabellaFilms.getColumnModel().getColumn(0).setPreferredWidth(200); // Titolo
        tabellaFilms.getColumnModel().getColumn(1).setPreferredWidth(150); // Regista
        tabellaFilms.getColumnModel().getColumn(2).setPreferredWidth(100); // Anno di Uscita
        tabellaFilms.getColumnModel().getColumn(3).setPreferredWidth(100); // Genere
        tabellaFilms.getColumnModel().getColumn(4).setPreferredWidth(80);  // Valutazione
        tabellaFilms.getColumnModel().getColumn(5).setPreferredWidth(100); // Stato Visione

        JScrollPane scrollPane = new JScrollPane(tabellaFilms);
        panelCentrale.add(scrollPane, BorderLayout.CENTER);

        return panelCentrale;
    }

    private JPanel creaPanelInferiore() {
        JPanel panelInferiore = new JPanel(new BorderLayout());

        // Pulsanti per le operazioni sui films
        JPanel panelOperazioni = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

        btnAggiungi = new JButton("Aggiungi Film");
        btnAggiungi.addActionListener(e -> mostraDialogAggiungiFilm());

        btnModifica = new JButton("Modifica Film");
        btnModifica.addActionListener(e -> mostraDialogModificaFilm());

        btnElimina = new JButton("Elimina Film");
        btnElimina.addActionListener(e -> eliminaFilmSelezionato());

        btnUndo = new JButton("Undo");
        btnUndo.setToolTipText("Annulla l'ultima operazione");
        btnUndo.addActionListener(e -> controller.undo());
        btnUndo.setEnabled(false);

        btnRedo = new JButton("Redo");
        btnRedo.setToolTipText("Ripristina l'ultima operazione annullata");
        btnRedo.addActionListener(e -> controller.redo());
        btnRedo.setEnabled(false);

        panelOperazioni.add(btnAggiungi);
        panelOperazioni.add(btnModifica);
        panelOperazioni.add(btnElimina);
        panelOperazioni.add(btnUndo);
        panelOperazioni.add(btnRedo);

        // Pulsante info
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        btnInfo = new JButton("Info");
        btnInfo.addActionListener(e -> mostraInfoDialog());
        panelCentro.add(btnInfo);

        // Pulsanti per il salvataggio/caricamento
        JPanel panelPersistenza = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));

        btnSalvaJSON = new JButton("Salva JSON");
        btnSalvaJSON.addActionListener(e -> salvaFile("JSON"));

        btnSalvaCSV = new JButton("Salva CSV");
        btnSalvaCSV.addActionListener(e -> salvaFile("CSV"));

        btnCaricaJSON = new JButton("Carica JSON");
        btnCaricaJSON.addActionListener(e -> caricaFile("JSON"));

        btnCaricaCSV = new JButton("Carica CSV");
        btnCaricaCSV.addActionListener(e -> caricaFile("CSV"));

        //Pulsante per pulire la videoteca
        btnPulisciVideoteca = new JButton("Pulisci Videoteca");
        btnPulisciVideoteca.addActionListener(e -> pulisciVideoteca());
        panelPersistenza.add(btnSalvaJSON);
        panelPersistenza.add(btnSalvaCSV);
        panelPersistenza.add(btnCaricaJSON);
        panelPersistenza.add(btnCaricaCSV);
        panelPersistenza.add(btnPulisciVideoteca);

        // Contenitore per i pulsanti
        JPanel bottonePulsantiContainer = new JPanel();
        bottonePulsantiContainer.setLayout(new BoxLayout(bottonePulsantiContainer, BoxLayout.X_AXIS));
        bottonePulsantiContainer.add(panelOperazioni);
        bottonePulsantiContainer.add(Box.createHorizontalGlue());
        bottonePulsantiContainer.add(panelCentro);
        bottonePulsantiContainer.add(Box.createHorizontalGlue());
        bottonePulsantiContainer.add(panelPersistenza);
        bottonePulsantiContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        // Scroll panel per i pulsanti
        JScrollPane scrollPanelInferiore = new JScrollPane(bottonePulsantiContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanelInferiore.setBorder(null);
        scrollPanelInferiore.getHorizontalScrollBar().setUnitIncrement(15);

        panelInferiore.add(scrollPanelInferiore, BorderLayout.CENTER);

        return panelInferiore;
    }

    /**
     * Aggiorna lo stato dei pulsanti Undo e Redo in base alla disponibilità delle operazioni.
     *
     * @param canUndo         true se l'operazione di undo è disponibile
     * @param canRedo         true se l'operazione di redo è disponibile
     * @param undoDescription descrizione dell'operazione di undo
     * @param redoDescription descrizione dell'operazione di redo
     */
    public void aggiornaStatoPulsantiUndoRedo(boolean canUndo, boolean canRedo,
                                              String undoDescription, String redoDescription) {
        btnUndo.setEnabled(canUndo);
        btnRedo.setEnabled(canRedo);

        // Aggiorna i tooltip con la descrizione delle azioni
        if (canUndo) {
            btnUndo.setToolTipText("Annulla: " + undoDescription);
        } else {
            btnUndo.setToolTipText("Nessuna operazione da annullare");
        }

        if (canRedo) {
            btnRedo.setToolTipText("Ripristina: " + redoDescription);
        } else {
            btnRedo.setToolTipText("Nessuna operazione da ripristinare");
        }
    }

    /**
     * Mostra la finestra di dialogo delle informazioni sull'applicazione.
     */
    private void mostraInfoDialog() {
        String infoText = "Nome: Gestione Videoteca Personale\n\n" +
                "Versione: v1.0\n\n" +
                "Descrizione:\n" +
                "- Aggiungi, modifica e elimina i tuoi films\n" +
                "- Cerca films per titolo o regista\n" +
                "- Filtra per genere, regista, anno, stato di visione, valutazione\n" +
                "- Ordina i films secondo diversi criteri\n" +
                "- Funzionalità Undo/Redo per annullare o ripristinare le operazioni\n" +
                "- Salva e carica la tua videoteca in formato JSON o CSV\n\n" +
                "Questo è un Progetto Demo Java Vanilla";

        JOptionPane.showMessageDialog(this,
                infoText,
                "Informazioni sull'applicazione",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Aggiorna la tabella con i films forniti.
     *
     * @param films Lista di films da visualizzare
     */
    public void aggiornaTabella(List<Film> films) {
        // Pulisci la tabella
        modelloTabella.setRowCount(0);

        // Aggiungi le righe per ogni film
        for (Film film : films) {
            Vector<Object> riga = new Vector<>();
            riga.add(film.getTitolo());
            riga.add(film.getRegista());
            riga.add(film.getAnnoUscita());
            riga.add(film.getGenere());

            // Visualizza la valutazione come stringa ("da valutare" o stelle)
            riga.add(film.getValutazioneAsString());

            riga.add(film.getStatoVisioneAsString());

            modelloTabella.addRow(riga);
        }
    }

    /**
     * Aggiorna la combo box dei generi con i generi unici disponibili.
     *
     * @param generi Lista di generi unici
     */
    public void aggiornaComboBoxGeneri(List<String> generi) {
        String genereSelezionato = (String) comboGenere.getSelectedItem();

        comboGenere.removeAllItems();
        comboGenere.addItem("Tutti");

        for (String genere : generi) {
            comboGenere.addItem(genere);
        }

        // Ripristina la selezione precedente se possibile
        if (genereSelezionato != null) {
            comboGenere.setSelectedItem(genereSelezionato);
        } else {
            comboGenere.setSelectedIndex(0);
        }
    }

    /**
     * Aggiorna la combo box dei registi con i registi unici disponibili.
     *
     * @param registi Lista di registi unici
     */
    public void aggiornaComboBoxRegisti(List<String> registi) {
        String registaSelezionato = (String) comboRegista.getSelectedItem();

        comboRegista.removeAllItems();
        comboRegista.addItem("Tutti");

        for (String regista : registi) {
            comboRegista.addItem(regista);
        }

        // Ripristina la selezione precedente se possibile
        if (registaSelezionato != null) {
            comboRegista.setSelectedItem(registaSelezionato);
        } else {
            comboRegista.setSelectedIndex(0);
        }
    }

    /**
     * Aggiorna la combo box degli anni di uscita con gli anni unici disponibili.
     *
     * @param anniUscita Lista di anni di uscita unici
     */
    public void aggiornaComboBoxAnnoUscita(List<String> anniUscita) {
        String annoSelezionato = (String) comboAnnoUscita.getSelectedItem();

        comboAnnoUscita.removeAllItems();
        comboAnnoUscita.addItem("Tutti");

        for (String anno : anniUscita) {
            comboAnnoUscita.addItem(anno);
        }

        // Ripristina la selezione precedente se possibile
        if (annoSelezionato != null) {
            comboAnnoUscita.setSelectedItem(annoSelezionato);
        } else {
            comboAnnoUscita.setSelectedIndex(0);
        }
    }

    /**
     * Mostra la finestra di dialogo per aggiungere un nuovo film.
     */
    private void mostraDialogAggiungiFilm() {
        DialogAggiungiModificaFilm dialog = new DialogAggiungiModificaFilm(this, null);
        dialog.setVisible(true);

        if (dialog.isConfermato()) {
            String titolo = dialog.getTitolo();
            String regista = dialog.getRegista();
            String annoUscita = dialog.getAnnoUscita();
            String genere = dialog.getGenere();
            int valutazione = dialog.getValutazione();
            StatoVisione statoVisione = dialog.getStatoVisione();

            boolean successo = controller.aggiungiFilm(titolo, regista, annoUscita, genere, valutazione, statoVisione);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Film aggiunto con successo!", "Operazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile aggiungere il film.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mostra la finestra di dialogo per modificare un film esistente.
     */
    private void mostraDialogModificaFilm() {
        Film filmSelezionato = getFilmSelezionato();

        if (filmSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un film da modificare.",
                    "Nessun film selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DialogAggiungiModificaFilm dialog = new DialogAggiungiModificaFilm(this, filmSelezionato);
        dialog.setVisible(true);

        if (dialog.isConfermato()) {
            String titolo = dialog.getTitolo();
            String regista = dialog.getRegista();
            String annoUscita = dialog.getAnnoUscita();
            String genere = dialog.getGenere();
            int valutazione = dialog.getValutazione();
            StatoVisione statoVisione = dialog.getStatoVisione();

            try {
                boolean successo = controller.modificaFilm(filmSelezionato, titolo, regista, annoUscita, genere, valutazione, statoVisione);

                if (successo) {
                    JOptionPane.showMessageDialog(this, "Film modificato con successo!", "Operazione completata",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Impossibile modificare il film, verifica che non sia già presente.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException iae) {
                String[] parts = iae.getMessage().split(" - ", 2);
                switch (parts[0]) {
                    case "1":
                        JOptionPane.showMessageDialog(this, parts[1],
                                "Errore: Film già presente", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "2":
                        JOptionPane.showMessageDialog(this, parts[1],
                                "Warning: Modifica", JOptionPane.WARNING_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Impossibile modificare il film.",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            }
        }
    }

    /**
     * Elimina il film selezionato dalla tabella.
     */
    private void eliminaFilmSelezionato() {
        Film filmSelezionato = getFilmSelezionato();

        if (filmSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un film da eliminare.",
                    "Nessun film selezionato", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler eliminare il film \"" + filmSelezionato.getTitolo() + "\"?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            boolean successo = controller.eliminaFilm(filmSelezionato);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Film eliminato con successo!", "Operazione completata",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Impossibile eliminare il film.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Ottiene il film selezionato nella tabella.
     *
     * @return Film selezionato o null se nessun film è selezionato
     */
    private Film getFilmSelezionato() {
        int rigaSelezionata = tabellaFilms.getSelectedRow();

        if (rigaSelezionata == -1) {
            return null;
        }

        String titolo = (String) tabellaFilms.getValueAt(rigaSelezionata, 0);
        String regista = (String) tabellaFilms.getValueAt(rigaSelezionata, 1);
        String annoUscita = (String) tabellaFilms.getValueAt(rigaSelezionata, 2);
        String genere = (String) tabellaFilms.getValueAt(rigaSelezionata, 3);

        // Gestione della valutazione come stringa
        String valutazioneString = (String) tabellaFilms.getValueAt(rigaSelezionata, 4);
        int valutazione;
        if ("Da valutare".equalsIgnoreCase(valutazioneString)) {
            valutazione = 0;
        } else {
            try {
                valutazione = Integer.parseInt(valutazioneString);
            } catch (NumberFormatException e) {
                valutazione = 0; // Default a "da valutare" in caso di errore
            }
        }

        String statoVisioneString = (String) tabellaFilms.getValueAt(rigaSelezionata, 5);

        StatoVisione statoVisione;
        try {
            statoVisione = StatoVisione.fromString(statoVisioneString);
        } catch (IllegalArgumentException e) {
            statoVisione = StatoVisione.DA_VEDERE;
        }

        return new Film(titolo, regista, annoUscita, genere, valutazione, statoVisione);
    }

    /**
     * Resetta tutti i filtri ai valori predefiniti.
     */
    private void resetFiltri() {
        campoCerca.setText("");
        comboTipoCerca.setSelectedIndex(0);
        comboGenere.setSelectedIndex(0);
        comboRegista.setSelectedIndex(0);
        comboAnnoUscita.setSelectedIndex(0);
        comboStatoVisione.setSelectedIndex(0);
        comboValutazione.setSelectedIndex(0);
        comboOrdinamento.setSelectedIndex(0);

        controller.caricaFilms();
    }

    /**
     * Mostra un dialogo per salvare la videoteca in un file.
     *
     * @param formato Formato del file (JSON o CSV)
     */
    private void salvaFile(String formato) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva videoteca in " + formato);

        String estensione = formato.toLowerCase();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                formato.toUpperCase() + " Files (*." + estensione + ")", estensione);
        fileChooser.setFileFilter(filtro);

        int risultato = fileChooser.showSaveDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = fileChooser.getSelectedFile();
            String percorso = fileSelezionato.getAbsolutePath();

            // Aggiungi l'estensione se mancante
            if (!percorso.toLowerCase().endsWith("." + estensione)) {
                percorso += "." + estensione;
            }

            Status esito = controller.salvaVideoteca(percorso, formato);
            if (esito.isSuccess())
                JOptionPane.showMessageDialog(this, esito.getMessage(),
                        "Salvataggio completato", JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, esito.getMessage(),
                        "Errore", JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * Mostra un dialogo per caricare la videoteca da un file.
     *
     * @param formato Formato del file (JSON o CSV)
     */
    private void caricaFile(String formato) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carica videoteca da " + formato);

        String estensione = formato.toLowerCase();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                formato.toUpperCase() + " Files (*." + estensione + ")", estensione);
        fileChooser.setFileFilter(filtro);

        int risultato = fileChooser.showOpenDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File fileSelezionato = fileChooser.getSelectedFile();
            Status esito = controller.caricaVideoteca(fileSelezionato.getAbsolutePath(), formato);
            if (esito.isSuccess())
                JOptionPane.showMessageDialog(this, esito.getMessage(),
                        "Caricamento completato", JOptionPane.INFORMATION_MESSAGE);
            else
                mostraErroreConScrollSeNecessario(esito.getMessage());
        }
    }

    /**
     * Mostra un messaggio di errore in un JOptionPane con scroll se il messaggio è lungo.
     *
     * @param messaggio Messaggio di errore da mostrare
     */
    private void mostraErroreConScrollSeNecessario(String messaggio) {
        if (messaggio != null && messaggio.length() > 200) {
            JTextArea textArea = new JTextArea(messaggio);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Errore di caricamento", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, messaggio, "Errore di caricamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ottiene il genere selezionato nella combo box.
     *
     * @return Genere selezionato o "Tutti" se nessuno è selezionato
     */
    public String getGenereSelezionato() {
        return (String) comboGenere.getSelectedItem();
    }


    /**
     * Ottiene il tipo di ricerca selezionato nella combo box.
     *
     * @return Tipo di ricerca selezionato
     */
    public String getCampoTipoCerca() {
        return (String) comboTipoCerca.getSelectedItem();
    }

    /**
     * Ottiene il testo inserito nel campo di ricerca.
     *
     * @return Testo di ricerca
     */
    public String getCampoCerca() {
        return campoCerca.getText();
    }

    /**
     * Ottiene il regista selezionato nella combo box.
     *
     * @return Regista selezionato o "Tutti" se nessuno è selezionato
     */
    public String getRegistaSelezionato() {
        return (String) comboRegista.getSelectedItem();
    }

    /**
     * Ottiene l'anno di uscita selezionato nella combo box.
     *
     * @return Anno di uscita selezionato o "Tutti" se nessuno è selezionato
     */
    public String getAnnoUscitaSelezionato() {
        return (String) comboAnnoUscita.getSelectedItem();
    }

    /**
     * Ottiene lo stato di visione selezionato nella combo box.
     *
     * @return Stato visione selezionato o "Tutti" se nessuno è selezionato
     */
    public String getStatoVisioneSelezionato() {
        return (String) comboStatoVisione.getSelectedItem();
    }

    /**
     * Ottiene la valutazione selezionata nella combo box.
     *
     * @return Valutazione selezionata o -1 se "Tutti" è selezionato (nessun filtro),
     * 0 se "da valutare" è selezionato, altrimenti il valore numerico (1-5)
     */
    public int getValutazioneSelezionata() {
        String valutazione = (String) comboValutazione.getSelectedItem();
        if (valutazione == null || "Tutti".equals(valutazione)) {
            return -1; // Nessun filtro
        } else if ("Da valutare".equals(valutazione)) {
            return 0; // Filtra per "da valutare"
        } else {
            try {
                return Integer.parseInt(valutazione);
            } catch (NumberFormatException e) {
                return -1; // In caso di errore, non applica filtro
            }
        }
    }

    /**
     * Ottiene l'ordinamento selezionato nella combo box.
     *
     * @return Ordinamento selezionato
     */
    public String getOrdinamentoSelezionato() {
        return (String) comboOrdinamento.getSelectedItem();
    }

    /**
     * Pulisce la videoteca dopo conferma dell'utente.
     * Rimuove tutti i films dalla videoteca.
     */
    private void pulisciVideoteca() {
        int conferma = JOptionPane.showConfirmDialog(this,
                "Sei sicuro di voler pulire la videoteca? Tutti i films saranno rimossi.\n" +
                        "Questa operazione non può essere annullata e cancellerà anche la cronologia di Undo/Redo.",
                "Conferma pulizia videoteca",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (conferma == JOptionPane.YES_OPTION) {
            controller.pulisciVideoteca();
            JOptionPane.showMessageDialog(this,
                    "La videoteca è stata pulita con successo.",
                    "Operazione completata",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
