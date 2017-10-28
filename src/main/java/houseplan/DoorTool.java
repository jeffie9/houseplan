package houseplan;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Transform;

public class DoorTool extends Tool {

    public DoorTool(MainFrameController controller) {
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
                }
            } else {
                System.out.println("Window complete");
                eraseLine(gc, curLine);
                Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawDoor(controller.activeLayer.getGraphicsContext2D(), curLine);
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
                drawDoor(gc, curLine);
            } else if (wallHitTest(event.getX(), event.getY()) != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
            } else {
                controller.userInputCanvas.setCursor(null);
            }
            break;
        }
    }

    protected void drawDoor(GraphicsContext gc, Line line) {
        Point2D start = new Point2D(line.getStartX(), line.getStartY());
        Point2D end = new Point2D(line.getEndX(), line.getEndY());
        double length = end.distance(start);
//        double angle = angle(start, end);
        controller.rightStatusLabel.setText(String.format("%.2f", length));

        gc.setLineWidth(line.getStrokeWidth() + 1.0);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(line.getStartX(), line.getStartY(),
                line.getEndX(), line.getEndY());

        // opening door by angle
        Point2D pt = Transform.rotate(30.0, line.getStartX(), line.getStartY())
                .transform(line.getEndX(), line.getEndY());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4.0);
        gc.strokeLine(line.getStartX(), line.getStartY(),
                pt.getX(), pt.getY());

        // and sweep line
        gc.setLineWidth(1.0);
//        gc.strokeArc(start.getX() - doorWidth, start.getY() - doorWidth,
//                2.0 * doorWidth, 2.0 * doorWidth,
//                angle, angle - 10.0, ArcType.OPEN);
    }

}
