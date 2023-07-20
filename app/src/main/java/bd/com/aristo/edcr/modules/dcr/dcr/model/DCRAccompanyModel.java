package bd.com.aristo.edcr.modules.dcr.dcr.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 6/13/17.
 */

public class DCRAccompanyModel extends RealmObject {

    @PrimaryKey
    private long id;
    private long dcrID;
    private String empID;

    @Ignore
    public static String COL_ID = "id", COL_EMP_ID = "empID", COL_DCR_ID = "dcrID";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDcrID() {
        return dcrID;
    }

    public void setDcrID(long dcrID) {
        this.dcrID = dcrID;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    @Override
    public String toString() {
        return "DCRAccompanyModel{" +
                "id=" + id +
                ", dcrID=" + dcrID +
                ", empID='" + empID + '\'' +
                '}';
    }
}
