package com.glongtech.adc;

import android.os.UEventObserver;
import android.util.Log;


//Adc定时可通过UEventObserver方式通知到上层
public class AdcEvent {

	// Adc Path for event
	private String[] path_adc;
	
	// Adc value for event
	private String val_adc[]=new String[4];;
	
	//--配置开发板型号--//
	AdcPath adc =new AdcPath(806);
	
		
	 //--监听 Adc-//
	 public void MyObserverStart ()
	 {
		 path_adc=adc.GetAdcPath();
		for(int i=0;i<adc.GetAdcNum();i++)
		 {		 
			 myObserver.startObserving(path_adc[i]);		 
			// Log.i("AdcPath",path_adc[i]);
			 //Log.i("Adc_Num=",String.valueOf(adc.GetAdcNum()));
			 
		 }	
	 }
	 
	 
	// 事件处理
	private   UEventObserver myObserver = new UEventObserver()
	{
           public void onUEvent(UEvent event)
           	{
        	   Log.i("Val_Event","===================== adc hava a new event ! ==================================");

        	   //-- 获取Adc上报的值
        	   for(int i=1;i<=adc.GetAdcNum();i++)
        	   {
        		  val_adc[i]=event.get("adc"+String.valueOf(i));  // event.get("adc1"),"adc2","adc3"...
        		
        		  // debug
        		  if( !(val_adc[i] == null || val_adc[i].equals("")) )
        		   {
             		  Log.i("Val_adc=",val_adc[i]);
        		   }
    		  
        	   }		   
        	
           	}  
	}; 
   	
	
}
