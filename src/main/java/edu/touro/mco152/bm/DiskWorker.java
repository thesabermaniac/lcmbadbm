package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.util.List;

import static edu.touro.mco152.bm.App.*;

/**
 * Run the disk benchmarking as a Swing-compliant thread (only one of these threads can run at
 * once.) Cooperates with Swing to provide and make use of interim and final progress and
 * information, which is also recorded as needed to the persistence store, and log.
 * <p>
 * Depends on static values that describe the benchmark to be done having been set in App and Gui classes.
 * The DiskRun class is used to keep track of and persist info about each benchmark at a higher level (a run),
 * while the DiskMark class described each iteration's result, which is displayed by the UI as the benchmark run
 * progresses.
 * <p>
 * This class only knows how to do 'read' or 'write' disk benchmarks. It is instantiated by the
 * startBenchmark() method.
 * <p>
 * To be Swing compliant this class extends SwingWorker and declares that its final return (when
 * doInBackground() is finished) is of type Boolean, and declares that intermediate results are communicated to
 * Swing using an instance of the DiskMark class.
 */

public class DiskWorker extends SwingWorker<Boolean, DiskMark> {

    Invoker invoker = new Invoker();


    @Override
    protected Boolean doInBackground() throws Exception {

        /**
         * We 'got here' because: a) End-user clicked 'Start' on the benchmark UI,
         * which triggered the start-benchmark event associated with the App::startBenchmark()
         * method.  b) startBenchmark() then instantiated a DiskWorker, and called
         * its (super class's) execute() method, causing Swing to eventually
         * call this doInBackground() method.
         *
         * The read and write responsibilities have been de-coupled from this method. The only
         * job of this method now is to update the GUI and call the appropriate methods.
         *
         */
        System.out.println("*** starting new worker thread");
        msg("Running readTest " + App.readTest + "   writeTest " + App.writeTest);


        Gui.updateLegend();  // init chart legend info;

        if (App.autoReset) {
            App.resetTestData();
            Gui.resetTestData();
        }

        addToInvoker();

        invoker.executeCommands();
        App.nextMarkNumber += App.numOfMarks;
        return true;
    }

    public void addToInvoker(){
        if (App.writeTest) {
            WriteTest wt = new WriteTest();
            WriteCommand wc = new WriteCommand(wt, numOfMarks, numOfBlocks, blockSizeKb, blockSequence);
            invoker.addCommand(wc);
        }

        if (App.readTest) {
            ReadTest rt = new ReadTest();
            ReadCommand rc = new ReadCommand(rt, numOfMarks, numOfBlocks, blockSizeKb, blockSequence);
            invoker.addCommand(rc);
            publish(rt.rMark);
        }

    }

    @Override
    protected void process(List<DiskMark> markList) {
        /**
         * We are passed a list of one or more DiskMark objects that our thread has previously
         * published to Swing. Watch Professor Cohen's video - Module_6_RefactorBadBM Swing_DiskWorker_Tutorial.mp4
         */
        markList.stream().forEach((dm) -> {
            if (dm.type == DiskMark.MarkType.WRITE) {
                Gui.addWriteMark(dm);
            } else {
                Gui.addReadMark(dm);
            }
        });
    }

    @Override
    protected void done() {
        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }
}
