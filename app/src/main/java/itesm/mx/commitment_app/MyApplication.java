package itesm.mx.commitment_app;

import android.app.Application;

/**
 * Created by nloyola on 6/11/16.
 */
public class MyApplication extends Application {

    private String project;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}
