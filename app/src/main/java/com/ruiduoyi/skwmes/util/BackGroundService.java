package com.ruiduoyi.skwmes.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Chen on 2018/5/11.
 */

public class BackGroundService extends Service{
    public static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public static ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    public static void setScheduledThreadPoolExecutor(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        BackGroundService.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduledThreadPoolExecutor != null){
            scheduledThreadPoolExecutor.shutdown();
        }
    }
}
