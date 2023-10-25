package com.fidelity.business;

//REQUIREMENT: add relevant properties based on columns from the proper table defined in HR schema 
//exclude the following properties commission, jobId, managerId and deptId
//from this model class, the product owner indicated the client 
//isn't interested in that data for this current release

//NOTE:  properties names MUST NOT have underscores
//       do not change existing properties

public class Employee {
	
	
	private String hireDate;
	
	public String getDateHired() {
		return hireDate;
	}

	public void setDateHired(String hireDate) {
		this.hireDate = hireDate;
	}

	private Job job;
	


	public Job getCurrentJob() {
		return job;
	}

	public void setCurrentJob(Job currentJob) {
		this.job = currentJob;
	}


	

}
