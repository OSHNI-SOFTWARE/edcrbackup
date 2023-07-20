package bd.com.aristo.edcr.modules.pwds;

import java.io.Serializable;

/**
 * Created by monir.sobuj on 6/24/2018.
 */

public class PWDSUtilsModel implements Serializable{

    String code;
    String name = "";
    int plannedCount = 0;
    int position;

    public PWDSUtilsModel(String code, String name, int plannedCount, int position) {
        this.code = code;
        this.name = name;
        this.plannedCount = plannedCount;
        this.position = position;
    }

    public PWDSUtilsModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlannedCount() {
        return plannedCount;
    }

    public void setPlannedCount(int plannedCount) {
        this.plannedCount = plannedCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
