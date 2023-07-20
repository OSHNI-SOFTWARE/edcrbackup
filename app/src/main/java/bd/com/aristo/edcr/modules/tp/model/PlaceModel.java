package bd.com.aristo.edcr.modules.tp.model;

import bd.com.aristo.edcr.utils.StringUtils;

/**
 * Created by monir.sobuj on 6/8/17.
 */

public class PlaceModel  {

    private long id;

    private String InstitutionCode;

    private String InstitutionName;

    private String ShiftName;
    public PlaceModel(){

    }
    public PlaceModel(long id, String institutionCode, String institutionName, String shiftName) {
        this.id = id;
        InstitutionCode = institutionCode;
        InstitutionName = institutionName;
        ShiftName = shiftName;
    }

    public String getInstitutionCode() {
        return InstitutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.InstitutionCode = institutionCode;
    }

    public String getInstitutionName() {
        return InstitutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.InstitutionName = institutionName;
    }

    public String getShift() {
        return ShiftName;
    }

    public void setShift(String shift) {
        this.ShiftName = shift;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PlaceModel{" +
                "unique id='" + StringUtils.hashString64Bit(InstitutionCode) + '\'' +
                "InstitutionCode='" + InstitutionCode + '\'' +
                ", InstitutionName='" + InstitutionName + '\'' +
                ", Shift='" + ShiftName + '\'' +
                '}';
    }

}
