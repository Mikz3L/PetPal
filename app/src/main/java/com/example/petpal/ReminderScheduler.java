package com.example.petpal;

import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReminderScheduler {

    public static void scheduleReminder(Context context, String activityName, String date, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date reminderDate = sdf.parse(date + " " + time);
            long delay = reminderDate.getTime() - System.currentTimeMillis();

            if (delay > 0) {
                Data data = new Data.Builder()
                        .putString("activityName", activityName)
                        .build();

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInputData(data)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

                WorkManager.getInstance(context).enqueue(workRequest);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
