/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 */

package controller;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.*;

public class ResizeEvent extends ComponentAdapter {

    private Frame frame;

    public ResizeEvent(Frame frame) {
        this.frame = frame;
    }


    @Override
    public void componentResized(ComponentEvent e) {
        int height = frame.getHeight();
        for(Component comp: frame.getComponents()) {
            comp.setFont(new Font("Arial", Font.PLAIN, height / 25));
            frame.revalidate();
        }
    }
}
