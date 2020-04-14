package com.tuyano.springboot.repository;

import com.tuyano.springboot.MyData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDataRepository  extends JpaRepository<MyData, Long> {
	
}