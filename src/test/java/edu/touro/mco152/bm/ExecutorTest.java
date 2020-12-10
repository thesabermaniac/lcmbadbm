package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ExecutorTest {

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

    @Test
    public void writeTest() throws IOException {
        setupDefaultAsPerProperties();
        Invoker invoker = new Invoker();
        WriteTest wt = new WriteTest();
        App.numOfMarks = 1;
        Command writeCommand = new WriteCommand(wt, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL);
        invoker.addCommand(writeCommand);
        invoker.executeCommands();
        Assertions.assertTrue((Float.parseFloat(wt.wMark.getAvgAsString()) > 0) && Float.parseFloat(wt.wMark.getAvgAsString()) < 10_000 );
        Assertions.assertTrue((Float.parseFloat(wt.wMark.getBwMbSecAsString()) > 0) && Float.parseFloat(wt.wMark.getBwMbSecAsString()) < 10_000 );
    }

    @Test
    public void readTest() throws IOException {
        setupDefaultAsPerProperties();
        Invoker invoker = new Invoker();
        ReadTest rt = new ReadTest();
        App.numOfMarks = 1;
        Command readCommand = new ReadCommand(rt, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL);
        invoker.addCommand(readCommand);
        invoker.executeCommands();
        Assertions.assertTrue((Float.parseFloat(rt.rMark.getAvgAsString()) > 0) && Float.parseFloat(rt.rMark.getAvgAsString()) < 10_000 );
        Assertions.assertTrue((Float.parseFloat(rt.rMark.getBwMbSecAsString()) > 0) && Float.parseFloat(rt.rMark.getBwMbSecAsString()) < 10_000 );

    }
}
