import java.util.*;

class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{ x: " + x + ", y: " + y + " }";
    }
}

class ClosestPairResult {
    double distance;
    List<Point> pair;

    public ClosestPairResult(double distance, List<Point> pair) {
        this.distance = distance;
        this.pair = pair;
    }
}

public class ClosestPair {

    private static double distance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static ClosestPairResult bruteForce(List<Point> points) {
        if (points.size() < 2) {
            return new ClosestPairResult(Double.POSITIVE_INFINITY, new ArrayList<>());
        }

        double minDist = Double.POSITIVE_INFINITY;
        List<Point> pair = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double d = distance(points.get(i), points.get(j));
                if (d < minDist) {
                    minDist = d;
                    pair = new ArrayList<>(Arrays.asList(points.get(i), points.get(j)));
                }
            }
        }

        pair.sort(Comparator.comparingDouble(p -> p.x));
        return new ClosestPairResult(minDist, pair);
    }

    private static ClosestPairResult stripClosest(List<Point> strip, double d) {
        double minDist = d;
        List<Point> bestPair = new ArrayList<>();

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDist; j++) {
                double dist = distance(strip.get(i), strip.get(j));
                if (dist < minDist) {
                    minDist = dist;
                    bestPair = new ArrayList<>(Arrays.asList(strip.get(i), strip.get(j)));
                }
            }
        }

        if (bestPair.size() == 2) {
            bestPair.sort(Comparator.comparingDouble(p -> p.x));
        }

        return new ClosestPairResult(minDist, bestPair);
    }

    private static ClosestPairResult closestPairUtil(List<Point> Px, List<Point> Py) {
        int n = Px.size();

        if (n <= 3) {
            return bruteForce(Px);
        }

        int mid = n / 2;
        Point midPoint = Px.get(mid);

        List<Point> leftPx = Px.subList(0, mid);
        List<Point> rightPx = Px.subList(mid, n);

        List<Point> leftPy = new ArrayList<>();
        List<Point> rightPy = new ArrayList<>();

        for (Point p : Py) {
            if (p.x <= midPoint.x) {
                leftPy.add(p);
            } else {
                rightPy.add(p);
            }
        }

        ClosestPairResult dl = closestPairUtil(leftPx, leftPy);
        ClosestPairResult dr = closestPairUtil(rightPx, rightPy);

        ClosestPairResult dmin = (dl.distance < dr.distance) ? dl : dr;

        List<Point> strip = new ArrayList<>();
        for (Point p : Py) {
            if (Math.abs(p.x - midPoint.x) < dmin.distance) {
                strip.add(p);
            }
        }

        ClosestPairResult stripResult = stripClosest(strip, dmin.distance);

        return (stripResult.distance < dmin.distance) ? stripResult : dmin;
    }

    public static ClosestPairResult getClosestPair(List<Point> points) {
        if (points.size() < 2) {
            return new ClosestPairResult(Double.POSITIVE_INFINITY, new ArrayList<>());
        }

        List<Point> Px = new ArrayList<>(points);
        List<Point> Py = new ArrayList<>(points);

        Px.sort(Comparator.comparingDouble(p -> p.x));
        Py.sort(Comparator.comparingDouble(p -> p.y));

        ClosestPairResult result = closestPairUtil(Px, Py);

        if (result.pair.size() == 2) {
            result.pair.sort(Comparator.comparingDouble(p -> p.x));
        }

        return result;
    }

    // Example usage
    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
            new Point(1, 2),
            new Point(3, 3),
            new Point(2, 2)
        );

        ClosestPairResult res = getClosestPair(points);
        System.out.println("Distance: " + res.distance);
        System.out.println("Pair: " + res.pair);
    }
}
