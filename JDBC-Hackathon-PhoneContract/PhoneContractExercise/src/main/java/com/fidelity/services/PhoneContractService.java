package com.fidelity.services;

import java.math.BigDecimal;
import java.util.List;

import com.fidelity.model.PhoneContract;

public interface PhoneContractService {
	BigDecimal calculateTotalValue(List<PhoneContract> details);
}
