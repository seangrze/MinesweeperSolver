/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package view;

import java.awt.*;

public class Tile extends Button {

    public int row, col;

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
