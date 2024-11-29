import java.util.ArrayList;
import java.util.List;

class KDTree2D {

    private static class Node {
        Point2D point;
        Node left, right;

        Node(Point2D point) { this.point = point; }
    }

    private Node root;

    // Constructor: Creates an empty kd-tree.
    public KDTree2D() { this.root = null; }

    // Inserts the given point into the tree.
    public void insert(Point2D point) {
        if (point == null) return;
        if (search(point) != null) return; // Point already exists
        root = insertRec(root, point, 0);
    }

    private Node insertRec(Node node, Point2D point, int depth) {
        if (node == null) return new Node(point);

        int cd = depth % 2; // 0 for x, 1 for y
        if ((cd == 0 && point.x() <= node.point.x()) || (cd == 1 && point.y() <= node.point.y())) {
            node.left = insertRec(node.left, point, depth + 1);
        } else {
            node.right = insertRec(node.right, point, depth + 1);
        }
        return node;
    }

    // Searches for the given point in the tree.
    public Point2D search(Point2D point) { return searchRec(root, point, 0); }

    private Point2D searchRec(Node node, Point2D point, int depth) {
        if (node == null) return null;
        if (node.point.equals(point)) return node.point;

        int cd = depth % 2; // 0 for x, 1 for y
        if ((cd == 0 && point.x() <= node.point.x()) ||
                (cd == 1 && point.y() <= node.point.y())) {
            return searchRec(node.left, point, depth + 1);
        } else {
            return searchRec(node.right, point, depth + 1);
        }
    }

    // Removes the given point from the tree.
    public void remove(Point2D point) { root = removeRec(root, point, 0); }

    private Node removeRec(Node node, Point2D point, int depth) {
        if (node == null) return null;

        if (node.point.equals(point)) {
            if (node.right != null) {
                node.point = findMinRec(node.right, depth % 2, 0);
                node.right = removeRec(node.right, node.point, depth + 1);
            } else if (node.left != null) {
                node.point = findMinRec(node.left, depth % 2, 0);
                node.right = removeRec(node.left, node.point, depth + 1);
                node.left = null;
            } else {
                return null;
            }
        } else {
            int cd = depth % 2;
            if ((cd == 0 && point.x() <= node.point.x()) ||
                    (cd == 1 && point.y() <= node.point.y())) {
                node.left = removeRec(node.left, point, depth + 1);
            } else {
                node.right = removeRec(node.right, point, depth + 1);
            }
        }
        return node;
    }

    private Point2D findMinRec(Node node, int d, int depth) {
        if (node == null) return null;

        int cd = depth % 2;
        if (cd == d) {
            if (node.left == null) return node.point;
            return findMinRec(node.left, d, depth + 1);
        }
        Point2D leftMin = findMinRec(node.left, d, depth + 1);
        Point2D rightMin = findMinRec(node.right, d, depth + 1);

        Point2D min = node.point;
        if (leftMin != null && (d == 0 ? leftMin.x() < min.x() : leftMin.y() < min.y()))
            min = leftMin;
        if (rightMin != null && (d == 0 ? rightMin.x() < min.x() : rightMin.y() < min.y()))
            min = rightMin;

        return min;
    }

    // Finds points within a circular range.
    Iterable<Point2D> printCircularRange(Point2D c, double r) {
        if (r < 0) {
            throw new IllegalArgumentException("Radius must be non-negative.");
        }
        List<Point2D> result = new ArrayList<>();
        findCircularRange(root, c, r, result, 0);
        return result;
    }

    private void findCircularRange(Node node, Point2D c, double r, List<Point2D> result, int depth) {
        if (node == null) return;

        double distance = Math.sqrt(Math.pow(node.point.x() - c.x(), 2) + Math.pow(node.point.y() - c.y(), 2));
        if (distance <= r) {
            result.add(node.point);
        }

        int cd = depth % 2;
        if ((cd == 0 && c.x() - r <= node.point.x()) || (cd == 1 && c.y() - r <= node.point.y())) {
            findCircularRange(node.left, c, r, result, depth + 1);
        }
        if ((cd == 0 && c.x() + r >= node.point.x()) || (cd == 1 && c.y() + r >= node.point.y())) {
            findCircularRange(node.right, c, r, result, depth + 1);
        }
    }
}
