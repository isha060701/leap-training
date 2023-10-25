package com.fidelity.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.fidelity.model.Employee;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class EmployeeManagementServiceProxyTest {
	private EmployeeManagementService mockService;
//	private EmployeeManagementServiceProxy proxy;
//	private TransactionManager mockManager;
	
	@BeforeEach
	void setUp() throws Exception {
		mockService = mock(EmployeeManagementService.class);
//		mockManager = mock(TransactionManager.class);				
//		proxy = new EmployeeManagementServiceProxy(mockService, mockManager);
	}

	@AfterEach
	void tearDown() throws Exception {
	}



}
