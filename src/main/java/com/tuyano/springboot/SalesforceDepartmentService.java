package com.tuyano.springboot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesforceDepartmentService {

	@Autowired
	SalesforceDepartmentRepository salesforceRepository;
	
	  public List<SalesforceDepartment> sfFindAll(){
		  return salesforceRepository.findAll();
	  }
}
