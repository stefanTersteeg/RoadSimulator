package com.stefan.objects;

import java.awt.*;
import java.util.List;

/**
 * Created by stersteeg on 30/05/2017.
 */
public class Road extends Node {

    public static final Color BLOCK_ROAD_COLOR = new Color(255, 0, 0,50);

    private boolean blocked = false;

    public Road(List<Node> map, int x, int y, int id) {
        super(map, x, y, id);
    }

    public boolean hasNeighborNorth() {
        return x % 2 == 0 && y % 2 == 1;
    }

    public boolean hasNeighborEast() {
        return x % 2 == 1 && y % 2 == 0;
    }

    public boolean hasNeighborSouth() {
        return x % 2 == 0 && y % 2 == 1;
    }

    public boolean hasNeighborWest() {
        return x % 2 == 1 && y % 2 == 0;
    }

    public boolean addCar(Car car) {
        return cars.add(car);
    }

    public Node getDestination(Node from) {
        Node n = getNeighborNorth();
        if(n != null && n.equals(from)) {
            return getNeighborSouth();
        }
        n = getNeighborEast();
        if(n != null && n.equals(from)) {
            return getNeighborWest();
        }
        n = getNeighborSouth();
        if(n != null && n.equals(from)) {
            return getNeighborNorth();
        }
        n = getNeighborWest();
        if(n != null && n.equals(from)) {
            return getNeighborEast();
        }
        return null;
    }

    public Node getNearestNode() {
        Node n = getNeighborNorth();
        if(n != null) {
            return n;
        }
        n = getNeighborEast();
        if(n != null) {
            return n;
        }
        n = getNeighborSouth();
        if(n != null) {
            return n;
        }
        n = getNeighborWest();
        if(n != null) {
            return n;
        }
        return null;
    }

    public void changeRoadStatus() {
        blocked = !blocked;
    }

    public Color getColor() {
        return blocked ? BLOCK_ROAD_COLOR : Color.GRAY;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
