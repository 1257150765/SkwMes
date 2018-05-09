package com.glongtech.gpio;

import com.glongtech.board.Plat_jld803;
import com.glongtech.board.Plat_jld806;

public class GpioPath {
	
	/*-- 定义板子型号 --*/	
	private int jld_borad=0;
	
	
	/*-- 配置开发板型号--*/
	GpioPath(int borad)
	{
		switch (borad)
		{
			case 803: this.jld_borad = 803;
			case 806: this.jld_borad = 806;
			default : this.jld_borad = 806;
		}

	}


	
	public String[] GetGpiosPath()
	{
			
		switch (jld_borad)
		{
			case 803:	Plat_jld803 path_803=new Plat_jld803();
					   	return path_803.GetInGpioPath_803();
				
			case 806: 	Plat_jld806 path_806=new Plat_jld806();
		   				return path_806.GetInGpioPath_806();	
		}
		
		return null;
	}
	
	public int GetGpiosNum()
	{
		switch (jld_borad)
		{
			case 803:	Plat_jld803 path_803=new Plat_jld803();
					   	return path_803.GetGpiosNum_803();
				
			case 806: 	Plat_jld806 path_806=new Plat_jld806();
		   				return path_806.GetGpiosNum_806();
			
		}
		
		return -1;
	}
	



}
