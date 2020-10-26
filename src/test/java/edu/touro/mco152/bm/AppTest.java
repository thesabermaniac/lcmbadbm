package edu.touro.mco152.bm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests methods from the App class
 */
public class AppTest {
    /**
     * Tests the functionality of the getVersion method
     */
    @Test
    public void testGetVersion(){
        //Arrange
        String expected = "0.4";
        String actual = App.getVersion();

        //Assert
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetVersionWrong(){
        //Arrange
        String expected = "0.5";
        String actual = App.getVersion();

        //Assert
        Assertions.assertNotEquals(expected, actual);
    }

    /**
     * Tests the functionality of the targetMarkSizeKb method
     */
    @Test
    public void testTargetMarkSizeKb(){
        //Arrange
        long expected = App.numOfBlocks * App.blockSizeKb;
        long actual = App.targetMarkSizeKb();

        //Assert
        Assertions.assertEquals(expected, actual);
    }

    /**
     * Exercises the P in RIGHT BICEP by testing the run time of the
     * targetMarkSizeKB method
     *
     * It also tests for the T in CORRECT boudary testing by ensuring
     * it's taking an acceptable amount of time to run.
     *
     */
    @Test
    public void testTargetMarkSizeKBPerformance(){
        //Arrange
        long start = System.nanoTime();

        //Act
        for (int i = 0; i < 1000; i++){
            App.targetMarkSizeKb();
        }
        long stop = System.nanoTime();
        long elapsedTimeMS = (stop - start) / 1_000_000;

        //Assert
        Assertions.assertTrue(elapsedTimeMS < 10);
    }
}
