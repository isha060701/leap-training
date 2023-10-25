package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit.jupiter.SpringExtension;

//Make sure DAO tests follow best practices from Relational DB course
//as well as the Spring Test Case recommendations from the current course
class EmployeeDaoTest {

	@Autowired
	private EmployeeDao dao;

	@Test
	void testSanityCheck() throws Exception {
		fail("Just a sanity check");
	}

}
