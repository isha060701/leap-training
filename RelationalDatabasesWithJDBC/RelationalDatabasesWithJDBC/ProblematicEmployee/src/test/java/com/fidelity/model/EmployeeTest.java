package com.fidelity.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class EmployeeTest {

	@Test
	void testSalaryScaleIsSetCorrectly() {
		String inputSalary = "100";
		String expected = inputSalary + ".00";
		Employee emp = new Employee(1, "KENT", "CLERK", 0, "17-DEC-1980", new BigDecimal(inputSalary), 
				new BigDecimal("200"), 10);
		
		assertEquals(new BigDecimal(expected), emp.getSalary(), "Expect salary to be forced to 2 decimal places");
	}

	@Test
	void testCommissionScaleIsSetCorrectly() {
		String inputCommission = "200";
		String expected = inputCommission + ".00";
		Employee emp = new Employee(1, "KENT", "CLERK", 0, "17-DEC-1980", new BigDecimal("100"), 
				new BigDecimal(inputCommission), 10);
		
		assertEquals(new BigDecimal(expected), emp.getComm(), "Expect comm to be forced to 2 decimal places");
	}
	
}
