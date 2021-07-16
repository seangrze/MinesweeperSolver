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

    Frame f;

    public WindowEvents(Frame f) {
        this.f = f;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        f.dispose();
    }
}
