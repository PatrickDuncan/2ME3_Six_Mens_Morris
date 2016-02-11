package me3assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

public class Game {

    private final int FRAME_WIDTH = 900, FRAME_HEIGHT = 550;
    private JFrame frame;
    private JPanel layoutP, buttonP, colourP, redP, blueP, boardP;
    private JLayeredPane discLayer;
    private JLabel redL, blueL;
    private JButton emptyB, modifyB, redB, blueB;
    private Press press;
    private Click click;
    private boolean modifying = false, redTurn = true;
    private enum e {none, blue, red};
    private final e[] states = new e[]{e.none, e.blue, e.red};
    private final int[][] points = new int[16][2];
    private JLabel[] discs = new JLabel[16];
    private ImageIcon redImg, blueImg;
    
    /* Setup the frame and panels
    */
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
        redL = new JLabel("   Red Score: 0   ");
        redP.add(redL);
        redB = new JButton("Place Red Discs");
        redP.add(redB);
        redB.setVisible(false);
        redP.setBackground(Color.red);
        // Blue layoutP setup
        blueP = new JPanel(new GridLayout(2, 1, 0, 0));
        blueL = new JLabel("   Blue Score: 0   ");
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
        // Setup for the layeredpane which will hold all of the discs
        discLayer = new JLayeredPane();
        frame.add(discLayer);
        discLayer.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        discLayer.add(layoutP, new Integer(0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        pointSetUp();
        discSetUp();
    }
    
    /*
    */
    private void pointSetUp() {
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
    
    private void discSetUp() {
        redImg = createImageIcon("/red.png");
        blueImg = createImageIcon("/blue.png");
    }
    
    
    /*
    */
    public  void start() {
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
    private  class Press implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent ae) {
           if (modifyB.isFocusOwner() && !modifying) {
                modifying = true;
                emptyB.setText("Done");
                modifyB.setText("Restart Edit");                
                redB.addActionListener(press);
                redB.setVisible(true);
                blueB.setVisible(true);
                blueB.addActionListener(press);
           }
           else if (modifyB.isFocusOwner() && modifying) {
               //restart, remove discs
           }
           else if (emptyB.isFocusOwner() && modifying) {
               //Check if shit's good
           }
           else if (redB.isFocusOwner() && modifying) {
               redTurn = true;
           } 
           else if (blueB.isFocusOwner() && modifying) {
               redTurn = false;
           }           
        }
    }
    
    private boolean pressed = false;
    private long pressTime = 0;
    private int index = 0;
    private class Click implements MouseListener {
        
        @Override
        public void mouseClicked(MouseEvent me) {}   // Pressed and releaseed

        @Override
        public void mousePressed(MouseEvent me) {   // Just the download motion
            if (modifying) {     
                pressed = true;
                pressTime = System.currentTimeMillis();
                index = 0;
                int x = me.getX(), y = me.getY(), placeX = 0, placeY = 0;
                boolean canPlace = false;
                for (int[] point : points) {
                    if (Math.abs(point[0] - x) < 25 && Math.abs(point[1] - y) < 25) {
                        canPlace = true;
                        placeX = point[0];
                        placeY = point[1];
                        break;
                    }
                    index++;
                }
                if (canPlace) {
                    System.out.println("presssed " + placeX + " " + placeY);
                    //JLabel img;
                    if (redTurn) {
                        discs[index] = new JLabel(redImg);
                        discs[index].setBounds(placeX-11, placeY-53, 50, 50);
                    } else {
                        discs[index] = new JLabel(blueImg);
                        discs[index].setBounds(placeX-11, placeY-53, 50, 50);
                    }
                    discLayer.add(discs[index], new Integer(1));
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            if (modifying && System.currentTimeMillis() - pressTime > 1000f) {
               pressed = false;
               System.out.println(index);
               System.out.println((discs[index].getClass().getName()));
               //discLayer.remove(discs[index]);
               discLayer.remove(0);
               discLayer.repaint();
               System.out.println("held"); //discLayer.remove
            }
        }

        @Override
        public void mouseEntered(MouseEvent me) {}

        @Override
        public void mouseExited(MouseEvent me) {}        
    }
}