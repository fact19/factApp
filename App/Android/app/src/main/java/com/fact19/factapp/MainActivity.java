package com.fact19.factapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    SeekBar handWashSeekBar;
    SeekBar gargleSeekBar;
    TextView handWashTxtVw;
    TextView gargleTxtVw;
    int handWashChangedValue = 0;
    int gargleChangedValue = 0;
    ImageView handWashIMG;
    ImageView gargleIMG;
    Animation animationIMG;
    NotificationManager notificationManager;
    String handWashMsg = "Its time to HandWash :)";
    String gargleMsg   = "Its time to Gargle :)";
    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //String message = savedInstanceState.getString("message");
            //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            handWashChangedValue = savedInstanceState.getInt("handWashValue", 0);
            gargleChangedValue = savedInstanceState.getInt("gargleValue", 0);
        }

        handWashSeekBar = (SeekBar)findViewById(R.id.handWashSeekBar);
        gargleSeekBar   = (SeekBar)findViewById(R.id.gargleSeekBar);
        handWashTxtVw   = (TextView)findViewById(R.id.handWashVal);
        gargleTxtVw     = (TextView)findViewById(R.id.gargleVal);
        handWashIMG     = (ImageView)findViewById(R.id.handWashImg);
        gargleIMG       = (ImageView)findViewById(R.id.gargleImg);

        handWashSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Timer handWashTimer;
            TimerTask handWashTask;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handWashChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(handWashTimer!=null) {
                    handWashTimer.cancel();
                    handWashTask.cancel();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //handWashTimer.cancel();
                //handWashTask.cancel();
                handWashTimer = new Timer();
                handWashTask = new TimerTask() {
                    public void run() {
                        playAlarm(R.raw.softbells);
                        notifyActivity(R.drawable.handwashsmall, handWashMsg, NotificationHandwash.class);
                    }
                };
                handWashTimer.schedule(handWashTask, handWashChangedValue * 60000, handWashChangedValue * 60000);
                handWashTxtVw.setText(String.valueOf(handWashSeekBar.getProgress()) + " min");
                toastMessage("Handwash interval set to " + handWashChangedValue + " min");
            }
        });

        gargleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Timer gargleTimer;
            TimerTask gargleTask;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gargleChangedValue = progress;
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
                        notifyActivity(R.drawable.gargle2small, gargleMsg, NotificationGargle.class);
                    }
                };
                gargleTimer.schedule(gargleTask, gargleChangedValue * 60000, gargleChangedValue * 60000);
                gargleTxtVw.setText(String.valueOf(gargleSeekBar.getProgress()) + " min");
                toastMessage("Gargle interval set to " + gargleChangedValue + " min");
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("handWashValue", handWashChangedValue);
        outState.putInt("gargleValue", gargleChangedValue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Toast.makeText(getApplicationContext(), "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
        handWashChangedValue = savedInstanceState.getInt("handWashValue", 0);
        gargleChangedValue = savedInstanceState.getInt("gargleValue", 0);
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

    private void notifyActivity(int resourceID, String msg, Class activity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
                                                    .setSmallIcon(resourceID)
                                                    .setContentTitle("FactApp")
                                                    .setContentText(msg)
                                                    .setAutoCancel(true)
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(this, activity);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}