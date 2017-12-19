package com.example.home_pc.coloredprogressbar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.example.home_pc.coloredprogressbar.MainActivity.PENDING_INTENT;
import static com.example.home_pc.coloredprogressbar.MainActivity.PENDING_RESULT;
import static com.example.home_pc.coloredprogressbar.MainActivity.STATUS_FINISH;


public class MyService extends Service {
    ExecutorService executorService;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    Notification notification;
    MyTask myTask;
    int progress = 10;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = intent.getParcelableExtra(PENDING_INTENT);
        myTask = new MyTask(pendingIntent, startId);
        executorService.execute(myTask);
        createNotification();
        return super.onStartCommand(intent, flags, startId);  //return START_REDELIVER_INTENT;
    }

    private void createNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(MyService.this);

        Intent intent = new Intent(MainActivity.context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.context, 3, intent, 0);

        mBuilder.setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("Task implementing");
    }


    class MyTask implements Runnable {
        PendingIntent pendingIntent;
        int startId;

        MyTask(PendingIntent pendingIntent, int startId) {
            this.pendingIntent = pendingIntent;
            this.startId = startId;
        }

        @Override
        public void run() {
            for (int i = 0; i != 19; i++) {
                progress = i;
                Intent intent = new Intent();
                intent.putExtra(PENDING_RESULT, i);
                Log.v("tag", "i ==" + i);

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    pendingIntent.send(MyService.this, STATUS_FINISH, intent);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }

                mBuilder.setProgress(18, i, false);
                notification = mBuilder.build();
                mNotifyManager.notify(1, notification);

                if (i == 18) {
                    mNotifyManager.cancel(1);
                    stopSelf(startId);
                }
            }
        }
    }
}
