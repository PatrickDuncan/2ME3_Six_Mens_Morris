package me3assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
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
    private JLayeredPane pane;
    private JLabel redL, blueL, botL, topL;
    private JButton topB, midB, botB, redB, blueB;
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
        buttonP = new JPanel(new GridLayout(3, 1, 0, 0));
        topB = new JButton("New Game");
        midB = new JButton("Edit Game");
        botB = new JButton("Load Game");
        pane = new JLayeredPane();
        botL = new JLabel("Hold the disc to remove it.");
        topL = new JLabel("Cannot put a disc on top of another. Remove if you want to replace.");
        moves = new Moves();
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
        buttonP.add(midB);
        buttonP.add(botB);
        // Add colour panels to colour layout panel
        colourP.add(redP);
        colourP.add(blueP);
        // Add the colour layout, board, and button layout panel to the layout main layout panel
        layoutP.add(colourP, BorderLayout.WEST);
        layoutP.add(boardP, BorderLayout.CENTER);
        layoutP.add(buttonP, BorderLayout.EAST);
        // Setup for the layeredpane which will hold all of the discs
        frame.add(pane);
        pane.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        pane.add(layoutP, new Integer(0));
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
        botL.setVisible(false);
        topL.setVisible(false);
        botL.setFont(botL.getFont().deriveFont(24f));
        topL.setFont(topL.getFont().deriveFont(20f));
        pane.add(botL, new Integer(1));
        pane.add(topL, new Integer(1));
        botL.setBounds(320, 480, 500, 50);
        topL.setBounds(145, -10, 700, 50);
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
        midB.addActionListener(button);
        botB.addActionListener(button);
        topB.setBackground(Color.black);
        midB.setBackground(Color.black);
        botB.setBackground(Color.black);
        topB.setForeground(Color.white);
        midB.setForeground(Color.white);
        botB.setForeground(Color.white);
        mouse = new Mouse();
        frame.addMouseListener(mouse);
    }

    /**
     * Starts the game by randomizing who’s turn it is and removing the
     * modifying UI elements.
     */
    private void play(boolean loading) {
        current = flow.place;
        if (!loading) {
            int random = (int) (Math.random() * 2);
            redTurn = (random == 0);
        }
        topB.setText("");
        midB.setText("   Restart   ");
        botB.setText("Save");
        topL.setText("Game in progress. . .");
        topL.setVisible(true);
        topL.setBounds(380, -10, 750, 50);
        if (redTurn)
            botL.setText("Turn: Red");
        else {
            botL.setText("Turn: Blue");
        }
        botL.setVisible(true);
        botL.setBounds(410, 480, 500, 50);
        redB.setVisible(false);
        blueB.setVisible(false);
        redB.removeActionListener(button);
        blueB.removeActionListener(button);
        topB.removeActionListener(button);
        pane.repaint();
    }

    /**
     * Clear the board of all the discs.
     */
    private void clearBoard() {
        redCount = blueCount = 6;
        redL.setText("   Red Remaining: " + redCount + "   ");
        blueL.setText("   Blue Remaining: " + blueCount + "   ");
        for (int i = 0; i < discs.length; i++) {
            if (pane.getIndexOf(discs[i]) != -1)
                pane.remove(pane.getIndexOf(discs[i]));
            discs[i] = null;
            discStates[i] = states.none;
        }
        clearErrors();
        pane.repaint();
    }

    private void clearErrors() {
        for (JLabel error : errors) {
            if (error != null && pane.getIndexOf(error) != -1) {
                pane.remove(pane.getIndexOf(error));
            }
        }
        pane.repaint();
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
        midB.setText("Edit Game");
        botB.setText("Load Game");
        topL.setVisible(false);
        topL.setText("Cannot put a disc on top of another. Remove if you want to replace.");
        topL.setBounds(145, -10, 700, 50);
        botL.setVisible(false);
        topB.addActionListener(button);
    }

    /**
     * Add the error image to the board
     */
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
                pane.add(errors[i], new Integer(2));
                pane.repaint();
            }
        }
    }

    /**
     * Saves current board to a text file
     */
    private void save() {
        /* 0 -> nothing
         1 -> red
         2-> blue
         spaces inbetween */
        char[] temp = new char[16];
        for (int i = 0; i < temp.length; i++) {
            if (discStates[i] == states.none)
                temp[i] = '0';
            else if (discStates[i] == states.red)
                temp[i] = '1';
            else
                temp[i] = '2';
        }
        String s = "";
        for (char i : temp) {
            s += i;
        }
        if (redTurn)
            s += '1';
        else
            s += '0';
        try {
            java.net.URL url = getClass().getResource("/load.txt");
            if (url != null) {
                String dir = System.getProperty("user.dir");
                File f = new File(dir + "\\load.txt");
                f.createNewFile();
                FileWriter writer = new FileWriter(dir + "\\load.txt", false);
                BufferedWriter write = new BufferedWriter(writer);
                write.write(s);
                write.close();
                writer.close();
            } else
                System.out.println("URL is null");
        } catch (Exception io) {
            System.out.println("IOException");
        }
    }

    /**
     * Load the board state from the text file
     */
    private void load() {
        try {
            java.net.URL url = getClass().getResource("/load.txt");
            if (url != null) {
                String dir = System.getProperty("user.dir");
                FileReader reader = new FileReader(dir + "\\load.txt");
                BufferedReader buffer = new BufferedReader(reader);
                String s = buffer.readLine();
                if (s == null) {
                    FileWriter writer = new FileWriter(url.getFile(), false);
                    BufferedWriter write = new BufferedWriter(writer);
                    s = "0000000000000000";
                    if (redTurn)
                        s += "1";
                    else
                        s += "0";
                    write.write(s);
                    s = buffer.readLine();
                    write.close();
                    writer.close();
                }
                redTurn = s.charAt(16) == '1';
                buffer.close();
                reader.close();
                clearBoard();
                redCount = blueCount = 6;
                for (int i = 0; i < discs.length; i++) {
                    if (s.charAt(i) == '1') {
                        discStates[i] = states.red;
                        discs[i] = new JLabel(redImg);
                        discs[i].setBounds(points[i][0] - 11, points[i][1] - 53, 50, 50);
                        discStates[i] = states.red;
                        --redCount;
                    } else if (s.charAt(i) == '2') {
                        discStates[i] = states.blue;
                        discs[i] = new JLabel(blueImg);
                        discs[i].setBounds(points[i][0] - 11, points[i][1] - 53, 50, 50);
                        discStates[i] = states.blue;
                        --blueCount;
                    }
                    if (discs[i] != null)
                        pane.add(discs[i], new Integer(1));
                }
                redL.setText("   Red Remaining: " + redCount + "   ");
                blueL.setText("   Blue Remaining: " + blueCount + "   ");
                if (redCount == 0)
                    redFull = true;
                if (blueCount == 0)
                    blueFull = true;
                pane.repaint();
            } else
                System.out.println("URL is null");
        } catch (Exception io) {
            io.printStackTrace();
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
                if (midB.isFocusOwner()) {
                    //restart, remove discs
                    topL.setText("Cannot put a disc on top of another. Remove if you want to replace.");
                    clearBoard();
                } else if (topB.isFocusOwner()) {
                    boolean legal = moves.modifyLegal(discStates);
                    if (!legal) {
                        topL.setText("Too many discs on the board!");
                        errors();
                    } else {
                        for (JLabel error : errors) {
                            if (error != null && pane.getIndexOf(error) != -1)
                                pane.remove(pane.getIndexOf(error));
                        }
                        if (blueCount == 0)
                            blueFull = true;
                        else if (redCount == 0)
                            redFull = true;
                        play(false);
                    }
                } else if (redB.isFocusOwner())
                    redTurn = true;
                else if (blueB.isFocusOwner())
                    redTurn = false;
            } else {
                if (midB.isFocusOwner() && current == flow.place)
                    restart();
                else if (midB.isFocusOwner()) {  // When the modify button is pressed initially
                    current = flow.modify;
                    botL.setVisible(true);
                    topL.setVisible(true);
                    topB.setText("Analyze");
                    midB.setText("   Restart   ");
                    botB.setText("");
                    topB.addActionListener(button);
                    redB.addActionListener(button);
                    redB.setVisible(true);
                    blueB.setVisible(true);
                    blueB.addActionListener(button);
                } else if (topB.isFocusOwner())
                    play(false);
                else if (botB.isFocusOwner() && current == flow.selection) {
                    load();
                    play(true);
                } else if (botB.isFocusOwner() && current == flow.place)
                    save();
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
        public void mousePressed(MouseEvent me) {
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
                if (canPlace && discs[index] == null) { // Makes sure there isn't already a disc on the place
                    if (redTurn && !redFull) {
                        discs[index] = new JLabel(redImg);
                        discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                        discStates[index] = states.red;
                        --redCount;
                        redL.setText("   Red Remaining: " + redCount + "   ");
                        if (current == flow.place && redCount == 0)
                            redFull = true;
                        pane.add(discs[index], new Integer(1));
                    } else if (!redTurn && !blueFull) {
                        discs[index] = new JLabel(blueImg);
                        discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                        discStates[index] = states.blue;
                        --blueCount;
                        blueL.setText("   Blue Remaining: " + blueCount + "   ");
                        if (current == flow.place && blueCount == 0) {
                            blueFull = true;
                        }
                        pane.add(discs[index], new Integer(1));
                    }
                    if (current == flow.place) {
                        redTurn = !redTurn;
                    }
                    if (current == flow.place) {
                        if (redTurn && !redFull)
                            botL.setText("Turn: Red");
                        else if (!redTurn && !blueFull)
                            botL.setText("Turn: Blue");
                        pane.repaint();
                    }
                    if (current == flow.place)
                        moves.checkMills(discStates);
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
                if (pane.getIndexOf(errors[index]) != -1)
                    pane.remove(pane.getIndexOf(errors[index]));
                pane.remove(pane.getIndexOf(discs[index]));
                pane.repaint();
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
            if (current == flow.place && redFull && blueFull) {
                pressTime = System.currentTimeMillis();
                int p2 = 0;
                int x = me.getX(), y = me.getY(), placeX = 0, placeY = 0;
                boolean canPlace = false;
                for (int[] point : points) {
                    if (Math.abs(point[0] - x) < 35 && Math.abs(point[1] - y) < 35) {
                        canPlace = true;
                        placeX = point[0];
                        placeY = point[1];
                        break;
                    }
                    p2++;
                }
                //moves.checkAdjency(discStates);
                boolean slideTurn = moves.checkMovement(discStates, index, p2);
                if (canPlace && slideTurn) {
                    System.out.println(moves.checkMovement(discStates, index, p2));
                    pane.remove(pane.getIndexOf(discs[index]));
                    if (discStates[index] == states.blue)
                        discs[p2] = new JLabel(blueImg);
                    else if (discStates[index] == states.red)
                        discs[p2] = new JLabel(redImg);
                    discs[p2].setBounds(placeX - 11, placeY - 53, 50, 50);
                    discStates[index] = states.none;
                    discStates[p2] = states.blue;
                    pane.add(discs[p2], new Integer(1));
                    pane.repaint();
                }
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
