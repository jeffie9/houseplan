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
                curLine.setUserData("wall");
            } else {
                System.out.println("Wall complete");
                eraseLine(gc, curLine);
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine.setEndX(snap.getX());
                curLine.setEndY(snap.getY());
                drawWall(controller.activeLayer.getGraphicsContext2D(), curLine);
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
            drawWall(gc, curLine);
            break;
        }
    }

    protected void drawWall(GraphicsContext gc, Line line) {
        Point2D start = new Point2D(line.getStartX(), line.getStartY());
        Point2D end = new Point2D(line.getEndX(), line.getEndY());
        double length = end.distance(start);
        controller.rightStatusLabel.setText(String.format("%.2f", length));

        gc.setLineWidth(line.getStrokeWidth());
        gc.setStroke(line.getStroke());
        gc.strokeLine(line.getStartX(), line.getStartY(),
                line.getEndX(), line.getEndY());
    }

}
