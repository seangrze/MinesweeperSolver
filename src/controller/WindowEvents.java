/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package controller;

import view.Board;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowEvents extends WindowAdapter {

    private Window w;

    public WindowEvents(Window w) {
        this.w = w;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        w.dispose();
    }
}
