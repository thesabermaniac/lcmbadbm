package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class BuilderTests {

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */
    private void setupDefaultAsPerProperties()
    {
        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath()+File.separator+App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        }
        else
        {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }

    /**
     * Tests Write Builder without making any changes to the initial build
     *
     * @throws IOException
     */
    @Test
    public void testWriteBuilder() throws IOException {
        setupDefaultAsPerProperties();
        Invoker invoker = new Invoker();
        WriteTest wt = new WriteTest();
        WriteCommand.WriteBuilder wb = WriteCommand.builder()
                .wt(wt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        WriteCommand wc = wb.build();
        invoker.addCommand(wc);
        invoker.executeCommands();
        Assertions.assertEquals(wb.numOfMarks, 1);
        Assertions.assertEquals(wb.numOfBlocks, 256);
        Assertions.assertEquals(wb.blockSizeKb, 128);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }

    /**
     * Tests write builder with multiple changes to original build,
     * but without building the Write Command object between changes
     */
    @Test
    public void testWriteBuilderWithChanges(){
        setupDefaultAsPerProperties();
        WriteTest wt = new WriteTest();
        WriteCommand.WriteBuilder wb = WriteCommand.builder()
                .wt(wt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        Assertions.assertEquals(wb.numOfMarks, 1);
        Assertions.assertEquals(wb.numOfBlocks, 256);
        Assertions.assertEquals(wb.blockSizeKb, 128);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
        wb.numOfMarks(10)
        .numOfBlocks(128)
        .blockSizeKb(64)
        .blockSequence(DiskRun.BlockSequence.RANDOM);
        Assertions.assertEquals(wb.numOfMarks, 10);
        Assertions.assertEquals(wb.numOfBlocks, 128);
        Assertions.assertEquals(wb.blockSizeKb, 64);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.RANDOM);
        wb.numOfMarks(100);
        Assertions.assertEquals(wb.numOfMarks, 100);
        wb.numOfBlocks(512);
        Assertions.assertEquals(wb.numOfBlocks, 512);
        wb.blockSizeKb(256);
        Assertions.assertEquals(wb.blockSizeKb, 256);
        wb.blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }

    /**
     * Tests Write Builder with multiple changes and building Write Command object
     * between each change.
     */
    @Test
    public void testWriteBuilderWithChangesAndBuilds(){
        setupDefaultAsPerProperties();
        WriteTest wt = new WriteTest();
        WriteCommand.WriteBuilder wb = WriteCommand.builder()
                .wt(wt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        wb.build();
        Assertions.assertEquals(wb.numOfMarks, 1);
        Assertions.assertEquals(wb.numOfBlocks, 256);
        Assertions.assertEquals(wb.blockSizeKb, 128);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
        wb.numOfMarks(10)
        .numOfBlocks(128)
        .blockSizeKb(64)
        .blockSequence(DiskRun.BlockSequence.RANDOM);
        wb.build();
        Assertions.assertEquals(wb.numOfMarks, 10);
        Assertions.assertEquals(wb.numOfBlocks, 128);
        Assertions.assertEquals(wb.blockSizeKb, 64);
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.RANDOM);
        wb.numOfMarks(100).build();
        Assertions.assertEquals(wb.numOfMarks, 100);
        wb.numOfBlocks(512).build();
        Assertions.assertEquals(wb.numOfBlocks, 512);
        wb.blockSizeKb(256).build();
        Assertions.assertEquals(wb.blockSizeKb, 256);
        wb.blockSequence(DiskRun.BlockSequence.SEQUENTIAL).build();
        Assertions.assertEquals(wb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }

    /**
     * Tests Read Builder without making any changes to the initial build
     *
     * @throws IOException
     */
    @Test
    public void testReadBuilder() throws IOException {
        setupDefaultAsPerProperties();
        Invoker invoker = new Invoker();
        ReadTest rt = new ReadTest();
        ReadCommand.ReadBuilder rb = ReadCommand.builder()
                .rt(rt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        ReadCommand rc = rb.build();
        invoker.addCommand(rc);
        invoker.executeCommands();
        Assertions.assertEquals(rb.numOfMarks, 1);
        Assertions.assertEquals(rb.numOfBlocks, 256);
        Assertions.assertEquals(rb.blockSizeKb, 128);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }

    /**
     * Tests read builder with multiple changes to original build,
     * but without building the Read Command object between changes
     */
    @Test
    public void testReadBuilderWithChanges(){
        setupDefaultAsPerProperties();
        ReadTest rt = new ReadTest();
        ReadCommand.ReadBuilder rb = ReadCommand.builder()
                .rt(rt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        Assertions.assertEquals(rb.numOfMarks, 1);
        Assertions.assertEquals(rb.numOfBlocks, 256);
        Assertions.assertEquals(rb.blockSizeKb, 128);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
        rb.numOfMarks(10)
        .numOfBlocks(128)
        .blockSizeKb(64)
        .blockSequence(DiskRun.BlockSequence.RANDOM);
        Assertions.assertEquals(rb.numOfMarks, 10);
        Assertions.assertEquals(rb.numOfBlocks, 128);
        Assertions.assertEquals(rb.blockSizeKb, 64);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.RANDOM);
        rb.numOfMarks(100);
        Assertions.assertEquals(rb.numOfMarks, 100);
        rb.numOfBlocks(512);
        Assertions.assertEquals(rb.numOfBlocks, 512);
        rb.blockSizeKb(256);
        Assertions.assertEquals(rb.blockSizeKb, 256);
        rb.blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }

    /**
     * Tests Read Builder with multiple changes and building Read Command object
     * between each change.
     */
    @Test
    public void testReadBuilderWithChangesAndBuilds() {
        setupDefaultAsPerProperties();
        ReadTest rt = new ReadTest();
        ReadCommand.ReadBuilder rb = ReadCommand.builder()
                .rt(rt)
                .numOfMarks(1)
                .numOfBlocks(256)
                .blockSizeKb(128)
                .blockSequence(DiskRun.BlockSequence.SEQUENTIAL);
        rb.build();
        Assertions.assertEquals(rb.numOfMarks, 1);
        Assertions.assertEquals(rb.numOfBlocks, 256);
        Assertions.assertEquals(rb.blockSizeKb, 128);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
        rb.numOfMarks(10)
        .numOfBlocks(128)
        .blockSizeKb(64)
        .blockSequence(DiskRun.BlockSequence.RANDOM);
        rb.build();
        Assertions.assertEquals(rb.numOfMarks, 10);
        Assertions.assertEquals(rb.numOfBlocks, 128);
        Assertions.assertEquals(rb.blockSizeKb, 64);
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.RANDOM);
        rb.numOfMarks(100).build();
        Assertions.assertEquals(rb.numOfMarks, 100);
        rb.numOfBlocks(512).build();
        Assertions.assertEquals(rb.numOfBlocks, 512);
        rb.blockSizeKb(256).build();
        Assertions.assertEquals(rb.blockSizeKb, 256);
        rb.blockSequence(DiskRun.BlockSequence.SEQUENTIAL).build();
        Assertions.assertEquals(rb.blockSequence, DiskRun.BlockSequence.SEQUENTIAL);
    }
}
