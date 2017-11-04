package houseplan;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class PolyLineTool extends Tool {

    public PolyLineTool(MainFrameController controller) {
        super(controller);
    }

    private Line curLine = null;
    private List<Point2D> points = null;

    @Override
    public void handleMouseEvent(MouseEvent event) {
        final GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        switch (event.getEventType().getName()) {
        case "MOUSE_CLICKED":
            if (event.getClickCount() > 1) {
                System.out.println("clicks: " + event.getClickCount());
                System.out.println("polyline: " + points);
                points = null;
                curLine = null;
            } else {
                if (points == null) {
                    System.out.println("Start new line");
                    points = new ArrayList<>();
                }
                Point2D snap = snapToGrid(event.getX(), event.getY());
                points.add(snap);
                if (curLine == null) {
                    curLine = new Line(
                            snap.getX(), snap.getY(),
                            snap.getX(), snap.getY()
                            );
                    curLine.setStrokeWidth(1.0);
                    curLine.setStroke(Color.BLACK);
                } else {
                    System.out.println("Add vertex");
                    eraseLine(gc, curLine);
                    curLine.setEndX(snap.getX());
                    curLine.setEndY(snap.getY());
                    drawLine(controller.activeLayer.getGraphicsContext2D(), curLine);
                    curLine.setStartX(snap.getX());
                    curLine.setStartY(snap.getY());
                }
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

    private void drawLine(GraphicsContext gc, Line line) {
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
