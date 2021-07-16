/* Name: Sean Grzenda
 * Project: Minesweeper Solver
 * This class was used for testing the functionality of the game. It allows the user
 * to play minesweeper in the console. To play, enter inputs in the form: action row col. Where
 * action is either L, M, or R. And indexing is such that (0, 0) is the top left corner.
 */

package mine;

import java.util.Scanner;
import static mine.Actions.*;

public class PlayerDriver {

    public static void main(String[] args) {
        Game game = new Game(30, 16, 99);

        Scanner in = new Scanner(System.in);

        Input input = new Input(0, 0, START);
        GameState state = game.makeAction(input);
        System.out.println(state);

        while(!state.win && !state.lose) {
            char click = in.next().charAt(0);
            input.row = in.nextInt();
            input.col = in.nextInt();
            switch(click) {
                case 'L': input.action = LEFT;
                break;
                case 'M': input.action = MIDDLE;
                break;
                case 'R': input.action = RIGHT;
                break;
            }
            state = game.makeAction(input);
            System.out.println(state);
        }

    }

    public static void displayBoard(GameState state) {
        for(int i=0;i<state.board.length;i++) {
            for(int j=0;j<state.board[0].length;j++) {
                System.out.print(state.board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
