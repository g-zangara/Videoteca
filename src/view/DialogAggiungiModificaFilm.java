package view;

import model.Film;
import model.StatoVisione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Finestra di dialogo per aggiungere o modificare un film.
 */
public class DialogAggiungiModificaFilm extends JDialog {

    private JTextField campoTitolo;
    private JTextField campoRegista;
    private JTextField campoAnnoUscita;
    private JTextField campoGenere;
    private JComboBox<String> comboValutazione;
    private JComboBox<String> comboStatoVisione;

    private boolean confermato = false;

    /**
     * Costruttore per la finestra di dialogo.
     *
     * @param parent Frame genitore
     * @param film   Film da modificare, o null se si sta aggiungendo un nuovo film
     */
    public DialogAggiungiModificaFilm(JFrame parent, Film film) {
        super(parent, film == null ? "Aggiungi Film" : "Modifica Film", true);

        initUI(film);
    }

    /**
     * Inizializza l'interfaccia utente del dialogo.
     *
     * @param film Film da modificare, o null se si sta aggiungendo un nuovo film
     */
    private void initUI(Film film) {
        // Impostazioni base della finestra
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Layout principale
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Pannello per i campi di input
        JPanel panelCampi = new JPanel(new GridLayout(6, 2, 10, 10));

        // Campi di input
        campoTitolo = new JTextField();
        campoRegista = new JTextField();
        campoAnnoUscita = new JTextField();
        campoGenere = new JTextField();

        // Valutazione con "da valutare" come opzione
        comboValutazione = new JComboBox<>(new String[]{"Da valutare", "1", "2", "3", "4", "5"});

        comboStatoVisione = new JComboBox<>();
        for (StatoVisione stato : StatoVisione.values()) {
            comboStatoVisione.addItem(stato.getDescrizione());
        }

        // Aggiungi componenti al pannello
        panelCampi.add(new JLabel("Titolo:"));
        panelCampi.add(campoTitolo);
        panelCampi.add(new JLabel("Regista:"));
        panelCampi.add(campoRegista);
        panelCampi.add(new JLabel("Anno di Uscita:"));
        panelCampi.add(campoAnnoUscita);
        panelCampi.add(new JLabel("Genere:"));
        panelCampi.add(campoGenere);
        panelCampi.add(new JLabel("Valutazione:"));
        panelCampi.add(comboValutazione);
        panelCampi.add(new JLabel("Stato Visione:"));
        panelCampi.add(comboStatoVisione);

        // Pannello per i pulsanti
        JPanel panelPulsanti = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        JButton btnConferma = new JButton(film == null ? "Aggiungi" : "Modifica");
        btnConferma.addActionListener(e -> {
            if (validaCampi()) {
                confermato = true;
                dispose();
            }
        });

        panelPulsanti.add(btnAnnulla);
        panelPulsanti.add(btnConferma);

        // Aggiungi i pannelli al layout principale
        contentPane.add(panelCampi, BorderLayout.CENTER);
        contentPane.add(panelPulsanti, BorderLayout.SOUTH);

        // Se stiamo modificando un film, popola i campi con i suoi dati
        if (film != null) {
            campoTitolo.setText(film.getTitolo());
            campoRegista.setText(film.getRegista());
            campoAnnoUscita.setText(film.getAnnoUscita());
            //campoAnnoUscita.setEnabled(false);
            //campoAnnoUscita.setToolTipText("Non si può modificare l'anno di uscita di un film esistente!");
            campoGenere.setText(film.getGenere());

            // Imposta la valutazione corretta
            if (film.getValutazione() == 0) {
                comboValutazione.setSelectedItem("Da valutare");
            } else {
                comboValutazione.setSelectedItem(String.valueOf(film.getValutazione()));
            }

            comboStatoVisione.setSelectedItem(film.getStatoVisioneAsString());
        }
    }

    /**
     * Valida i campi di input.
     *
     * @return true se tutti i campi sono validi, false altrimenti
     */
    private boolean validaCampi() {
        // Validazione titolo
        if (campoTitolo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Il titolo non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoTitolo.requestFocus();
            return false;
        }

        // Validazione regista
        if (campoRegista.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Il regista non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoRegista.requestFocus();
            return false;
        }

        // Validazione anno
        String anno = campoAnnoUscita.getText().trim();
        if (anno.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "L'anno di uscita non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoAnnoUscita.requestFocus();
            return false;
        }

        if (!Pattern.matches("^[0-9]{4}$", anno)) {
            JOptionPane.showMessageDialog(this,
                    "L'anno di uscita deve contenere solo numeri e deve essere nel formato aaaa.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoAnnoUscita.requestFocus();
            return false;
        }

        // Validazione genere
        if (campoGenere.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Il genere non può essere vuoto.",
                    "Errore di validazione",
                    JOptionPane.ERROR_MESSAGE);
            campoGenere.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Verifica se l'utente ha confermato l'operazione.
     *
     * @return true se l'utente ha confermato, false altrimenti
     */
    public boolean isConfermato() {
        return confermato;
    }

    /**
     * Ottiene il titolo inserito.
     *
     * @return Titolo del film
     */
    public String getTitolo() {
        return campoTitolo.getText().trim();
    }

    /**
     * Ottiene il regista inserito.
     *
     * @return Regista del film
     */
    public String getRegista() {
        return campoRegista.getText().trim();
    }

    /**
     * Ottiene l'anno di uscita inserito.
     *
     * @return Anno di uscita del film
     */
    public String getAnnoUscita() {
        return campoAnnoUscita.getText().trim();
    }

    /**
     * Ottiene il genere inserito.
     *
     * @return Genere del film
     */
    public String getGenere() {
        return campoGenere.getText().trim();
    }

    /**
     * Ottiene la valutazione selezionata.
     *
     * @return Valutazione del film (0 = da valutare, 1-5 = stelle)
     */
    public int getValutazione() {
        String val = (String) comboValutazione.getSelectedItem();
        if ("Da valutare".equals(val)) {
            return 0;
        } else {
            try {
                assert val != null;
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return 0; // Default a "da valutare" in caso di errore, in teoria non dovrebbe mai arrivare qui
            }
        }
    }

    /**
     * Ottiene lo stato di visione selezionato.
     *
     * @return Stato visione del film
     */
    public StatoVisione getStatoVisione() {
        String statoString = (String) comboStatoVisione.getSelectedItem();
        return StatoVisione.fromString(statoString);
    }
}
