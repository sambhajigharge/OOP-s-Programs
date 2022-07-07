package objectOrientedProgram.StockAccountManagement.main.userInterface;

import objectOrientedProgram.StockAccountManagement.main.exceptions.NegativeDoubleException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.NonCapLetterException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.RiskOutOfBoundaryException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.TicketLengthException;
import objectOrientedProgram.StockAccountManagement.main.model.Portfolio;
import objectOrientedProgram.StockAccountManagement.main.model.Stock;
import objectOrientedProgram.StockAccountManagement.main.persistance.JsonReader;
import objectOrientedProgram.StockAccountManagement.main.persistance.JsonWriter;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;


public class PortfolioApp {
    private static final String JSON_STORE = "./data/portfolio.json";

    private Portfolio portfolio;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    public PortfolioApp() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPortfolio();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runPortfolio() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase(Locale.ROOT);

            // q is quit
            if (command.equals("9")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("1")) {
            addMoney();
        } else if (command.equals("2")) {
            addStock();
        } else if (command.equals("3")) {
            sellStock();
        } else if (command.equals("4")) {
            investMoreInExistingStock();
        } else if (command.equals("5")) {
            investPortfolio();
        } else if (command.equals("6")) {
            returnPortfolioBalance();
        } else if (command.equals("7")) {
            saveCurrentPortfolio();
        } else if (command.equals("8")) {
            loadPortfolio();
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes portfolio

    public void init() {
        portfolio = new Portfolio();
        input = new Scanner(System.in);
    }

    // EFFECTS: option menu for user
    private void displayMenu() {
        System.out.println("\nStock Portfolio Simulation Options:");
        System.out.println("\t[1]: Add Money");
        System.out.println("\t[2]: Add Stock");
        System.out.println("\t[3]: Sell and Remove Stock");
        System.out.println("\t[4]: Invest More in Current Stock");
        System.out.println("\t[5]: Invest in Stocks");
        System.out.println("\t[6]: Check Portfolio Balance");
        System.out.println("\t[7]: Save Current Portfolio");
        System.out.println("\t[8]: Load Portfolio");
        System.out.println("\t[9]: End Simulation");
    }

    // MODIFIES: this
    // EFFECTS: adds money to the portfolio balance if >= 0.0 and prints balance afterwards
    private void addMoney() {
        System.out.print("Enter amount to deposit: $");
        double amount = input.nextDouble();

        if (amount >= 0.0) {
            portfolio.deposit(amount);
        } else {
            System.out.println("Cannot deposit negative amount...\n");
        }

        returnPortfolioBalance();
    }


    // MODIFIES: this
    // EFFECTS: call invest methods on portfolio. Invests amount specified for each stock for inputted number of days
    private void investPortfolio() {
        investMenu();

        printInvestingOutcome();
    }

    // MODIFIES: this
    // EFFECTS: sells the stock that is inputted and removes it from the list of stocks active
    private void sellStock() {
        System.out.println("What is the ticker of the stock you would like to sell?");
        String ticker = input.next();

        Stock stock = portfolio.checkForTicker(ticker);

        portfolio.sellStock(stock);


    }

    // MODIFIES: THIS
    // EFFECTS: will add stock with all relevant fields to portfolio
    private void addStock() {
        Stock stock = new Stock();

        tickerMenu(stock);

        stockPrice(stock);

        marketCap(stock);

        riskFactor(stock);

        portfolio.addStock(stock);
    }

    // EFFECT: prints out current free balance of portfolio (not including money invested)
    private void returnPortfolioBalance() {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Portfolio balance: $" + df.format(portfolio.getBalance()));
    }

    // REQUIRES: ticker must not already be used
    // EFFECTS: prompts user for stock ticker
    //              - keeps asking if it isn't a string of all capital letters
    //              - sets stock ticker to the inputted string
    public void tickerMenu(Stock stock) {
        System.out.println("What is the stock ticker? (eg. AAPL)");
        String ticker = input.next();

        while (!ticker.matches("[A-Z]+")) {
            System.out.println("Must be all capital letters.");
            ticker = input.next();
        }
        try {
            stock.setSymbol(ticker);
        } catch (NonCapLetterException exception) {
            System.out.println("Only capital letters allowed");
        } catch (TicketLengthException exception) {
            System.out.println("Invalid symbol: longer than 5 characters");
        }
    }

    // EFFECTS: asks user for stock price
    //              - accepts double only if it is >= 0
    //              - sets stockprice to inputted number
    public void stockPrice(Stock stock) {
        System.out.println("What is the current stock price? (eg. AAPL: 135.37)");
        double stockPrice = input.nextDouble();

        while (stockPrice < 0) {
            System.out.println("Input positive number.");
            stockPrice = input.nextDouble();
        }
        try {
            stock.setStockPriceCurrent(stockPrice);
        } catch (NegativeDoubleException exception) {
            System.out.println("Negative stock price is invalid");
        }

    }

    //EFFECTS: asks user for marketCap of stock
    //               - doesn't accept until positive number has been inputted
    //               - sets stock market cap to inputted value
    public void marketCap(Stock stock) {
        System.out.println("What is the market cap of this stock? (eg. AAPL: 2723000000000");
        double marketCap = input.nextDouble();

        while (marketCap < 0) {
            System.out.println("Input positive number.");
            marketCap = input.nextDouble();
        }

        try {
            stock.setMarketCap(marketCap);

        } catch (NegativeDoubleException e) {
            System.out.println("Input positive number for marketcap");
        }
    }

    //EFFECTS: asks user for how risk the stock is
    //               - doesn't accept unless value is between 1 and 5 inclusive
    //               - sets stock risk value to inputted value
    public void riskFactor(Stock stock) {
        System.out.println(
                "How risky is the stock from 1 - 5? (eg. 5 is riskiest, could lose or gain the most)");
        int riskFactor = input.nextInt();

        while (riskFactor < 1 || riskFactor > 5) {
            System.out.println("Input integer from between 1 and 5.");
            riskFactor = input.nextInt();
        }
        try {
            stock.setRisk(riskFactor);
        } catch (RiskOutOfBoundaryException e) {
            System.out.println("Risk outside of 1 - 5 boundary");
        }
    }

    // MODIFIES: this
    // EFFECTS: For each stock in portfolio:
    //              - asks for how much user would like to invest
    //              - takes user input only if portfolio balance is larger than investment amount
    //              - sets investment amount to inputted amount and subtracts that amount from portfolio balance
    //          Asks how many days to invest
    //              -only accepts positive number
    //              - sets each stock to be invested for that maount of days
    private void investMenu() {
        for (Stock i : portfolio.getPortfolioList()) {
            System.out.println("How much would you like to invest in stock " + i.getSymbol() + "?");

            double amount = input.nextDouble();

            if (amount > portfolio.getBalance()) {
                System.out.println("Not enough money in portfolio");
                return;
            }
            try {
                i.setCurrentInvestmentWorth(amount);
                portfolio.subtractBalance(amount);
            } catch (NegativeDoubleException e) {
                System.out.println("Cannot set negative amount to invest");
            }
        }
        System.out.println("For how many days would you like to invest?");
        int days = input.nextInt();
        while (days < 0) {
            System.out.println("Please input a positive number for amount of days to invest:");
            days = input.nextInt();
        }
        portfolio.investStocksForDays(days);
    }

    // EFFECTS: Prints result of latest investing cycle
    private void printInvestingOutcome() {
        DecimalFormat df = new DecimalFormat("#.##");
        for (Stock i : portfolio.getPortfolioList()) {
            System.out.println("Stock " + i.getSymbol() + " outcome:");
            System.out.println("Invested for " + i.getDaysToInvest() + " days.");
            System.out.println("Previous stock price: $" + df.format(i.getStockPricePrevious()));
            System.out.println("Current stock price: $" + df.format(i.getStockPriceCurrent()));
            System.out.println("Initial investment: $" + df.format(i.getInitialInvestment()));
            System.out.println("Current investment worth: $" + df.format(i.getCurrentInvestmentWorth()));
            System.out.println("Gains/losses: " + df.format(i.getCurrentInvestmentWorth() - i.getInitialInvestment()));
            System.out.println("");
        }
    }

    // EFFECTS: user prompt for investing more into an individual stock
    //                 - gets user input
    //                 - adds that to current amount invested
    private void investMoreInExistingStock() {
        System.out.println("Enter ticker of stock you would like to invest more in: ");
        String ticker = input.next();

        Stock stock = portfolio.checkForTicker(ticker);

        if (!(stock == null)) {
            System.out.println("How much would you like to invest?");
            double amount = input.nextInt();

            try {
                stock.addInvestmentAmount(amount);
            } catch (NegativeDoubleException e) {
                System.out.println("Inputted negative number");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: loads portfolio from file
    private void loadPortfolio() {
        try {
            portfolio = jsonReader.read();
            System.out.println("Loaded portfolio saved at " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the portfolio to file in data folder
    private void saveCurrentPortfolio() {
        try {
            jsonWriter.open();
            jsonWriter.write(portfolio);
            jsonWriter.close();
            System.out.println("Saved portfolio to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}

