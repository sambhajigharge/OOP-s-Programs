package objectOrientedProgram.StockAccountManagement.test.model;

import objectOrientedProgram.StockAccountManagement.main.model.Portfolio;
import objectOrientedProgram.StockAccountManagement.main.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    private Portfolio testPortfolio;
    DecimalFormat df = new DecimalFormat("#.##");

    @BeforeEach
    void runBefore() {
        testPortfolio = new Portfolio();
    }

    @Test
    void testConstructor() {
        assertEquals(0.0, testPortfolio.getBalance());
        assertEquals(0.00, testPortfolio.getValueCurrentlyInvested());
        assertTrue(testPortfolio.getPortfolioList().isEmpty());
    }

    @Test
    void testDepositFromZero() {
        testPortfolio.deposit(500.43);
        assertEquals(500.43, testPortfolio.getBalance());
    }

    @Test
    void testDepositFromSet() {
        testPortfolio.setBalance(432.32);
        testPortfolio.deposit(32.43);
        assertEquals(464.75, testPortfolio.getBalance());
    }

    @Test
    void testSubtractBalance() {
        testPortfolio.setBalance(300.22);
        testPortfolio.subtractBalance(44.32);
        assertEquals(255.90000000000003, testPortfolio.getBalance());
    }

    @Test
    void testAddStockOnce() {
        Stock stock = new Stock();

        stock.setStockPriceCurrent(100);

        testPortfolio.addStock(stock);

        assertEquals(1, testPortfolio.getPortfolioList().size());
        assertEquals(100, testPortfolio.getPortfolioList().get(0).getStockPriceCurrent());
    }

    @Test
    void testAddStockMultiple() {
        Stock stock1 = new Stock();
        stock1.setSymbol("AAPL");

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGL");

        testPortfolio.addStock(stock1);
        testPortfolio.addStock(stock2);

        assertEquals(2, testPortfolio.getPortfolioList().size());
        assertEquals("AAPL", testPortfolio.getPortfolioList().get(0).getSymbol());
        assertEquals("GOOGL", testPortfolio.getPortfolioList().get(1).getSymbol());
    }

    @Test
    void testUpdateValueOneStock() {
        Stock stock = new Stock();

        stock.setCurrentInvestmentWorth(500);

        testPortfolio.addStock(stock);

        testPortfolio.updateValue();

        assertEquals(500, testPortfolio.getValueCurrentlyInvested());
    }

    @Test
    void testUpdateValueMultipleStock() {
        Stock stock1 = new Stock();
        stock1.setCurrentInvestmentWorth(50);
        testPortfolio.addStock(stock1);

        Stock stock2 = new Stock();
        stock2.setCurrentInvestmentWorth(75);
        testPortfolio.addStock(stock2);

        Stock stock3 = new Stock();
        stock3.setCurrentInvestmentWorth(75);
        testPortfolio.addStock(stock3);

        testPortfolio.updateValue();

        assertEquals(200, testPortfolio.getValueCurrentlyInvested());
    }

    @Test
    void testInvestStocksForDaysOneStock() {
        Stock stock1 = new Stock();
        stock1.setCurrentInvestmentWorth(500);
        stock1.setRisk(3);
        stock1.setMarketCap(1000000);
        stock1.setStockPriceCurrent(123);
        testPortfolio.addStock(stock1);

        testPortfolio.investStocksForDays(1);

        assertTrue(400 <= testPortfolio.getValueCurrentlyInvested()
                && testPortfolio.getValueCurrentlyInvested() <= 600);

    }

    @Test
    void testInvestStocksForDaysManyStock() {
        Stock stock1 = new Stock();
        stock1.setCurrentInvestmentWorth(500);
        stock1.setRisk(3);
        stock1.setMarketCap(1000000);
        stock1.setStockPriceCurrent(123);
        testPortfolio.addStock(stock1);

        Stock stock2 = new Stock();
        stock2.setCurrentInvestmentWorth(500);
        stock2.setRisk(3);
        stock2.setMarketCap(1000000);
        stock2.setStockPriceCurrent(123);
        testPortfolio.addStock(stock2);

        testPortfolio.investStocksForDays(1);

        assertTrue(800 <= testPortfolio.getValueCurrentlyInvested()
                && testPortfolio.getValueCurrentlyInvested() <= 1200);

    }

    @Test
    void testCheckForTickerOneStockPresent() {
        Stock stock = new Stock();
        stock.setSymbol("AAPL");
        testPortfolio.addStock(stock);


        assertEquals(stock, testPortfolio.checkForTicker("AAPL"));
    }

    @Test
    void testCheckForTickerOneStockNotPresent() {
        Stock stock = new Stock();
        stock.setSymbol("DUDE");
        testPortfolio.addStock(stock);


        assertEquals(null, testPortfolio.checkForTicker("CHAR"));
    }

    @Test
    void testCheckForTickerManyStockPresent() {
        Stock stock1 = new Stock();
        stock1.setSymbol("AAPL");
        testPortfolio.addStock(stock1);

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGL");
        testPortfolio.addStock(stock2);

        assertEquals(stock2, testPortfolio.checkForTicker("GOOGL"));
    }

    @Test
    void testCheckForTickerManyStockNotPresent() {
        Stock stock1 = new Stock();
        stock1.setSymbol("AAPL");
        testPortfolio.addStock(stock1);

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGL");
        testPortfolio.addStock(stock2);

        assertEquals(null, testPortfolio.checkForTicker("BAGL"));
    }

    @Test
    void testSellStock() {
        Stock stock = new Stock();
        stock.setCurrentInvestmentWorth(500);
        testPortfolio.addStock(stock);

        testPortfolio.sellStock(stock);

        assertEquals(500, testPortfolio.getBalance());
        assertEquals(0, testPortfolio.getPortfolioList().size());
    }

    @Test
    void testToJsonNoStocks() {
        testPortfolio.deposit(300);
        JSONObject json = testPortfolio.toJson();
        assertEquals(300, json.getDouble("balance"));
        assertEquals(0, json.getDouble("value currently invested"));
        assertTrue(json.getJSONArray("portfolio").isEmpty());
    }

    @Test
    void testToJsonTwoStocks() {
        testPortfolio.deposit(300);
        Stock stock1 = new Stock("AAPL", 123, 123, 500,
                500, (500 / 123), 1, 3, 1000000);
        Stock stock2 = new Stock("GOOGL", 250, 250, 500,
                500, 2, 0, 5, 3000000);
        testPortfolio.addStock(stock1);
        testPortfolio.addStock(stock2);

        JSONObject json = testPortfolio.toJson();
        assertEquals(300, json.getDouble("balance"));
        assertEquals(0, json.getDouble("value currently invested"));

        JSONArray stockList = json.getJSONArray("portfolio");

        assertEquals(2, stockList.length());
        assertEquals("AAPL", stockList.getJSONObject(0).getString("symbol"));
        assertEquals("GOOGL", stockList.getJSONObject(1).getString("symbol"));
    }

    @Test
    void testPortfolioToJsonOneStock() {
        Stock stock1 = new Stock("AAPL", 123, 123, 500,
                500, (500 / 123), 1, 3, 1000000);
        testPortfolio.addStock(stock1);

        JSONArray json = testPortfolio.portfolioToJson();

        assertEquals(1, json.length());
        assertEquals("AAPL", json.getJSONObject(0).getString("symbol"));
        assertEquals(123, json.getJSONObject(0).getDouble("stock price current"));
        assertEquals(123, json.getJSONObject(0).getDouble("stock price previous"));
        assertEquals(500, json.getJSONObject(0).getDouble("current investment worth"));
        assertEquals(500, json.getJSONObject(0).getDouble("initial investment"));
        assertEquals((500 / 123), json.getJSONObject(0).getDouble("shares bought"));
        assertEquals(1, json.getJSONObject(0).getInt("days to invest"));
        assertEquals(3, json.getJSONObject(0).getInt("risk"));
        assertEquals(1000000, json.getJSONObject(0).getDouble("market cap"));
    }

    @Test
    void testPortfolioToJsonMultipleStock() {
        Stock stock1 = new Stock("AAPL", 123, 123, 500,
                500, (500 / 123), 1, 3, 1000000);
        testPortfolio.addStock(stock1);

        Stock stock2 = new Stock("GOOGL", 250, 250, 500,
                500, 2, 0, 5, 3000000);
        testPortfolio.addStock(stock2);

        JSONArray json = testPortfolio.portfolioToJson();

        assertEquals(2, json.length());
        assertEquals("AAPL", json.getJSONObject(0).getString("symbol"));
        assertEquals(123, json.getJSONObject(0).getDouble("stock price current"));
        assertEquals(123, json.getJSONObject(0).getDouble("stock price previous"));
        assertEquals(500, json.getJSONObject(0).getDouble("current investment worth"));
        assertEquals(500, json.getJSONObject(0).getDouble("initial investment"));
        assertEquals((500 / 123), json.getJSONObject(0).getDouble("shares bought"));
        assertEquals(1, json.getJSONObject(0).getInt("days to invest"));
        assertEquals(3, json.getJSONObject(0).getInt("risk"));
        assertEquals(1000000, json.getJSONObject(0).getDouble("market cap"));

        assertEquals("GOOGL", json.getJSONObject(1).getString("symbol"));
        assertEquals(250, json.getJSONObject(1).getDouble("stock price current"));
        assertEquals(250, json.getJSONObject(1).getDouble("stock price previous"));
        assertEquals(500, json.getJSONObject(1).getDouble("current investment worth"));
        assertEquals(500, json.getJSONObject(1).getDouble("initial investment"));
        assertEquals(2, json.getJSONObject(1).getDouble("shares bought"));
        assertEquals(0, json.getJSONObject(1).getInt("days to invest"));
        assertEquals(5, json.getJSONObject(1).getInt("risk"));
        assertEquals(3000000, json.getJSONObject(1).getDouble("market cap"));
    }
}

