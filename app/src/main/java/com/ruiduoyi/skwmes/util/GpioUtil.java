package com.ruiduoyi.skwmes.util;

import android.os.Gpio;
import android.util.Log;

import com.glongtech.gpio.GpioEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Chen on 2018/4/17.
 */

public class GpioUtil{
    private static final String TAG = GpioUtil.class.getSimpleName();
    private String gpioIndex;
    private long time = 2000;
    private OnGpioChangeListener onGpioChangeListener;
    private String gpioType;
    public static final String GPIO_TYPE_HIGHT = "1";
    public static final String GPIO_TYPE_LOW= "0";
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public GpioUtil(String gpioIndex, final OnGpioChangeListener onGpioChangeListener) {
        setGpioIndex(gpioIndex);
        //1 -> 高电平    0 -> 低电平
        if (Gpio.GetGpioValue(gpioIndex) == 1){
            LogWraper.d(TAG,""+ Gpio.SetGpioOutputLow(this.gpioIndex));
        }
        this.onGpioChangeListener = onGpioChangeListener;
        GpioEvent event_gpio = new GpioEvent() {
            @Override
            public void onGpioSignal(int index, boolean level) {
                if (onGpioChangeListener != null){
                    onGpioChangeListener.onGpioStatuChange(""+index,level?GPIO_TYPE_HIGHT:GPIO_TYPE_LOW);
                }

            }

        };
        event_gpio.MyObserverStart();
        //start();
    }

    public void setGpioIndex(String gpioIndex) {
        this.gpioIndex = "gpio"+gpioIndex;
    }
    /**
     * 开始
     */
    public void sendOne(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Gpio.SetGpioOutputHigh(gpioIndex);
                try {
                    //短时间内不能设置多次输出
                    Thread.sleep(time);
                    Gpio.SetGpioOutputLow(gpioIndex);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public interface OnGpioChangeListener{
        void onGpioStatuChange(String index, String statu);
    }
}
