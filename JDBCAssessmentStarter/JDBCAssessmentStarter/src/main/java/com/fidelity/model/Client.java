package com.fidelity.model;

import java.util.Objects;

public class Client {
	// you may not change any of these fields
	private int clientId;
	private String clientName;
	private ClientRisk clientRisk;
	private String workPhone;
	
	// Eclipse-generated from here

	public Client(int clientId, String clientName, 
			      ClientRisk clientRisk, String workPhone) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
		this.clientRisk = clientRisk;
		this.workPhone = workPhone;
	}

	public int getClientId() {
		return clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public ClientRisk getClientRisk() {
		return clientRisk;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientId, clientName, clientRisk, workPhone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return clientId == other.clientId && Objects.equals(clientName, other.clientName)
				&& clientRisk == other.clientRisk && Objects.equals(workPhone, other.workPhone);
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", clientName=" + clientName + ", clientRisk=" + clientRisk
				+ ", workPhone=" + workPhone + "]";
	}
	
	
}
