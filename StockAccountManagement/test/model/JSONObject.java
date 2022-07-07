package objectOrientedProgram.StockAccountManagement.test.model;

public class JSONObject {
    public JSONObject(String jsonData) {

    }

    public JSONObject() {

    }

    public String getString(String symbol) {
        return symbol;
    }

    public String getDouble(String stock_price_current) {
        return stock_price_current;
    }


    public int getInt(String days_to_invest) {
        return days_to_invest();
    }

    private int days_to_invest() {
        return 0;
    }

    public JSONArray getJSONArray(String portfolio) {
        return null;
    }

    public void put(String balance, double balance1) {

    }

    public String toString(int tab) {
        return null;
    }

    public void put(String portfolio, JSONArray portfolioToJson) {

    }
}
