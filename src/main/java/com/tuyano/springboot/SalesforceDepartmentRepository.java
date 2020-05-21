package com.tuyano.springboot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesforceDepartmentRepository extends JpaRepository<SalesforceDepartment,Integer>{

		public List<SalesforceDepartment> findById(int id);

}
