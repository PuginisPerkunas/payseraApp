package skarnulis.tomas.one.version.payseraapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MainDataObject implements Parcelable {

    @SerializedName("freeCommissionsAmount")
    private int freeCommissionsAmount;
    @SerializedName("commissionsTax")
    private double commissionsTax;
    @SerializedName("EUR")
    private double EUR;
    @SerializedName("USD")
    private double USD;
    @SerializedName("JPY")
    private double JPY;

    public MainDataObject(int freeCommissionsAmount, double commissionsTax, double EUR, double USD, double JPY) {
        this.freeCommissionsAmount = freeCommissionsAmount;
        this.commissionsTax = commissionsTax;
        this.EUR = EUR;
        this.USD = USD;
        this.JPY = JPY;
    }

    public int getFreeCommissionsAmount() {
        return freeCommissionsAmount;
    }

    public double getCommissionsTax() {
        return commissionsTax;
    }

    public double getEUR() {
        return EUR;
    }

    public double getUSD() {
        return USD;
    }

    public double getJPY() {
        return JPY;
    }

    public void setCommissionsTax(double commissionsTax) {
        this.commissionsTax = commissionsTax;
    }

    public void setFreeCommissionsAmount(int freeCommissionsAmount) {
        this.freeCommissionsAmount = freeCommissionsAmount;
    }

    public void setEUR(double EUR) {
        this.EUR = EUR;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public void setJPY(double JPY) {
        this.JPY = JPY;
    }

    public MainDataObject(Parcel item) {
        freeCommissionsAmount = item.readInt();
        commissionsTax = item.readDouble();
        EUR = item.readDouble();
        USD = item.readDouble();
        JPY = item.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(freeCommissionsAmount);
        dest.writeDouble(commissionsTax);
        dest.writeDouble(EUR);
        dest.writeDouble(USD);
        dest.writeDouble(JPY);
    }

    public static final Creator<MainDataObject> CREATOR = new Creator<MainDataObject>() {

        @Override
        public MainDataObject createFromParcel(Parcel source) {
            return new MainDataObject(source);
        }

        @Override
        public MainDataObject[] newArray(int size) {
            return new MainDataObject[0];
        }

    };
}
