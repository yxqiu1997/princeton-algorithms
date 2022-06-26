/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int sideLength;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final int[] dx = {-1, 0, 0, 1};
    private final int[] dy = {0, -1, 1, 0};
    private int numOpenSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.grid = new boolean[n][n];
        this.sideLength = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.ufFull = new WeightedQuickUnionUF(n * n + 1);
        this.numOpenSite = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException();
        }
        int index = getIndex(row, col);
        if (!grid[row - 1][col - 1]) {
            grid[row - 1][col - 1] = true;
            if (row == 1) {
                uf.union(0, index);
                ufFull.union(0, index);
            }
            if (row == sideLength) {
                uf.union(sideLength * sideLength + 1, index);
            }
            connectNeighbours(row, col);
            ++numOpenSite;
        }
    }

    // is the site (row, col) open ?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException();
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException();
        }
        return ufFull.find(getIndex(row, col)) == ufFull.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(sideLength * sideLength + 1);
    }

    // check if the row and col are valid
    private boolean isValid(int row, int col) {
        return row >= 1 && col >= 1 && row <= sideLength && col <= sideLength;
    }

    // compute the index that starts at 1 in one dimensional grid
    private int getIndex(int row, int col) {
        return (row - 1) * sideLength + col;
    }

    // connect open neighbours with the given site
    private void connectNeighbours(int row, int col) {
        int index = getIndex(row, col);
        for (int i = 0; i < 4; ++i) {
            int neighbourRow = row + dx[i];
            int neighbourCol = col + dy[i];
            if (isValid(neighbourRow, neighbourCol) && isOpen(neighbourRow, neighbourCol)) {
                uf.union(getIndex(neighbourRow, neighbourCol), index);
                ufFull.union(getIndex(neighbourRow, neighbourCol), index);
            }
        }
    }
}
