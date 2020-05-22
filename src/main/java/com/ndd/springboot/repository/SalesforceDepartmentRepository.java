package com.ndd.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ndd.springboot.model.SalesforceDepartment;

@Repository
public interface SalesforceDepartmentRepository extends JpaRepository<SalesforceDepartment,Integer>{

		public List<SalesforceDepartment> findById(int id);

}
