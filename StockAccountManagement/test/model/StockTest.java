package objectOrientedProgram.StockAccountManagement.test.model;

import objectOrientedProgram.StockAccountManagement.main.exceptions.*;
import objectOrientedProgram.StockAccountManagement.main.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

class StockTest {
    private Stock testStock;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("AAPL", 123, 123, 500,
                500, (500 / 123), 1, 3, 1000000);
    }

    @Test
    void testSetSymbolNoException() {
        try {
            testStock.setSymbol("GOOGL");
            assertEquals("GOOGL", testStock.getSymbol());
        } catch (TicketLengthException e) {
            fail("should not have exception for ticker length");
        } catch (NonCapLetterException e) {
            fail("should not have exception nonCapLetter");
        }
    }

    @Test
    void testSetSymbolLengthException() {
        try {
            testStock.setSymbol("GOOGLE");
            fail("Length exception");
        } catch (TicketLengthException e) {
            assertEquals("AAPL", testStock.getSymbol());
        } catch (NonCapLetterException e) {
            fail("should not have exception noncapletter");
        }
    }

    @Test
    void testSetSymbolLetterException() {
        try {
            testStock.setSymbol("GGoL");
            fail("Letter expception");
        } catch (TicketLengthException e) {
            fail("should not have exception for ticker length");
        } catch (NonCapLetterException e) {
            assertEquals("AAPL", testStock.getSymbol());
        }
    }

    @Test
    void setStockPriceCurrentNoException() {
        try {
            testStock.setStockPriceCurrent(350.47);
            assertEquals(350.47, testStock.getStockPriceCurrent());
        } catch (NegativeDoubleException e) {
            fail("Should not have exception");
        }
    }

    @Test
    void setStockPriceCurrentException() {
        try {
            testStock.setStockPriceCurrent(-154.67);
            fail("Should have exception");
        } catch (NegativeDoubleException e) {
            assertEquals(123, testStock.getStockPriceCurrent());
        }
    }

    @Test
    void testSetCurrentInvestmentWorthValid() {
        try {
            testStock.setCurrentInvestmentWorth(1000);
            assertEquals(1000, testStock.getCurrentInvestmentWorth());
        } catch (NegativeDoubleException e) {
            fail("Should not catch exceptions");
        }
    }

    @Test
    void testSetCurrentInvestmentWorthInvalid() {
        try {
            testStock.setCurrentInvestmentWorth(-400);
            fail("Should catch exceptions");
        } catch (NegativeDoubleException e) {
            assertEquals(500, testStock.getCurrentInvestmentWorth());
        }
    }

    @Test
    void testSetDaysToInvestValid() {
        try {
            testStock.setDaysToInvest(2);
            assertEquals(2, testStock.getDaysToInvest());
        } catch (NegativeIntException e) {
            fail("No excpetion should be thrown");
        }
    }

    @Test
    void testSetDaysToInvestInvalid() {
        try {
            testStock.setDaysToInvest(-1);
            fail("Exception should be thrown");
        } catch (NegativeIntException e) {
            assertEquals(1, testStock.getDaysToInvest());
        }
    }

    @Test
    void testSetMarketCapValid() {
        try {
            testStock.setMarketCap(450000000);
            assertEquals(450000000, testStock.getMarketCap());
        } catch (NegativeDoubleException e) {
            fail("Should not catch any exception here");
        }
    }

    @Test
    void testSetMarketCapInvalid() {
        try {
            testStock.setMarketCap(-500040030);
            fail("Should catch exception here");
        } catch (NegativeDoubleException e) {
            assertEquals(1000000, testStock.getMarketCap());
        }
    }

    @Test
    void testSetRiskValid() {
        try {
            testStock.setRisk(1);
            testStock.setRisk(2);
            testStock.setRisk(3);
            testStock.setRisk(4);
            testStock.setRisk(5);
            assertEquals(5, testStock.getRisk());
        } catch (RiskOutOfBoundaryException e) {
            fail("should not catch exception");
        }
    }

    @Test
    void testSetRiskInvalidHigher() {
        try {
            testStock.setRisk(6);
            fail("should  catch exception");
        } catch (RiskOutOfBoundaryException e) {
            assertEquals(3, testStock.getRisk());
        }
    }

    @Test
    void testSetRiskInvalidLower() {
        try {
            testStock.setRisk(0);
            fail("should  catch exception");
        } catch (RiskOutOfBoundaryException e) {
            assertEquals(3, testStock.getRisk());
        }
    }

    @Test
    void testGetDaysToInvest() {
        assertEquals(1, testStock.getDaysToInvest());
    }

    @Test
    void testAddInvestmentAmount() {
        try {
            testStock.addInvestmentAmount(150);
            assertEquals(650, testStock.getCurrentInvestmentWorth());
        } catch (NegativeDoubleException e) {
            fail("No exception should be thrown here");
        }
    }

    @Test
    void testAddInvestmentAmountNegative() {
        try {
            testStock.addInvestmentAmount(-150);
            fail("Exception should be thrown");
        } catch (NegativeDoubleException e) {
            assertEquals(500, testStock.getCurrentInvestmentWorth());
        }

    }

    @Test
    void testGetRiskFactor() {
        testStock.setRisk(1);
        assertEquals(0.05, testStock.getRiskFactor());

        testStock.setRisk(2);
        assertEquals(0.1, testStock.getRiskFactor());

        testStock.setRisk(3);
        assertEquals(0.2, testStock.getRiskFactor());

        testStock.setRisk(4);
        assertEquals(0.3, testStock.getRiskFactor());

        testStock.setRisk(5);
        assertEquals(0.5, testStock.getRiskFactor());
    }

    @Test
    void testInvestIndividualStockMaxMarketCap() {
        // test many times to account for randomness
        for (int i = 0; i < 20; i++) {
            runBefore();

            testStock.setMarketCap(testStock.getStockPriceCurrent());

            testStock.investIndividualStock();

            assertEquals(500, testStock.getInitialInvestment());
            assertTrue(testStock.getStockPriceCurrent() <= testStock.getStockPricePrevious() &&
                    testStock.getStockPriceCurrent() >= 98.4);
        }
    }

    @Test
    void testInvestIndividualStockMaxNotMarketCap() {
        for (int i = 0; i < 20; i++) {
            runBefore();

            testStock.investIndividualStock();

            assertEquals(500, testStock.getInitialInvestment());
            assertTrue(testStock.getStockPriceCurrent() <= 147.6 &&
                    testStock.getStockPriceCurrent() >= 98.4);
            assertTrue(400 <= testStock.getCurrentInvestmentWorth() &&
                    testStock.getCurrentInvestmentWorth() <= 600);
        }
    }

    @Test
    void testInvestIndividualStockManyDays() {
        for (int i = 0; i < 20; i++) {
            runBefore();

            testStock.setDaysToInvest(3);
            testStock.investIndividualStock();

            assertEquals(500, testStock.getInitialInvestment());

            assertTrue(testStock.getStockPriceCurrent() <= 212.544 &&
                    testStock.getStockPriceCurrent() >= 62.976);
            assertTrue(864 >= testStock.getCurrentInvestmentWorth() &&
                    testStock.getCurrentInvestmentWorth() >= 256);
        }
    }

    @Test
    void testInvestIndividualStockBelowZero() {
        for (int i = 0; i < 20; i++) {
            testStock = new Stock("AAPL", -10000000, 123, 500,
                    500, (500 / 123), 1, 3, 1000000);

            testStock.setDaysToInvest(1);

            testStock.investIndividualStock();

            assertEquals(0, testStock.getStockPriceCurrent());
        }
    }

    @Test
    void testToJson() {
        JSONObject json = testStock.toJson();
        assertEquals("AAPL", json.getString("symbol"));
        assertEquals(123, json.getDouble("stock price current"));
        assertEquals(123, json.getDouble("stock price previous"));
        assertEquals(500, json.getDouble("current investment worth"));
        assertEquals(500, json.getDouble("initial investment"));
        assertEquals((500 / 123), json.getDouble("shares bought"));
        assertEquals(1, json.getInt("days to invest"));
        assertEquals(3, json.getInt("risk"));
        assertEquals(1000000, json.getDouble("market cap"));

    }
}

