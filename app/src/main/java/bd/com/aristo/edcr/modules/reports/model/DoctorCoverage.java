package bd.com.aristo.edcr.modules.reports.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by monir.sobuj on 10/10/2018.
 */

public class DoctorCoverage {
/*
* 1. TotalDoctor,
* 2. TotalPlannedDoctor,
* 3. PlannedPer,
* 4. TotalDoctorVisited,
* 5. DoctorCoveragePer,
* 6. TotalNoOfDOTPlan,
* 7. TotalExecutionOfDOTPlan,
* 8. TotalVisitOfDOTPlannedDoctorWithoutPlan,
* 9. PlanExecutionPer,
* 10. TotalVisitOfUnplannedDoctor,
* 11. TotalNoOfNonDOTDoctorVisited,
* 12. TotalExecutionOfNewDoctor,
* 13. TotalVisitOfNewDoctor,
* 14. TotalVisitOfInterDoctor
*/

    @SerializedName("TotalDoctor")
    private String totalDoctor; //1
    @SerializedName("TotalPlannedDoctor")
    private String totalPlannedDoctor; //2
    @SerializedName("PlannedPer")
    private String planned; //3
    @SerializedName("TotalDoctorVisited")
    private String totalDoctorVisited; //4
    @SerializedName("DoctorCoveragePer")
    private String doctorCoverage; // 5
    @SerializedName("TotalNoOfDOTPlan")
    private String totalNoOfDOTPlan; //6
    @SerializedName("TotalExecutionOfDOTPlan")
    private String totalExecutionOfDOTPlan; //7
    @SerializedName("TotalVisitOfDOTPlannedDoctorWithoutPlan")
    private String totalVisitOfDOTPlannedDoctorWithoutPlan; //8
    @SerializedName("PlanExecutionPer")
    private String planExecution; //9
    @SerializedName("TotalVisitOfUnplannedDoctor")
    private String totalVisitOfUnplannedDoctor; //10
    @SerializedName("TotalNoOfNonDOTDoctorVisited")
    private String totalNoOfNonDOTDoctorVisited;//11
    @SerializedName("TotalExecutionOfNewDoctor")
    private String totalExecutionOfNewDoctor; //12
    @SerializedName("TotalVisitOfNewDoctor")
    private String totalVisitOfNewDoctor; //13
    @SerializedName("TotalVisitOfInterDoctor")
    private String totalVisitOfInterDoctor; //14

    public String getTotalDoctor() {
        return this.totalDoctor;
    }

    public void setTotalDoctor(final String totalDoctor) {
        this.totalDoctor = totalDoctor;
    }

    public String getTotalPlannedDoctor() {
        return this.totalPlannedDoctor;
    }

    public void setTotalPlannedDoctor(final String totalPlannedDoctor) {
        this.totalPlannedDoctor = totalPlannedDoctor;
    }

    public String getPlanned() {
        return this.planned;
    }

    public void setPlanned(final String planned) {
        this.planned = planned;
    }

    public String getTotalDoctorVisited() {
        return this.totalDoctorVisited;
    }

    public void setTotalDoctorVisited(final String totalDoctorVisited) {
        this.totalDoctorVisited = totalDoctorVisited;
    }

    public String getDoctorCoverage() {
        return this.doctorCoverage;
    }

    public void setDoctorCoverage(final String doctorCoverage) {
        this.doctorCoverage = doctorCoverage;
    }

    public String getTotalNoOfDOTPlan() {
        return this.totalNoOfDOTPlan;
    }

    public void setTotalNoOfDOTPlan(final String totalNoOfDOTPlan) {
        this.totalNoOfDOTPlan = totalNoOfDOTPlan;
    }

    public String getTotalExecutionOfDOTPlan() {
        return this.totalExecutionOfDOTPlan;
    }

    public void setTotalExecutionOfDOTPlan(final String totalExecutionOfDOTPlan) {
        this.totalExecutionOfDOTPlan = totalExecutionOfDOTPlan;
    }

    public String getTotalVisitOfDOTPlannedDoctorWithoutPlan() {
        return this.totalVisitOfDOTPlannedDoctorWithoutPlan;
    }

    public void setTotalVisitOfDOTPlannedDoctorWithoutPlan(final String totalVisitOfDOTPlannedDoctorWithoutPlan) {
        this.totalVisitOfDOTPlannedDoctorWithoutPlan = totalVisitOfDOTPlannedDoctorWithoutPlan;
    }

    public String getPlanExecution() {
        return this.planExecution;
    }

    public void setPlanExecution(final String planExecution) {
        this.planExecution = planExecution;
    }

    public String getTotalVisitOfUnplannedDoctor() {
        return this.totalVisitOfUnplannedDoctor;
    }

    public void setTotalVisitOfUnplannedDoctor(final String totalVisitOfUnplannedDoctor) {
        this.totalVisitOfUnplannedDoctor = totalVisitOfUnplannedDoctor;
    }

    public String getTotalNoOfNonDOTDoctorVisited() {
        return this.totalNoOfNonDOTDoctorVisited;
    }

    public void setTotalNoOfNonDOTDoctorVisited(final String totalNoOfNonDOTDoctorVisited) {
        this.totalNoOfNonDOTDoctorVisited = totalNoOfNonDOTDoctorVisited;
    }

    public String getTotalExecutionOfNewDoctor() {
        return this.totalExecutionOfNewDoctor;
    }

    public void setTotalExecutionOfNewDoctor(final String totalExecutionOfNewDoctor) {
        this.totalExecutionOfNewDoctor = totalExecutionOfNewDoctor;
    }

    public String getTotalVisitOfNewDoctor() {
        return this.totalVisitOfNewDoctor;
    }

    public void setTotalVisitOfNewDoctor(final String totalVisitOfNewDoctor) {
        this.totalVisitOfNewDoctor = totalVisitOfNewDoctor;
    }

    public String getTotalVisitOfInterDoctor() {
        return this.totalVisitOfInterDoctor;
    }

    public void setTotalVisitOfInterDoctor(final String totalVisitOfInterDoctor) {
        this.totalVisitOfInterDoctor = totalVisitOfInterDoctor;
    }
}
