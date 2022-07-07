package objectOrientedProgram.StockAccountManagement.main.persistance;

import objectOrientedProgram.StockAccountManagement.main.model.Portfolio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import objectOrientedProgram.StockAccountManagement.main.model.Stock;
import objectOrientedProgram.StockAccountManagement.test.model.JSONArray;
import objectOrientedProgram.StockAccountManagement.test.model.JSONObject;

public class JsonReader {
    private String source;
    private Iterable<? extends Object> JSONArray;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads portfolio from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Portfolio read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePortfolio(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses portfolio from JSON object and returns it
    private Portfolio parsePortfolio(JSONObject jsonObject) {
        double balance = Double.parseDouble(jsonObject.getDouble("balance"));
        double valueCurrentlyInvested = Double.parseDouble(jsonObject.getDouble("value currently invested"));
        Portfolio portfolio = new Portfolio(balance, valueCurrentlyInvested);
        addStocks(portfolio, jsonObject);
        return portfolio;
    }

    // MODIFIES: portfolio
    // EFFECTS: parses thingies from JSON object and adds them to portfolio
    private void addStocks(Portfolio portfolio, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("portfolio");
        for (Object json : JSONArray) {
            JSONObject nextStock = (JSONObject) json;
            addStock(portfolio, nextStock);
        }
    }

    // MODIFIES: portfolio
    // EFFECTS: parses thingy from JSON object and adds it to portfolio
    private void addStock(Portfolio portfolio, JSONObject jsonObject) {
        String symbol = jsonObject.getString("symbol");
        double stockPriceCurrent = Double.parseDouble(jsonObject.getDouble("stock price current"));
        double stockPricePrevious = Double.parseDouble(jsonObject.getDouble("stock price previous"));
        double currentInvestmentWorth = Double.parseDouble(jsonObject.getDouble("current investment worth"));
        double initialInvestment = Double.parseDouble(jsonObject.getDouble("initial investment"));
        double sharesBought = Double.parseDouble(jsonObject.getDouble("shares bought"));
        int daysToInvest = jsonObject.getInt("days to invest");
        int risk = jsonObject.getInt("risk");
        double marketCap = Double.parseDouble(jsonObject.getDouble("market cap"));
        Stock stock = new Stock(symbol, stockPriceCurrent, stockPricePrevious, currentInvestmentWorth,
                initialInvestment, sharesBought, daysToInvest, risk, marketCap);
        portfolio.addStock(stock);
    }
}

