package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

import java.io.IOException;

public class ReadCommand implements Command{
    ReadTest readTest;

    public ReadCommand(ReadTest rt, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence){
        readTest = rt;
        System.out.println("*** starting new worker thread");
        System.out.println("num files: " + numOfMarks + ", num blks: " + numOfBlocks
                + ", blk size (kb): " + blockSizeKb + ", blockSequence: " + blockSequence);
    }

    @Override
    public void execute() throws IOException {
        readTest.runTest();
    }
}
