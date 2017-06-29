package com.stefan.component;

import com.stefan.objects.*;
import com.stefan.protocols.FloodingProtocol;
import com.stefan.protocols.PathProtocol;
import com.stefan.util.Settings;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.stefan.objects.Node.TILE_HEIGHT;
import static com.stefan.objects.Node.TILE_WIDTH;

/**
 * Created by stersteeg on 29/05/2017.
 */
public class PaintPanel extends JPanel implements MouseInputListener {

    private List<Node> map = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private Car selectedCar = null;
    private final Model model;

    public PaintPanel(Model model) {
        this.model = model;

        setPreferredSize(new Dimension(Node.TILE_HOR_COUNT * TILE_WIDTH, Node.TILE_VER_COUNT * Node.TILE_HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        int id = 0;
        for(int x = 0; x < Node.TILE_HOR_COUNT; x++) {
            for(int y = 0; y < Node.TILE_VER_COUNT; y++) {
                if(x % 2 != 1 && y % 2 != 1) {
                    map.add(new Node(map, x, y, id));
                } else {
                    map.add(new Road(map, x, y, id));
                }
                id++;
            }
        }

        for(Node n : map) {
            if(n instanceof Road) {
                if(n.isDriveable()) {
                    Car c = new Car(map, model, new PathProtocol(model));
                    n.addCar(c);
                    c.setStart(n);
                    c.setNewDestination();
                    cars.add(c);
                }

            }
        }

        model.setCarCount(cars.size());

        Thread t = new Thread(() -> {
            while(true) {
                try {
                    for(Car c : cars) {
                        model.addMessageCount(c.exchangeData(cars));
                    }
                    Thread.sleep(model.getMessageDelay());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        });
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(31, 112, 31));
        g.fillRect(0, 0, getWidth(), getHeight());
        for(Node n : map) {
            n.paint(g);
        }
        for(Node n : map) {
            n.paintCars(g);
        }
        if(selectedCar != null) {
            g.setColor(Color.ORANGE);
            boolean paint = false;
            List<Node> path = selectedCar.getPath();
            for(int i = 0; i < path.size() - 1; i++) {
                if(path.get(i).equals(selectedCar.getLocation())) {
                    paint = true;
                    Point from = selectedCar.getScreenLocation();
                    Point to = path.get(i + 1).getScreenCenterLocation();
                    g.drawLine(from.x, from.y, to.x, to.y);
                    continue;
                }
                if(paint) {
                    Point from = path.get(i).getScreenCenterLocation();
                    Point to = path.get(i + 1).getScreenCenterLocation();
                    g.drawLine(from.x, from.y, to.x, to.y);
                }
            }
            g.setColor(Settings.RADIUS_COLOR);
            Point p = selectedCar.getScreenLocation();
            g.fillArc((int) (p.getX() - Settings.CONTACT_RADIUS / 2), (int) (p.getY() - Settings.CONTACT_RADIUS / 2), Settings.CONTACT_RADIUS, Settings.CONTACT_RADIUS, 0, 360);
            g.setColor(Color.ORANGE);
            g.drawArc((int) (p.getX() - Settings.CONTACT_RADIUS / 2), (int) (p.getY() - Settings.CONTACT_RADIUS / 2), Settings.CONTACT_RADIUS, Settings.CONTACT_RADIUS, 0, 360);
            for(BlockedRoadStatus s : selectedCar.getNetworkAdapter().getBlockedRoadList().values()) {
                Road r = s.getRoad();
                if(s.isBlocked()) {
                    g.drawRect(r.getX() * TILE_WIDTH + TILE_WIDTH / 3, r.getY() * TILE_HEIGHT + TILE_HEIGHT / 3, TILE_WIDTH / 3, TILE_HEIGHT / 3);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(model.isCarSelection()) {
            for(Car c : cars) {
                if(c.setSelected(c.isHovering(x, y))) {
                    if(selectedCar != null && !selectedCar.equals(c)) {
                       selectedCar.setSelected(false);
                    }
                    this.selectedCar = c;
                    break;
                }
            }
            if(selectedCar != null && !selectedCar.isSelected()) {
                selectedCar = null;
            }
        } else if(model.isRoadBlocking()) {
            for(Node n : map) {
                if(n instanceof Road) {
                    if(n.isHovering(x, y)) {
                        ((Road) n).changeRoadStatus();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(model.isCarSelection()) {
            for(Car c : cars) {
                if(c.isHovering(x, y)) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return;
                }
            }
        } else if(model.isRoadBlocking()) {
            for(Node n : map) {
                if(n instanceof Road) {
                    if(n.isHovering(x, y)) {
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                        return;
                    }
                }
            }
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}