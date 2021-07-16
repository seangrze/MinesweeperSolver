/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class is used for solving a minesweeper board. It takes the current game state and
 * returns the best move.
 * The solver works by generating every valid board configuration and counting the number of mines that
 * appear at each square. The probability of a certain space containing a mine is equal to the
 * number of times a mine appears at that location divided by the total number of configurations.
 * This solver only considers tiles that border a number square.
 */

package solver;

import mine.*;
import java.util.*;

public class Solver {

    //This queue contains spaces that are guaranteed to be safe or contain a mine
    private Queue<Input> solvedInputs = new LinkedList<>();

    //Number of mine occurrences for particular spaces
    private HashMap<Pair, Integer> occurrences = new HashMap<>();

    private char[][] board;
    private int width, height;
    private int flagCounter;

    //Flag variables for isMine
    private final int MINE = 0, NO_MINE = 1, BOTH = 2, NEITHER = 3;

    //Returns the "safest" input given the game state
    public Input solve(GameState state) {
        //Retrieves input from queue if available
        if(!solvedInputs.isEmpty()) {
            return solvedInputs.remove();
        }
        board = state.board;
        width = board[0].length;
        height = board.length;
        flagCounter = state.mines;

        //All empty tiles that are adjacent to numbers
        HashSet<Pair> borderTiles = findBorderTiles();

        //Sets up occurrences HashMap
        setupTable(borderTiles);

        //If there are no border tiles, then it picks the first available square
        if(borderTiles.isEmpty()) {
            Pair empty = firstAvailable();
            return new Input(empty.row, empty.col, Actions.LEFT);
        }

        //Breaks up borderTiles into groups if they are in separate sections of the board
        HashSet<ArrayList<Pair>> borders = divideBorderTiles(borderTiles);

        //Finds tile with lowest probability
        Pair lowestPair = null;
        double lowestProb = 69;
        for(ArrayList<Pair> border: borders) {
            //Counts the number of mines that appear at each tile and stores it into occurrences
            int configurations = countOccurrences(border, border.get(0), 0);

            //Selects tile with lowest probability that does not equal 0
            Pair checkPair = lowestProbability(border, configurations, lowestProb);
            if(checkPair != null) {
                lowestPair = checkPair;
                lowestProb = ((double) occurrences.get(lowestPair))/configurations;
            }
        }

        //Resets occurrences HashMap
        removeTableEntries();

        //If no guaranteed squares, then select tile with lowest probability
        if(solvedInputs.isEmpty()) {
            return new Input(lowestPair.row, lowestPair.col, Actions.LEFT);
        }
        return solvedInputs.remove();

    }

    //Finds first empty square on the board
    private Pair firstAvailable() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                if(board[i][j] == '-') {
                    return new Pair(i, j, width, height);
                }
            }
        }
        return null;
    }

    //Returns tile with smallest probability in border that isn't equal to 0
    private Pair lowestProbability(ArrayList<Pair> border, int configurations, double currentLowest) {
        Pair lowest = null;
        double lowProb = currentLowest;
        for(Pair pair: border) {
            if(occurrences.get(pair) == 0) {
                solvedInputs.add(new Input(pair.row, pair.col, Actions.LEFT));
            } else if(occurrences.get(pair) == configurations) {
                solvedInputs.add(new Input(pair.row, pair.col, Actions.RIGHT));
            } else {
                double probability = ((double) occurrences.get(pair))/configurations;
                if(probability < lowProb) {
                    lowest = pair;
                    lowProb = probability;
                }
            }
        }
        return lowest;
    }

    //Sets the number of mines for each tile to 0
    private void setupTable(HashSet<Pair> tiles) {
        for(Pair tile: tiles) {
            occurrences.put(tile, 0);
        }
    }

    //Removes all entries from occurrences
    private void removeTableEntries() {
        occurrences = new HashMap<>();
    }

    //Returns the total number of configurations of mines from pairs
    //Records the number of mines that appear on each tile in occurrences
    private int countOccurrences(ArrayList<Pair> pairs, Pair current, int index) {
        if(current == null) {
            return 1;
        }
        int withMine = 0;
        int withoutMine = 0;

        //Check if the tile can have a mine
        int isMine = isMine(current);
        Pair next = null;

        //If contradiction, then return
        if(isMine == NEITHER) {
            return 0;
        }

        if(index < pairs.size()-1) {
            next = pairs.get(index+1);
        }

        //Try placing a mine if it can have a mine
        if(isMine == MINE || isMine == BOTH) {
            board[current.row][current.col] = 'F';
            flagCounter--;
            withMine = countOccurrences(pairs, next, index+1);
            board[current.row][current.col] = '-';
            occurrences.put(current, occurrences.get(current)+withMine);
            flagCounter++;
        }

        //Set to number if it doesn't need a mine,
        //Placeholder 'N' is used as the number
        if(isMine == NO_MINE || isMine == BOTH) {
            board[current.row][current.col] = 'N';
            withoutMine = countOccurrences(pairs, next, index+1);
            board[current.row][current.col] = '-';
        }
        return withMine + withoutMine;
    }

    //Returns MINE if there must be a mine on pair
    //        NO_MINE if there cannot be a mine on pair
    //        BOTH if pair may or may not be a mine
    //        NEITHER if pair both must be a mine and must not be a mine
    private int isMine(Pair pair) {
        boolean mine = false;
        boolean noMine = false;
        HashSet<Pair> neighbors = pair.getNeighbors();
        for(Pair neighbor: neighbors) {
            char neighborIcon = board[neighbor.row][neighbor.col];
            if(neighborIcon != '-' && neighborIcon != 'F' && neighborIcon != 'N') {
                HashSet<Pair> numNeighbors = neighbor.getNeighbors();
                int num = ((int) neighborIcon - '0');
                int flags = 0, spaces = 0;
                for(Pair numNeighbor: numNeighbors) {
                    char icon = board[numNeighbor.row][numNeighbor.col];
                    if(icon == '-') {
                        spaces++;
                    }
                    if(icon == 'F') {
                        flags++;
                    }
                }
                int trueValue = num - flags;
                if(trueValue == spaces) {
                    mine = true;
                }
                if(trueValue == 0) {
                    noMine = true;
                }
            }
        }
        int retValue = BOTH;
        if(mine && noMine) {
            retValue = NEITHER;
        } else if(mine) {
            retValue = MINE;
            if(flagCounter == 0) {
                retValue = NEITHER;
            }
        } else if(noMine) {
            retValue = NO_MINE;
        }
        return retValue;
    }

    //Divides borderTiles into groups depending on the sections in the board
    //This is for efficiency. Calling countOccurrences with several small
    //data sets will be faster than calling it with one large data set.
    private HashSet<ArrayList<Pair>> divideBorderTiles(HashSet<Pair> borderTiles) {
        HashSet<ArrayList<Pair>> borders = new HashSet<>();
        Iterator<Pair> iter;
        while(!borderTiles.isEmpty()) {
            iter = borderTiles.iterator();
            Pair start = iter.next();
            ArrayList<Pair> border = new ArrayList<>();
            borderTiles.remove(start);
            border.add(start);
            findBorder(start, borderTiles, border);
            borders.add(border);
        }
        return borders;
    }

    //Finds an empty tile adjacent to current that is not already in border
    private void findBorder(Pair current, HashSet<Pair> borderTiles, ArrayList<Pair> border) {
        HashSet<Pair> neighbors = current.getNeighbors();
        for(Pair tile: neighbors) {
            if(borderTiles.contains(tile) && !border.contains(tile)) {
                borderTiles.remove(tile);
                border.add(tile);
                findBorder(tile, borderTiles, border);
            }
        }
    }

    //Returns a set containing all empty tiles that are adjacent to a number tile
    private HashSet<Pair> findBorderTiles() {
        HashSet<Pair> borderTiles = new HashSet<>();
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                Pair pair = new Pair(i, j, width, height);
                if(isBorderTile(pair)) {
                    borderTiles.add(pair);
                }
            }
        }
        return borderTiles;
    }

    //Returns true if pair is empty and is adjacent to a number
    private boolean isBorderTile(Pair pair) {
        if(board[pair.row][pair.col] != '-') {
            return false;
        }
        HashSet<Pair> neighbors = pair.getNeighbors();
        for(Pair neighbor: neighbors) {
            int row = neighbor.row;
            int col = neighbor.col;
            if(board[row][col] != '-' && board[row][col] != 'F') {
                return true;
            }
        }
        return false;
    }

}
