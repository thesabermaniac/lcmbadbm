package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

import static edu.touro.mco152.bm.App.msg;

public class WriteCommand implements Command{
    WriteTest writeTest;

    public WriteCommand(WriteTest wt, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence){

        writeTest = wt;
        System.out.println("*** starting new worker thread");
        System.out.println("num files: " + numOfMarks + ", num blks: " + numOfBlocks
                + ", blk size (kb): " + blockSizeKb + ", blockSequence: " + blockSequence);

    }

    @Override
    public void execute() {
        writeTest.runTest();
    }

}
