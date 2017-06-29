package com.stefan.protocols;

import com.stefan.objects.BlockedRoadStatus;
import com.stefan.objects.Car;
import com.stefan.objects.Model;
import com.stefan.objects.Road;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stersteeg on 23/06/2017.
 */
public abstract class AbstractProtocol {

    protected final Map<Road, BlockedRoadStatus> blockedRoads = new HashMap<>();

    private Model model;

    public AbstractProtocol(Model model) {
        this.model = model;
    }

    public abstract int getMessageLiveTime();

    public abstract boolean exchangeData(Car car, Car neighbor);

    public Map<Road,BlockedRoadStatus> getKnownBlockedRoads() {
        cleanOutdatedItems();
        return blockedRoads;
    }

    protected void cleanOutdatedItems() {
        for(Road r : blockedRoads.keySet()) {
            BlockedRoadStatus s = blockedRoads.get(r);
            double factor = model.getSpeedFactor();
            if(factor != 0 && s.outDated((long) (getMessageLiveTime() / factor))) {
                blockedRoads.remove(r);
            }
        }
    }

    public void updateRoadStatuses(Road[] roads) {
        for(Road r : roads) {
            BlockedRoadStatus status = blockedRoads.get(r);
            if(r.isBlocked()) {
                if(status == null) {
                    status = new BlockedRoadStatus(r);
                    blockedRoads.put(r, status);
                }
                status.setBlocked();
            } else {
                if(status != null && status.isBlocked()) {
                    status.setUnblocked();
                }
            }
        }
    }

    public Map<Road, BlockedRoadStatus> getBlockedRoadList() {
        return this.blockedRoads;
    }

    public boolean updateRoadStatus(Road r, BlockedRoadStatus status) {
        BlockedRoadStatus thisStatus = blockedRoads.get(r);
        if(thisStatus == null) { // Other car does not know an event happened on road r
            return blockedRoads.put(r, new BlockedRoadStatus(status)) != null;
        } else if(thisStatus.getTimeStamp() < status.getTimeStamp()) { // Other car information is older
            return blockedRoads.put(r, new BlockedRoadStatus(status)) != null;
        }
        return false;
    }
}
