package houseplan;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class SelectTool extends Tool {

    public SelectTool(MainFrameController controller) {
        super(controller);
    }

    @Override
    public void handleMouseEvent(MouseEvent event) {
        final GraphicsContext gc = controller.userInputCanvas.getGraphicsContext2D();
        switch (event.getEventType().getName()) {
        case "MOUSE_CLICKED":
            //System.out.println("event: " + event);
            Shape hit = hitTest(event.getX(), event.getY());
            System.out.println("hit: " + hit.getBoundsInLocal());
            Bounds b = hit.getBoundsInLocal();
            gc.setStroke(Color.ORANGE);
            gc.strokeRect(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
            break;
        }
    }

    public Shape hitTest(double x, double y) {
        return controller.housePlan.getWindows().stream()
                .filter(s -> s.contains(x, y))
                .findFirst()
                .orElse(controller.housePlan.getDoors().stream()
                        .filter(s -> s.contains(x, y))
                        .findFirst()
                        .orElse(controller.housePlan.getWalls().stream()
                                .filter(s -> s.contains(x, y))
                                .findFirst()
                                .orElse(null)));
    }
}
