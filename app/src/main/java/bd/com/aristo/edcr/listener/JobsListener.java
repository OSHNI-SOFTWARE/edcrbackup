package bd.com.aristo.edcr.listener;

import android.app.job.JobParameters;

/**
 * Created by monir.sobuj on 10/1/2018.
 */

public interface JobsListener {
    void onFinish(JobParameters params);
}
