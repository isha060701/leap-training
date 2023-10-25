package com.roifmr.presidents.restcontroller;

/**
 * BiographyDto is a data transfer object (DTO) that wraps a President's biography.
 * Without the DTO:
 * 	{"On April 30, ..."}
 * A web service method can return a BiographyDto instance to ensure that the response
 * body contains well-formed JSON:
 *     {"bio": "On April 30, 1789, ..."}
 */
public class BiographyDto {
	private String presBiography;
	
	public BiographyDto() {}
	
	public BiographyDto(String bio) {
		this.presBiography = bio;
	}
	
	public String getBio() {
		return presBiography;
	}
	
	public void setBio(String bio) {
		this.presBiography = bio;
	}
}
