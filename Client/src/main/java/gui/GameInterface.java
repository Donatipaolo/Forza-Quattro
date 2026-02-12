package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameInterface extends JFrame {

    private JButton playButton;
    private JButton exitButton;
    private JComboBox<String> languageComboBox;
    private JList<String> playerList;
    private JLabel connectionStatus;
    private JLabel turnStatusLabel; // Etichetta per mostrare il turno
    private JLabel opponentLabel; // Etichetta per mostrare l'avversario
    private JPanel gamePanel;
    private String[][] board; // La griglia di gioco
    private boolean isRedTurn = true; // Determina il turno (rosso o giallo)
    private final int ROWS = 6; // Righe della griglia
    private final int COLS = 7; // Colonne della griglia
    private JButton[] columnButtons; // Pulsanti per le colonne
    private JButton[][] gridButtons; // Pulsanti per la griglia (6x7)
    private String opponentName = "Player2"; // Nome dell'avversario
    private boolean isConnected = true; // Stato della connessione

    public GameInterface() {
        // Imposta il titolo della finestra iniziale (Lobby)
        setTitle("Game Lobby");

        // Imposta layout manager
        setLayout(new BorderLayout());

        // Creazione dei componenti
        playButton = new JButton("Play");
        exitButton = new JButton("Exit");

        // Lista dei giocatori online
        String[] players = {"Player1", "Player2", "Player3"};
        playerList = new JList<>(players);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Etichetta per la connessione al server
        connectionStatus = new JLabel("Connection: Active", JLabel.CENTER);
        connectionStatus.setForeground(Color.GREEN);

        // Etichetta per il turno e l'avversario
        opponentLabel = new JLabel("Opponent: " + opponentName, JLabel.CENTER);
        opponentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        turnStatusLabel = new JLabel("Turn: Red", JLabel.CENTER);
        turnStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnStatusLabel.setForeground(Color.RED);

        // Pannello per i componenti della lobby
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Players Online:"));
        panel.add(new JScrollPane(playerList));
        panel.add(opponentLabel); // Aggiungi etichetta dell'avversario
        panel.add(turnStatusLabel); // Aggiungi etichetta per il turno
        panel.add(connectionStatus); // Connessione solo nella lobby
        panel.add(playButton);
        panel.add(exitButton);

        // Aggiungi il pannello alla finestra
        add(panel, BorderLayout.CENTER);

        // Aggiungi listener ai pulsanti
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });

        // Imposta le dimensioni della finestra e la visibilità
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Centra la finestra
    }

    private void startGame() {
        // Cambia il titolo della finestra a "Game"
        setTitle("Game");

        // Logica per avviare il gioco
        JOptionPane.showMessageDialog(this, "Game Started!");

        // Crea il pannello di gioco
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS));
        gamePanel.setPreferredSize(new Dimension(700, 600));
        board = new String[ROWS][COLS];
        columnButtons = new JButton[COLS];
        gridButtons = new JButton[ROWS][COLS]; // Inizializziamo la matrice per i pulsanti

        // Inizializza la griglia vuota e i pulsanti delle colonne
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = ""; // Inizializza la cella vuota
                gridButtons[row][col] = new JButton();
                gridButtons[row][col].setBackground(Color.WHITE);
                gridButtons[row][col].setPreferredSize(new Dimension(100, 100));
                gridPanel.add(gridButtons[row][col]); // Aggiungi i pulsanti alla griglia
            }
        }

        // Aggiungi pulsanti per le colonne
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new GridLayout(1, COLS));
        for (int i = 0; i < COLS; i++) {
            columnButtons[i] = new JButton("Column " + (i + 1));
            columnButtons[i].setPreferredSize(new Dimension(100, 50));
            int column = i; // Necessario per il corretto utilizzo nel listener
            columnButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    makeMove(column); // Cliccando il pulsante della colonna, si fa una mossa
                }
            });
            columnPanel.add(columnButtons[i]);
        }

        // Aggiungi la griglia e i pulsanti delle colonne al pannello del gioco
        gamePanel.add(gridPanel, BorderLayout.CENTER);
        gamePanel.add(columnPanel, BorderLayout.SOUTH);

        // Aggiungi il pannello del gioco alla finestra
        getContentPane().removeAll(); // Rimuove il pannello della lobby
        add(gamePanel, BorderLayout.CENTER); // Aggiungi la griglia del gioco

        // Aggiungi l'etichetta di stato della connessione nel gioco
        connectionStatus.setText("Connection: Active");
        gamePanel.add(connectionStatus, BorderLayout.NORTH);

        revalidate(); // Rende visibile la griglia
        repaint(); // Rende la finestra aggiornata
    }

    private void makeMove(int col) {
        // Trova la prima riga vuota della colonna
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col].equals("")) {
                // Inserisci la pedina nella griglia
                board[row][col] = isRedTurn ? "Red" : "Yellow";

                // Cambia il colore del bottone
                gridButtons[row][col].setBackground(isRedTurn ? Color.RED : Color.YELLOW);

                // Controlla se c'è una vittoria
                if (checkWin(row, col)) {
                    String winner = isRedTurn ? "Red" : "Yellow";
                    JOptionPane.showMessageDialog(this, winner + " wins!");
                    resetGame();
                    return;
                }

                // Cambia il turno
                isRedTurn = !isRedTurn;
                turnStatusLabel.setText("Turn: " + (isRedTurn ? "Red" : "Yellow"));
                turnStatusLabel.setForeground(isRedTurn ? Color.RED : Color.YELLOW);
                return;
            }
        }

        // Se la colonna è piena, non fare nulla
        JOptionPane.showMessageDialog(this, "Column is full!");
    }

    private boolean checkWin(int row, int col) {
        // Controlla se ci sono 4 pedine consecutive in orizzontale, verticale o diagonale
        return (checkDirection(row, col, 0, 1) || // Orizzontale
                checkDirection(row, col, 1, 0) || // Verticale
                checkDirection(row, col, 1, 1) || // Diagonale principale
                checkDirection(row, col, 1, -1)); // Diagonale secondaria
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol) {
        String player = board[row][col];
        int count = 1;

        // Controlla in una direzione
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c].equals(player)) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Controlla nell'altra direzione
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c].equals(player)) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        return count >= 4;
    }

    private void resetGame() {
        // Ripristina la griglia per una nuova partita
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = "";
                gridButtons[row][col].setBackground(Color.WHITE);
            }
        }
        isRedTurn = true;
        turnStatusLabel.setText("Turn: Red");
        turnStatusLabel.setForeground(Color.RED);
    }

    private void exitGame() {
        // Logica per uscire dal gioco
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crea e mostra la finestra della lobby
                new GameInterface().setVisible(true);
            }
        });
    }
}
