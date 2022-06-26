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

public class FastCollinearPoints {

    private final List<LineSegment> lineSegmentList = new ArrayList<>();

    /**
     * Finds all line segments containing 4 points
     * @param points points to be checked
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        int n = points.length;
        for (int i = 0; i < n; ++i) {
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
        Point[] tmpPoints = pointsCopy.clone();
        for (Point point : pointsCopy) {
            Arrays.sort(tmpPoints, point.slopeOrder());
            int i = 1;
            while (i < n) {
                int j = i + 1;
                while (j < n && point.slopeTo(tmpPoints[i]) == point.slopeTo(tmpPoints[j])) {
                    j++;
                }
                if (j - i >= 3 && tmpPoints[0].compareTo(min(tmpPoints, i, j - 1)) < 0) {
                    lineSegmentList.add(new LineSegment(tmpPoints[0], max(tmpPoints, i, j - 1)));
                }
                if (j == n) {
                    break;
                }
                i = j;
            }
        }
    }

    private Point min(Point[] points, int low, int high) {
        Point minElem = points[low];
        for (int i = low + 1; i <= high; ++i) {
            if (minElem.compareTo(points[i]) > 0) {
                minElem = points[i];
            }
        }
        return minElem;
    }

    private Point max(Point[] points, int low, int high) {
        Point maxElem = points[low];
        for (int i = low + 1; i <= high; ++i) {
            if (maxElem.compareTo(points[i]) < 0) {
                maxElem = points[i];
            }
        }
        return maxElem;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
