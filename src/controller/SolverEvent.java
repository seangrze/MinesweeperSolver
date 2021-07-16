/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package controller;

import mine.Actions;
import mine.Game;
import mine.Input;
import solver.Solver;
import view.Board;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SolverEvent extends KeyAdapter {

    private Board board;
    private Game game;
    private Solver solver;

    public SolverEvent(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.solver = new Solver();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == 's') {
            board.useSolver(game.makeAction(new Input(0, 0, Actions.CURRENT)));
        }
    }
}
