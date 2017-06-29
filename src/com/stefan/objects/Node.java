package com.stefan.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by stersteeg on 29/05/2017.
 */
public class Node {
    public static final int TILE_HEIGHT = 45, TILE_WIDTH = 45;
    public static final int TILE_HOR_COUNT = 21, TILE_VER_COUNT = 21;

    protected final int x, y, id;

    protected List<Car> cars = new ArrayList<>();
    protected List<Node> map = new ArrayList<>();

    public Node(List<Node> map, int x, int y, int id) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public Node getNeighborNorth() {
        if(hasNeighborNorth()) {
            return map.get(id - 1);
        }
        return null;
    }

    public boolean hasNeighborNorth() {
        return y != 0;
    }

    public Node getNeighborEast() {
        if(hasNeighborEast()) {
            return map.get(id + Node.TILE_VER_COUNT);
        }
        return null;
    }

    public boolean hasNeighborEast() {
        return x != Node.TILE_HOR_COUNT - 1;
    }

    public Node getNeighborSouth() {
        if(hasNeighborSouth()) {
            return map.get(id + 1);
        }
        return null;
    }

    public boolean hasNeighborSouth() {
        return y != Node.TILE_VER_COUNT - 1;
    }

    public Node getNeighborWest() {
        if(hasNeighborWest()) {
            return map.get(id - Node.TILE_VER_COUNT);
        }
        return null;
    }

    public boolean hasNeighborWest() {
        return x != 0;
    }

    public Point getScreenCenterLocation() {
        return new Point((int) ((x + 0.5) * TILE_WIDTH), (int) ((y + 0.5) * TILE_HEIGHT));
    }

    public Color getColor() {
        return Color.GRAY;
    }

    public void paint(Graphics g) {
        int xLoc = x * TILE_WIDTH;
        int yLoc = y * TILE_HEIGHT;
        g.setColor(getColor());
        if(hasNeighborNorth()) {
            g.fillRect(xLoc + TILE_WIDTH / 3, yLoc, TILE_WIDTH / 3, 2 * TILE_HEIGHT / 3);
        }
        if(hasNeighborEast()) {
            g.fillRect(xLoc + TILE_WIDTH / 3, yLoc + TILE_HEIGHT / 3,  2 * TILE_WIDTH / 3, TILE_HEIGHT / 3);
        }
        if(hasNeighborSouth()) {
            g.fillRect(xLoc + TILE_WIDTH / 3, yLoc + TILE_HEIGHT / 3,  TILE_WIDTH / 3, 2 * TILE_HEIGHT / 3);
        }
        if(hasNeighborWest()) {
            g.fillRect(xLoc, yLoc + TILE_HEIGHT / 3, 2 * TILE_WIDTH / 3, TILE_HEIGHT / 3);
        }
        if(hasNeighborNorth() && hasNeighborEast()) {
            Polygon p = new Polygon();
            p.addPoint(xLoc + TILE_WIDTH / 3, yLoc);
            p.addPoint(xLoc + 2 * TILE_WIDTH / 3, yLoc);
            p.addPoint(xLoc + TILE_WIDTH, yLoc + TILE_HEIGHT / 3);
            p.addPoint(xLoc + TILE_WIDTH, yLoc + 2 * TILE_HEIGHT / 3);
            g.fillPolygon(p);
        }
        if(hasNeighborEast() && hasNeighborSouth()) {
            Polygon p = new Polygon();
            p.addPoint(xLoc + TILE_WIDTH, yLoc + TILE_HEIGHT / 3);
            p.addPoint(xLoc + TILE_WIDTH, yLoc + 2 * TILE_HEIGHT / 3);
            p.addPoint(xLoc + 2 * TILE_WIDTH / 3, yLoc + TILE_HEIGHT);
            p.addPoint(xLoc + TILE_WIDTH / 3, yLoc + TILE_HEIGHT);
            g.fillPolygon(p);
        }
        if(hasNeighborSouth() && hasNeighborWest()) {
            Polygon p = new Polygon();
            p.addPoint(xLoc + 2 * TILE_WIDTH / 3, yLoc + TILE_HEIGHT);
            p.addPoint(xLoc + TILE_WIDTH / 3, yLoc + TILE_HEIGHT);
            p.addPoint(xLoc, yLoc + 2 * TILE_HEIGHT / 3);
            p.addPoint(xLoc, yLoc + TILE_HEIGHT / 3);
            g.fillPolygon(p);
        }
        if(hasNeighborNorth() && hasNeighborWest()) {
            Polygon p = new Polygon();
            p.addPoint(xLoc + TILE_WIDTH / 3, yLoc);
            p.addPoint(xLoc + 2 * TILE_WIDTH / 3, yLoc);
            p.addPoint(xLoc, yLoc + 2 * TILE_HEIGHT / 3);
            p.addPoint(xLoc, yLoc + TILE_HEIGHT / 3);
            g.fillPolygon(p);
        }
    }

    public void paintCars(Graphics g) {
        for(int i = 0; i < cars.size(); i++) {
            cars.get(i).paint(g);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public boolean addCar(Car car) {
        return cars.add(car);
    }

    public boolean removeCar(Car car) {
        return cars.remove(car);
    }

    public boolean isDriveable() {
        return hasNeighborEast() || hasNeighborWest() || hasNeighborSouth() || hasNeighborEast();
    }

    public Road[] getRoads() {
        List<Road> roads = new LinkedList<>();
        if(!(this instanceof Road)) {
            if(hasNeighborNorth()) {
                roads.add((Road) getNeighborNorth());
            }
            if(hasNeighborEast()) {
                roads.add((Road) getNeighborEast());
            }
            if(hasNeighborSouth()) {
                roads.add((Road) getNeighborSouth());
            }
            if(hasNeighborWest()) {
                roads.add((Road) getNeighborWest());
            }
        }
        return roads.toArray(new Road[roads.size()]);
    }

    @Override
    public String toString() {
        return "Node location " + x + " : " + y;
    }

    public boolean isHovering(int x, int y) {
        return this.x * TILE_WIDTH <= x && (this.x + 1) * TILE_WIDTH >= x && this.y * TILE_HEIGHT <= y && (this.y + 1) * TILE_HEIGHT >= y;
    }

    public Road getRoadTo(Node node) {
        for(Road r : getRoads()) {
            if(r.getDestination(this).equals(node)) {
                return r;
            }
        }
        return null;
    }
}
