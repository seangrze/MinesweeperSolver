/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package controller;

import mine.*;
import view.*;

import java.awt.event.*;

public class TileEvent extends MouseAdapter {

    private Tile tile;
    private Game game;
    private Board board;

    public TileEvent(Tile tile, Game game, Board board) {
        this.tile = tile;
        this.game = game;
        this.board = board;
    }

    public void mousePressed(MouseEvent e) {
        GameState state = new GameState(null, 0, false, false);
        if(e.getButton() == MouseEvent.BUTTON1) {
            state = game.makeAction(new Input(tile.row, tile.col, Actions.LEFT));
        } else if(e.getButton() == MouseEvent.BUTTON2) {
            state = game.makeAction(new Input(tile.row, tile.col, Actions.MIDDLE));
        } else {
            state = game.makeAction(new Input(tile.row, tile.col, Actions.RIGHT));
        }
        board.display(state);
    }

    public void mouseReleased(MouseEvent e) {

    }

}
