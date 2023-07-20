package bd.com.aristo.edcr.models;


/**
 * Created by monir.sobuj on 01/11/17.
 */

public class DOTPlan {

    private int morningDOT;
    private int eveningDOT;
    private int morningWP;
    private int eveningWP;

    public int getMorningDOT() {
        return morningDOT;
    }

    public void setMorningDOT(int morningDOT) {
        this.morningDOT = morningDOT;
    }

    public int getEveningDOT() {
        return eveningDOT;
    }

    public void setEveningDOT(int eveningDOT) {
        this.eveningDOT = eveningDOT;
    }

    public int getMorningWP() {
        return morningWP;
    }

    public void setMorningWP(int morningWP) {
        this.morningWP = morningWP;
    }

    public int getEveningWP() {
        return eveningWP;
    }

    public void setEveningWP(int eveningWP) {
        this.eveningWP = eveningWP;
    }

    @Override
    public String toString() {
        return "DOTPlan{" +
                "morningDOT=" + morningDOT +
                ", eveningDOT=" + eveningDOT +
                ", morningWP=" + morningWP +
                ", eveningWP=" + eveningWP +
                '}';
    }
}
