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
                }
            } else {
                System.out.println("Window complete");
                eraseLine(gc, curLine);
                Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawLine(controller.activeLayer.getGraphicsContext2D(), curLine);
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
                drawLine(gc, curLine);
            } else if (wallHitTest(event.getX(), event.getY()) != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
            } else {
                controller.userInputCanvas.setCursor(null);
            }
            break;
        }
    }

}
