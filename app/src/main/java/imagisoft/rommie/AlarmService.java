package imagisoft.rommie;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import imagisoft.edepa.ScheduleEvent;

public class AlarmService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {

        assert job.getExtras() != null;
        String json = job.getExtras().getString("ScheduleEvent");
        ScheduleEvent event = ScheduleEvent.fromJson(json);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("TAG", "Job cancelled!");
        return false;
    }

}