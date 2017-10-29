package houseplan;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public class DimensionTool extends Tool {

    public DimensionTool(MainFrameController controller) {
        super(controller);
    }

    private final int ARR_SIZE = 8;
    private Line curLine = null;

    @Override
    public void handleMouseEvent(MouseEvent event) {
        final GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        switch (event.getEventType().getName()) {
        case "MOUSE_CLICKED":
            if (curLine == null) {
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine = new Line(
                        snap.getX(), snap.getY(),
                        snap.getX(), snap.getY()
                        );
                curLine.setStrokeWidth(1.0);
                curLine.setStroke(Color.DARKGREY);
            } else {
                clearInputLayer();
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine.setEndX(snap.getX());
                curLine.setEndY(snap.getY());
                drawArrow(controller.activeLayer.getGraphicsContext2D(), curLine);
                controller.housePlan.addWall(curLine);
                curLine = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine == null) {
                break;
            }

            clearInputLayer();
            curLine.setEndX(event.getX());
            curLine.setEndY(event.getY());
            drawArrow(gc, curLine);
            break;
        }
    }

    void drawArrow(GraphicsContext gc, Line line) {
        gc.setStroke(Color.DARKGREY);

        double dx = line.getEndX() - line.getStartX(), dy = line.getEndY() - line.getStartY();
        double angle = Math.atan2(dy, dx);
        double len = Math.sqrt(dx * dx + dy * dy);
        String text = String.format("%.2f", len);
        controller.rightStatusLabel.setText(text);

        Transform transform = Transform.translate(line.getStartX(), line.getStartY());
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        Affine oldTransform = gc.getTransform();
        gc.setTransform(new Affine(transform));

        double mp = len / 2.0;
        double textWidth = text.length() / 0.2; // half the width times 10 pixels - needs better way
        gc.setLineWidth(1.0);
        gc.strokeLine(0, 0, mp - textWidth, 0);
        gc.strokeLine(mp + textWidth, 0, len, 0);
        gc.strokePolyline(new double[]{len - ARR_SIZE, len, len - ARR_SIZE},
                new double[]{-ARR_SIZE, 0, ARR_SIZE},
                3);
        gc.strokePolyline(new double[]{ARR_SIZE, 0, ARR_SIZE},
                new double[]{-ARR_SIZE, 0, ARR_SIZE},
                3);

        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText(text, mp, 0);

        gc.setTransform(oldTransform);
    }
}
