/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class stores a pair in the form (row, col)
 */

package solver;

import java.util.Comparator;
import java.util.HashSet;

public class Pair  {

    public int row, col, width, height;
    private HashSet<Pair> neighbors;

    public Pair(int row, int col, int width, int height) {
        this.row = row;
        this.col = col;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Pair)) {
            return false;
        }
        Pair pair = ((Pair) o);
        return this.row == pair.row && this.col == pair.col;
    }

    @Override
    public int hashCode() {
        return row*width + col;
    }

    public HashSet<Pair> getNeighbors() {
        if(neighbors != null) {
            return neighbors;
        }
        neighbors = new HashSet<>();
        for(int i=row-1;i<=row+1;i++) {
            for(int j=col-1;j<=col+1;j++) {
                if(inBounds(i, j) && !(i == row && j == col)) {
                    neighbors.add(new Pair(i, j, width, height));
                }
            }
        }
        return neighbors;
    }

    private boolean inBounds(int i, int j) {
        return i >= 0 && i < height && j >= 0 && j < width;
    }

    public String toString() {
        return "(" + row + ", " + col + ")";
    }

}
