package com.stefan.protocols;

import com.stefan.objects.*;

import java.awt.*;

/**
 * Created by stersteeg on 28/06/2017.
 */
public class PathProtocol extends AbstractProtocol {

    public PathProtocol(Model model) {
        super(model);
    }

    @Override
    public int getMessageLiveTime() {
        return 45000;
    }

    @Override
    public boolean exchangeData(Car car, Car neighbor) {
        cleanOutdatedItems();
        boolean connected = false;
        for(Road r : blockedRoads.keySet()) {
            BlockedRoadStatus s = blockedRoads.get(r);
            Point roadPoint = r.getScreenCenterLocation();
            if(car.getScreenLocation().distance(roadPoint) < (Node.TILE_HEIGHT + Node.TILE_WIDTH) / 2 * 6 &&
                    car.getDestination().getScreenCenterLocation().distance(roadPoint) > car.getScreenLocation().distance(roadPoint)) {
                neighbor.getNetworkAdapter().updateRoadStatus(r, s);
                connected = true;
            }
        }
        return connected;
    }
}