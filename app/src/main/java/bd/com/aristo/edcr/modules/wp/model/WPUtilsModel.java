package bd.com.aristo.edcr.modules.wp.model;

import java.io.Serializable;

/**
 * Created by Tariqul.Islam on 6/13/17.
 */

public class WPUtilsModel implements Serializable{

    private String docId;
    private String docName;
    private String docIns;
    private boolean isMorning;

    public WPUtilsModel(String docId, String docName, String docIns, boolean isMorning) {
        this.docId = docId;
        this.docName = docName;
        this.docIns = docIns;
        this.isMorning = isMorning;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocIns() {
        return docIns;
    }

    public void setDocIns(String docIns) {
        this.docIns = docIns;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }
}
