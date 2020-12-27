package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

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

    /**
     * Returns a new instance of a WriteBuilder object
     *
     * @return
     */
    public static WriteBuilder builder(){
        return new WriteBuilder();
    }

    /**
     * Acts as a buffer to keep track of a WriteCommand's parameters
     * without instantiating the object. This allows for more flexibility
     * and can more easily accommodate a user that changes his/her mind while
     * making a WriteCommand object.
     */
    public static class WriteBuilder{
        public WriteTest wt;
        public int numOfMarks;
        public int numOfBlocks;
        public int blockSizeKb;
        public DiskRun.BlockSequence blockSequence;

        WriteBuilder(){
        }

        /**
         * Sets the value of our builder's WriteTest object
         *
         * @param wt
         * @return WriteBuilder
         */
        public WriteBuilder wt(WriteTest wt){
            this.wt = wt;
            return this;
        }

        /**
         * Sets the value of our builder's numOfMarks
         *
         * @param numOfMarks
         * @return WriteBuilder
         */
        public WriteBuilder numOfMarks(int numOfMarks){
            this.numOfMarks = numOfMarks;
            return this;
        }

        /**
         * Sets the value of our builder's numOfBlocks
         *
         * @param numOfBlocks
         * @return WriteBuilder
         */
        public WriteBuilder numOfBlocks(int numOfBlocks){
            this.numOfBlocks = numOfBlocks;
            return this;
        }

        /**
         * Sets the value of our builder's blockSizeKb
         *
         * @param blockSizeKb
         * @return WriteBuilder
         */
        public WriteBuilder blockSizeKb(int blockSizeKb){
            this.blockSizeKb = blockSizeKb;
            return this;
        }

        /**
         * Sets the value of our builder's blockSequence
         *
         * @param blockSequence
         * @return WriteBuilder
         */
        public WriteBuilder blockSequence(DiskRun.BlockSequence blockSequence){
            this.blockSequence = blockSequence;
            return this;
        }

        /**
         * Finalizes the instantiation of our WriteCommand object by
         * passing it all our builder's variables.
         *
         * Run this when you are done changing the parameters and want
         * to create your WriteCommand object.
         *
         * @return WriteCommand
         */
        public WriteCommand build(){
            return new WriteCommand(this.wt, this.numOfMarks, this.numOfBlocks, this.blockSizeKb, this.blockSequence);
        }
    }

}
