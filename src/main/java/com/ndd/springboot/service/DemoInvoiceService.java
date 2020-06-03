package com.ndd.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndd.springboot.model.DemoInvoice;
import com.ndd.springboot.repository.DemoInvoiceRepository;

@Service
@Transactional
public class DemoInvoiceService {
	
	@Autowired
	DemoInvoiceRepository repository;
	
	  public List<DemoInvoice> findAll(){
		  return repository.findAll();
	  }
	
}
