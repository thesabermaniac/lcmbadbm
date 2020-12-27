package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

public class TestObserver implements Observer{
    public boolean triggered = false;

    @Override
    public void update(DiskRun run) {
        triggered = true;
    }
}
