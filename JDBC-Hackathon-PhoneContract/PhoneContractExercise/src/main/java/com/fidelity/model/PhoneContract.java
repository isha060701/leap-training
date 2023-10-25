package com.fidelity.model;

import java.math.BigDecimal;

public class PhoneContract {

	private int phoneContractId;
	private String phoneContractName;
	private String rateName;
	private int quantity;
	private BigDecimal totalValue;
	
	// Eclipse-generated from here

	public PhoneContract(int pcId, String pcName, String rateName, int quantity,
			BigDecimal totalValue) {
		super();
		this.phoneContractId = pcId;
		this.phoneContractName = pcName;
		this.rateName = rateName;
		this.quantity = quantity;
		this.totalValue = totalValue;
	}

}
