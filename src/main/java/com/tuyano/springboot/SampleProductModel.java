package com.tuyano.springboot;
import lombok.Data;

@Data
public class SampleProductModel {
	 
	private String Product_name;
	private int Price;

	public SampleProductModel(String name, int Price){
	
		this.Product_name = name;
	    this.Price = Price;
	}
}
