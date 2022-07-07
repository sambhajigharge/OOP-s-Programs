package objectOrientedProgram.StockAccountManagement.main.persistance;

import objectOrientedProgram.StockAccountManagement.test.model.JSONObject;

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
