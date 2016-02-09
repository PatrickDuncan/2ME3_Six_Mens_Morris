package me3assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private static Press press;
    private static Click click;
    private static boolean modifying = false;
    private static enum e {none, blue, red};
    private static final e[] states = new e[]{e.none, e.blue, e.red};
    private static final int[][] points = new int[16][2];
    
    /* Setup the frame and panels
    */
    public void setUp() {
        // Window setup
        frame = new JFrame();
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setTitle("Six Men's Morris");
        // Main layout panel setup
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
        // Add the colour layout, board, and button layout panel to the layout main layout panel
        layoutP.add(colourP, BorderLayout.WEST);
        layoutP.add(boardP, BorderLayout.CENTER);
        layoutP.add(buttonP, BorderLayout.EAST);
        frame.add(layoutP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    /*
    */
    public static void pointSetUp() {
        points[0] = new int[]{248, 78};
        points[1] = new int[]{450, 78};
        points[2] = new int[]{657, 78};
        points[3] = new int[]{348, 180};
        points[4] = new int[]{455, 180};
        points[5] = new int[]{557, 180};
        points[6] = new int[]{250, 280};
        points[7] = new int[]{350, 280};
        points[8] = new int[]{555, 280};
        points[9] = new int[]{657, 280};
        points[10] = new int[]{350, 280};
        points[11] = new int[]{455, 385};
        points[12] = new int[]{555, 385};
        points[13] = new int[]{250, 487};
        points[14] = new int[]{452, 487};
        points[15] = new int[]{660, 487};
    }
    
    /*
    */
    public static void start() {
        press = new Press();
        emptyB.addActionListener(press);
        modifyB.addActionListener(press);
        click = new Click();
        frame.addMouseListener(click);
    }
    
    /* From official Java docs: https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
    */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } 
        else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /* Action listener for the buttons, when a button is pressed the actionPerformed will be invoked 
    */
    private static class Press implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent ae) {
           if (modifyB.isFocusOwner() && !modifying) {
                modifying = true;
                emptyB.setText("Done");
                modifyB.setText("Restart Edit");                
           }
           else if (modifyB.isFocusOwner() && modifying) {
               //restart, remove discs
           }
           else if (emptyB.isFocusOwner() && modifying) {
               //CHeck if shit's good
           }
        }
    }
    
    private static class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent me) {}   // Pressed and releaseed

        @Override
        public void mousePressed(MouseEvent me) {   // Just the download motion
            int x = me.getX(), y = me.getY();
            boolean canPlace = false;
            for (int[] point : points) {
                if (Math.abs(point[0] - x) < 25 && Math.abs(point[1] - y) < 25) {
                    canPlace = true;
                    break;
                }
            }
            if (canPlace) {
                System.out.println("place");
            }
        }

        @Override
        public void mouseReleased(MouseEvent me) {}

        @Override
        public void mouseEntered(MouseEvent me) {}

        @Override
        public void mouseExited(MouseEvent me) {}        
    }
}