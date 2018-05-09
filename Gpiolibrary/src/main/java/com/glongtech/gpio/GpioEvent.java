package com.glongtech.gpio;

import android.os.Gpio;
import android.os.UEventObserver;
import android.util.Log;


// Gpio的高低电平发生变化时，可通过UEventObserver方式通知到上层
public abstract class GpioEvent {
	
	// 重复上报的过滤标志
	private static int h_flag=0;  // 高电平过滤标志
	private static int l_flag=0;  // 低电平过滤标志
	
	// Gpios Path for event
	private String[] path_gpios;
	
	// Gpios value for event
	private String val_gpios[]=new String[12];
	
	//--配置开发板型号--//
	GpioPath gpio =new GpioPath(806);
	
		
	 //--监听 Input Gpios--//
	 public void MyObserverStart ()
	 {
		 path_gpios=gpio.GetGpiosPath();
		 
		for(int i=0;i<gpio.GetGpiosNum();i++)
		 {		 
			 myObserver.startObserving(path_gpios[i]);		 
			//Log.i("GpioPath",path_gpios[i]);
			//Log.i("Gpios_Num=",String.valueOf(gpio.GetGpiosNum()));
			 
		 }	
		 
	 }
	 
	 
	// 事件处理
	private   UEventObserver myObserver = new UEventObserver()
	{
           public void onUEvent(UEvent event)
           	{
        	   Log.i("Val_Event","===================== gpio hava a new event ! ==================================");

        	   //-- 获取Gpio上报的值,即"HIGH" 或者 "LOW"--//
        	   for(int i=1;i<=gpio.GetGpiosNum();i++)
        	   {
        		  val_gpios[i]=event.get("gpio"+String.valueOf(i));  // event.get("gpio1"),"gpio2","gpio3"...
        		
        		  // debug
        		  if( !(val_gpios[i] == null || val_gpios[i].equals("")) )
        		   {
             		  Log.i("Val_Gpios",val_gpios[i]);
        		   }
    		  
        	   }

        	   //--gpio event 事件处理--// 
        	   for(int j=1;j<=gpio.GetGpiosNum();j++)
        	   {
        		   if( "HIGH".equals(val_gpios[j]))    // 高电平事件处理
        		   {
   	            		h_flag++;  // 高电平上锁
   	            		if(1==h_flag) 
   	            		{
   	            			l_flag=0; // 低电平解锁
   	            			
   	            			/*
   	            			 * 上锁解锁：有时候上层会多次执行处理事件，需要过滤掉
   	            			 * 用户事件：用户自定义处理动作
   	            			 * 
   	            			 */
   	            			Log.i("HIGH","Gpio"+String.valueOf(j)+"value is high");
							onGpioSignal(j,true);;
   	  	            		//--红外测试：gpio1 接入红外感应器，当有人来，屏自动亮起--//
   	   	            		// Gpio.SetGpioOutputHigh("gpio_bl");
   	   	            		// Gpio.SetGpioOutputHigh("gpio_lcd_en"); 
   	            			
   	            			
   	            			
   	            		}
 
        			   
        		   }
        		   else if("LOW".equals(val_gpios[j])) // 低电平事件处理
        		   {
        			   l_flag++;  // 低电平上锁
        			   if(1==l_flag)
  	            	 	{
        				   h_flag=0;  // 高电平解锁
        				   /*--相应事情--*/
        				   Log.i("LOW","Gpio"+String.valueOf(j)+"value is low");
                            onGpioSignal(j,false);
  	            	 	}
        			   	//--红外测试：gpio1 接入红外感应器，当有人离开，屏自动熄灭--//
        			   //  Gpio.SetGpioOutputLow("gpio_bl");  
  	            	 	//Gpio.SetGpioOutputLow("gpio_lcd_en");
        			   
        			   
        			   
        		   }
        		   else
        		   {
        			// Log.i("ERR","==============Get Value is fail==========================!!!"); 
        		   }    		  
        		   
        	   }

        	   Log.i("Val_Event","====================== gpio event is end! =================================");
           	}  
	}; 
   

	public abstract void onGpioSignal(int index,boolean level);



}
