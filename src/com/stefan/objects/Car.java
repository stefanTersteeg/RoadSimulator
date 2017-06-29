package com.stefan.objects;

import com.stefan.protocols.AbstractProtocol;
import com.stefan.protocols.FloodingProtocol;
import com.stefan.util.Path;
import com.stefan.util.Settings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

/**
 * Created by stersteeg on 05/06/2017.
 */
public class Car {

    private int x = 0, y = 0;
    private Node location, destination;
    private double direction;
    private boolean selected = false;
    private long startTime;

    /* ROUTE TO DESTINATION */
    private List<Node> path = null;

    private AbstractProtocol networkHandler;


    private final List<Node> map;
    private Model model;

    public Car(List<Node> tiles, Model model, AbstractProtocol networkHandler) {
        this.model = model;
        this.networkHandler = networkHandler;
        startTime = System.currentTimeMillis();
        map = (List<Node>) ((ArrayList<Node>) tiles).clone();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isLocationSet() {
        return x != 0 && y != 0;
    }

    public void setStart(Node start) {
        this.location = start;
    }

    public void setNewDestination() {
        while(destination == null || !destination.isDriveable() || destination == location) {
            Random r = new Random();
            int v = r.nextInt(map.size());
            destination = map.get(v);
        }
        path = Path.getPath(location, destination, networkHandler.getKnownBlockedRoads());
        if(path.size() > 1) {
            Point p1 = path.get(0).getScreenCenterLocation();
            Point p2 = path.get(1).getScreenCenterLocation();
            direction = Math.toDegrees(Math.atan2(p2.x - p1.x, p2.y - p1.y));
        } else {
            destination = null;
            setNewDestination();
        }
    }

    private void updateLocation() {
        if(startTime + model.getCarSpeed() < System.currentTimeMillis()) {
            location.removeCar(this);
            location = path.get(1);
            location.addCar(this);

            if(!(location instanceof Road)) {
                networkHandler.updateRoadStatuses(location.getRoads());
            }

            if(location.equals(destination)) {
                destination = null;
                setNewDestination();
            }

            path = Path.getPath(location, destination, networkHandler.getKnownBlockedRoads());
            if(path.size() == 1) {
                destination = null;
                setNewDestination();
            }
            startTime = startTime + model.getCarSpeed();
        }
        if(path.size() > 1) {
            Point p1 = path.get(0).getScreenCenterLocation();
            Point p2 = path.get(1).getScreenCenterLocation();
            double percentage = ((double) (System.currentTimeMillis() - startTime)) / model.getCarSpeed();
            int xDifference = p2.x - p1.x;
            int yDifference = p2.y - p1.y;
            x = (int) (xDifference * percentage);
            y = (int) (yDifference * percentage);
            p1 = getScreenLocation();
            direction = Math.toDegrees(Math.atan2(p2.x - p1.x, p2.y - p1.y));
            if(direction == 0) {
                x -= 3;
            } else if(direction == 180) {
                x += 3;
            } else if(direction == 90) {
                y += 3;
            } else if(direction == 270) {
                y -= 3;
            }
        } else {
        }
    }

    public void paint(Graphics g) {
        updateLocation();
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

        int xLoc = (int) ((location.getX() + 0.5) * Node.TILE_WIDTH + x);
        int yLoc = (int) ((location.getY() + 0.5) * Node.TILE_HEIGHT + y);

        g2d.translate(xLoc, yLoc);
        g2d.rotate(Math.toRadians(direction));
        g2d.setColor(selected ? Color.ORANGE : Color.BLUE);


        g2d.fillArc(-Settings.CAR_WIDTH / 2, -Settings.CAR_HEIGHT / 2, Settings.CAR_WIDTH, Settings.CAR_HEIGHT, 0, 360);
        g2d.setTransform(old);
    }

    public boolean isHovering(int x, int y) {

        return getScreenLocation().distance(x, y) < (Settings.CAR_HEIGHT + Settings.CAR_HEIGHT) / 1.5;
    }

    @Override
    public String toString() {
        return "Car located on " + location.toString();
    }

    public boolean setSelected(boolean selected) {
        this.selected = selected;
        return selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public List<Node> getPath() {
        return path;
    }

    public Node getLocation() {
        return location;
    }

    public Point getScreenLocation() {
        return new Point((int) ((location.getX() + 0.5) * Node.TILE_WIDTH + x), (int) ((location.getY() + 0.5) * Node.TILE_HEIGHT + y));
    }

    public AbstractProtocol getNetworkAdapter() {
        return this.networkHandler;
    }

    public int exchangeData(List<Car> cars) {
        int counter = 0;
        for(Car c : cars) {
            if(c.getScreenLocation().distance(getScreenLocation()) < Settings.CONTACT_RADIUS) {
                if(networkHandler.exchangeData(this, c)) {
                    counter++;
                }
//                for(BlockedRoadStatus s : c.getBlockedRoadList().values()) {
//                    if(blockedRoads.get(s.getRoad()).getTimeStamp() < s.getTimeStamp()) {
//                        blockedRoads.get(s.getRoad()).update(s);
//                    }
//                }
            }
        }
        return counter;
    }

    public Node getDestination() {
        return destination;
    }
}
