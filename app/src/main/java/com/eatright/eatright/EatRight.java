package com.eatright.eatright;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class EatRight extends Application {

    public static String RESTAURANTNAME;
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                RESTAURANTNAME = region.getIdentifier();
                callApp();
            }

            @Override
            public void onExitedRegion(Region region) {
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.setBackgroundScanPeriod(6000, 500);
                beaconManager.startMonitoring(new Region("IndianCuisine", null, 0001, 0001));
                beaconManager.startMonitoring(new Region("OliveGarden", null, 0002, 0002));
                beaconManager.startMonitoring(new Region("Chuys", null, 0003, 0003));
            }
        });
    }

    private void callApp() {
        showNotification("Open EatRight!", "You are at " + RESTAURANTNAME);
    }


    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
