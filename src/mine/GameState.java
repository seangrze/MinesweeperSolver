/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class contains information for the GUI. That is, the player board, and whether or not they won or
 * lost.
 */

package mine;

public class GameState {

    public char[][] board;
    public int mines;
    public boolean win, lose;

    public GameState(char[][] board, int mines, boolean win, boolean lose) {
        this.board = board;
        this.mines = mines;
        this.win = win;
        this.lose = lose;
    }

    public String toString() {
        StringBuilder board = new StringBuilder("Mines: " + mines + "\n");
        for(int i=0;i<this.board.length;i++) {
            for(int j=0;j<this.board[0].length;j++) {
                board.append(this.board[i][j]).append(" ");
            }
            board.append("\n");
        }
        if(win) {
            board.append("win");
        }
        if(lose) {
            board.append("lose");
        }
        return board.toString();
    }
}
