package com.roifmr.presidents.models;

public class President {
	private int id;
	private String firstName;
	private String lastName;
	private int firstYear;
	private int lastYear;
	private String image;
	private String biography;
	
	public President() {
	}

	public President(int id, String firstName, String lastName, int firstYear, int lastYear, String image,
			String biography) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.firstYear = firstYear;
		this.lastYear = lastYear;
		this.image = image;
		this.biography = biography;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getFirstYear() {
		return firstYear;
	}

	public void setFirstYear(int firstYear) {
		this.firstYear = firstYear;
	}

	public int getLastYear() {
		return lastYear;
	}

	public void setLastYear(int lastYear) {
		this.lastYear = lastYear;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((biography == null) ? 0 : biography.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + firstYear;
		result = prime * result + id;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + lastYear;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		President other = (President) obj;
		if (biography == null) {
			if (other.biography != null)
				return false;
		} else if (!biography.equals(other.biography))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (firstYear != other.firstYear)
			return false;
		if (id != other.id)
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (lastYear != other.lastYear)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "President [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", firstYear="
				+ firstYear + ", lastYear=" + lastYear + ", image=" + image + ", biography=" + biography + "]";
	}
	
	
	
}
