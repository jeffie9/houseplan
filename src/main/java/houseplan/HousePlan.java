package houseplan;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class HousePlan {
    private List<Shape> walls = new ArrayList<>();
    private List<Shape> doors = new ArrayList<>();
    private List<Shape> windows = new ArrayList<>();

    public List<Shape> getWalls() {
        return walls;
    }

    public void addWall(Shape wall) {
        walls.add(wall);
    }

    public List<Shape> getDoors() {
        return doors;
    }

    public void addDoor(Line door) {
        doors.add(door);
    }

    public List<Shape> getWindows() {
        return windows;
    }

    public void addWindow(Line window) {
        windows.add(window);
    }
}
