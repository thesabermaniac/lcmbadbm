package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.updateMetrics;
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;

/**
 * This class handles all the logic for running a read test.
 * It implements the Subject interface, which means it maintains
 * a list of observers and notifies them when it completes a run.
 */
public class ReadTest implements Subject{
    int startFileNum = 0;
    DiskMark rMark;

    int blockSize = blockSizeKb * KILOBYTE;
    byte[] blockArr = new byte[blockSize];
    int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete = 0;
    float percentComplete;
    int rUnitsTotal = readTest ? numOfBlocks * numOfMarks : 0;
    DiskRun run = new DiskRun(DiskRun.IOMode.READ, blockSequence);
    ArrayList<Observer> observers = new ArrayList<>();

    public ReadTest(){
    }

    public void runTest() throws IOException {
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }
        run.setNumMarks(numOfMarks);
        run.setNumBlocks(numOfBlocks);
        run.setBlockSize(blockSizeKb);
        run.setTxSize(targetTxSizeKb());
        run.setDiskInfo(Util.getDiskInfo(dataDir));

        startFileNum = nextMarkNumber;

        System.out.println("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        // Create a test data file using the default file system and config-specified location
        if (!multiFile) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }

        for (int m = startFileNum; m < startFileNum + numOfMarks; m++) {

            if (multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            rMark = new DiskMark(READ);
            rMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesReadInMark = 0;

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                    for (int b = 0; b < numOfBlocks; b++) {
                        if (blockSequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numOfBlocks - 1);
                            rAccFile.seek(rLoc * blockSize);
                        } else {
                            rAccFile.seek(b * blockSize);
                        }
                        rAccFile.readFully(blockArr, 0, blockSize);
                        totalBytesReadInMark += blockSize;
                        rUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) rUnitsTotal * 100f;
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
            rMark.setBwMbSec(mbRead / sec);
            updateMetrics(rMark);

            run.setRunMax(rMark.getCumMax());
            run.setRunMin(rMark.getCumMin());
            run.setRunAvg(rMark.getCumAvg());
            run.setEndTime(new Date());
            Gui.progressBar.setValue((int)percentComplete);
            Gui.addWriteMark(rMark);
            long kbProcessed = (long)(percentComplete) * targetTxSizeKb() / 100;
            Gui.progressBar.setString(kbProcessed + " / " + targetTxSizeKb());
            Gui.mainFrame.refreshReadMetrics();
        }
        notifyObservers();

    }

    /**
     * Adds Observer objects to our observers list. This
     * allows the class to notify any interested observers
     * in a decoupled fashion.
     *
     * @param o
     */
    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    /**
     * Removes Observer objects from our observers list. This
     * allows the class to stop notifying Observer objects that
     * no longer require updates.
     *
     * @param o
     */
    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    /**
     * Iterates over our observers list and calls each object's
     * update method, passing our run data.
     */
    @Override
    public void notifyObservers() {
        for(Observer observer:observers){
            observer.update(run);
        }
    }
}
