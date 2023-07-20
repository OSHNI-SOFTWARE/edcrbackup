package bd.com.aristo.edcr.modules.tp.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class TPPlaceRealmModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String code;
    private String shift;
    private long tpId;

    @Ignore
    public static String COL_ID = "id", COL_CODE = "code", COL_TP_ID = "tpId";
    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public long getTpId() {
        return tpId;
    }

    public void setTpId(long tpId) {
        this.tpId = tpId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPPlaceRealmModel that = (TPPlaceRealmModel) o;

        if (!code.equals(that.code)) return false;
        return shift.equals(that.shift);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + shift.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TPPlaceRealmModel{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", shift='" + shift + '\'' +
                '}';
    }
}
