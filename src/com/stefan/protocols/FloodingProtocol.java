package com.stefan.protocols;

import com.stefan.objects.BlockedRoadStatus;
import com.stefan.objects.Car;
import com.stefan.objects.Model;
import com.stefan.objects.Road;

import java.util.Map;

/**
 * Created by stersteeg on 26/06/2017.
 */
public class FloodingProtocol extends AbstractProtocol {

    public FloodingProtocol(Model model) {
        super(model);
    }

    @Override
    public int getMessageLiveTime() {
        return 45000;
    }

    @Override
    public boolean exchangeData(Car car, Car neighbor) {
        cleanOutdatedItems();
        if(blockedRoads.isEmpty()) {
            return false;
        }
        for(Road r : blockedRoads.keySet()) {
            BlockedRoadStatus s = blockedRoads.get(r);
            neighbor.getNetworkAdapter().updateRoadStatus(r, s);
        }
        return true;
    }
}
