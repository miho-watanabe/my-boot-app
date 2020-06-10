package com.ndd.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ndd.springboot.model.DemoInvoice;

public interface DemoInvoiceRepository extends JpaRepository<DemoInvoice,String>{

	public List<DemoInvoice> findAllByOrderByBudgetId();
	public List<DemoInvoice> findByBudgetId(String id);
	
}
