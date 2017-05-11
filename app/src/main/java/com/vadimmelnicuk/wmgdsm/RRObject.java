package com.vadimmelnicuk.wmgdsm;

/**
 * Created by vadimmelnicuk on 09/05/2017.
 */

public class RRObject {
    private double rr = 0.0;
    private double timestamp = 0.0;
    private int filtered = 0;

    // filtered flag
    // 0 - clean
    // 1 - not clean
    // 2 - extopic beat fixed
    // 3 - missed detection fixed

    public RRObject(double rr, double timestamp, int filtered) {
        this.rr = rr;
        this.timestamp = timestamp;
        this.filtered = filtered;
    }

    public double getRR() {
        return rr;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public int getFiltered() {
        return filtered;
    }
}
