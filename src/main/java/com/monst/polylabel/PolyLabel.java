package com.monst.polylabel;

import java.util.PriorityQueue;

public class PolyLabel {

    public static PolyLabel polyLabel(Number[][][] polygon) {
        return polyLabel(polygon, 1.0, false);
    }

    public static PolyLabel polyLabel(Number[][][] polygon, double precision) {
        return polyLabel(polygon, precision, false);
    }

    public static PolyLabel polyLabel(Number[][][] polygon, boolean debug) {
        return polyLabel(polygon, 1.0, debug);
    }

    public static PolyLabel polyLabel(Number[][][] polygon, double precision, boolean debug) {

        // find the bounding box of the outer ring
        double minX = polygon[0][0][0].doubleValue();
        double maxX = minX;
        double minY = polygon[0][0][1].doubleValue();
        double maxY = minY;
        for (int i = 1; i < polygon[0].length; i++) {
            double x = polygon[0][i][0].doubleValue();
            double y = polygon[0][i][1].doubleValue();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        double width = maxX - minX;
        double height = maxY - minY;
        double cellSize = Math.min(width, height);
        double half = cellSize / 2;

        if (cellSize == 0) {
            return new PolyLabel(minX, minY, 0);
        }

        // a priority queue of cells in order of their "potential" (max distance to polygon)
        PriorityQueue<Cell> cellQueue = new PriorityQueue<>(Cell::compareTo);

        for (double x = minX; x < maxX; x += cellSize)
            for (double y = minY; y < maxY; y += cellSize)
                cellQueue.add(new Cell(x + half, y + half, half, polygon));

        // take centroid as the first best guess
        Cell bestCell = getCentroidCell(polygon);

        // special case for rectangular polygons
        Cell bboxCell = new Cell(minX + width / 2, minY + height / 2, 0, polygon);
        if (bboxCell.distance > bestCell.distance)
            bestCell = bboxCell;

        int numProbes = cellQueue.size();

        while (cellQueue.size() > 0) {
            // pick the most promising cell from the queue
            Cell cell = cellQueue.poll();

            // update the best cell if we found a better one
            if (cell.distance > bestCell.distance) {
                bestCell = cell;
                if (debug)
                    System.out.printf("Found best %s after %d probes%n\n", String.format("%.2f", cell.distance), numProbes);
            }

            // do not drill down further if there's no chance of a better solution
            if (cell.max - bestCell.distance <= precision)
                continue;

            // split the cell into four cells
            half = cell.half / 2;
            cellQueue.add(new Cell(cell.x - half, cell.y - half, half, polygon));
            cellQueue.add(new Cell(cell.x + half, cell.y - half, half, polygon));
            cellQueue.add(new Cell(cell.x - half, cell.y + half, half, polygon));
            cellQueue.add(new Cell(cell.x + half, cell.y + half, half, polygon));
            numProbes += 4;
        }

        if (debug) {
            System.out.printf("Num probes: %d\n", numProbes);
            System.out.printf("Best distance: %f\n", bestCell.distance);
        }

        return new PolyLabel(bestCell.x, bestCell.y, bestCell.distance);
    }

    // get polygon centroid
    private static Cell getCentroidCell(Number[][][] polygon) {
        double area = 0;
        double x = 0;
        double y = 0;
        Number[][] points = polygon[0];

        for (int i = 0, len = points.length, j = len - 1; i < len; j = i++) {
            double a0 = points[i][0].doubleValue();
            double a1 = points[i][1].doubleValue();
            double b0 = points[j][0].doubleValue();
            double b1 = points[j][1].doubleValue();
            double diff = a0 * b1 - b0 * a1;
            x += (a0 + b0) * diff;
            y += (a1 + b1) * diff;
            area += diff * 3;
        }
        if (area == 0)
            return new Cell(points[0][0].doubleValue(), points[0][1].doubleValue(), 0, polygon);
        return new Cell(x / area, y / area, 0, polygon);
    }

    private final double x, y, distance;

    private PolyLabel(double x, double y, double distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double[] getCoordinates() {
        return new double[]{x, y};
    }

    public double getDistance() {
        return distance;
    }

}
