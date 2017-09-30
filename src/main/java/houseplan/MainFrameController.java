package houseplan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainFrameController {
    @FXML
    Canvas userInputCanvas;

    @FXML
    AnchorPane draftTable;

    private Tool selectedTool;
    Canvas activeLayer;

    @FXML
    public void initialize() {
        System.out.println("Initializing!");
        selectedTool = new WallTool(this);
        activeLayer = new Canvas(2000, 2000);
        draftTable.getChildren().add(activeLayer);
        userInputCanvas.toFront();
    }

    public void onFileClose(ActionEvent event) {
        System.out.println(event);
    }

    public void onPickTool(ActionEvent event) {
        System.out.println(event);
        selectedTool = new WallTool(this);
    }

    public void onCanvasMouseEvent(MouseEvent event) {
        selectedTool.handleMouseEvent(event);
    }

}
