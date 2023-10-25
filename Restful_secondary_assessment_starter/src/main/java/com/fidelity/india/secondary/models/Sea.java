package com.fidelity.india.secondary.models;

import java.util.Objects;

public class Sea {

	private String port_name;
	
	public Sea() {}

	public Sea(String port_name) {
		super();
		this.port_name = port_name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(port_name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sea other = (Sea) obj;
		return Objects.equals(port_name, other.port_name);
	}

	public String getPort_name() {
		return port_name;
	}

	public void setPort_name(String port_name) {
		this.port_name = port_name;
	}
	
}
