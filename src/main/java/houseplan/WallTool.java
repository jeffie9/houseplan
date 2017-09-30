package houseplan;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
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
                curLine = new Line(
                        event.getX(), event.getY(), 
                        event.getX(), event.getY()
                        );
            } else {
                System.out.println("Wall complete");
                drawWall(controller.activeLayer.getGraphicsContext2D());
                Bounds b = curLine.getBoundsInLocal();
                gc.clearRect(b.getMinX() - 10, b.getMinY() - 10,
                        b.getMaxX() + 10, b.getMaxY() + 10);
                curLine = null;
            }
            break;
        case "MOUSE_MOVED":
            if (curLine == null) {
                break;
            }

            Bounds b = curLine.getBoundsInLocal();
            gc.clearRect(b.getMinX() - 10, b.getMinY() - 10,
                    b.getMaxX() + 10, b.getMaxY() + 10);
            curLine.setEndX(event.getX());
            curLine.setEndY(event.getY());
            drawWall(gc);
            break;
        }
    }

    private void drawWall(GraphicsContext gc) {
        double oldWidth = gc.getLineWidth();
        gc.setLineWidth(7.0);
        gc.strokeLine(curLine.getStartX(), curLine.getStartY(),
                curLine.getEndX(), curLine.getEndY());
        gc.setLineWidth(oldWidth);
    }

}
