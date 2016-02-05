package me3assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javafx.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class Game {

    private static final int FRAME_WIDTH = 900, FRAME_HEIGHT = 550;
    private static JFrame frame;
    private static JPanel layoutP, buttonP, colourP, redP, blueP, boardP;
    private static JLabel redL, blueL;
    private static JButton emptyB, modifyB;

    public void SetUp() {
        frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setTitle("Six Men's Morris");
        layoutP = new JPanel();
        layoutP.setLayout(new BorderLayout());
        // Red layoutP setup
        redP = new JPanel();
        redL = new JLabel("   Red Score: 0   ");
        redP.add(redL);
        redP.setBackground(Color.red);
        // Blue layoutP setup
        blueP = new JPanel();
        blueL = new JLabel("   Blue Score: 0   ");
        blueP.add(blueL);
        blueP.setBackground(Color.blue);
        // Board layoutP setup
        boardP = new JPanel();
        boardP.setBackground(Color.WHITE);
        ImageIcon board = createImageIcon("/board.png");
        JLabel boardLabel = new JLabel(board);     // Must add imageicon to label
        boardP.add(boardLabel);
        // Colour layoutP setup
        colourP = new JPanel();
        colourP.setLayout(new GridLayout(2, 1, 0, 0));
        colourP.add(redP);
        colourP.add(blueP);
        // Button panel setup
        buttonP = new JPanel(new GridLayout(2, 1, 0, 0));
        buttonP.setBackground(Color.yellow);
        emptyB = new JButton("New Game");
        modifyB = new JButton("Edit Game");
        buttonP.add(emptyB);
        buttonP.add(modifyB);
        // Add colour panels to colour layout panel
        colourP.add(redP);
        colourP.add(blueP);
        // Add the layoutPs to the layout layoutP
        layoutP.add(colourP, BorderLayout.WEST);
        layoutP.add(boardP, BorderLayout.CENTER);
        layoutP.add(buttonP, BorderLayout.EAST);
        frame.add(layoutP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void Start() {
        Press press = new Press();
        emptyB.addActionListener(press);

    }

    public static void placeDisc() {
        boolean playerTurn;

    }

    // From official Java docs: 
    // https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private static class Press implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent ae) {
            if (emptyB.isFocusOwner()) {
                System.out.println("pressed");
            }
        }
    }
}
