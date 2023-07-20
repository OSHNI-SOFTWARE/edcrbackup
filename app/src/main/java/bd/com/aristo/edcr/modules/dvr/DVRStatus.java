package bd.com.aristo.edcr.modules.dvr;

public class DVRStatus {
    int[] status ;
    boolean[] newStatus;

    public int[] getStatus() {
        return status;
    }

    public void setStatus(int[] status) {
        this.status = status;
    }

    public boolean[] getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(boolean[] newStatus) {
        this.newStatus = newStatus;
    }
}
