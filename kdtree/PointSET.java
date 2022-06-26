/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> pointSet;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    /**
     * Number of points in the set
     */
    public int size() {
        return pointSet.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointSet.add(p);
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    /**
     * All points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Point2D smallestPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D largestPoint = new Point2D(rect.xmax(), rect.ymax());
        List<Point2D> pointsInRectangle = new ArrayList<>();
        for (Point2D p : pointSet.subSet(smallestPoint, true, largestPoint, true)) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax()) {
                pointsInRectangle.add(p);
            }
        }
        return pointsInRectangle;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearestPoint = null;
        double minDistance = 0.0;
        int num = 0;
        for (Point2D point : pointSet) {
            ++num;
            if (num == 1) {
                minDistance = p.distanceSquaredTo(point);
                nearestPoint = point;
                continue;
            }
            if (p.distanceSquaredTo(point) < minDistance) {
                nearestPoint = point;
                minDistance = p.distanceSquaredTo(point);
            }
        }
        return nearestPoint;
    }

}
