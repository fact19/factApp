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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    SeekBar handWash_SB;
    SeekBar gargle_SB;
    TextView handWashTxtVw;
    TextView gargleTxtVw;
    int handWashChangedValue = 0;
    int gargleChangedValue = 0;
    ImageView handWashIMG;
    ImageView gargleIMG;
    Animation animationIMG;
    String handWashMsg = "Its time to HandWash :)";
    String gargleMsg   = "Its time to Gargle :)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handWash_SB   = (SeekBar)findViewById(R.id.handWash_SB);
        gargle_SB     = (SeekBar)findViewById(R.id.gargle_SB);
        handWashTxtVw = (TextView)findViewById(R.id.handWashVal);
        gargleTxtVw   = (TextView)findViewById(R.id.gargleVal);
        handWashIMG   = (ImageView)findViewById(R.id.handWash_img);
        gargleIMG     = (ImageView)findViewById(R.id.gargle_img);

        handWash_SB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Timer handWashTimer;
            TimerTask handWashTask;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handWashChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handWashTimer = new Timer();
                handWashTask = new TimerTask() {
                    public void run() {
                        playAlarm(R.raw.softbells);

                        /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder notificationBuilder;

                        Bitmap remote_picture = null;
                        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();

                            remote_picture = BitmapFactory.decodeResource(getResources(), R.drawable.handwash);

                        notiStyle.bigPicture(remote_picture);

                        notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.drawable.handwash)
                                .setContentTitle("FactApp notification")
                                .setContentText("It's Time to HandWash !!")
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent)
                                .setStyle(notiStyle);

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notificationBuilder.build()); */
                    }
                };
                handWashTimer.schedule(handWashTask, handWashChangedValue * 60000, handWashChangedValue * 60000);
                handWashTxtVw.setText(String.valueOf(handWash_SB.getProgress()) + " min");
                toastMessage("Handwash interval set to " + handWashChangedValue + "min");
            }
        });

        gargle_SB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Timer gargleTimer;
            TimerTask gargleTask;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gargleChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                gargleTimer = new Timer();
                gargleTask = new TimerTask() {
                    public void run() {
                        playAlarm(R.raw.garglenotification);
                        //toastMessage(handWashMsg);
                    }
                };
                gargleTimer.schedule(gargleTask, gargleChangedValue * 60000, gargleChangedValue * 60000);
                gargleTxtVw.setText(String.valueOf(gargle_SB.getProgress()) + " min");
                toastMessage("Gargle interval set to " + gargleChangedValue + "min");
            }
        });
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
        toast.setGravity(Gravity.BOTTOM, 0, 0);
    }
}