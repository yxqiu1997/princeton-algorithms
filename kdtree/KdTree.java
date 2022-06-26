/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private enum Separator { HORIZONTAL, VERTICAL }

    private Node root;

    private int size;

    /**
     * Construct an empty set of points
     */
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * Is the set empty?
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Number of points in the set
     */
    public int size() {
        return size;
    }

    /**
     * Add the point to the set (if it is not already in the set)
     */
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            root = new Node(Separator.VERTICAL, p, new RectHV(0, 0, 1, 1));
            ++size;
            return;
        }

        // find the position for insertion
        Node currNode = root;
        Node prevNode = null;
        while (currNode != null) {
            if (currNode.point.equals(p)) {
                return;
            }
            prevNode = currNode;
            currNode = currNode.isOnWest(p) || currNode.isOnSouth(p)
                       ? currNode.left : currNode.right;
        }

        // insert
        if (prevNode.isOnWest(p) || prevNode.isOnSouth(p)) {
            prevNode.left = new Node(prevNode.getNextSeparator(), p,
                                               prevNode.getRectangleNE());
        } else {
            prevNode.right = new Node(prevNode.getNextSeparator(), p,
                                              prevNode.getRectangleSW());
        }
        ++size;
    }

    /**
     * Does the set contain point p?
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node currNode = root;
        while (currNode != null) {
            if (currNode.point.equals(p)) {
                return true;
            }
            currNode = (currNode.isOnWest(p) || currNode.isOnSouth(p))
                       ? currNode.left : currNode.right;
        }
        return false;
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        drawHelper(root, null);
    }

    private void drawHelper(Node node, Node parent) {
        if (node == null) {
            return;
        }
        if (parent == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), 0, node.point.x(), 1);
        } else if (node.separator == Separator.VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rectangle.ymin(), node.point.x(), node.rectangle.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rectangle.xmin(), node.point.y(), node.rectangle.xmax(), node.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        drawHelper(node.left, node);
        drawHelper(node.right, node);
    }

    /**
     * All points that are inside the rectangle (or on the boundary)
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> pointList = new ArrayList<>();
        addPoints(root, rect, pointList);
        return pointList;
    }

    private void addPoints(Node node, RectHV rect, List<Point2D> pointList) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            pointList.add(node.point);
            addPoints(node.left, rect, pointList);
            addPoints(node.right, rect, pointList);
            return;
        }
        if (node.isOnSouth(new Point2D(rect.xmin(), rect.ymin()))
                || node.isOnWest(new Point2D(rect.xmin(), rect.ymin()))) {
            addPoints(node.left, rect, pointList);
        }
        if (!node.isOnWest(new Point2D(rect.xmax(), rect.ymax()))
                && !node.isOnSouth(new Point2D(rect.xmax(), rect.ymax()))) {
            addPoints(node.right, rect, pointList);
        }
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return isEmpty() ? null : nearestHelper(p, root.point, root);
    }

    private Point2D nearestHelper(Point2D target, Point2D closestNode, Node node) {
        if (node == null) {
            return closestNode;
        }
        // Search SW/NE recursively
        double minDistance = closestNode.distanceSquaredTo(target);
        if (node.rectangle.distanceSquaredTo(target) < minDistance) {
            double nodeDistance = node.point.distanceSquaredTo(target);
            if (nodeDistance < minDistance) {
                closestNode = node.point;
            }
            if (node.isOnSouth(target) || node.isOnWest(target)) {
                closestNode = nearestHelper(target, closestNode, node.left);
                closestNode = nearestHelper(target, closestNode, node.right);
            } else {
                closestNode = nearestHelper(target, closestNode, node.right);
                closestNode = nearestHelper(target, closestNode, node.left);
            }
        }
        return closestNode;
    }

    private class Node {

        private final Separator separator;

        private final Point2D point;

        private final RectHV rectangle;

        private Node left;

        private Node right;

        public Node(Separator separator, Point2D point, RectHV rectangle) {
            this.separator = separator;
            this.point = point;
            this.rectangle = rectangle;
        }

        public Separator getNextSeparator() {
            return separator == Separator.HORIZONTAL ? Separator.VERTICAL : Separator.HORIZONTAL;
        }

        public boolean isOnWest(Point2D otherPoint) {
            return separator == Separator.VERTICAL && point.x() > otherPoint.x();
        }

        public boolean isOnSouth(Point2D otherPoint) {
            return separator == Separator.HORIZONTAL && point.y() > otherPoint.y();
        }

        public RectHV getRectangleNE() {
            return separator == Separator.VERTICAL
                   ? new RectHV(rectangle.xmin(), rectangle.ymin(), point.x(), rectangle.ymax())
                   : new RectHV(rectangle.xmin(), rectangle.ymin(), rectangle.xmax(), point.y());
        }

        public RectHV getRectangleSW() {
            return separator == Separator.VERTICAL
                   ? new RectHV(point.x(), rectangle.ymin(), rectangle.xmax(), rectangle.ymax())
                   : new RectHV(rectangle.xmin(), point.y(), rectangle.xmax(), rectangle.ymax());
        }
    }

}
