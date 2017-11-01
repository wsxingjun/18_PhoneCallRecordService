package service.oztaking.com.a18_phonelistenerservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.support.annotation.Nullable;
import android.telephony.CellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


import java.util.List;

/**
 * Created by Administrator on 2017-11-01.
 */

public class PhoneListenerService extends Service {

    private MediaRecorder mRecorder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

//        public void listen (PhoneStateListener listener, int events)
//        listener - The PhoneStateListener object to register (or unregister)
//        events - The telephony state(s) of interest to the listener, as a bitwise-OR combination of PhoneStateListener LISTEN_ flags.
        PhoneStateListener phoneStateListener = new PhoneStateListener();
        telephonyManager.listen(new MyPhoneStateListener(), phoneStateListener.LISTEN_CALL_STATE);


    }
    //监听电话的状态
    public class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //判断电话的状态
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mRecorder != null){
                        Log.d("System","录音机空闲中..........");
                        mRecorder.stop();
                        mRecorder.release();
                        mRecorder.reset();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("System","开始录...........");
                    mRecorder.start();

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("System","准备一个收音机出来");
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile("/mnt/sdcard/luyin.3gp");
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                    } catch (Exception e) {
                        Log.e("System", "准备收音机失败");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //当服务销毁的时候调用
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
