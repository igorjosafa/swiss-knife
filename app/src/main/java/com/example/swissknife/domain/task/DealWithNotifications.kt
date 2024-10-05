package com.example.swissknife.domain.task

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Date

class DealWithNotifications {
    companion object {
        @SuppressLint("ScheduleExactAlarm")
        fun scheduleNotification(context: Context, taskId: Int, taskName: String, date: Date?) {

            val alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    ContextCompat.startActivity(context, intent, null)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) !=
                    PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(
                            Manifest.permission.POST_NOTIFICATIONS
                        ),
                        123
                    )
                }
            }
            val intent = Intent(context, TaskBroadcastReceiver::class.java).apply {
                putExtra("taskName", taskName)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                taskId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerAtMillis = date?.time


            triggerAtMillis?.let {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    it, pendingIntent)
            }

        }

        fun cancelNotification(context: Context, taskId: Int, taskName: String, date:
        Date
        ) {
                val intent = Intent(context, TaskBroadcastReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context, taskId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                pendingIntent.cancel()
        }
    }


}