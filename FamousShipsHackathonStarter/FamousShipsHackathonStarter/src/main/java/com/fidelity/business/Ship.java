package com.fidelity.business;

import java.util.Objects;

public class Ship {
	private long id;
	private String name;
	private String nickname;
	private String captain;
	private String description;
	private String type;
	
	public Ship(){}
	
	
	
	public Ship(long id, String name, String nickname, String captain, String description, String type) {
		super();
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.captain = captain;
		this.description = description;
		this.type = type;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getNickname() {
		return nickname;
	}



	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public String getCaptain() {
		return captain;
	}



	public void setCaptain(String captain) {
		this.captain = captain;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	@Override
	public int hashCode() {
		return Objects.hash(captain, description, id, name, nickname, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ship other = (Ship) obj;
		return Objects.equals(captain, other.captain) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(name, other.name) && Objects.equals(nickname, other.nickname)
				&& Objects.equals(type, other.type);
	}
	@Override
	public String toString() {
		return "Ship [id=" + id + ", name=" + name + ", nickname=" + nickname + ", captain=" + captain
				+ ", description=" + description + ", type=" + type + "]";
	}
	
	
	
	
}
