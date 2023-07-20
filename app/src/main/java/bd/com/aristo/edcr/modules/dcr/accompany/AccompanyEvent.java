package bd.com.aristo.edcr.modules.dcr.accompany;

/**
 * Created by altaf.sil on 3/11/18.
 */

public class AccompanyEvent {
    private String accompanyID;

    public AccompanyEvent(String accompanyID) {
        this.accompanyID = accompanyID;
    }

    public String getAccompanyID() {
        return accompanyID;
    }

    public void setAccompanyID(String accompanyID) {
        this.accompanyID = accompanyID;
    }
}
