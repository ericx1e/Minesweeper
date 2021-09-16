import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//Eric Xie

public class MineSweeper extends JPanel {

    private Square[][] board;
    private boolean firstMove = true;
    private boolean gameover = false;
    public static final int SIZE = 30;  //30 is arbitrary. Please adjust to make it look good to you.
    private static final double MINERATE = 0.2;

    public MineSweeper(int width, int height) {
        setSize(width, height);

        board = new Square[20][20];  //this is a tad small, feel free to adjust.

        //TODO: go to each spot in the board, and assign a new Square to that spot.
        //Double for loop time!
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                boolean isMine = Math.random() < MINERATE;
                board[r][c] = new Square(isMine, r, c, board);
            }
        }

        setupMouseListener();
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        //TODO: Go to each Square in the board and draw it.  board[r][c].draw(g2).
        for(int r = 0; r < board.length; r++) {
            for(int c = 0; c < board[0].length; c++) {
                board[r][c].draw(g2);
                if(board[r][c].gameover) {
                    gameover = true;
                }
            }
        }

        if(gameover) {
            g2.setPaint(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 70));
            g2.drawString("GAME OVER", 106, getHeight()/2);
        }
    }

    private void setupMouseListener(){
        addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(gameover) {
                    return;
                }

                //This is basically set up.
                // TODO: Later, after everything else, if you want to add flagging, you can use:
                //if(e.getButton() == MouseEvent.BUTTON2)
                //to determine if the user regular clicked or cntrl/2Finger/RightMouse or whatever.
                //Note, you'll also need to modify Square a bit, to handle flagging the Squares.


                //these 4 lines convert the mouse location to an [r][c] index in the board.
                int x = e.getX();
                int y = e.getY();

                int r = y / SIZE;
                int c = x / SIZE;
                if(e.getButton() == MouseEvent.BUTTON1 && !board[r][c].isFlagged())  {
                    if(firstMove) { //Ensure first move reveals an area
                        while(board[r][c].getIsMine() || board[r][c].numNeighborMines() > 0) {
                            for (int rr = 0; rr < board.length; rr++) { //Generate new board
                                for (int cc = 0; cc < board[0].length; cc++) {
                                    boolean isMine = Math.random() < MINERATE;
                                    board[rr][cc] = new Square(isMine, rr, cc, board);
                                }
                            }

                        }
                        board[r][c].reveal();
                        firstMove = false;
                    } else {
                        if(board[r][c].isRevealed()) {
                            board[r][c].revealArea();
                        } else {
                            board[r][c].reveal();
                        }
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON3) {
                    if(!board[r][c].isRevealed())
                        board[r][c].toggleFlag();
                }

                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    //sets ups the panel and frame.  You can change width and height of the window.
    public static void main(String[] args) {
        JFrame window = new JFrame("Minesweeper");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, 600 + 7, 600 + 22); //(x, y, w, h) 22 due to title bar.

        MineSweeper panel = new MineSweeper(600, 600);

        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(false);
    }

}