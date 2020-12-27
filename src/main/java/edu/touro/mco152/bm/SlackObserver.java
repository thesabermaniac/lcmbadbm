package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

/**
 * Observer class that connects to slack and messages
 * the updates channel when the max run is a greater than
 * 3 percent increase over the average runtime.
 */
public class SlackObserver implements Observer{

    @Override
    public void update(DiskRun run) {
        SlackManager slackmgr = new SlackManager("BadBM");
        double increase = run.getRunMax() - run.getRunAvg();
        double percentIncrease = increase/ run.getRunAvg() * 100;
        String str = "";
        if(percentIncrease > 3) {
            str = String.format(":cry: The " + run.getIoMode() +" Benchmark Max runtime of %.2f is %.2f%% greater than the Average of %.2f"
                    , run.getRunMax(), run.getRunAvg(), percentIncrease);
            Boolean worked = slackmgr.postMsg2OurChannel(str);
            System.out.println(str);
        }
    }
}
