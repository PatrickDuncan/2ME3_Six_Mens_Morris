package me3assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

public class Game implements IGame {

    private final int FRAME_WIDTH = 900, FRAME_HEIGHT = 550;

    public enum e {

        none, blue, red
    };
    private int redCount, blueCount;
    private JFrame frame;
    private JPanel layoutP, buttonP, colourP, redP, blueP, boardP;
    private JLayeredPane discLayer;
    private JLabel redL, blueL, modifyL, onTopL;
    private JButton topB, botB, redB, blueB;
    private Button button;
    private Mouse mouse;
    private boolean modifying = false, started = false, redTurn = true,
            redFull, blueFull, canRestart = false;
    private final e[] states = new e[16];
    private final int[][] points = new int[16][2];
    private final JLabel[] discs = new JLabel[16];
    private ImageIcon redImg, blueImg;

    /**
     * Creates all the GUI objects and adds them to the correct components. 
     * Calls the pointSetUp and discsSetUp.
     */
    @Override
    public void setUp() {
        // Window setup
        frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setTitle("Six Men's Morris");
        frame.setLayout(new BorderLayout());
        // Main layout panel setup
        layoutP = new JPanel(new BorderLayout());
        layoutP.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        // Red layoutP setup
        redP = new JPanel(new GridLayout(2, 1, 0, 0));
        redL = new JLabel("   Red Remaining: 6   ");
        redL.setForeground(Color.blue);
        redP.add(redL);
        redB = new JButton("Place Red Discs");
        redP.add(redB);
        redB.setVisible(false);
        redP.setBackground(Color.red);
        // Blue layoutP setup
        blueP = new JPanel(new GridLayout(2, 1, 0, 0));
        blueL = new JLabel("   Blue Remaining: 6   ");
        blueL.setForeground(Color.red);
        blueP.add(blueL);
        blueB = new JButton("Place Blue Discs");
        blueB.setVisible(false);
        blueP.add(blueB);
        blueP.setBackground(Color.blue);
        // Board layoutP setup
        boardP = new JPanel();
        boardP.setBackground(Color.WHITE);
        ImageIcon board = createImageIcon("/board.png");
        boardP.add(new JLabel(board));
        // Colour layoutP setup
        colourP = new JPanel();
        colourP.setLayout(new GridLayout(2, 1, 0, 0));
        colourP.add(redP);
        colourP.add(blueP);
        // Button panel setup
        buttonP = new JPanel(new GridLayout(2, 1, 0, 0));
        buttonP.setBackground(Color.yellow);
        topB = new JButton("New Game");
        botB = new JButton("Edit Game");
        buttonP.add(topB);
        buttonP.add(botB);
        // Add colour panels to colour layout panel
        colourP.add(redP);
        colourP.add(blueP);
        // Add the colour layout, board, and button layout panel to the layout main layout panel
        layoutP.add(colourP, BorderLayout.WEST);
        layoutP.add(boardP, BorderLayout.CENTER);
        layoutP.add(buttonP, BorderLayout.EAST);
        // Setup for the layeredpane which will hold all of the discs
        discLayer = new JLayeredPane();
        frame.add(discLayer);
        discLayer.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        discLayer.add(layoutP, new Integer(0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    /**
     *Set where the individual points are.
     */
    public void pointSetUp() {
        points[0] = new int[]{248, 78};
        points[1] = new int[]{450, 78};
        points[2] = new int[]{653, 78};
        points[3] = new int[]{348, 180};
        points[4] = new int[]{450, 180};
        points[5] = new int[]{555, 180};
        points[6] = new int[]{250, 280};
        points[7] = new int[]{350, 280};
        points[8] = new int[]{555, 280};
        points[9] = new int[]{653, 280};
        points[10] = new int[]{350, 385};
        points[11] = new int[]{450, 385};
        points[12] = new int[]{555, 385};
        points[13] = new int[]{250, 487};
        points[14] = new int[]{450, 487};
        points[15] = new int[]{653, 487};
    }
    /**
     *Sets up the game for the ability to add discs and track the state of the board.
     */
    public void discSetUp() {
        redImg = createImageIcon("/red.png");
        blueImg = createImageIcon("/blue.png");
        modifyL = new JLabel("Hold the disc to remove it.");
        modifyL.setVisible(false);
        modifyL.setFont(modifyL.getFont().deriveFont(24f));
        onTopL = new JLabel("Cannot put a disc on top of another. Remove if you want to replace.");
        onTopL.setVisible(false);
        onTopL.setFont(onTopL.getFont().deriveFont(20f));
        discLayer.add(modifyL, new Integer(1));
        discLayer.add(onTopL, new Integer(1));
        modifyL.setBounds(320, 480, 500, 50);
        onTopL.setBounds(145, 0, 700, 50);
        for (int i = 0; i < states.length; i++) {
            states[i] = e.none;
        }
        redCount = blueCount = 6;
        redFull = blueFull = false;
    }
    /**
     *Adds functionality to the buttons so you can start the game or modify the board.
     */
    @Override
    public void start() {
        button = new Button();
        topB.addActionListener(button);
        botB.addActionListener(button);
        mouse = new Mouse();
        frame.addMouseListener(mouse);
    }
    /**
     * Starts the game by randomizing who’s turn it is and removing the modifying UI elements.
     */
    private void play() {
        modifying = false;
        started = true;
        int random = (int) (Math.random() * 2);
        redTurn = (random == 0);
        redB.removeActionListener(button);
        redB.setVisible(false);
        blueB.setVisible(false);
        blueB.removeActionListener(button);
        topB.removeActionListener(button);
    }
    /**
     * Clear the board of all the discs.
     */
    private void clearBoard() {
        for (int i = 0; i < discs.length; i++) {
            if (discLayer.getIndexOf(discs[i]) != -1) {
                discLayer.remove(discLayer.getIndexOf(discs[i]));
            }
            discs[i] = null;
        }
        modifyL.setVisible(false);
        onTopL.setVisible(false);
        discLayer.repaint();
        redCount = blueCount = 6;
        redL.setText("   Red Remaining: " + redCount + "   ");
        blueL.setText("   Blue Remaining: " + blueCount + "   ");
    }
    /**
     * Restarts the board and UI for a new game
     */
    private void restart() {
        clearBoard();
        redL = new JLabel("   Red Remaining: 6   ");
        blueL = new JLabel("   Blue Remaining: 6   ");
        topB.setText("New Game");
        botB.setText("Edit Game");
        for (int i = 0; i < states.length; i++) {
            states[i] = e.none;
        }
        redCount = blueCount = 6;
        started = canRestart = modifying = redFull = blueFull = false;
        redTurn = true;
    }
    /**
     * Creates an imageicon from the path specified.
     * From official Java docs:
     * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
     * @return an ImageIcon with correct file pathing
     * @param path path to the file
     */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    // Deals with button input.
    private class Button implements ActionListener {
        /**
         * Action listener for the buttons, when a button is pressed the
         * actionPerformed will be invoked.
         * @param ae the ActionEvent of the button
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!modifying) {
                if (botB.isFocusOwner() && canRestart) {
                    restart();
                }
                else if (botB.isFocusOwner()) {  // When the modify button is pressed initially
                    modifyL.setVisible(true);
                    onTopL.setVisible(true);
                    modifying = true;
                    topB.setText("Done");
                    botB.setText("Restart Edit");
                    redB.addActionListener(button);
                    redB.setVisible(true);
                    blueB.setVisible(true);
                    blueB.addActionListener(button);
                } else if (topB.isFocusOwner()) {
                    started = true;
                    play();
                }
            } else {
                if (botB.isFocusOwner()) {
                    //restart, remove discs
                    clearBoard();
                } else if (topB.isFocusOwner()) {
                    Moves moves = new Moves();
                    boolean legal = moves.ModifyLegal(states);
                    if (!legal) {
                        clearBoard();
                    } else {
                        modifyL.setVisible(false);
                        onTopL.setVisible(false);
                        botB.setText("Restart Game");
                        canRestart = true;
                        play();
                    }
                } else if (redB.isFocusOwner()) {
                    redTurn = true;
                } else if (blueB.isFocusOwner()) {
                    redTurn = false;
                }
            }
        }
    }
    
    private long pressTime = 0;
    private int index = 0;
    // Deals with mouse input.
    private class Mouse implements MouseListener {
        /**
         * Invoked when the mouse is pressed and released.
         * @param me the mouse event
         */
        @Override
        public void mouseClicked(MouseEvent me) {
        }   // Pressed and releaseed
        /**
         * Invoked when the mouse is pressed downward.
         * @param me the mouse event
         */
        @Override
        public void mousePressed(MouseEvent me) {   // Just the download motion
            if (modifying || started) {
                if (modifying) {
                    pressTime = System.currentTimeMillis();
                }
                index = 0;
                int x = me.getX(), y = me.getY(), placeX = 0, placeY = 0;
                boolean canPlace = false;
                for (int[] point : points) {
                    if (Math.abs(point[0] - x) < 35 && Math.abs(point[1] - y) < 35) {
                        canPlace = true;
                        placeX = point[0];
                        placeY = point[1];
                        break;
                    }
                    index++;
                }
                if (canPlace && discs[index] == null) {    // Makes sure there isn't already a disc on the place
                    if (redTurn && !redFull) {
                        discs[index] = new JLabel(redImg);
                        discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                        states[index] = e.red;
                        --redCount;
                        redL.setText("   Red Remaining: " + redCount + "   ");
                        if (started && redCount == 0) {
                            redFull = true;
                        }
                        discLayer.add(discs[index], new Integer(1));
                    } else if (!redTurn && !blueFull) {
                        discs[index] = new JLabel(blueImg);
                        discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                        states[index] = e.blue;
                        --blueCount;
                        blueL.setText("   Blue Remaining: " + blueCount + "   ");
                        if (started && blueCount == 0) {
                            blueFull = true;
                        }
                        discLayer.add(discs[index], new Integer(1));
                    }
                    if (started) {
                        redTurn = !redTurn;
                    }
                }
            }
        }
        /**
         * Invoked when the mouse is released.
         * @param me the mouse event
         */
        @Override
        public void mouseReleased(MouseEvent me) {
            if (modifying && System.currentTimeMillis() - pressTime > 400f) {
                discLayer.remove(discLayer.getIndexOf(discs[index]));
                discLayer.repaint();
                discs[index] = null;
                if (states[index] == e.red) {
                    ++redCount;
                    redL.setText("   Red Remaining: " + redCount + "   ");
                } else if (states[index] == e.blue) {
                    ++blueCount;
                    blueL.setText("   Blue Remaining: " + blueCount + "   ");
                }
                states[index] = e.none;
            }
        }
        /**
         * Invoked when the mouse enters a component.
         * @param me the mouse event
         */
        @Override
        public void mouseEntered(MouseEvent me) {
        }
        /**
         * Invoked when the mouse exited a component.
         * @param me the mouse event
         */
        @Override
        public void mouseExited(MouseEvent me) {
        }
    }
}
