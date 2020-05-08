package com.tuyano.springboot;

import lombok.Data;

@Data
public class Product {

	private String ProductName;
	private int ProductPrice;
	
	public Product(String name, int Price){
		
		this.ProductName = name;
	    this.ProductPrice = Price;
	}
}
