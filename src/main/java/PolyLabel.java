import java.util.PriorityQueue;

public class PolyLabel {

    static Result polyLabel(double[][][] polygon) {
        return polyLabel(polygon, 1.0, false);
    }

    static Result polyLabel(double[][][] polygon, double precision) {
        return polyLabel(polygon, precision, false);
    }

    static Result polyLabel(double[][][] polygon, boolean debug) {
        return polyLabel(polygon, 1.0, debug);
    }

    static Result polyLabel(double[][][] polygon, double precision, boolean debug) {

        double minX = polygon[0][0][0];
        double maxX = minX;
        double minY = polygon[0][0][1];
        double maxY = minY;
        for (int i = 1; i < polygon[0].length; i++) {
            double[] arr = polygon[0][i];
            if (arr[0] < minX) minX = arr[0];
            if (arr[0] > maxX) maxX = arr[0];
            if (arr[1] < minY) minY = arr[1];
            if (arr[1] > maxY) maxY = arr[1];
        }

        double width = maxX - minX;
        double height = maxY - minY;
        double cellSize = Math.min(width, height);
        double half = cellSize / 2;

        if (cellSize == 0) {
            return new Result(0, 0, 0);
        }

        java.util.Queue<Cell> cellQueue = new PriorityQueue<>((cell1, cell2) -> (int) (cell2.max - cell1.max));

        for (double x = minX; x < maxX; x += cellSize)
            for (double y = minY; y < maxY; y += cellSize)
                cellQueue.add(new Cell(x + half, y + half, half, polygon));

        Cell bestCell = getCentroidCell(polygon);

        Cell bboxCell = new Cell(minX + width / 2, minY + height / 2, 0, polygon);
        if (bboxCell.distance > bestCell.distance)
            bestCell = bboxCell;

        int numProbes = cellQueue.size();

        while (cellQueue.size() > 0) {
            Cell cell = cellQueue.poll();

            if (cell.distance > bestCell.distance) {
                bestCell = cell;
                if (debug)
                    System.out.printf("Found best %s after %d probes", String.format("%.2f", cell.distance), numProbes);
            }

            if (cell.max - bestCell.distance <= precision)
                continue;

            half = cell.half / 2;
            cellQueue.add(new Cell(cell.x - half, cell.y - half, half, polygon));
            cellQueue.add(new Cell(cell.x + half, cell.y - half, half, polygon));
            cellQueue.add(new Cell(cell.x - half, cell.y + half, half, polygon));
            cellQueue.add(new Cell(cell.x + half, cell.y + half, half, polygon));
            numProbes += 4;
        }

        if (debug) {
            System.out.println("Num probes: " + numProbes);
            System.out.println("Best distance: " + bestCell.distance);
        }

        return new Result(bestCell.x, bestCell.y, bestCell.distance);
    }
    
    static Cell getCentroidCell(double[][][] polygon) {
        double area = 0;
        double x = 0;
        double y = 0;
        double[][] points = polygon[0];

        for (int i = 0, len = points.length, j = len - 1; i < len; j = i++) {
            double[] a = points[i];
            double[] b = points[j];
            double diff = a[0] * b[1] - b[0] * a[1];
            x += (a[0] + b[0]) * diff;
            y += (a[1] + b[1]) * diff;
            area += diff * 3;
        }
        if (area == 0)
            return new Cell(points[0][0], points[0][1], 0, polygon);
        return new Cell(x / area, y / area, 0, polygon);
    }

    static class Result {
        double x, y, distance;
        Result(double x, double y, double distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

}
