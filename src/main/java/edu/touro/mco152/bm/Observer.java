package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

/**
 * Observer interface for creating Observer objects
 * to register to subjects, who will in turn notify
 * the Object when appropriate.
 */
public interface Observer {
    public void update(DiskRun run);
}
