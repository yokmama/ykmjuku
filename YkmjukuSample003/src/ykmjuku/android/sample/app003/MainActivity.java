package ykmjuku.android.sample.app003;
/***
 * Copyright (c) 2010-2012 Re:Kayo-System, Ltd. All rights reserved.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

public class MainActivity extends Activity implements SensorEventListener {
    private final float POWER_THRESHOLD = 30f;
    private SensorManager mSensorManager;
    private boolean mIsAccSensor;
    private SoundPool mSoundPool;
    private int mSwingSound;
    private float[] mLowpass = { 0f, 0f, 0f };
    private int mCounter = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSwingSound = mSoundPool.load(this, R.raw.tm2_swing000, 1);

        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {
            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mSensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                mIsAccSensor = true;
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // センサーマネージャのリスナ登録破棄
        if (mIsAccSensor) {
            mSensorManager.unregisterListener(this);
            mIsAccSensor = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
            return;
        }

        mCounter++;
        mLowpass[0] = event.values[0] * 0.1f + mLowpass[0] * 0.9f;
        mLowpass[1] = event.values[1] * 0.1f + mLowpass[1] * 0.9f;
        mLowpass[2] = event.values[2] * 0.1f + mLowpass[2] * 0.9f;
        double power = Math
                .abs((event.values[0] + event.values[1] + event.values[1])
                        - (mLowpass[0] + mLowpass[1] + mLowpass[2]));
        if (power > POWER_THRESHOLD) {
            if (mCounter > 10) {
                mCounter = 0;
                onSwing();
            }
        }
    }

    public void onSwing() {
        mSoundPool.play(mSwingSound, 100, 100, 1, 0, 1);
    }
}