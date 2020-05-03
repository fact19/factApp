package com.fact19.factapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FactAppActivity extends AppCompatActivity {

    SeekBar gargleSeekBar;

    TextView gargleTxtVw;
    TextView trackHandStatus;
    TextView accMeterValueTxtVw;

    ImageView gargleIMG;

    Switch handTrackerSwitch;

    Button faceTrackOnBtn;
    Button factTrackOffBtn;

    NotificationManager notificationManager;

    String gargleMsg   = "Its time to Gargle :)";

    int gargleChangedValue = 0;

    long lastUpdate;

    boolean defaultAccMeterStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("FactApp", 0);
        gargleChangedValue = sharedPref.getInt("gargleValue", 0);
        defaultAccMeterStatus = sharedPref.getBoolean("handTrackerStatus", false);

        if (savedInstanceState != null) {
            gargleChangedValue = savedInstanceState.getInt("gargleValue", 0);
        }

        gargleSeekBar      = findViewById(R.id.gargleSeekBar);
        gargleTxtVw        = findViewById(R.id.gargleVal);
        accMeterValueTxtVw = findViewById(R.id.accMeterValueTxtVw);
        gargleIMG          = findViewById(R.id.gargleImg);
        handTrackerSwitch  = findViewById(R.id.handTrackerSwitch);

        gargleSeekBar.setProgress(gargleChangedValue);
        handTrackerSwitch.setChecked(defaultAccMeterStatus);
        gargleTxtVw.setText(gargleChangedValue + " min");

        gargleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Timer gargleTimer;
            TimerTask gargleTask;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gargleChangedValue = progress;
                gargleTxtVw.setText(progress + " min");

                int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
                int thumbPos = seekBar.getPaddingLeft() + width * seekBar.getProgress() / seekBar.getMax();

                gargleTxtVw.measure(0, 0);
                int txtW = gargleTxtVw.getMeasuredWidth();
                int delta = txtW / 2;
                gargleTxtVw.setX(seekBar.getX() + thumbPos - delta);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(gargleTimer!=null) {
                    gargleTimer.cancel();
                    gargleTask.cancel();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //gargleTimer.cancel();
                //gargleTask.cancel();
                gargleTimer = new Timer();
                gargleTask = new TimerTask() {
                    public void run() {
                        playAlarm(R.raw.garglenotification);
                        showNotification(FactAppActivity.this, gargleMsg, NotificationGargle.class);
                    }
                };
                if(gargleChangedValue > 0) {
                    gargleTimer.schedule(gargleTask, gargleChangedValue * 60000, gargleChangedValue * 60000);
                    gargleTxtVw.setText(gargleSeekBar.getProgress() + " min");
                    toastMessage("Gargle interval set to " + gargleChangedValue + " min");
                }
            }
        }); //End of gargle seekbar

        handTrackerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    toastMessage("Hand tracker enabled");
                    defaultAccMeterStatus = true;
                    //starting service
                    startService(new Intent(getApplicationContext(), AccMeterService.class));
                } else {
                    toastMessage("Hand tracker disabled");
                    defaultAccMeterStatus = false;
                    //stopping service
                    stopService(new Intent(getApplicationContext(), AccMeterService.class));
                }
            }
        }); //End of hand tracker switchs

    } //End of activity onCreate()


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("gargleValue", gargleChangedValue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Toast.makeText(getApplicationContext(), "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        gargleChangedValue = savedInstanceState.getInt("gargleValue", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("FactApp", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("gargleValue", gargleChangedValue);
        editor.putBoolean("handTrackerStatus", defaultAccMeterStatus);
        editor.commit();
    }


    private void playAlarm(int sound) {
        MediaPlayer mp = MediaPlayer.create(this, sound);
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    private void toastMessage(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        toast.show();
        toast.setGravity(Gravity.BOTTOM, 0, 30);
    }

    public void showNotification(Context context, String msg, Class activity) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, activity);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                                    .setContentTitle("FactApp")
                                                                    .setContentText(msg);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
