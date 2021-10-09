import java.awt.*;

public class Square {

    private boolean isMine, isRevealed, isFlagged;
    private int r, c;
    private Square[][] board;  //a reference to the entire game board.
    public boolean gameover;

    public Square(boolean isMine, int r, int c, Square[][] board) {
        this.isMine = isMine;
        this.r = r;
        this.c = c;
        this.isRevealed = false;
        this.board = board;
        this.isFlagged = false;
        this.gameover = false;
        
    }

    public int numNeighborMines(){
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int tr = r+i-1;
                int tc = c+j-1;
                if(isValidSquare(tr, tc)) {
                    count+=board[tr][tc].isMine?1:0;
                }
            }
        }
        /*
        TODO: Count how many neighboring squares have mines.
        Note.  Make sure you call isValidSquare on any index you access
        that may be out of bounds. For example...
        board[r-1][c].getIsMine() will cause an error when called on a top row Square.
        if(isValidSquare(r-1, c)){ board[r-1][c].getIsMine(); }  will NOT cause an error.
         */

        return count;
    }

    /*
    This method returns true if [r][c] is an in-bounds index
    in board.
     */
    public boolean isValidSquare(int r, int c){
        return (r < board.length && r >= 0) && (c < board[0].length && c >= 0);
        //TODO:  If r is out of bounds or c is out of bounds, return false.
        //Remember that an index is in valid if it's too big or too small.
    }

    public void draw(Graphics2D g2){
        int size = MineSweeper.SIZE;
        if(isRevealed) {
            if(isMine) {
                g2.setPaint(Color.RED);
            } else {
                g2.setPaint(Color.WHITE);
            }
        } else {
            g2.setPaint(Color.DARK_GRAY);
        }
        g2.fillRect(c*size, r*size, size, size);
        if(isFlagged) {
            g2.setPaint(Color.RED);
            int[] xs = new int[] {size*c+size/2, size*c+size/2-10, size*c+size/2};
            int[] ys = new int[] {size*r+size/2-10, size*r+size/2-5, size*r+size/2};
            g2.fillPolygon(xs, ys, 3);
            g2.setPaint(Color.BLACK);
            xs = new int[] {size*c+size/2, size*c+size/2+1, size*c+5, size*c+size/2, size*c+size-5, size*c+size/2+1, size*c+size/2};
            ys = new int[] {size*r+size/2-10, size*r+size-5, size*r+size-5, size*r+size-10, size*r+size-5, size*r+size-5, size*r+size/2-10};
            g2.fillPolygon(xs, ys, 7);
            xs = new int[] {size*c+size/2, size*c+size/2, size*c+size/2+1};
            ys = new int[] {size*r+size/2-10, size*r+size-5, size*r+size-5};
            g2.fillPolygon(xs, ys, 3);
        }
//        g2.setPaint(Color.WHITE);
        if(isRevealed && !isMine) {
            if(numNeighborMines() > 0)
                switch(numNeighborMines()) {
                    case 1:
                        g2.setPaint(new Color(0, 0,110));
                        break;
                    case 2:
                        g2.setPaint(new Color(0, 100,0));
                        break;
                    case 3:
                        g2.setPaint(new Color(120, 0,0));
                        break;
                    case 4:
                        g2.setPaint(new Color(0, 100,100));
                        break;
                    case 5:
                        g2.setPaint(new Color(100, 0,100));
                        break;
                    case 6:
                        g2.setPaint(new Color(150, 100,0));
                        break;
                    default:
                        g2.setPaint(Color.BLACK);
                        break;
                }
                g2.setFont(new Font ("Monospaced", Font.BOLD, 15));
                g2.drawString(numNeighborMines() + "", c*size+size/2, r*size+size/2);
        }
        g2.setPaint(Color.GRAY);
        g2.drawRect(c*size, r*size, size, size);
        /*
        TODO: Draw this Square.
        There are a couple of cases.
        1.  If it's not revealed, fill a grey rectangle at (c*size, r*size).
        2.  Revealed...
            a. isMine?  fill a red rectangle, or something nicer.
            b. not a mine?  drawString("" + (number of neighboring mines).
                In "real" minesweeper, 0 squares don't write 0, they are just blank.
                You could do that, too.
        These are just simple suggestions.  You can totally make it look nicer.
         */
    }

    public void reveal(){
        isRevealed = true;
        if(isMine) {
            gameover = true;
        }
        if(numNeighborMines() == 0) {
            if(isValidSquare(r-1, c) && !board[r-1][c].isRevealed) {
                board[r-1][c].reveal();
            }
            if(isValidSquare(r+1, c) && !board[r+1][c].isRevealed) {
                board[r+1][c].reveal();
            }
            if(isValidSquare(r, c-1) && !board[r][c-1].isRevealed) {
                board[r][c-1].reveal();
            }
            if(isValidSquare(r, c+1) && !board[r][c+1].isRevealed) {
                board[r][c + 1].reveal();
            }
            if(isValidSquare(r-1, c-1) && !board[r-1][c-1].isRevealed) {
                board[r-1][c-1].reveal();
            }
            if(isValidSquare(r-1, c+1) && !board[r-1][c+1].isRevealed) {
                board[r-1][c+1].reveal();
            }
            if(isValidSquare(r+1, c-1) && !board[r+1][c-1].isRevealed) {
                board[r+1][c-1].reveal();
            }
            if(isValidSquare(r+1, c+1) && !board[r+1][c+1].isRevealed) {
                board[r+1][c + 1].reveal();
            }
        }


        /*
        TODO: This is called when this Square is clicked on.
        FIRST: Change the value of the instance field(s) so this cell is revealed.

        IF this Square has ZERO mines around it...we have to reveal the "blob".
          So, for each neighboring Square...
            if it's a valid Square...
                if the Square is NOT yet revealed,
                    tell it to reveal().
                    (Note that this is the recursion.)
                    (this reveal calls another Square's reveal, which calls another...

         */
    }


    public void revealArea() {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int tr = r + i - 1;
                int tc = c + j - 1;
                if (isValidSquare(tr, tc)) {
                    count += board[tr][tc].isFlagged ? 1 : 0;
                }
            }
        }
        if(count == numNeighborMines()) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int tr = r + i - 1;
                    int tc = c + j - 1;
                    if(isValidSquare(tr, tc) && !board[tr][tc].isFlagged) {
                        board[tr][tc].reveal();
                    }
                }
            }
        }
    }

    public void toggleFlag() {
        isFlagged = !isFlagged;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    //TODO: Implement any getters or setters you may need.


}