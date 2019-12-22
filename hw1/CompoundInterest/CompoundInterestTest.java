import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals( 20, CompoundInterest.numYears(2039));
        assertEquals( 18, CompoundInterest.numYears(2037));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(4455.516, CompoundInterest.futureValue(1200.0,30,2024),tolerance);
        assertEquals(729, CompoundInterest.futureValue(1000.0,-10,2022),tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(2005.142581,CompoundInterest.futureValueReal(1000, 30, 2022, 3),tolerance);
        assertEquals(5903.48,CompoundInterest.futureValueReal(2500, 25, 2024, 5),tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(23205, CompoundInterest.totalSavings(5000,2022,10),tolerance);
        assertEquals(16550, CompoundInterest.totalSavings(5000,2021,10),tolerance);
        assertEquals(10500, CompoundInterest.totalSavings(5000,2020,10),tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15572, CompoundInterest.totalSavingsReal(5000,2021,10, 3),tolerance);
        assertEquals(10335, CompoundInterest.totalSavingsReal(5000,2020,10,3),tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
