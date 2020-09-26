public class Cell {
    double x, y, half, distance, max;
    double[][][] polygon;

    Cell(double x, double y, double half, double[][][] polygon) {
        this.x = x;
        this.y = y;
        this.half = half;
        this.distance = pointToPolygonDist(x, y, polygon);
        this.polygon = polygon;
        this.max = this.distance + this.half * Math.sqrt(2.0);
    }

    private static double pointToPolygonDist(double x, double y, double[][][] polygon) {
        boolean inside = false;
        double minDistSq = Double.MAX_VALUE;

        for (double[][] ring : polygon)
            for (int i = 0; i < ring.length; i++) {
                double[] current = ring[(i + 1) % ring.length];
                double[] prev = ring[i];

                if ((current[1] > y != prev[1] > y)
                        && (x < (prev[0] - current[0]) * (y - current[1]) / (prev[1] - current[1]) + current[0]))
                    inside = !inside;

                double distSq = getSegDistSq(x, y, current, prev);
                if (distSq < minDistSq)
                    minDistSq = distSq;
            }

        return minDistSq == 0 ? 0 : (inside ? 1 : -1) * Math.sqrt(minDistSq);
    }

    private static double getSegDistSq(double px, double py, double[] a, double[] b) {

        double x = a[0];
        double y = a[1];
        double dx = b[0] - x;
        double dy = b[1] - y;

        if (dx != 0 || dy != 0) {

            double t = ((px - x) * dx + (py - y) * dy) / (dx * dx + dy * dy);

            if (t > 1) {
                x = b[0];
                y = b[1];

            } else if (t > 0) {
                x += dx * t;
                y += dy * t;
            }
        }

        dx = px - x;
        dy = py - y;

        return dx * dx + dy * dy;
    }

}
