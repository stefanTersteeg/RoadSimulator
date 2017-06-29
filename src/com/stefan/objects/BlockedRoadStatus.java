package com.stefan.objects;

/**
 * Created by stersteeg on 20/06/2017.
 */
public class BlockedRoadStatus {

    private final Road road;
    private long timeStamp;
    private boolean blocked;

    public BlockedRoadStatus(Road road) {
        this.road = road;
        this.timeStamp = System.currentTimeMillis();
        this.blocked = road.isBlocked();
    }

    public BlockedRoadStatus(BlockedRoadStatus s) {
        this.road = s.getRoad();
        if(s.getRoad().equals(getRoad())) {
            this.blocked = s.isBlocked();
            this.timeStamp = s.getTimeStamp();
            return;
        }
        throw new NullPointerException("Invalid Operation");
    }

    public void updateTimeStamp() {
        this.timeStamp = System.currentTimeMillis();
    }

    public Road getRoad() {
        return road;
    }

    public boolean outDated(long liveTime) {
        return this.timeStamp + liveTime < System.currentTimeMillis();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked() {
        setBlocked(true);
    }

    private void setBlocked(boolean flag) {
        this.blocked = flag;
        updateTimeStamp();
    }

    public void setUnblocked() {
        setBlocked(false);
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
