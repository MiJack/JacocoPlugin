package cn.mijack.jacocoplugin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static cn.mijack.jacocoplugin.JacocoInstrumentation.TAG;
import static cn.mijack.jacocoplugin.JacocoInstrumentation.getCoverageFile;


/**
 * @author Mi&Jack
 * @since 2018/6/27
 */
public class ActivityCallBack implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Log.d(TAG, "onActivityCreated() : " + activity.getClass().getName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
//                Log.d(TAG, "onActivityStarted() : " + activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
//                Log.d(TAG, "onActivityResumed() : " + activity.getClass().getName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
//                Log.d(TAG, "onActivityPaused() : " + activity.getClass().getName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
//                Log.d(TAG, "onActivityStopped() : " + activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                Log.d(TAG, "onActivitySaveInstanceState() : " + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//                Log.d(TAG, "onActivityDestroyed() : " + activity.getClass().getName());
        if (activity.getClass().getAnnotation(AppEntry.class) != null) {
            generateCoverageReport(activity);
        }
    }

    private void generateCoverageReport(Activity activity) {
        Log.d(TAG, "generateCoverageReport()");
        OutputStream out = null;
        try {
            out = new FileOutputStream(getCoverageFile(activity), false);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            Log.d(TAG, e.toString(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
