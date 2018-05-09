package com.glongtech.adc;

import com.glongtech.board.Plat_jld803;
import com.glongtech.board.Plat_jld806;

public class AdcPath 
{
	/*-- 定义板子型号 --*/	
	private int jld_borad=0;
	
	
	/*-- 配置开发板型号--*/
	AdcPath(int borad)
	{
		switch (borad)
		{
			case 803: this.jld_borad = 803;
			case 806: this.jld_borad = 806;
			default : this.jld_borad = 806;
		}

	}


	
	public String[] GetAdcPath()
	{
			
		switch (jld_borad)
		{
			case 803:	Plat_jld803 path_803=new Plat_jld803();
					   	return path_803.GetAdcPath_803();
				
			case 806: 	Plat_jld806 path_806=new Plat_jld806();
		   				return path_806.GetAdcPath_806();	
		}
		
		return null;
	}
	
	public int GetAdcNum()
	{
		switch (jld_borad)
		{
			case 803:	Plat_jld803 path_803=new Plat_jld803();
					   	return path_803.GetAdcNum_803();
				
			case 806: 	Plat_jld806 path_806=new Plat_jld806();
		   				return path_806.GetAdcNum_806();
			
		}
		
		return -1;
	}

}
