package houseplan;

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
                if ((curWall = wallHitTest(event.getX(), event.getY())) != null) {
                    Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                    curLine = new Line(
                            pt.getX(), pt.getY(), 
                            pt.getX(), pt.getY()
                            );
                    curLine.setStrokeWidth(controller.wallWidth);
                    curLine.setStroke(Color.LIGHTYELLOW);
                    curLine.setUserData("window");
                }
            } else {
                System.out.println("Window complete");
                eraseLine(gc, curLine);
                Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawWindow(controller.activeLayer.getGraphicsContext2D(), curLine);
                controller.housePlan.addWindow(curLine);
                curLine = null;
                curWall = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
                eraseLine(gc, curLine);
                Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawWindow(gc, curLine);
            } else if (wallHitTest(event.getX(), event.getY()) != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
            } else {
                controller.userInputCanvas.setCursor(null);
            }
            break;
        }
    }

    protected void drawWindow(GraphicsContext gc, Line line) {
        Point2D start = new Point2D(line.getStartX(), line.getStartY());
        Point2D end = new Point2D(line.getEndX(), line.getEndY());
        double length = end.distance(start);
        controller.rightStatusLabel.setText(String.format("%.2f", length));

        // simple fixed window
        gc.setLineWidth(line.getStrokeWidth());
        gc.setStroke(Color.WHITE);
        gc.strokeLine(line.getStartX(), line.getStartY(),
                line.getEndX(), line.getEndY());
        gc.setLineWidth(1.0);
        gc.setStroke(Color.BLACK);
        gc.strokeLine(line.getStartX(), line.getStartY(),
                line.getEndX(), line.getEndY());
    }

}
