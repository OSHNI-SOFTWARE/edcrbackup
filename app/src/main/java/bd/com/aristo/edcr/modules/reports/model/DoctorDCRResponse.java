package bd.com.aristo.edcr.modules.reports.model;


import java.util.ArrayList;
import java.util.List;

import bd.com.aristo.edcr.modules.reports.ss.model.NewDoctorDCR;

/**
 * Created by altaf.sil on 3/18/18.
 */

public class DoctorDCRResponse {

    private static DoctorDCRResponse ddResponse = null;
    private DoctorDCRResponse(){

    }

    public static DoctorDCRResponse getInstance(){
        if(ddResponse == null){
            ddResponse = new DoctorDCRResponse();
        }

        return ddResponse;
    }

    private List<IDOTExecution> idotExecutionList = new ArrayList<>();
    private List<AbsentReport> absentReportList = new ArrayList<>();
    private List<NewDoctorDCR> newDoctorDCRList = new ArrayList<>();

    public List<IDOTExecution> getIdotExecutionList() {
        return idotExecutionList;
    }

    public void setIdotExecutionList(List<IDOTExecution> idotExecutionList) {
        this.idotExecutionList = idotExecutionList;
    }

    public List<AbsentReport> getAbsentReportList() {
        return absentReportList;
    }

    public void setAbsentReportList(List<AbsentReport> absentReportList) {
        this.absentReportList = absentReportList;
    }

    public List<NewDoctorDCR> getNewDoctorDCRList() {
        return newDoctorDCRList;
    }

    public void setNewDoctorDCRList(List<NewDoctorDCR> newDoctorDCRList) {
        this.newDoctorDCRList = newDoctorDCRList;
    }
}
