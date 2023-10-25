package com.fidelity.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.jupiter.api.Test;

class EmployeeTest {

	@Test
	void testSalaryScaleIsSetCorrectly() {
		String inputSalary = "100";
		String expected = inputSalary + ".00";
		LocalDate hireDate = LocalDate.parse("1980-12-17");
		Employee emp = new Employee(1, "KENT", "CLERK", 0, hireDate, new BigDecimal(inputSalary), 
				new BigDecimal("200"), 10);
		
		assertEquals(new BigDecimal(expected), emp.getSalary(), "Expect salary to be forced to 2 decimal places");
	}

	@Test
	void testCommissionScaleIsSetCorrectly() {
		String inputCommission = "200";
		String expected = inputCommission + ".00";
		LocalDate hireDate = LocalDate.parse("1980-12-17");
		Employee emp = new Employee(1, "KENT", "CLERK", 0, hireDate, new BigDecimal("100"), 
				new BigDecimal(inputCommission), 10);
		
		assertEquals(new BigDecimal(expected), emp.getComm(), "Expect comm to be forced to 2 decimal places");
	}
	
}
