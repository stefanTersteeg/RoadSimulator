package com.stefan.objects;

import com.stefan.util.Settings;

/**
 * Created by stersteeg on 20/06/2017.
 */
public class Model {

    private boolean carSelection = true;
    private boolean roadBlocking = false;
    private int messageCount = 0;
    private int carCount;
    private double speedFactor = 1;

    public Model() {

    }

    public boolean isCarSelection() {
        return carSelection;
    }

    public boolean isRoadBlocking() {
        return roadBlocking;
    }

    public void changeRoadBlocking() {
        this.roadBlocking = !this.roadBlocking;
    }

    public void changeCarSelection() {
        this.carSelection = !this.carSelection;
    }

    public void addMessageCount(int count) {
        this.messageCount += count;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public int getCarCount() {
        return carCount;
    }

    public long getCarSpeed() {
        return (long) (Settings.CAR_SPEED / speedFactor);
    }

    public long getMessageDelay() {
        return (long) (Settings.MESSAGE_DELAY / speedFactor);
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }
}
