package com.ndd.springboot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "demoinvoice")

public class DemoInvoice {
	
	@Id
	@Column(name = "budget_id")
    private String budgetId;
	
	@Column(name = "account_name")
    private String accountName;
	
	@Column(name = "invoice_amount")
    private int invoiceAmount;
	
	@Column(name = "invoice_date")
    private Date invoiceDate;
	
	@Column(name = "invoice_statement")
    private String invoiceStatement;
	
	@Column(name = "invoice_status")
    private String invoiceStatus;
		
	@Column(name = "invoice_name")
    private String invoiceName;
	
	@Column(name = "project_name")
    private String projectName;
	
	@Column(name = "invoice_ym")
    private String invoiceYm;
}
