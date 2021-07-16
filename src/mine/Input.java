/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class specifies the type of input used by Game
 */

package mine;

public class Input {

    int row, col, action;

    public Input(int row, int col, int action) {
        this.row = row;
        this.col = col;
        this.action = action;
    }
}
