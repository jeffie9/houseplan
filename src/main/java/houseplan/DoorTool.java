package houseplan;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Affine;
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
                    curLine.setUserData("door");
                }
            } else {
                System.out.println("Window complete");
                clearInputLayer();
                Point2D pt = closestPointOnWall(event.getX(), event.getY(), curWall);
                curLine.setEndX(pt.getX());
                curLine.setEndY(pt.getY());
                drawDoor(controller.activeLayer.getGraphicsContext2D(), curLine);
                controller.housePlan.addDoor(curLine);
                curLine = null;
                curWall = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine != null) {
                controller.userInputCanvas.setCursor(Cursor.CROSSHAIR);
                clearInputLayer();
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
        double dx = line.getEndX() - line.getStartX(), dy = line.getEndY() - line.getStartY();
        double angle = Math.atan2(dy, dx);
        double len = Math.sqrt(dx * dx + dy * dy);
        controller.rightStatusLabel.setText(String.format("%.2f", len));

        Transform transform = Transform.translate(line.getStartX(), line.getStartY());
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        Affine oldTransform = gc.getTransform();
        gc.setTransform(new Affine(transform));

        // door threshold
        gc.setLineWidth(line.getStrokeWidth() + 1.0);
        gc.setStroke(Color.WHITE);
        gc.strokeLine(0, 0, len, 0);

        // sweep line
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        gc.strokeArc(-len, -len, len * 2.0, len * 2.0, 0, -90.0, ArcType.OPEN);

        // opened door
        transform = transform.createConcatenation(Transform.rotate(60.0, 0, 0));
        gc.setTransform(new Affine(transform));
        gc.setLineWidth(4.0);
        gc.strokeLine(0, 0, len, 0);

        gc.setTransform(oldTransform);
    }

}
