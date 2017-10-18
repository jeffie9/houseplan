package houseplan;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Shape;

public class HousePlan {
    private List<Shape> walls = new ArrayList<>();

    public List<Shape> getWalls() {
        return walls;
    }

    public void addWall(Shape wall) {
        walls.add(wall);
    }
}
