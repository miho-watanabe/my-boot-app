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
    private Integer invoiceAmount;
	
	@Column(name = "invoice_amount_tax")
    private Integer invoiceAmountTax;

	@Column(name = "invoice_statement_amout1")
    private Integer InvoiceStatementAmout1;

	@Column(name = "invoice_statement_amout2")
    private Integer InvoiceStatementAmout2;
	
	@Column(name = "invoice_date")
    private Date invoiceDate;
	
	@Column(name = "invoice_statement")
    private String invoiceStatement;
	
	@Column(name = "invoice_statement2")
    private String invoiceStatement2;
	
	@Column(name = "invoice_status")
    private String invoiceStatus;
		
	@Column(name = "invoice_name")
    private String invoiceName;
	
	@Column(name = "project_name")
    private String projectName;
	
	@Column(name = "invoice_ym")
    private String invoiceYm;
	
	@Column(name = "invoice_date_japan")
    private String invoiceDateJapan;
		
}
