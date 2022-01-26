/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class runs the minesweeper game
 */

package mine;

import static mine.Actions.*;

public class Game {

    private final int width, height, numMines;
    private int mineCounter;
    private boolean win = false, lose = false;
    private boolean firstClick = true;
    private int[][] internalBoard;          //Board containing mine positions
    private char[][] playBoard;             //Board user sees

    //Creates a game of specified width, height, and number of mines for the board
    public Game(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        mineCounter = numMines;
        internalBoard = new int[height][width];
        playBoard = new char[height][width];
    }

    //Computes the GameState for the specified input
    public GameState makeAction(Input input) {
        switch(input.action) {
            case LEFT: leftClick(input.row, input.col);
            break;
            case RIGHT: rightClick(input.row, input.col);
            break;
            case START: startGame();
            break;
            default:
        }
        return new GameState(copyPlayBoard(), mineCounter, win, lose);
    }

    private void startGame() {
        setupPlayBoard();
        setupInternalBoard();
        mineCounter = numMines;
        firstClick = true;
        win = false;
        lose = false;
    }

    //Sets the  playBoard to empty boxes using '-'
    private void setupPlayBoard() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                playBoard[i][j] = '-';
            }
        }
    }

    //Sets all elements of internalBoard to no mines using 0
    private void setupInternalBoard() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                internalBoard[i][j] = 0;
            }
        }
    }

    //Performs left click
    //No action is performed if a flag is clicked
    //Adjacent tiles will be revealed if a number is clicked
    private void leftClick(int row, int col) {
        if(!inBounds(row, col) || playBoard[row][col] == 'F' || playBoard[row][col] == '0' || win || lose) {
            return;
        }
        if(playBoard[row][col] != '-' && playBoard[row][col] - '0' == countFlags(row, col)) {
            for(int i=row-1;i<=row+1;i++) {
                for(int j=col-1;j<=col+1;j++) {
                    if(inBounds(i, j) && playBoard[i][j] == '-') {
                        leftClickHelper(i, j);
                    }
                }
            }
        }
        leftClickHelper(row, col);
        if(checkForWin()) {
            win = true;
            lose = false;
        }
    }

    //Performs left click on space (row, col) and recursively calls on adjacent
    //spaces if (row, col) has a value of 0
    private void leftClickHelper(int row, int col) {
        //Generates board if first click
        if(firstClick) {
            firstClick = false;
            generateBoard(row, col);
        }
        //Player loses if they click a mine
        if(internalBoard[row][col] == 1) {
            lose = true;
            win = false;
            revealMines();
            playBoard[row][col] = 'C';
            return;
        }
        int neighborMines = countMines(row, col);
        playBoard[row][col] = (char) (neighborMines + '0');

        //Call on neighbors if necessary
        if(neighborMines == 0) {
            for(int i=row-1;i<=row+1;i++) {
                for(int j=col-1;j<=col+1;j++) {
                    if(inBounds(i, j) && (i != row || j != col) && playBoard[i][j] == '-') {
                        leftClickHelper(i, j);
                    }
                }
            }
        }
    }

    //Returns the number of mines adjacent to (row, col)
    //Assumes (row, col) is a number tile
    private int countMines(int row, int col) {
        int total = 0;
        for(int i=row-1;i<=row+1;i++) {
            for(int j=col-1;j<=col+1;j++) {
                if(inBounds(i, j) && internalBoard[i][j] == 1) {
                    total++;
                }
            }
        }
        return total;
    }

    //Returns the number of flags adjacent to (row, col)
    //Assumes tile at (row, col) is a number
    private int countFlags(int row, int col) {
        int total = 0;
        for(int i=row-1;i<=row+1;i++) {
            for(int j=col-1;j<=col+1;j++) {
                if(inBounds(i, j) && playBoard[i][j] == 'F') {
                    total++;
                }
            }
        }
        return total;
    }

    //Returns true if the current board state is winning
    //False otherwise
    private boolean checkForWin() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                if(internalBoard[i][j] == 0 && playBoard[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    //Reveals mines when player loses
    private void revealMines() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                if(internalBoard[i][j] == 1 && playBoard[i][j] != 'F') {
                    playBoard[i][j] = 'M';
                }
            }
        }
    }

    //Sets (row, col) to flag if it is an empty space
    private void rightClick(int row, int col) {
        if(!inBounds(row, col) || playBoard[row][col] == '0' || win || lose || firstClick) {
            return;
        }
        if(playBoard[row][col] == '-') {
            playBoard[row][col] = 'F';
            mineCounter--;
        } else if(playBoard[row][col] == 'F') {
            playBoard[row][col] = '-';
            mineCounter++;
        } else if(playBoard[row][col] - '0' - countFlags(row, col) == countEmpty(row, col)){
            for(int i=row-1;i<=row+1;i++) {
                for(int j=col-1;j<=col+1;j++) {
                    if(inBounds(i, j) && playBoard[i][j] == '-') {
                        playBoard[i][j] = 'F';
                        mineCounter--;
                    }
                }
            }
        }
    }

    //Returns the number of empty tiles adjacent to (row, col)
    //Assumes (row, col) is a number tile
    private int countEmpty(int row, int col) {
        int total = 0;
        for(int i=row-1;i<=row+1;i++) {
            for(int j=col-1;j<=col+1;j++) {
                if(inBounds(i, j) && playBoard[i][j] == '-') {
                    total++;
                }
            }
        }
        return total;
    }

    //Returns true if row and col are within the boundaries of the board
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    //Randomly places mines in the internal board denoted by 1
    private void generateBoard(int row, int col) {
        for(int i=1;i<=numMines;i++) {
            int randRow = (int) (Math.random()*height);
            int randCol = (int) (Math.random()*width);
            while(withinRadius(row, col, randRow, randCol, 2) || internalBoard[randRow][randCol] == 1) {
                randRow = (int) (Math.random()*height);
                randCol = (int) (Math.random()*width);
            }
            internalBoard[randRow][randCol] = 1;
        }
    }

    //Returns true if (row1, col1) is within radius squares of (row2, col2) on the board
    private boolean withinRadius(int row1, int col1, int row2, int col2, int radius) {
        return Math.abs(row1 - row2) <= radius && Math.abs(col1 - col2) <= radius;
    }

    //Creates a copy of the board seen by the player
    private char[][] copyPlayBoard() {
        char[][] retPlayBoard = new char[height][width];
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                retPlayBoard[i][j] = playBoard[i][j];
            }
        }
        return retPlayBoard;
    }
}
