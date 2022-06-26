/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;

    private int blankRow;

    private int blankCol;

    /**
     * Create a board from an n-by-n array of tiles,
     * where tiles[row][col] = title at (row, col)
     */
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0 || tiles[0].length == 0) {
            throw new IllegalArgumentException();
        }

        // clone the tile
        this.tiles = cloneTile(tiles);

        // find the blank square
        for (int row = 0; row < tiles.length; ++row) {
            for (int col = 0; col < tiles.length; ++col) {
                if (tiles[row][col] == 0) {
                    this.blankRow = row;
                    this.blankCol = col;
                    break;
                }
            }
        }
    }

    private int[][] cloneTile(int[][] tile) {
        int[][] copy = new int[tile.length][];
        for (int row = 0; row < tile.length; ++row) {
            copy[row] = tile[row].clone();
        }
        return copy;
    }

    /**
     * String representation of this board
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append("\n");
        for (int row = 0; row < dimension(); ++row) {
            for (int col = 0; col < dimension(); ++col) {
                sb.append(String.format("%2d ", tiles[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Board dimension n
     */
    public int dimension() {
        return tiles.length;
    }

    /**
     * Number of tiles out of place
     */
    public int hamming() {
        int num = 0;
        for (int row = 0; row < dimension(); ++row) {
            for (int col = 0; col < dimension(); ++col) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                if (getManhattanDistance(row, col) != 0) {
                    ++num;
                }
            }
        }
        return num;
    }

    /**
     * Sum of Manhattan distances between tiles and goal
     */
    public int manhattan() {
        int num = 0;
        for (int row = 0; row < dimension(); ++row) {
            for (int col = 0; col < dimension(); ++col) {
                if (row == blankRow && col == blankCol) {
                    continue;
                }
                num += getManhattanDistance(row, col);
            }
        }
        return num;
    }

    private int getManhattanDistance(int row, int col) {
        int targetVal = tiles[row][col] - 1;
        int targetRow = targetVal / dimension();
        int targetCol = targetVal % dimension();
        return Math.abs(row - targetRow) + Math.abs(col - targetCol);
    }

    /**
     * Is this board the goal board?
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * Does this board equal y?
     */
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass() || ((Board) y).dimension() != this.dimension()) {
            return false;
        }
        Board targetTile = (Board) y;
        for (int row = 0; row < dimension(); ++row) {
            for (int col = 0; col < dimension(); ++col) {
                if (tiles[row][col] != targetTile.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * All neighboring boards
     */
    public Iterable<Board> neighbors() {
        List<Board> neighborList = new ArrayList<>();

        if (blankRow > 0) {
            int[][] tile = cloneTile(tiles);
            swap(tile, blankRow, blankCol, blankRow - 1, blankCol);
            neighborList.add(new Board(tile));
        }
        if (blankRow < dimension() - 1) {
            int[][] tile = cloneTile(tiles);
            swap(tile, blankRow, blankCol, blankRow + 1, blankCol);
            neighborList.add(new Board(tile));
        }
        if (blankCol > 0) {
            int[][] tile = cloneTile(tiles);
            swap(tile, blankRow, blankCol, blankRow, blankCol - 1);
            neighborList.add(new Board(tile));
        }
        if (blankCol < dimension() - 1) {
            int[][] tile = cloneTile(tiles);
            swap(tile, blankRow, blankCol, blankRow, blankCol + 1);
            neighborList.add(new Board(tile));
        }
        return neighborList;
    }

    private void swap(int[][] tile, int row, int col, int targetRow, int targetCol) {
        int tmp = tile[row][col];
        tile[row][col] = tile[targetRow][targetCol];
        tile[targetRow][targetCol] = tmp;
    }

    /**
     * A board that is obtained by exchanging any pair of tiles
     */
    public Board twin() {
        int[][] tile = cloneTile(tiles);
        if (blankRow == 0) {
            swap(tile, 1, 0, 1, 1);
        } else {
            swap(tile, 0, 0, 0, 1);
        }
        return new Board(tile);
    }
}
