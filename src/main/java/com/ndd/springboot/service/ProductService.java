package com.ndd.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndd.springboot.model.Product;
import com.ndd.springboot.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	@Autowired
	ProductRepository repository;

	
	  public List<Product> findAll(){
		  return repository.findAll();
	  }

	
}
