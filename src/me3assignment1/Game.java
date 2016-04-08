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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

public class Game implements IGame {

    public enum states {

        none, blue, red
    };
    private final states[] discStates = new states[16];
    private final states[] mills = new states[8];

    private enum flow {

        players, selection, modify, place, redRemove, blueRemove, win
    };
    private flow current = flow.players;

    private final int FRAME_WIDTH = 900, FRAME_HEIGHT = 550, N = 16;
    private int redCount, blueCount;
    private final int[][] points = new int[16][2];
    private boolean redTurn = true, redFull = false, blueFull = false, vsAI = false;

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
    private Timer timer;

    private Moves moves;
    private Computer computer;

    /**
     * Creates all the GUI objects.
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
        topB = new JButton("  1 Player  ");
        midB = new JButton("  2 Player  ");
        botB = new JButton("");
        pane = new JLayeredPane();
        botL = new JLabel("Hold the disc to remove it.");
        topL = new JLabel("Cannot put a disc on top of another. Remove if you want to replace.");
        timer = new Timer(0, null);
        moves = new Moves();
        computer = new Computer();
    }

    /**
     * Set the properties of the GUI objects.
     */
    @Override
    public void setUp() {
        // Window setup
        frame.setIconImage(createImageIcon("/board.png").getImage());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setTitle("Six Men's Morris");
        frame.setLayout(new BorderLayout());
        layoutP.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT - 28);
        // Red
        redL.setForeground(Color.white);
        redP.add(redL);
        redP.add(redB);
        redB.setVisible(false);
        redB.setBackground(Color.black);
        redB.setForeground(Color.white);
        redP.setBackground(Color.red);
        // Blue
        blueL.setForeground(Color.white);
        blueP.add(blueL);
        blueB.setVisible(false);
        blueB.setBackground(Color.black);
        blueB.setForeground(Color.white);
        blueP.add(blueB);
        blueP.setBackground(Color.blue);

        boardP.add(new JLabel(createImageIcon("/board.png")));
        boardP.setBackground(Color.gray);

        colourP.add(redP);
        colourP.add(blueP);

        buttonP.add(topB);
        buttonP.add(midB);
        buttonP.add(botB);

        colourP.add(redP);
        colourP.add(blueP);
        // Add the colour layout, board, and button layout panel to the layout main layout panel
        layoutP.add(colourP, BorderLayout.WEST);
        layoutP.add(boardP, BorderLayout.CENTER);
        layoutP.add(buttonP, BorderLayout.EAST);
        // Setup for the layeredpane which will hold all of the discs + everything else
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
        for (int i = 0; i < N; i++) {
            discStates[i] = states.none;
            errors[i] = null;
        }
        for (int i = 0; i < mills.length; i++) {
            mills[i] = states.none;
        }
    }

    /**
     * Adds functionality to the buttons.
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
        labelChange();
        botL.setVisible(true);
        botL.setBounds(410, 480, 500, 50);
        redB.setVisible(false);
        blueB.setVisible(false);
        redB.removeActionListener(button);
        blueB.removeActionListener(button);
        topB.removeActionListener(button);
        pane.repaint();
        if (vsAI && !redTurn) {
            AI();
            placingLogic();
        }
    }

    /**
     * Clear the board of all the discs.
     */
    private void clearBoard() {
        redCount = blueCount = 6;
        if (!vsAI) {
            redL.setText("   Red Remaining: " + redCount + "   ");
            blueL.setText("   Blue Remaining: " + blueCount + "   ");
        } else {
            redL.setText("  Player Remaining: " + redCount + "  ");
            blueL.setText(" Computer Remaining: " + blueCount + "  ");
        }
        for (int i = 0; i < N; i++) {
            if (pane.getIndexOf(discs[i]) != -1) {
                pane.remove(pane.getIndexOf(discs[i]));
            }
            discs[i] = null;
            discStates[i] = states.none;
        }
        for (int i = 0; i < mills.length; i++) {
            mills[i] = states.none;
        }
        clearErrors();
        pane.repaint();
    }

    /**
     * Remove all the error images from the board
     */
    private void clearErrors() {
        for (JLabel error : errors) {
            if (error != null && pane.getIndexOf(error) != -1)
                pane.remove(pane.getIndexOf(error));
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
        if (!vsAI) {
            redL.setText("   Red Remaining: " + redCount + "   ");
            blueL.setText("   Blue Remaining: " + blueCount + "   ");
        } else {
            redL.setText("  Player Remaining: " + redCount + "  ");
            blueL.setText(" Computer Remaining: " + blueCount + "  ");
        }
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
        for (int i = 0; i < N; i++) {
            if (discStates[i] == states.red)
                ++r;
            else if (discStates[i] == states.blue)
                ++b;
            if ((r > 6 && discStates[i] == states.red) || (b > 6 && discStates[i] == states.blue)) {
                int x, y;
                x = discs[i].getX();
                y = discs[i].getY();
                if (errors[i] == null) {
                    errors[i] = new JLabel(yellowImg);
                }
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
        for (int i = 0; i < N; i++) {
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
        if (redFull && blueFull)
            s += '0';
        else
            s += '1';
        s += redCount;
        s += blueCount;
        try {
            String dir = System.getProperty("user.dir");
            File f = new File(dir + "\\load.txt");
            f.createNewFile();
            FileWriter writer = new FileWriter(dir + "\\load.txt", false);
            BufferedWriter write = new BufferedWriter(writer);
            write.write(s);
            write.close();
            writer.close();
        } catch (Exception io) {
            System.out.println("IOException");
        }
    }

    /**
     * Load the board state from the text file
     */
    private void load() {
        try {
            clearBoard();
            String dir = System.getProperty("user.dir");
            FileReader reader = new FileReader(dir + "\\load.txt");
            BufferedReader buffer = new BufferedReader(reader);
            String s = buffer.readLine();
            if (s == null) {
                FileWriter writer = new FileWriter(dir + "\\load.txt", false);
                BufferedWriter write = new BufferedWriter(writer);
                s = "0000000000000000";
                if (redTurn)
                    s += "1";
                else
                    s += "0";
                s += 1;     // Not full
                s += 6;     // 6 red to place
                s += 6;     // 6 blue to place
                write.write(s);
                write.close();
                writer.close();
            }
            redTurn = s.charAt(16) == '1';
            redCount = Character.getNumericValue(s.charAt(18));
            blueCount = Character.getNumericValue(s.charAt(19));
            if (s.charAt(17) == '0') {
                redFull = blueFull = true;
                redCount = blueCount = 0;
            }
            buffer.close();
            reader.close();
            for (int i = 0; i < N; i++) {
                if (s.charAt(i) == '1') {
                    discStates[i] = states.red;
                    discs[i] = new JLabel(redImg);
                    discs[i].setBounds(points[i][0] - 11, points[i][1] - 53, 50, 50);
                    discStates[i] = states.red;
                } else if (s.charAt(i) == '2') {
                    discStates[i] = states.blue;
                    discs[i] = new JLabel(blueImg);
                    discs[i].setBounds(points[i][0] - 11, points[i][1] - 53, 50, 50);
                    discStates[i] = states.blue;
                }
                if (discs[i] != null)
                    pane.add(discs[i], new Integer(1));
            }
            if (!vsAI) {
                redL.setText("   Red Remaining: " + redCount + "   ");
                blueL.setText("   Blue Remaining: " + blueCount + "   ");
            } else {
                redL.setText("  Player Remaining: " + redCount + "  ");
                blueL.setText(" Computer Remaining: " + blueCount + "  ");
            }
            if (redCount == 0)
                redFull = true;
            if (blueCount == 0)
                blueFull = true;
            pane.repaint();
            millsLogic();
        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    /**
     * So the player can remove a piece when they get a mill
     */
    private boolean millsLogic() {
        boolean milled = false;
        if (current != flow.modify) {
            states[] g = moves.checkMills(discStates);
            for (int i = 0; i < mills.length; i++) {
                if (mills[i] != g[i] && g[i] != states.none) {
                    if (redTurn) {
                        current = flow.redRemove;
                        if (vsAI)
                            topL.setText("Player got a mill! Remove a blue.");
                        else
                            topL.setText("Red got a mill! Remove a blue.");
                    } else {
                        current = flow.blueRemove;
                        if (vsAI)
                            topL.setText("Computer got a mill! Remove a red.");
                        else
                            topL.setText("Blue got a mill! Remove a reds.");
                    }
                    topL.setBounds(340, -10, 750, 50);
                    milled = true;
                    break;  // Can only get one new mill
                }
            }
            System.arraycopy(g, 0, mills, 0, mills.length);

        }
        return milled;
    }

    /**
     * A player has won!
     *
     * @param redWin If its a red win or blue win
     */
    private void win(boolean blueWin) {
        current = flow.win;
        if (blueWin) {
            if (vsAI)
                topL.setText("Computer Won! Press to continue.");
            else
                topL.setText("Blue Won! Press to continue.");
        } else {
            if (vsAI)
                topL.setText("Player Won! Press to continue.");
            else
                topL.setText("Red Won! Press to continue.");
        }
        topL.setBounds(330, -10, 750, 50);
        pane.repaint();
    }
    int ass = 0;
    /**
     * The AI turns logic for placing/removing/sliding
     */
    private void AI() {
        if (!blueFull && current == flow.place) {
            int place = computer.place(discStates);
            int delay = 1500;
            redTurn = false;
            labelChange();
            pane.repaint();
            timer = new Timer(delay, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    AIadd(place);
                    redTurn = false;
                    labelChange();
                    millsLogic();
                    if (current == flow.blueRemove) {
                        int toRemove = computer.remove(discStates);
                        timer = new Timer(delay, new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                                AIremove(toRemove);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    redTurn = true;
                    labelChange();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
        
    }

    /**
     * Add disc for the AI
     *
     * @param i index to add to
     */
    private void AIadd(int i) {
        int placeX = points[i][0], placeY = points[i][1];
        discs[i] = new JLabel(blueImg);
        discs[i].setBounds(placeX - 11, placeY - 53, 50, 50);
        discStates[i] = states.blue;
        --blueCount;
        if (blueCount == 0)
            blueFull = true;
        blueL.setText("  Computer Remaining: " + blueCount + "  ");
        pane.add(discs[i], new Integer(1));
        pane.repaint();
    }

    /**
     * Remove disc for the AI
     *
     * @param i index to add to
     */
    private void AIremove(int i) {
        if (pane.getIndexOf(discs[i]) != -1)
            pane.remove(pane.getIndexOf(discs[i]));
        discs[i] = null;
        discStates[i] = states.none;
        topL.setText("Game in progress. . .");
        topL.setBounds(380, -10, 750, 50);
        current = flow.place;
        pane.repaint();
    }

    private void AIsliding() {
        int r=0, b=0;
        for (states i : discStates) {
            if (i == states.red) r++;
            else if (i == states.blue) b++;
        }
            
        if (r > 2 && b > 2) {
            int[] startEnd = computer.move(discStates);
            int delay = 1500, i = startEnd[0], j = startEnd[1];
            redTurn = false;
            labelChange();
            timer = new Timer(delay, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (pane.getIndexOf(discs[i]) != -1)
                        pane.remove(pane.getIndexOf(discs[i]));
                    discs[i] = null;
                    discStates[i] = states.none;        

                    int placeX = points[j][0], placeY = points[j][1];
                    discs[j] = new JLabel(blueImg);
                    discs[j].setBounds(placeX - 11, placeY - 53, 50, 50);
                    discStates[j] = states.blue;
                    pane.add(discs[j], new Integer(1));

                    pane.repaint();


                    millsLogic();

                    if (current == flow.blueRemove) {
                        int toRemove = computer.remove(discStates);
                        timer = new Timer(delay, new AbstractAction() {
                            @Override
                            public void actionPerformed(ActionEvent ae) {
                               System.out.println(toRemove);
                                AIremove(toRemove);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    redTurn = true;
                    labelChange();
                }
            });
            timer.setRepeats(false);
            timer.start();    
        }
    }
    /**
     * The events that happen after placing down a disc
     */
    private void placingLogic() {
        if (current == flow.modify) {
            states[] g = moves.checkMills(discStates);
            System.arraycopy(g, 0, mills, 0, g.length);
        }
        if (current == flow.place && !redFull || !blueFull)
            millsLogic();
        if (current == flow.place) {
            redTurn = !redTurn;
            labelChange();
            pane.repaint();
        }

    }

    private void labelChange() {
        if (redTurn) {
            if (vsAI)
                botL.setText("Turn: Player");
            else
                botL.setText("Turn: Red");
        } else {
            if (vsAI)
                botL.setText("Turn: Computer");
            else
                botL.setText("Turn: Blue");
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
            if (current == flow.players) {
                if (topB.isFocusOwner())
                    vsAI = true;
                else if (midB.isFocusOwner())
                    vsAI = false;
                current = flow.selection;
                topB.setText("New Game");
                midB.setText("Edit Game");
                botB.setText("Load Game");
                redL.setText("  Player Remaining: " + redCount + "  ");
                blueL.setText("  Computer Remaining: " + blueCount + "  ");
            } else if (current != flow.win) {
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
                                if (error != null && pane.getIndexOf(error) != -1) {
                                    pane.remove(pane.getIndexOf(error));
                                }
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
                } else if (current != flow.redRemove && current != flow.blueRemove) {
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
    }

    private long pressTime = 0;
    private int index = 0;

    // Deals with mouse input.
    private class Mouse implements MouseListener {

        /**
         * Invoked when the mouse is pressed downward.
         *
         * @param me the mouse event
         */
        @Override
        public void mousePressed(MouseEvent me) {
           
            if (((vsAI && redTurn) || !vsAI) && !timer.isRunning()) {
                 System.out.println(vsAI + " " + redTurn + " " + timer.isRunning());
                if (current == flow.win)
                    restart();
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
                        boolean won = false;
                        if (redTurn && !redFull) {
                            discs[index] = new JLabel(redImg);
                            discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                            discStates[index] = states.red;
                            --redCount;
                            if (!vsAI)
                                redL.setText("   Red Remaining: " + redCount + "   ");
                            else
                                redL.setText("  Player Remaining: " + redCount + "  ");
                            pane.add(discs[index], new Integer(1));
                            pane.repaint();
                            if (current == flow.place && redCount == 0) {
                                redFull = true;
                                if (redFull && blueFull)
                                    labelChange();
                                if (!millsLogic()) {
                                    states block = moves.checkBlocked(discStates);
                                    if (block == states.red) {
                                        win(true);
                                        
                                        won = true;
                                    } else if (block == states.blue) {
                                        win(false);System.out.println("hi");
                                        won = true;
                                    }
                                }
                            }

                        } else if (!vsAI && !redTurn && !blueFull) {
                            discs[index] = new JLabel(blueImg);
                            discs[index].setBounds(placeX - 11, placeY - 53, 50, 50);
                            discStates[index] = states.blue;
                            --blueCount;
                            if (!vsAI)
                                blueL.setText("   Blue Remaining: " + blueCount + "   ");
                            else
                                blueL.setText(" Computer Remaining: " + blueCount + "  ");
                            pane.add(discs[index], new Integer(1));
                            pane.repaint();
                            if (current == flow.place && blueCount == 0) {
                                blueFull = true;
                                if (redFull && blueFull) {
                                    if (vsAI)
                                        botL.setText("Turn: Player");
                                    else
                                        botL.setText("Turn: Red");
                                } else {
                                    if (vsAI)
                                        botL.setText("Turn: Computer");
                                    else
                                        botL.setText("Turn: Blue");
                                }
                                if (!millsLogic()) {
                                    states block = moves.checkBlocked(discStates);
                                    if (block == states.red) {
                                        win(true);
                                        
                                        won = true;
                                    } else if (block == states.blue) {
                                        win(false);System.out.println("hihi");
                                        won = true;
                                    }
                                }
                            }
                        }
                        placingLogic();
                        if (vsAI && current != flow.redRemove && current != flow.modify) {
                            AI();
                            redTurn = true;
                            placingLogic();
                            redTurn = true;
                            labelChange();
                        }
                    }
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
            // Removing a piece during the modify stage
            //  System.out.println(current);
            if (current == flow.modify && index < N
                    && System.currentTimeMillis() - pressTime > 400f) {
                if (pane.getIndexOf(errors[index]) != -1) {
                    pane.remove(pane.getIndexOf(errors[index]));
                }
                pane.remove(pane.getIndexOf(discs[index]));
                pane.repaint();
                discs[index] = null;
                errors[index] = null;
                if (discStates[index] == states.red) {
                    ++redCount;
                    redL.setText("   Red Remaining: " + redCount + "   ");
                } else if (discStates[index] == states.blue) {
                    ++blueCount;
                    blueL.setText("   Blue Remaining: " + blueCount + "   ");
                }
                discStates[index] = states.none;
                clearErrors();
                errors();
            } // Sliding pieces when all pieces have been laid down
            else if (current == flow.place && redFull && blueFull) {
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
                boolean slideTurn = false;
                if (p2 < N)
                    slideTurn = moves.checkMovement(discStates, index, p2);
                if (index < N) {
                    if ((discStates[index] == states.red && !redTurn)
                            || (discStates[index] == states.blue && redTurn)) {
                        slideTurn = false;
                    }
                }
                if (canPlace && slideTurn) {
                    pane.remove(pane.getIndexOf(discs[index]));
                    if (discStates[index] == states.blue) {
                        discs[p2] = new JLabel(blueImg);
                        discStates[p2] = states.blue;
                    } else if (discStates[index] == states.red) {
                        discs[p2] = new JLabel(redImg);
                        discStates[p2] = states.red;
                    }
                    discs[p2].setBounds(placeX - 11, placeY - 53, 50, 50);
                    discStates[index] = states.none;
                    pane.add(discs[p2], new Integer(1));
                    if (!millsLogic()) {
                        redTurn = !redTurn;
                        labelChange();
                    }
                    pane.repaint();
                    states block = moves.checkBlocked(discStates);
                    if (block == states.red) {
                        win(true);
                        
                    }
                    else if (block == states.blue)
                        win(false);System.out.println("hihihi");
                    
                    if (current != flow.redRemove)
                        AIsliding();
                }
            } // If red or blue got a mill and have to remove a piece.
            else if (current == flow.redRemove || current == flow.blueRemove) {
                int i = 0, x = me.getX(), y = me.getY();
                for (int[] point : points) {
                    if (Math.abs(point[0] - x) < 35 && Math.abs(point[1] - y) < 35) {
                        if ((current == flow.redRemove && discStates[i] == states.blue)
                                || (current == flow.blueRemove && discStates[i] == states.red)) {
                            pane.remove(pane.getIndexOf(discs[i]));
                            discStates[i] = states.none;
                            discs[i] = null;
                            current = flow.place;
                            redTurn = !redTurn;
                            labelChange();
                            topL.setText("Game in progress. . .");
                            topL.setBounds(360, -10, 750, 50);
                            millsLogic();
                            boolean won =false;
                            if (redFull && blueFull) {
                                int r = 0, b = 0;
                                for (int j = 0; j < N; j++) {
                                    if (discStates[j] == states.red)
                                        ++r;
                                    else if (discStates[j] == states.blue)
                                        ++b;
                                }
                                if (r < 3) {
                                    win(true);
                                    
                                    won = true;
                                }
                                else if (b < 3) {
                                    win(false);System.out.println("hihihihi");
                                    won = true;
                                }
                            }
                            pane.repaint();
                            if (vsAI && !won) {
                                AI();
                                placingLogic();
                            }  
                            break;
                        }
                    }
                    i++;
                }
                if (redFull && blueFull)
                     AIsliding();
            }
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

        @Override
        public void mouseClicked(MouseEvent me) {
        }
    }
}
