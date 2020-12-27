package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

/**
 * This class handles all the logic for running a write test.
 * It implements the Subject interface, which means it maintains
 * a list of observers and notifies them when it completes a write.
 */
public class WriteTest implements Subject{
    int startFileNum = 0;
    DiskMark wMark;

    int blockSize = blockSizeKb * KILOBYTE;
    byte[] blockArr = new byte[blockSize];
    int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete = 0;
    float percentComplete;
    int wUnitsTotal = writeTest ? numOfBlocks * numOfMarks : 0;
    DiskRun run = new DiskRun(DiskRun.IOMode.WRITE, App.blockSequence);
    ArrayList<Observer> observers = new ArrayList<>();

    public WriteTest(){
    }

    public void runTest() {
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

        /**
         * Begin an outer loop for specified duration (number of 'marks') of benchmark,
         * that keeps writing data (in its own loop - for specified # of blocks). Each 'Mark' is timed
         * and is reported to the GUI for display as each Mark completes.
         */
        for (int m = startFileNum; m < startFileNum + numOfMarks; m++) {

            if (multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            wMark = new DiskMark(WRITE);    // starting to keep track of a new bench Mark
            wMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesWrittenInMark = 0;

            String mode = "rw";
            if (writeSyncEnable) {
                mode = "rwd";
            }

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                    for (int b = 0; b < numOfBlocks; b++) {
                        if (App.blockSequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numOfBlocks - 1);
                            rAccFile.seek(rLoc * blockSize);
                        } else {
                            rAccFile.seek(b * blockSize);
                        }
                        rAccFile.write(blockArr, 0, blockSize);
                        totalBytesWrittenInMark += blockSize;
                        wUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) wUnitsTotal * 100f;

                        /**
                         * Report to GUI what percentage level of Entire BM (#Marks * #Blocks) is done.
                         */
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

            /**
             * Compute duration, throughput of this Mark's step of BM
             */
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
            wMark.setBwMbSec(mbWritten / sec);
            msg("m:" + m + " write IO is " + wMark.getBwMbSecAsString() + " MB/s     "
                    + "(" + Util.displayString(mbWritten) + "MB written in "
                    + Util.displayString(sec) + " sec)");
            updateMetrics(wMark);
            /**
             * Let the GUI know the interim result described by the current Mark
             */

            // Keep track of statistics to be displayed and persisted after all Marks are done.
            run.setRunMax(wMark.getCumMax());
            run.setRunMin(wMark.getCumMin());
            run.setRunAvg(wMark.getCumAvg());
            run.setEndTime(new Date());
            Gui.progressBar.setValue((int)percentComplete);
            Gui.addWriteMark(wMark);
            long kbProcessed = (long)(percentComplete) * targetTxSizeKb() / 100;
            Gui.progressBar.setString(kbProcessed + " / " + targetTxSizeKb());

        } // END outer loop for specified duration (number of 'marks') for WRITE bench mark

        /**
         * Persist info about the Write BM Run (e.g. into Derby Database) and add it to a GUI panel
         */
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
        for(Observer observer : observers){
            observer.update(run);
        }
    }
}
