package com.ndd.springboot.model;

import lombok.Data;

@Data
public class InvoiceStatement {
	
	public String name;
	public Integer price;
	
	public InvoiceStatement(String name,Integer price) {
		
		this.name=name;
		this.price= price;
		
	}
	
}
