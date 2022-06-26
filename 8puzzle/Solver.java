/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Deque;
import java.util.LinkedList;

public class Solver {

    private boolean isSolvable;

    private SearchNode solution;

    /**
     * Find a solution to the initial board (using the A* algorithm)
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> minPQ = new MinPQ<>();
        minPQ.insert(new SearchNode(initial, 0, null));

        while (!isSolvable) {
            SearchNode node = minPQ.delMin();
            Board board = node.getBoard();
            if (board.isGoal()) {
                isSolvable = true;
                solution = node;
                break;
            }
            if (board.twin().isGoal()) {
                isSolvable = false;
                break;
            }
            int currMoves = node.getMoves();
            Board prevBoard = currMoves > 0 ? node.getPrevNode().getBoard() : null;
            for (Board next : board.neighbors()) {
                if (next.equals(prevBoard)) {
                    continue;
                }
                minPQ.insert(new SearchNode(next, currMoves + 1, node));
            }
        }
    }

    /**
     * Is the initial board solvable?
     */
    public boolean isSolvable() {
        return isSolvable;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        return isSolvable ? solution.getMoves() : -1;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        }
        Deque<Board> solutionList = new LinkedList<>();
        SearchNode node = solution;
        while (node != null) {
            solutionList.addFirst(node.getBoard());
            node = node.prevNode;
        }
        return solutionList;
    }

    private class SearchNode implements Comparable<SearchNode> {

        private final Board board;

        private final int moves;

        private final SearchNode prevNode;

        private final int manhattanDistance;

        public SearchNode(Board board, int moves, SearchNode prevNode) {
            this.board = board;
            this.moves = moves;
            this.prevNode = prevNode;
            this.manhattanDistance = board.manhattan();
        }

        public int compareTo(SearchNode node) {
            return this.getPriority() - node.getPriority();
        }

        private int getPriority() {
            return manhattanDistance + moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrevNode() {
            return prevNode;
        }
    }

    /**
     * Unit testing
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
