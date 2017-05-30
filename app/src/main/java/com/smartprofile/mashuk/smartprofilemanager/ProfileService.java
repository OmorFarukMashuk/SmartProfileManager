package com.smartprofile.mashuk.smartprofilemanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class ProfileService extends Service implements SensorEventListener {


    private AudioManager myAudioManager;
    SensorManager sm;
    Sensor proxSensor;
    Sensor accelerometerSensor;
    Sensor lightSensor;

    private float proxVal;
    private float luxval;
    private float gVal;

    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service is created",Toast.LENGTH_LONG).show();

        proxVal = 5.0f;
        luxval = 0.0f;
        gVal = SensorManager.GRAVITY_EARTH;

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service is started",Toast.LENGTH_LONG).show();



        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometerSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proxSensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        lightSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);


        sm.registerListener(this,proxSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        sm.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service is stopped",Toast.LENGTH_LONG).show();
        super.onDestroy();
        //startService(new Intent(this, ProfileService.class));

    }




    public boolean isShacking(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 12) {
            Toast.makeText(this,"device has shaken",Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return false;
        }

    }


    public void SmartProfile(SensorEvent sensorEvent) {
        //home
        if (proxVal != 0 && gVal > -9 && luxval!=0) {

            myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
            //Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            //vib.cancel();

            myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);


        }
        //silent
        else if(gVal < -8 && luxval == 0 && proxVal == 0) {
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

        }

        //pocket
        else if (luxval == 0 && proxVal == 0){

            if(isShacking(sensorEvent)) {
                myAudioManager.setStreamVolume(AudioManager.STREAM_RING, myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING) / 2, 0);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

        }

        else {
            myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

    }

    public void onSensorChanged(SensorEvent sensorEvent) {

        /*
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proxVal = sensorEvent.values[0];

            SmartProfile(sensorEvent);

        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gVal = sensorEvent.values[2];

            SmartProfile(sensorEvent);

        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            luxval = sensorEvent.values[0];

            SmartProfile(sensorEvent);
        }*/

        Sensor sensor = sensorEvent.sensor;

        if(sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proxVal = sensorEvent.values[0];

            /*if(proxVal != 0){
                if(gVal > -9) {
                    myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
                    myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                }
            }*/
        }

        else if(sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gVal = sensorEvent.values[2];

            if(gVal < -9 && luxval == 0 && proxVal == 0) {
                //if(luxval == 0){
                //if(proxVal == 0)
                //{
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

                //Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                //v.vibrate(500);



                //}
                    /*else
                    {
                        //myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);

                        myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
                        //Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        //vib.cancel();
                    }*/

                //}

            }

            else if (luxval == 0 && proxVal == 0 && isShacking(sensorEvent)){
                //if(proxVal == 0) {
                //if(isShacking(sensorEvent,sensor)) {

                myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING)/2,0);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                //}
                //else {
                //myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);

                //myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
                //myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //Vibrator vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                //vib.cancel();
                //}
                //}

            }
            else if (proxVal != 0 && luxval!=0 && gVal > -9) {

                myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            }


            /*else {
                myAudioManager.setStreamVolume (AudioManager.STREAM_RING,myAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }*/








        }

        else if(sensor.getType() == Sensor.TYPE_LIGHT) {
            luxval = sensorEvent.values[0];
            /*if(luxval == 0) {
                if(gVal < -9) {
                    if(proxVal == 0) {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    else {
                        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    }

                }


            }*/

        }


    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
