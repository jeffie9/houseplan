package houseplan;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class WallTool extends Tool {

    public WallTool(MainFrameController controller) {
        super(controller);
    }

    private Line curLine = null;

    @Override
    public void handleMouseEvent(MouseEvent event) {
        final GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        switch (event.getEventType().getName()) {
        case "MOUSE_CLICKED":
            if (curLine == null) {
                System.out.println("Start new wall");
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine = new Line(
                        snap.getX(), snap.getY(),
                        snap.getX(), snap.getY()
                        );
                curLine.setStrokeWidth(controller.wallWidth);
                curLine.setStroke(Color.BLACK);
            } else {
                System.out.println("Wall complete");
                eraseLine(gc, curLine);
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine.setEndX(snap.getX());
                curLine.setEndY(snap.getY());
                drawLine(controller.activeLayer.getGraphicsContext2D(), curLine);
                controller.housePlan.addWall(curLine);
                curLine = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine == null) {
                break;
            }

            eraseLine(gc, curLine);
            curLine.setEndX(event.getX());
            curLine.setEndY(event.getY());
            drawLine(gc, curLine);
            break;
        }
    }

}
