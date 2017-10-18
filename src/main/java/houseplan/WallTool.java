package houseplan;

import javafx.geometry.Bounds;
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
                Bounds b = curLine.getBoundsInLocal();
                gc.clearRect(b.getMinX() - 10, b.getMinY() - 10,
                        b.getMaxX() + 10, b.getMaxY() + 10);
                Point2D snap = snapToGrid(event.getX(), event.getY());
                curLine.setEndX(snap.getX());
                curLine.setEndY(snap.getY());
                drawWall(controller.activeLayer.getGraphicsContext2D());
                controller.housePlan.addWall(curLine);
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
        gc.setLineWidth(curLine.getStrokeWidth());
        gc.setStroke(curLine.getStroke());
        gc.strokeLine(curLine.getStartX(), curLine.getStartY(),
                curLine.getEndX(), curLine.getEndY());
    }

    private Point2D snapToGrid(double x, double y) {
        double nx = Math.floor(x / controller.gridWidth) * controller.gridWidth;
        double ny = Math.floor(y / controller.gridWidth) * controller.gridWidth;
        double dx = x - nx;
        double dy = y - ny;
        double hg = controller.gridWidth / 2.0;
        if (dx > hg) nx += controller.gridWidth;
        if (dy > hg) ny += controller.gridWidth;
        //System.out.println("x: " + x + ", y: " + y + ", dx: " + dx + ", dy: " + dy + ", hg: " + hg + ", nx: " + nx + ", ny: " + ny);
        return new Point2D(nx, ny);
    }

}
