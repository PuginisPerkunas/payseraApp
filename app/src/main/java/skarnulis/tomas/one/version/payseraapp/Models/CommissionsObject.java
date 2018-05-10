package skarnulis.tomas.one.version.payseraapp.Models;

import com.google.gson.annotations.SerializedName;

public class CommissionsObject {

    @SerializedName("conversionDate")
    private String conversionDate;
    @SerializedName("conversionFromCurr")
    private String conversionFromCurr;
    @SerializedName("conversionFromAmount")
    private double conversionFromAmount;
    @SerializedName("conversionToCurr")
    private String conversionToCurr;
    @SerializedName("conversionToAmount")
    private double conversionToAmount;
    @SerializedName("commissionsPrice")
    private double commissionsPrice;

    public CommissionsObject(String conversionDate, String conversionFromCurr, double conversionFromAmount,
                             String conversionToCurr, double conversionToAmount, double commissionsPrice) {
        this.conversionDate = conversionDate;
        this.conversionFromCurr = conversionFromCurr;
        this.conversionFromAmount = conversionFromAmount;
        this.conversionToCurr = conversionToCurr;
        this.conversionToAmount = conversionToAmount;
        this.commissionsPrice = commissionsPrice;
    }

    public String getConversionDate() {
        return conversionDate;
    }


    public String getConversionFromCurr() {
        return conversionFromCurr;
    }


    public double getConversionFromAmount() {
        return conversionFromAmount;
    }


    public String getConversionToCurr() {
        return conversionToCurr;
    }


    public double getConversionToAmount() {
        return conversionToAmount;
    }


    public double getCommissionsPrice() {
        return commissionsPrice;
    }

}
