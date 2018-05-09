package com.glongtech.activity;

import android.os.Bundle;
import android.os.Gpio;

import com.example.gaodi.R;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.glongtech.adc.AdcEvent;
import com.glongtech.adc.AdcPoll;
import com.glongtech.gpio.GpioEvent;
import com.glongtech.gpio.GpioPoll;

public class MainActivity extends Activity {

	TextView tvResult;
	CheckBox cbIsH;
	EditText editNum;
	Button btnOutput,btnInput,btnVal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvResult = (TextView) findViewById(R.id.tvResult);
		cbIsH = (CheckBox) findViewById(R.id.cbIsHigh);	
		editNum = (EditText) findViewById(R.id.edNumInput);
		btnInput= (Button) findViewById(R.id.btnInput);
		btnOutput= (Button) findViewById(R.id.btnOutput);
		btnVal= (Button) findViewById(R.id.btnVal);
		
		boolean GpioEvent_Flag	=true;  				// Gpio event 开关			
		boolean GpioPoll_Flag 	=!GpioEvent_Flag;	 	// Gpio 轮询开关
		
		boolean AdcEvent_Flag	=false;  				// Adc event 开关	
		boolean AdcPoll_Flag	=false;  				// Adc 轮询 开关
		
		
		// 监听输入型Gpio event 事件
		if(GpioEvent_Flag)
		{
			GpioEvent event_gpio = new GpioEvent() {
				@Override
				public void onGpioSignal(int index, boolean level) {

				}

			};
			event_gpio.MyObserverStart();
		}

		// 监听Adc event 事件 ,默认不支持，若客户需要，需要改固件支持
		if(AdcEvent_Flag)
		{
			AdcEvent event_adc = new AdcEvent();
			event_adc.MyObserverStart();
		}
		
		//  轮询获取gpio 高低电平值
		if(GpioPoll_Flag)
		{
			 GpioPoll poll_gpio =new GpioPoll();
			 Thread mythread = new Thread(poll_gpio.runnable);
			 mythread.start();
		}
		
		//  轮询获取Adc 高低电平值,默认不支持，若客户需要，需要改固件支持
		if(AdcPoll_Flag)
		{
			 AdcPoll poll_adc =new AdcPoll();
			 Thread mythread = new Thread(poll_adc.runnable);
			 mythread.start();
		}
		
		
		// 配置成输入 按钮  
		btnInput.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View arg0)
			{
		
				int ret=-1;
				
				ret=Gpio.SetGpioInput("gpio" + editNum.getText().toString());	
				if (ret<0)
				{
					tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输入模式失败！");
					
				} 
				else
				{
					tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输入模式成功！");
				}
				
				// TODO Auto-generated method stub
				
			}
		});

		//-- 配置成输出 按钮 
		btnOutput.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{

				int ret=-1;
				
				if (cbIsH.isChecked())
				{
					ret=Gpio.SetGpioOutputHigh("gpio" + editNum.getText().toString());	// "gpio1" "gpio2" ....
					if (ret<0)
					{
						tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输出模式失败！");  
						
					} 
					else
					{
						tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输出高电平模式成功！");
					}
				}
				else
				{
					ret=Gpio.SetGpioOutputLow("gpio" + editNum.getText().toString());	
					if (ret<0)
					{
						tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输出模式失败！");
						
					} 
					else
					{
						tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 配置成输出低电平模式成功！");
					}
				}


				
				// TODO Auto-generated method stub

			}
		});


		//-- 获取Gpio值 
		btnVal.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{

				int ret_val=-1;
				
				ret_val=Gpio.GetGpioValue("gpio" + editNum.getText().toString());	
				if (1==ret_val)
				{
					tvResult.setText("gpio"+String.valueOf(editNum.getText()) + "当前处于一个高电平状态");
				} 
				else if (0==ret_val )
				{
					tvResult.setText("gpio"+String.valueOf(editNum.getText()) + "当前处于一个低电平状态");
				}
				else
				{
					tvResult.setText("gpio"+String.valueOf(editNum.getText()) + " 获取IO高低状态失败！");
				}
				
				// TODO Auto-generated method stub

			}
		});
		



	}

	
}
