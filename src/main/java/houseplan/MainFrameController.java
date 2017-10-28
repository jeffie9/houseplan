package houseplan;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class MainFrameController {
    @FXML
    Canvas userInputCanvas;

    @FXML
    AnchorPane draftTable;

    @FXML
    Label leftStatusLabel;

    @FXML
    Label rightStatusLabel;

    private Tool selectedTool;
    Canvas activeLayer;
    HousePlan housePlan;
    double gridWidth = 50.0;
    double wallWidth = 7.0;

    @FXML
    public void initialize() {
        System.out.println("initialize");
        selectedTool = new DoorTool(this);

        Canvas grid = new Canvas(2000, 2000);
        final GraphicsContext gc = grid.getGraphicsContext2D();
        final double oldWidth = gc.getLineWidth();
        final Paint oldStroke = gc.getStroke();
        gc.setLineWidth(0.5);
        gc.setStroke(Color.GREY);
        for (double g = gridWidth; g < grid.getWidth(); g += gridWidth) {
            gc.strokeLine(g, 0.0, g, grid.getHeight());
            gc.strokeLine(0.0, g, grid.getWidth(), g);
        }
        gc.setLineWidth(oldWidth);
        gc.setStroke(oldStroke);
        draftTable.getChildren().add(grid);

        activeLayer = new Canvas(2000, 2000);
        draftTable.getChildren().add(activeLayer);

        userInputCanvas.setWidth(2000);
        userInputCanvas.setHeight(2000);
        userInputCanvas.toFront();
    }

    public void onFileClose(ActionEvent event) {
        System.out.println(event);
    }

    public void onPickTool(ActionEvent event) {
        System.out.println(event);
        Button source = Button.class.cast(event.getSource());
        System.out.println(source.getText());
        switch (source.getText()) {
        case "Wall":
            selectedTool = new WallTool(this);
            break;
        case "Window":
            selectedTool = new WindowTool(this);
            break;
        case "Door":
            selectedTool = new DoorTool(this);
            break;
        }
    }

    public void onCanvasMouseEvent(MouseEvent event) {
        selectedTool.handleMouseEvent(event);
    }

    public void processArgs(List<String> args) {
        System.out.println("processArgs " + args);
        housePlan = new HousePlan();
        // TODO last arg is file to load as plan
        Line l = new Line(50.0, 50.0, 250.0, 50.0);
        l.setStrokeWidth(wallWidth);
        housePlan.addWall(l);
        l = new Line(250.0, 50.0, 250.0, 250.0);
        l.setStrokeWidth(wallWidth);
        housePlan.addWall(l);
        l = new Line(250.0, 250.0, 50.0, 250.0);
        l.setStrokeWidth(wallWidth);
        housePlan.addWall(l);
        l = new Line(50.0, 250.0, 50.0, 50.0);
        l.setStrokeWidth(wallWidth);
        housePlan.addWall(l);

        drawHousePlan();
    }

    public void drawHousePlan() {
        final GraphicsContext gc = activeLayer.getGraphicsContext2D();
        for (Shape s : housePlan.getWalls()) {
            if (s instanceof Line) {
                Line line = Line.class.cast(s);
                gc.setLineWidth(line.getStrokeWidth());
                gc.strokeLine(line.getStartX(), line.getStartY(),
                        line.getEndX(), line.getEndY());
            }
        }
    }
}
