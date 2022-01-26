# Minesweeper
Minesweeper is a game in which the player is presented a grid and must determine which tiles contain mines. Left- clicking on an empty tile will either reveal a number or a mine. If it
is the first click, it will always be a 0. This number is the number of mines that are touching that square. This includes tiles that are diagonal to it. Therefore, the number can be
from 0 to 8. If the number is 0, all neighboring squares will automatically be revealed, since none of them can be mines. Right- clicking will place a flag on the tile. This can be used
to mark where mines are on the grid. Left clicking on a number that is equal to the number of adjacent flags will reveal all adjacent empty tiles. Right- clicking on a number that is
equal to the number of empty tiles plus the number of adjacent flags will mark flags on all adjacent tiles

# How to Play

To play the game, run GUIDriver in Java 12.0.2. (I run it as a Maven Project in Intellij. It should work in other IDEs and editors, but I have not tested this). This will present the
user with a 30 x 16 grid with 99 mines. Then you can play as usual. To activate the solver, click the button titled "solve" at any time during game play and the solver will attempt to
solve the game with the given information (even if the information is incorrect).

# How it Works

The solver works by first finding the "border tiles." These are the empty tiles that are adjacent to at least one number. Then it will enumerate every valid
configuration of mines and record the number of times a mine appeared on each tile. To find every valid configuration, a backtracking algorithm is used. This means one tile is selected
to be the starting tile. There are three possibilities for this tile: either it must be a mine, must not be mine, or it may or may not be a mine. Whichever possibility it is will
determine the number of branches from this tile. For instance, if it must be a mine or must not be a mine, there will only be one branch. If it may or may not be a mine, then there will
be two branches. Then, this process will be repeated on every tile. This creates a tree, where each path on the tree is a configuration of mines. One more thing to note is the algorithm
will backtrack when it reaches a contradiction. This is usually when a tile is both "must be a mine" and "must not be a mine". The number of times a mine appears on a tile is recorded.
Then, the probability of a tile being a mine can be calculated by taking the number of mines that appeared on that tile and dividing it by the total number of configurations. Finally,
the tile with the lowest probability is chosen to be left- clicked or a tile that has a 100% chance of being a mine is right- clicked.


