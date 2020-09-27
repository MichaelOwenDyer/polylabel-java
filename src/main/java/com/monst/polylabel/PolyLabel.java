package com.monst.polylabel;

import java.util.PriorityQueue;

public class PolyLabel {

    public static Result polyLabel(Number[][][] polygon) {
        return polyLabel(polygon, 1.0, false);
    }

    public static Result polyLabel(Number[][][] polygon, double precision) {
        return polyLabel(polygon, precision, false);
    }

    public static Result polyLabel(Number[][][] polygon, boolean debug) {
        return polyLabel(polygon, 1.0, debug);
    }

    public static Result polyLabel(Number[][][] polygon, double precision, boolean debug) {

        // find the bounding box of the outer ring
        double minX = polygon[0][0][0].doubleValue();
        double maxX = minX;
        double minY = polygon[0][0][1].doubleValue();
        double maxY = minY;
        for (int i = 1; i < polygon[0].length; i++) {
            Number[] arr = polygon[0][i];
            if (arr[0].doubleValue() < minX) minX = arr[0].doubleValue();
            else if (arr[0].doubleValue() > maxX) maxX = arr[0].doubleValue();
            if (arr[1].doubleValue() < minY) minY = arr[1].doubleValue();
            else if (arr[1].doubleValue() > maxY) maxY = arr[1].doubleValue();
        }

        double width = maxX - minX;
        double height = maxY - minY;
        double cellSize = Math.min(width, height);
        double half = cellSize / 2;

        if (cellSize == 0) {
            return new Result(minX, minY, 0);
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

        return new Result(bestCell.x, bestCell.y, bestCell.distance);
    }

    // get polygon centroid
    private static Cell getCentroidCell(Number[][][] polygon) {
        double area = 0;
        double x = 0;
        double y = 0;
        Number[][] points = polygon[0];

        for (int i = 0, len = points.length, j = len - 1; i < len; j = i++) {
            Number[] a = points[i];
            Number[] b = points[j];
            double diff = a[0].doubleValue() * b[1].doubleValue() - b[0].doubleValue() * a[1].doubleValue();
            x += (a[0].doubleValue() + b[0].doubleValue()) * diff;
            y += (a[1].doubleValue() + b[1].doubleValue()) * diff;
            area += diff * 3;
        }
        if (area == 0)
            return new Cell(points[0][0].doubleValue(), points[0][1].doubleValue(), 0, polygon);
        return new Cell(x / area, y / area, 0, polygon);
    }

    public static class Result {
        private final double x;
        private final double y;
        private final double distance;
        private Result(double x, double y, double distance) {
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
            return new double[] {x, y};
        }

        public double getDistance() {
            return distance;
        }
    }

}
