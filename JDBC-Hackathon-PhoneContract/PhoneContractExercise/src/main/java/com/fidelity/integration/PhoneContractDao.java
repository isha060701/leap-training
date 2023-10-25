package com.fidelity.integration;

import java.util.List;

import com.fidelity.model.PhoneContract;

public interface PhoneContractDao {
	List<PhoneContract> getFullPhoneContracts();
	List<PhoneContract> getPhoneContractByID(int pcId);
}
