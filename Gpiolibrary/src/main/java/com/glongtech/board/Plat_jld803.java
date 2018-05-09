package com.glongtech.board;

public class Plat_jld803 
{
	// jld803 all Adc Path for event 
	private String  JLD803_PathAdc1       = null;
	
	// jld806 used adc  for event 
	private String[] JLD803_PathAdc = new String[10];
	private int Adc_Num=0;
	
	
	
	// jld803 all Gpios Path for event
	private String  JLD803_PathGpio1      = "DEVPATH=/devices/rk_gpio1.17/rk_gpios/gpio1";
	private String  JLD803_PathGpio2      = "DEVPATH=/devices/rk_gpio2.18/rk_gpios/gpio2";
	private String  JLD803_PathGpio3      = "DEVPATH=/devices/rk_gpio3.19/rk_gpios/gpio3";
	private String  JLD803_PathGpio4      = "DEVPATH=/devices/rk_gpio4.20/rk_gpios/gpio4";
	
	// jld803 input io  for event 
	private String[] JLD803_PathInGpio = new String[4];
	private int Gpios_Num=0;


	public void SetPathInGpio_803()
	{
		JLD803_PathInGpio[0]=JLD803_PathGpio1;
		JLD803_PathInGpio[1]=JLD803_PathGpio2;
		JLD803_PathInGpio[2]=JLD803_PathGpio3;
		JLD803_PathInGpio[3]=JLD803_PathGpio4;
		Gpios_Num=4;
		
	}
	
	public void SetPathAdc_803()
	{
		JLD803_PathAdc[0]=JLD803_PathAdc1;
		Adc_Num=0;
		
	}
	
	public String[] GetInGpioPath_803()
	{
		SetPathInGpio_803();
		return JLD803_PathInGpio;
		
	}
	
	public String[] GetAdcPath_803()
	{
		SetPathAdc_803();
		return JLD803_PathAdc;
		
	}
	
	public int GetGpiosNum_803()
	{
		SetPathInGpio_803();
		return Gpios_Num;
	}
	
	public int GetAdcNum_803()
	{
		SetPathAdc_803();
		return Adc_Num;
	}
}
