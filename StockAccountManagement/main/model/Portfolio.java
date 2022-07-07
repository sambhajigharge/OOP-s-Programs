package objectOrientedProgram.StockAccountManagement.main.model;

import objectOrientedProgram.StockAccountManagement.main.persistance.Writable;
import objectOrientedProgram.StockAccountManagement.main.exceptions.NegativeDoubleException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.NegativeIntException;
import objectOrientedProgram.StockAccountManagement.test.model.JSONArray;
import objectOrientedProgram.StockAccountManagement.test.model.JSONObject;

import java.util.ArrayList;

public class Portfolio implements Writable {
    // Fields:
    private double balance;
    private double valueCurrentlyInvested;
    ArrayList<Stock> portfolio;

    public Portfolio() {
        this.balance = 0.00;
        this.valueCurrentlyInvested = 0.00;
        portfolio = new ArrayList<>();
    }

    public Portfolio(double balance, double valueCurrentlyInvested) {
        this.balance = balance;
        this.valueCurrentlyInvested = valueCurrentlyInvested;
        portfolio = new ArrayList<>();
    }

    // Getters:
    public double getValueCurrentlyInvested() {

        return valueCurrentlyInvested;
    }

    public double getBalance() {

        return this.balance;
    }

    public ArrayList<Stock> getPortfolioList() {

        return this.portfolio;
    }

    public String getStocksAsString() {
        String stockString = "<html>";

        for (Stock stock : portfolio) {
            stockString = stockString + "Stock: " + stock.getSymbol() + "<br/>";
            stockString = stockString + "Current Stock Price: $" + stock.getStockPriceCurrent() + "<br/>";
            stockString = stockString + "Stock Price Before Investing: $" + stock.getStockPricePrevious() + "<br/>";
            stockString = stockString + "Current Amount Invested: $" + stock.getCurrentInvestmentWorth() + "<br/>";
            stockString = stockString + "Initial Invested Amount: $" + stock.getInitialInvestment() + "<br/>";
            stockString = stockString + "Amount of Shares: " + stock.getSharesBought() + "<br/>";
            stockString = stockString + "Stock Risk (1-5): " + stock.getRisk() + "<br/>";
            stockString = stockString + "Market Cap: $" + stock.getMarketCap() + "<br/><br/><br/>";
        }
        return stockString + "</html>";
    }

    // Setters:

    public void setBalance(double balance) {

        this.balance = balance;
    }

    // Methods:


    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: add the amount inputted onto the current portfolio balance
    public void deposit(double amount) {

        this.balance += amount;
    }

    // REQUIRES: positive amount
    // MODIFIES: this
    // EFFECTS: subtract the amount inputted onto the current portfolio balance
    public void subtractBalance(double amount) {

        this.balance -= amount;
    }


    // MODIFIES: this
    // EFFECTS: add a new stock to the portfolio arraylist
    public void addStock(Stock stock) {

        this.portfolio.add(stock);
    }

    // MODIFIES: this
    // EFFECTS: adds up each invested amount of each stock in a portfolio
    public void updateValue() {
        this.valueCurrentlyInvested = 0;
        for (Stock i : this.portfolio) {
            this.valueCurrentlyInvested += i.getCurrentInvestmentWorth();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the number of days to invest each stock. Individually invest all stock and then update
    // the total value currently invested in all stocks
    public void investStocksForDays(int days) {
        for (Stock i : portfolio) {
            try {
                i.setDaysToInvest(days);
                i.investIndividualStock();
            } catch (NegativeIntException e) {
                System.out.println("Can't have negative days");
            }
        }
        updateValue();
    }

    // EFFECTS: returns stock that matches the ticker
    public Stock checkForTicker(String ticker) {
        for (Stock i : portfolio) {
            if (ticker.equals(i.getSymbol())) {
                return i;
            }
        }
        System.out.println("No stock found with that symbol");
        return null;
    }


    // MODIFIES: this
    // EFFECTS: adds the amount invested in stock to portfolio balance.
    //              - sets amount invested in stock to 0
    //              - removes stock from portfolio
    public void sellStock(Stock stock) {
        double amountInvested = stock.getCurrentInvestmentWorth();

        try {
            stock.setCurrentInvestmentWorth(0);
        } catch (NegativeDoubleException e) {
            System.out.println("invested negative amount illegal");
        }

        this.balance += amountInvested;

        portfolio.remove(stock);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("balance", balance);
        json.put("value currently invested", valueCurrentlyInvested);
        json.put("portfolio", portfolioToJson());
        return json;
    }

    // EFFECTS: returns things in this portfolio as a JSON array
    public JSONArray portfolioToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Stock t : portfolio) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }
}

