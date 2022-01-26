/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package controller;

import mine.Actions;
import mine.Game;
import mine.Input;
import solver.Solver;
import view.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SolverEvent implements ActionListener {

    private Board board;
    private Game game;

    public SolverEvent(Board board, Game game) {
        this.board = board;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        board.useSolver(game.makeAction(new Input(0, 0, Actions.CURRENT)));
    }
}
