/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package view;

import mine.*;
import static mine.Actions.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import controller.*;
import solver.Solver;

import javax.swing.*;

public class Board extends Frame{

    private Tile[][] board;
    private Label flagCounter;
    private Game game;
    private Dialog message;

    public Board(Game game) {
        this.game = game;
        setupBoard(this.game.makeAction(new Input(0, 0, START)));
    }

    //Sets up the screen with a grid of buttons
    private void setupBoard(GameState initial) {
        int width = initial.board[0].length;
        int height = initial.board.length;
        board = new Tile[height][width];

        setSize(500, 500);

        Panel grid = new Panel();
        grid.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 2;
        flagCounter = new Label("Flags: " + initial.mines);
        grid.add(flagCounter, gbc);
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        for(int i=2;i<=height+1;i++) {
            for(int j=0;j<width;j++) {
                Tile tile = new Tile(i-2, j);
                gbc.gridx = j;
                gbc.gridy = i;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                tile.addMouseListener(new TileEvent(tile, game, this));
                grid.add(tile, gbc);
                board[i-2][j] = tile;
            }
        }
        add(grid);
        addComponentListener(new ResizeEvent(this));
        setVisible(true);
        addWindowListener(new WindowEvents(this));
        setFocusable(true);
        addKeyListener(new SolverEvent(this, game));
        message = new Dialog(this, "Message", true);
        message.setLayout(new FlowLayout());
        message.setBounds(50, 50, 300, 100);
        message.addWindowListener(new WindowEvents(message));
    }

    //Displays state on the screen and messages for if you win or lose
    public void display(GameState state) {
        flagCounter.setText("Flags: " + state.mines);
        int width = state.board[0].length;
        int height = state.board.length;
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                if(state.board[i][j] != '-' && state.board[i][j] != '0') {
                    board[i][j].setForeground(Color.WHITE);
                    board[i][j].setLabel("" + state.board[i][j]);
                } else {
                    board[i][j].setLabel("");
                }
                if(state.board[i][j] != '-' && state.board[i][j] != 'F') {
                    board[i][j].setBackground(Color.DARK_GRAY);
                }
            }
        }
        if(state.win) {
            message.add(new Label("You Win"));
            message.setVisible(true);
        }
        if(state.lose) {
            message.add(new Label("You Lose"));
            message.setVisible(true);
        }
    }

    //Sends state to solver and gets input
    //Repeats process until it wins or loses
    public void useSolver(GameState state) {
        Solver solver = new Solver();
        GameState current = state;
        while(!current.win && !current.lose) {
            current = game.makeAction(solver.solve(current));
            display(current);
        }
        if(state.win) {
            message.add(new Label("You Win"));
            message.setVisible(true);
        }
        if(state.lose) {
            message.add(new Label("You Lose"));
            message.setVisible(true);
        }
    }

    //Displays board to console
    public static void displayBoard(GameState state) {
        for(int i=0;i<state.board.length;i++) {
            for(int j=0;j<state.board[0].length;j++) {
                System.out.print(state.board[i][j] + " ");
            }
            System.out.println();
        }
    }




}
