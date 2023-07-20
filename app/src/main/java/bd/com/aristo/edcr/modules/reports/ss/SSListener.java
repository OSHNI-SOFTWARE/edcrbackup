package bd.com.aristo.edcr.modules.reports.ss;

import bd.com.aristo.edcr.modules.reports.ss.model.SSResponse;
import bd.com.aristo.edcr.modules.reports.ss.model.SampleStatementResponse;

public interface SSListener {

    void getSS(SSResponse ssResponse);
    void getSamples(SampleStatementResponse ssResponse);
    void onError();
}
