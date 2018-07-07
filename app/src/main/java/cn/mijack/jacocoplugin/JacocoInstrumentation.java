package cn.mijack.jacocoplugin;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author Mi&Jack
 */
public class JacocoInstrumentation extends Instrumentation {
    public static String TAG = "JacocoInstrumentation:";
    public static String DEFAULT_COVERAGE_FILE_NAME = "coverage.ec";
    private Intent mIntent;

    public JacocoInstrumentation() {
    }

    @Override
    public void onCreate(Bundle arguments) {
        Log.d(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);
        File file = getCoverageFile(getTargetContext());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.d(TAG, "新建文件异常：" + e);
                e.printStackTrace();
            }
        }
        String entryActivity = arguments.getString("entryActivity");
        if (entryActivity == null) {
            throw new IllegalArgumentException("entryActivity is null");
        }
        mIntent = new Intent();
        mIntent.setClassName(getTargetContext(), entryActivity);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Looper.prepare();
        Application application = (Application) getTargetContext().getApplicationContext();
        application.registerActivityLifecycleCallbacks(new ActivityCallBack());
        Activity activity = startActivitySync(mIntent);
        System.out.println("activity:" + activity.getClass().getName());
    }

    public static File getCoverageFile(Context context) {
        File filesDir = context.getFilesDir();
        return new File(filesDir, DEFAULT_COVERAGE_FILE_NAME);
    }


}