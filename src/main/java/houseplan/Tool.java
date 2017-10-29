package houseplan;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public abstract class Tool {
    protected final MainFrameController controller;

    public Tool(MainFrameController controller) {
        this.controller = controller;
    }

    public abstract void handleMouseEvent(MouseEvent event);

    protected void eraseLine(GraphicsContext gc, Line line) {
        Bounds b = line.getBoundsInLocal();
        gc.clearRect(
                b.getMinX() - line.getStrokeWidth(),
                b.getMinY() - line.getStrokeWidth(),
                b.getMaxX() + line.getStrokeWidth(),
                b.getMaxY() + line.getStrokeWidth());

    }

    protected Point2D snapToGrid(double x, double y) {
        double nx = Math.floor(x / controller.gridWidth) * controller.gridWidth;
        double ny = Math.floor(y / controller.gridWidth) * controller.gridWidth;
        double dx = x - nx;
        double dy = y - ny;
        double hg = controller.gridWidth / 2.0;
        if (dx > hg) nx += controller.gridWidth;
        if (dy > hg) ny += controller.gridWidth;
        //System.out.println("x: " + x + ", y: " + y + ", dx: " + dx + ", dy: " + dy + ", hg: " + hg + ", nx: " + nx + ", ny: " + ny);
        return new Point2D(nx, ny);
    }

    protected Shape wallHitTest(double x, double y) {
        for (Shape s : controller.housePlan.getWalls()) {
            if (s.contains(x, y)) {
                return s;
            }
        }
        return null;
    }

    protected Point2D closestPointOnWall(double x, double y, Shape wall) {
        if (wall instanceof Line) {
            //               (P2-P1)dot(v)
            //    Pr = P1 +  ------------- * v
            //                 (v)dot(v)
            Line l = Line.class.cast(wall);
            Point2D p2 = new Point2D(x, y);
            Point2D p1 = new Point2D(l.getStartX(), l.getStartY());
            Point2D v = new Point2D(l.getEndX() - l.getStartX(), l.getEndY() - l.getStartY());
            Point2D pr = v.multiply(p2.subtract(p1).dotProduct(v) / v.dotProduct(v)).add(p1);
            if (!l.contains(pr)) {
                if (p2.distance(p1) > p2.distance(l.getEndX(), l.getEndY())) {
                    pr = new Point2D(l.getEndX(), l.getEndY());
                } else {
                    pr = p1;
                }
            }
            return pr;
        } else {
            // TODO handle arc
            return new Point2D(x, y);
        }
    }

    public double angle(Point2D start, Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = start.getY() - end.getY();  // y reversed in screen coordinates
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    protected void clearInputLayer() {
        GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, controller.userInputCanvas.getWidth(),
                controller.userInputCanvas.getHeight());
    }
}
