package houseplan;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class WindowTool extends Tool {

    public WindowTool(MainFrameController controller) {
        super(controller);
    }

    private Line curLine = null;
    private Shape curWall = null;

    @Override
    public void handleMouseEvent(MouseEvent event) {
        final GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        switch (event.getEventType().getName()) {
        case "MOUSE_CLICKED":
            if (curLine == null) {
                System.out.println("Start new window");
                if ((curWall = hitTest(event.getX(), event.getY())) != null) {
                    Point2D pt = closestPointOnWall(event.getX(), event.getY());
                    curLine = new Line(
                            pt.getX(), pt.getY(), 
                            pt.getX(), pt.getY()
                            );
                    curLine.setStrokeWidth(controller.wallWidth);
                    curLine.setStroke(Color.LIGHTYELLOW);
                }
            } else {
                System.out.println("Window complete");
                Bounds b = curLine.getBoundsInLocal();
                gc.clearRect(
                        b.getMinX() - curLine.getStrokeWidth(),
                        b.getMinY() - curLine.getStrokeWidth(),
                        b.getMaxX() + curLine.getStrokeWidth(),
                        b.getMaxY() + curLine.getStrokeWidth());
                Point2D pt = closestPointOnWall(event.getX(), event.getY());
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawWindow(controller.activeLayer.getGraphicsContext2D());
                curLine = null;
                curWall = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
                Bounds b = curLine.getBoundsInLocal();
                gc.clearRect(
                        b.getMinX() - curLine.getStrokeWidth(),
                        b.getMinY() - curLine.getStrokeWidth(),
                        b.getMaxX() + curLine.getStrokeWidth(),
                        b.getMaxY() + curLine.getStrokeWidth());
                Point2D pt = closestPointOnWall(event.getX(), event.getY());
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawWindow(gc);
            } else if (hitTest(event.getX(), event.getY()) != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
            } else {
                controller.userInputCanvas.setCursor(null);
            }
            break;
        }
    }

    private Shape hitTest(double x, double y) {
        for (Shape s : controller.housePlan.getWalls()) {
            if (s.contains(x, y)) {
                return s;
            }
        }
        return null;
    }

    private Point2D closestPointOnWall(double x, double y) {
        if (curWall instanceof Line) {
            Line l = Line.class.cast(curWall);
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

    private void drawWindow(GraphicsContext gc) {
        gc.setLineWidth(curLine.getStrokeWidth());
        gc.setStroke(curLine.getStroke());
        gc.strokeLine(curLine.getStartX(), curLine.getStartY(),
                curLine.getEndX(), curLine.getEndY());
    }

}
