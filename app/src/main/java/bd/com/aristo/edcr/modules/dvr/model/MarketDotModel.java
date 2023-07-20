package bd.com.aristo.edcr.modules.dvr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 6/7/2017.
 */

public class MarketDotModel {

    @SerializedName("day")
    public String day;
    @SerializedName("a")
    public String a;
    @SerializedName("b")
    public String b;
    @SerializedName("c")
    public String c;
    @SerializedName("d")
    public String d;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    @Override
    public String toString() {
        return "MarketDotModel{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                '}';
    }
}
