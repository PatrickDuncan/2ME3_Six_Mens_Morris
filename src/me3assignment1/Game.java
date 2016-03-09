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

    public enum states {

        none, blue, red
    };
    private final states[] discStates = new states[16];

    private enum flow {

        selection, modify, place, game
    };
    private flow current = flow.selection;

    private final int FRAME_WIDTH = 900, FRAME_HEIGHT = 550;
    private int redCount, blueCount;
    private final int[][] points = new int[16][2];
    private boolean redTurn = true, redFull = false, blueFull = false;

    private JFrame frame;
    private JPanel layoutP, buttonP, colourP, redP, blueP, boardP;
    private JLayeredPane discLayer;
    private JLabel redL, blueL, modifyL, onTopL;
    private JButton topB, botB, redB, blueB;
    private Button button;
    private Mouse mouse;
    private final JLabel[] discs = new JLabel[16];
    private final JLabel[] errors = new JLabel[16];
    private ImageIcon redImg, blueImg, yellowImg;

    private Moves moves;

    /**
     * Create all the GUI object.
     */
    @Override
    public void objectCreate() {
        frame = new JFrame();
        layoutP = new JPanel(new BorderLayout());
        redP = new JPanel(new GridLayout(2, 1, 0, 0));
        redL = new JLabel("   Red Remaining: 6   ");
        redB = new JButton("Place Red Discs");
        blueP = new JPanel(new GridLayout(2, 1, 0, 0));
        blueL = new JLabel("   Blue Remaining: 6   ");
        blueB = new JButton("Place Blue Discs");
        colourP = new JPanel(new GridLayout(2, 1, 0, 0));
        boardP = new JPanel();
        buttonP = new JPanel(new GridLayout(2, 1, 0, 0));
        topB = new JButton("New Game");
        botB = new JButton("Edit Game");
        discLayer = new JLayeredPane();
        modifyL = new JLabel("Hold the disc to remove it.");
        onTopL = new JLabel("Cannot put a disc on top of another. Remove if you want to replace.");
    }

    /**
     * Creates all the GUI objects and adds them to the correct components.
     * Calls the pointSetUp and discsSetUp.
     */
    @Override
    public void setUp() {
        // Window setup
        frame.setIconImage(createImageIcon("/board.png").getImage());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setTitle("Six Men's Morris");
        frame.setLayout(new BorderLayout());
        // Main layout panel setup
        layoutP.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT - 28);
        // Red layoutP setup
        redL.setForeground(Color.white);
        redP.add(redL);
        redP.add(redB);
        redB.setVisible(false);
        redB.setBackground(Color.black);
        redB.setForeground(Color.white);
        redP.setBackground(Color.red);
        // Blue layoutP setup
        blueL.setForeground(Color.white);
        blueP.add(blueL);
        blueB.setVisible(false);
        blueB.setBackground(Color.black);
        blueB.setForeground(Color.white);
        blueP.add(blueB);
        blueP.setBackground(Color.blue);
        // Board layoutP setup
        boardP.add(new JLabel(createImageIcon("/board.png")));
        boardP.setBackground(Color.gray);
        // Colour layoutP setup
        colourP.add(redP);
        colourP.add(blueP);
        // Button panel setup
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
        frame.add(discLayer);
        discLayer.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        discLayer.add(layoutP, new Integer(0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Set where the individual points are.
     */
    @Override
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
     * Sets up the game for the ability to add discs and track the state of the
     * board.
     */
    @Override
    public void discSetUp() {
        redCount = blueCount = 6;
        redFull = blueFull = false;
        redImg = createImageIcon("/red.png");
        blueImg = createImageIcon("/blue.png");
        yellowImg = createImageIcon("/yellow.png");
        modifyL.setVisible(false);
        onTopL.setVisible(false);
        modifyL.setFont(modifyL.getFont().deriveFont(24f));
        onTopL.setFont(onTopL.getFont().deriveFont(20f));
        discLayer.add(modifyL, new Integer(1));
        discLayer.add(onTopL, new Integer(1));
        modifyL.setBounds(320, 480, 500, 50);
        onTopL.setBounds(145, 0, 700, 50);
        for (int i = 0; i < discStates.length; i++) {
            discStates[i] = states.none;
            errors[i] = null;
        }
    }

    /**
     * Adds functionality to the buttons so you can start the game or modify the
     * board.
     */
    @Override
    public void buttonSetUp() {
        button = new Button();
        topB.addActionListener(button);
        botB.addActionListener(button);
        topB.setBackground(Color.black);
        botB.setBackground(Color.black);
        topB.setForeground(Color.white);
        botB.setForeground(Color.white);
        mouse = new Mouse();
        frame.addMouseListener(mouse);
    }

    /**
     * Starts the game by randomizing who’s turn it is and removing the
     * modifying UI elements.
     */
    private void play() {
        current = flow.place;
        int random = (int) (Math.random() * 2);
        redTurn = (random == 0);
        botB.setText("   Restart   ");
        topB.setText("");
        onTopL.setText("Game in progress . . .");
        onTopL.setVisible(true);
        onTopL.setBounds(380, 0, 750, 50);
        redB.setVisible(false);
        blueB.setVisible(false);
        redB.removeActionListener(button);
        blueB.removeActionListener(button);
        topB.removeActionListener(button);
    }

    /**
     * Clear the board of all the discs.
     */
    private void clearBoard() {
        redCount = blueCount = 6;
        redL.setText("   Red Remaining: " + redCount + "   ");
        blueL.setText("   Blue Remaining: " + blueCount + "   ");
        for (int i = 0; i < discs.length; i++) {
            if (discLayer.getIndexOf(discs[i]) != -1)
                discLayer.remove(discLayer.getIndexOf(discs[i]));
            discs[i] = null;
            discStates[i] = states.none;
        }
        clearErrors();
        discLayer.repaint();
    }
    
    private void clearErrors() {
        for (JLabel error : errors) {
            if (error != null && discLayer.getIndexOf(error) != -1) {
                discLayer.remove(discLayer.getIndexOf(error));
            }
        }
        discLayer.repaint();
    }

    /**
     * Restarts the board and UI for a new game
     */
    private void restart() {
        clearBoard();
        redCount = blueCount = 6;
        redFull = blueFull = false;
        current = flow.selection;
        redTurn = true;
        redL.setText("   Red Remaining: 6   ");
        blueL.setText("   Blue Remaining: 6   ");
        topB.setText("New Game");
        botB.setText("Edit Game");
        onTopL.setVisible(false);
        onTopL.setText("Cannot put a disc on top of another. Remove if you want to replace.");
        onTopL.setBounds(145, 0, 700, 50);
        topB.addActionListener(button);
    }

    private void errors() {
        int r, b;
        r = b = 0;
        for (int i = 0; i < discStates.length; i++) {
            if (discStates[i] == states.red)
                ++r;
            else if (discStates[i] == states.blue)
                ++b;
            if ((r > 6 && discStates[i] == states.red) || (b > 6 && discStates[i] == states.blue)) {
                int x, y;
                x = discs[i].getX();
                y = discs[i].getY();
                if (errors[i] == null)
                    errors[i] = new JLabel(yellowImg);
                errors[i].setBounds(x, y, 70, 70);
                discLayer.add(errors[i], new Integer(2));
                discLayer.repaint();
            }
        }
    }

    /**
     * Creates an imageicon from the path specified. From official Java docs:
     * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
     *
     * @return an ImageIcon with correct file pathing
     * @param path path to the file
     */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    // Deals with button input.
    private class Button implements ActionListener {

        /**
         * Action listener for the buttons, when a button is pressed the
         * actionPerformed will be invoked.
         *
         * @param ae the ActionEvent of the button
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (current == flow.modify) {
                if (botB.isFocusOwner()) {
                    //restart, remove discs
                    onTopL.setText("Cannot put a disc on top of another. Remove if you want to replace.");
                    clearBoard();
                } else if (topB.isFocusOwner()) {
                    moves = new Moves();
                    boolean legal = moves.modifyLegal(discStates);
                    if (!legal) {
                        onTopL.setText("Too many discs on the board!");
                        errors();
                    } else {
                        modifyL.setVisible(false);
                        for (JLabel error : errors) {
                            if (error != null && discLayer.getIndexOf(error) != -1)
                                discLayer.remove(discLayer.getIndexOf(error));
                        }
                        if (blueCount == 0)
                            blueFull = true;
                        else if (redCount == 0)
                            redFull = true;
                        play();
                    }
                } else {
                    if (redB.isFocusOwner())
                        redTurn = true;
                    else if (blueB.isFocusOwner())
                        redTurn = false;
                }
            } else {
                if (botB.isFocusOwner() && current == flow.place)
                    restart();
                else if (botB.isFocusOwner()) {  // When the modify button is pressed initially
                    current = flow.modify;
                    modifyL.setVisible(true);
                    onTopL.setVisible(true);
                    topB.setText("Analyze");
                    botB.setText("   Restart   ");
                    topB.addActionListener(button);
                    redB.addActionListener(button);
                    redB.setVisible(true);
                    blueB.setVisible(true);
                    blueB.addActionListener(button);
                } else if (topB.isFocusOwner())
                    play();
            }
        }
    }

    private long pressTime = 0;
    private int index = 0;

    // Deals with mouse input.
    private class Mouse implements MouseListener {

        /**
         * Invoked when the mouse is pressed and released.
         *
         * @param me the mouse event
         */
        @Override
        public void mouseClicked(MouseEvent me) {
        }   // Pressed and releaseed

        /**
         * Invoked when the mouse is pressed downward.
         *
         * @param me the mouse event
         */
        @Override
        public void mousePressed(MouseEvent me) {   // Just the download motion
            if (current == flow.modify || current == flow.place) {
                if (current == flow.modify)
                    pressTime = System.currentTimeMillis();
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
                        discStates[index] = states.red;
                        --redCount;
                        redL.setText("   Red Remaining: " + redCount + "   ");
                        if (current == flow.place && redCount == 0)
                            redFull = true;
                        discLayer.add(discs[index], new Integer(1));
                    } else {
                        if (!redTurn && !blueFull) {
                            discs[index] = new JLabel(blueImg);
                            discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                            discStates[index] = states.blue;
                            --blueCount;
                            blueL.setText("   Blue Remaining: " + blueCount + "   ");
                            if (current == flow.place && blueCount == 0) {
                                blueFull = true;
                            }
                            discLayer.add(discs[index], new Integer(1));
                        }
                    }
                    if (current == flow.place)
                        redTurn = !redTurn;
                }
            }
        }

        /**
         * Invoked when the mouse is released.
         *
         * @param me the mouse event
         */
        @Override
        public void mouseReleased(MouseEvent me) {
            if (current == flow.modify && System.currentTimeMillis() - pressTime > 400f && index < discs.length) {
                if (discLayer.getIndexOf(errors[index]) != -1)
                    discLayer.remove(discLayer.getIndexOf(errors[index]));
                discLayer.remove(discLayer.getIndexOf(discs[index]));
                discLayer.repaint();
                discs[index] = null;
                errors[index] = null;
                if (discStates[index] == states.red) {
                    ++redCount;
                    redL.setText("   Red Remaining: " + redCount + "   ");
                } else {
                    if (discStates[index] == states.blue) {
                        ++blueCount;
                        blueL.setText("   Blue Remaining: " + blueCount + "   ");
                    }
                }
                discStates[index] = states.none;
                clearErrors();
                errors();
            }
        }

        /**
         * Invoked when the mouse enters a component.
         *
         * @param me the mouse event
         */
        @Override
        public void mouseEntered(MouseEvent me) {
        }

        /**
         * Invoked when the mouse exited a component.
         *
         * @param me the mouse event
         */
        @Override
        public void mouseExited(MouseEvent me) {
        }
    }
}
