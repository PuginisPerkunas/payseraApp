package skarnulis.tomas.one.version.payseraapp.Models;

import com.google.gson.annotations.SerializedName;

public class ConvertResponseObject {
    @SerializedName("amount")
    double amount;
    @SerializedName("currency")
    String currency;

    public ConvertResponseObject(float amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

}
