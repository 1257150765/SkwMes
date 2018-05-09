package com.glongtech.adc;

import android.os.Gpio;
import android.util.Log;

public class AdcPoll 
{
	//-- 轮询获取Adc 电平值--/ 
	public Runnable runnable = new Runnable() {
		String adc_id="adc1";
		int adc_val;
		
		public void run()
		{
			while (true)
			{
				adc_val=Gpio.GetAdcValue();
				Log.i(adc_id,"The adc value is ["+String.valueOf(adc_val)+"]");
				
				try {
						Thread.sleep(500);
					} catch (InterruptedException e) { }
			}
		}
	 };

}
