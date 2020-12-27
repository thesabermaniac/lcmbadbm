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

    /**
     * Returns a new instance of a ReadBuilder object
     *
     * @return
     */
    public static ReadBuilder builder(){
        return new ReadBuilder();
    }

    /**
     * Acts as a buffer to keep track of a ReadCommand's parameters
     * without instantiating the object. This allows for more flexibility
     * and can more easily accommodate a user that changes his/her mind while
     * making a ReadCommand object.
     */
    public static class ReadBuilder{
        public ReadTest rt;
        public int numOfMarks;
        public int numOfBlocks;
        public int blockSizeKb;
        public DiskRun.BlockSequence blockSequence;

        ReadBuilder(){
        }

        /**
         * Sets the value of our builder's ReadTest object
         *
         * @param rt
         * @return ReadBuilder
         */
        public ReadBuilder rt(ReadTest rt){
            this.rt = rt;
            return this;
        }

        /**
         * Sets the value of our builder's numOfMarks
         *
         * @param numOfMarks
         * @return ReadBuilder
         */
        public ReadBuilder numOfMarks(int numOfMarks){
            this.numOfMarks = numOfMarks;
            return this;
        }

        /**
         * Sets the value of our builder's numOfBlocks
         *
         * @param numOfBlocks
         * @return ReadBuilder
         */
        public ReadBuilder numOfBlocks(int numOfBlocks){
            this.numOfBlocks = numOfBlocks;
            return this;
        }

        /**
         * Sets the value of our builder's blockSizeKb
         *
         * @param blockSizeKb
         * @return ReadBuilder
         */
        public ReadBuilder blockSizeKb(int blockSizeKb){
            this.blockSizeKb = blockSizeKb;
            return this;
        }

        /**
         * Sets the value of our builder's blockSequence
         *
         * @param blockSequence
         * @return ReadBuilder
         */
        public ReadBuilder blockSequence(DiskRun.BlockSequence blockSequence){
            this.blockSequence = blockSequence;
            return this;
        }

        /**
         * Finalizes the instantiation of our ReadCommand object by
         * passing it all our builder's variables.
         *
         * Run this when you are done changing the parameters and want
         * to create your ReadCommand object.
         *
         * @return ReadCommand
         */
        public ReadCommand build(){
            return new ReadCommand(this.rt, this.numOfMarks, this.numOfBlocks, this.blockSizeKb, this.blockSequence);
        }
    }
}
