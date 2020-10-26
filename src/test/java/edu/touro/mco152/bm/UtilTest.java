package edu.touro.mco152.bm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UtilTest {
    /**
     * Parameterized test for the displayString method from the Util class.
     *
     * This method accomplishes the C of CORRECT boundary testing by assuring
     * the inputs and results conform to a certain format (namely, doubles
     * that are rounded to the second decimal place).
     *
     * @param num
     */
    @ParameterizedTest
    @ValueSource(doubles = {123.460001, 123.4567, 123.46012, 123.455555, 123.459999})
    public void testDisplayString(double num){
        String expected = Util.displayString(num);
        Assertions.assertEquals(expected, "123.46");
    }

    /**
     * The following 2 tests exercise the B from RIGHT BICEP by testing
     * the outermost boundaries of the double data type in our displayString method.
     */
    @Test
    public void testDisplayStringMax(){
        //Arrange
        double num = Double.MAX_VALUE;

        //Act
        String max = Util.displayString(Double.MAX_VALUE);

        //Assert
        Assertions.assertEquals(max, "179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    }
    @Test
    public void testDisplayStringMin(){
        //Arrange
        double num = Double.MIN_VALUE;

        //Act
        String min = Util.displayString(num);

        //Assert
        Assertions.assertEquals(min, "0");
    }
    /**
     * Exercises the C in RIGHT BICEP by testing our method against
     * Java's String formatter
     */
    @Test
    public void testDisplayStringCrossCheck(){
        //Arrange
        double test_num = 987.65432;

        //Act
        String actual = Util.displayString(test_num);
        String expected = String.format("%.2f", test_num);

        //Assert
        Assertions.assertEquals(actual, expected);
    }

    /**
     * Unit tests for the randInt method from the Util class
     *
     * This exercises the first R in CORRECT boundary testing by ensuring the oupute
     * is within the expected range (5 - 10 in this case).
     *
     */
    @Test
    public void testRandInt(){
        //Arrange
        int num1 = 5;
        int num2 = 10;

        //Act
        int actual = Util.randInt(num1, num2);

        //Assert
        Assertions.assertTrue(actual >= 5);
        Assertions.assertTrue(actual <= 10);
    }

    /**
     * Exercises the E from RIGHT BICEP by forcing an IllegalArgumentException
     */
    @Test
    public void testRandIntError(){
        //Arrange
        int num1 = 15;
        int num2 = 10;

        //Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> Util.randInt(num1, num2));
    }
}
