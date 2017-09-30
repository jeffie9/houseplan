package houseplan;

import javafx.scene.input.MouseEvent;

public abstract class Tool {
    protected final MainFrameController controller;
    public Tool(MainFrameController controller) {
        this.controller = controller;
    }
    public abstract void handleMouseEvent(MouseEvent event);
}
