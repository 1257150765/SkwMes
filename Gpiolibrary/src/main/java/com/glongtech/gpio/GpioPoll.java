package com.glongtech.gpio;

import android.os.Gpio;
import android.util.Log;


public class GpioPoll {

	//-- 轮询获取Gpio 高低电平值--/ 
	public Runnable runnable = new Runnable() {
		String gpio_id="gpio7";
		
		public void run()
		{
			while (true)
			{
					
				if (1==Gpio.GetGpioValue(gpio_id))
				{
					 	Log.i(gpio_id,"==============The Value is high==========================!!!");
		            	//Gpio.SetGpioOutputHigh("gpio_bl");     //背光led打开，亮屏
		            	//Gpio.SetGpioOutputHigh("gpio_lcd_en"); // lcd使能打开
				}
				else if(0==Gpio.GetGpioValue(gpio_id))
				{
					 Log.i(gpio_id,"==============The Value is low==========================!!!");
		           //  Gpio.SetGpioOutputLow("gpio_bl");     // 背光led关闭，熄屏  
	            	// Gpio.SetGpioOutputLow("gpio_lcd_en"); // lcd使能关闭
				}
				else
				{
				 	Log.i(gpio_id,"==============The Value is get fail==========================!!!");
				}
				
				try {
						Thread.sleep(500);
					} catch (InterruptedException e) { }
			}
		}
	 };


}
