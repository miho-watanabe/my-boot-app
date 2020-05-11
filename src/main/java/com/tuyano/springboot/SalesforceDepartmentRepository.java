package com.tuyano.springboot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesforceDepartmentRepository extends JpaRepository<SalesforceDepartment,Integer>{
}
