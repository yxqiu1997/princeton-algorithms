/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> lineSegmentList = new ArrayList<>();

    /**
     * Finds all line segments containing 4 points
     * @param points points to be checked
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        int n = points.length;
        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        if (n < 4) {
            return;
        }
        Point[] pointsCopy = Arrays.copyOf(points, n);
        Arrays.sort(pointsCopy);
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = j + 1; k < n; ++k) {
                    for (int m = k + 1; m < n; ++m) {
                        double slope1 = pointsCopy[i].slopeTo(pointsCopy[j]);
                        double slope2 = pointsCopy[i].slopeTo(pointsCopy[k]);
                        double slope3 = pointsCopy[i].slopeTo(pointsCopy[m]);
                        if (slope1 == slope2 && slope1 == slope3) {
                            lineSegmentList.add(new LineSegment(pointsCopy[i], pointsCopy[m]));
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the number of line segments
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return lineSegmentList.size();
    }

    /**
     * Get the line segments list
     * @return the line segments
     */
    public LineSegment[] segments() {
        return lineSegmentList.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
