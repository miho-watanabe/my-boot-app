package com.ndd.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ndd.springboot.model.DemoInvoice;


public interface DemoInvoiceRepository extends JpaRepository<DemoInvoice,String>{

}
