package bd.com.aristo.edcr.models.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 09/06/20.
 */

public class TokenModel extends RealmObject {

    @PrimaryKey
    @SerializedName("LocCode")
    @Expose
    private String locCode;

    @SerializedName("Token")
    @Expose
    private String token;

    @SerializedName("Designation")
    @Expose
    private String designation;

    @Ignore
    public static final String COL_LOC_CODE = "locCode", COL_DESIGNATION = "designation", COL_TOKEN = "token";

    public String getLocCode() {
        return locCode;
    }

    public void setLocCode(String locCode) {
        this.locCode = locCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "locCode='" + locCode + '\'' +
                ", token='" + token + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
