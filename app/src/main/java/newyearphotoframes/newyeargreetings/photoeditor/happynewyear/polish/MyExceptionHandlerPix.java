package newyearphotoframes.newyeargreetings.photoeditor.happynewyear.polish;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui.MyApplication;
import newyearphotoframes.newyeargreetings.photoeditor.happynewyear.ui.NYPFMainActivity;


public class MyExceptionHandlerPix implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandlerPix(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = null;
        if (activity != null) {
            intent = new Intent(activity, NYPFMainActivity.class);
        } else if (MyApplication.getContext() != null) {
            intent = new Intent(MyApplication.getContext(), NYPFMainActivity.class);
        }
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager mgr = (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
        System.exit(2);
    }
}