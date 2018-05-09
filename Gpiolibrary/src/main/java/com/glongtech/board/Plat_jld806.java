package com.glongtech.board;


public class Plat_jld806
{
	// 青岛深度传媒：将TP接口扩展成GPIO(Gpio6~Gpio9)
	private boolean Gpios_Develop_Flag=false; 
	
	// jld806 all Adc Path for event 
	private String  JLD806_PathAdc1       = "DEVPATH=/devices/platform/rk3188_adc_driver.1/rk_adc/adc1";
	
	// jld806 used adc  for event 
	private String[] JLD806_PathAdc = new String[10];
	private int Adc_Num=0;
	
	// jld806 all Gpios Path for event 
	private String JLD806_PathGpio1="DEVPATH=/devices/platform/rk3188_gpios_driver.1/rk_gpios/gpio1";
	private String JLD806_PathGpio2="DEVPATH=/devices/platform/rk3188_gpios_driver.2/rk_gpios/gpio2";
	private String JLD806_PathGpio3="DEVPATH=/devices/platform/rk3188_gpios_driver.3/rk_gpios/gpio3";
	private String JLD806_PathGpio4="DEVPATH=/devices/platform/rk3188_gpios_driver.4/rk_gpios/gpio4";
	private String JLD806_PathGpio5="DEVPATH=/devices/platform/rk3188_gpios_driver.5/rk_gpios/gpio5";
	
	// jld806 input io  for event 
	private String[] JLD806_PathInGpio = new String[10];
	private int Gpios_Num=0;

	
	public void SetPathInGpio_806()
	{
		
		JLD806_PathInGpio[0]=JLD806_PathGpio1;
		JLD806_PathInGpio[1]=JLD806_PathGpio2;
		JLD806_PathInGpio[2]=JLD806_PathGpio3;
		JLD806_PathInGpio[3]=JLD806_PathGpio4;
		JLD806_PathInGpio[4]=JLD806_PathGpio5;
		Gpios_Num=5;


		if(Gpios_Develop_Flag)
		{
			JLD806_PathInGpio[5]="DEVPATH=/devices/platform/rk3188_gpios_driver.6/rk_gpios/gpio6";
			JLD806_PathInGpio[6]="DEVPATH=/devices/platform/rk3188_gpios_driver.7/rk_gpios/gpio7";
			JLD806_PathInGpio[7]="DEVPATH=/devices/platform/rk3188_gpios_driver.8/rk_gpios/gpio8";
			JLD806_PathInGpio[8]="DEVPATH=/devices/platform/rk3188_gpios_driver.9/rk_gpios/gpio9";
			Gpios_Num+=4;
				
		}
		

			
		}
	
	
	public void SetPathAdc_806()
	{
		
		JLD806_PathAdc[0]=JLD806_PathAdc1;

		Adc_Num=1;


	}
		
	public String[] GetInGpioPath_806()
	{
			SetPathInGpio_806();
			return JLD806_PathInGpio;
			
	}
	
	public String[] GetAdcPath_806()
	{
		SetPathAdc_806();
		return JLD806_PathAdc;
		
	}
	
	public int GetGpiosNum_806()
	{
		SetPathInGpio_806();
		return Gpios_Num;
				
	}
		
		
	public int GetAdcNum_806()
	{
		SetPathAdc_806();
		return Adc_Num;
				
	}

	
}
