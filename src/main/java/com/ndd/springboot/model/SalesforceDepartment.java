package com.ndd.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "salesforcedepartment__c",schema="salesforce")
public class SalesforceDepartment {
	@Id
	@Column(name = "id")
    private int id;
	
	@Column(name = "sfid")
    private String sfid;
	
	@Column(name = "name")
    private String name;
}
