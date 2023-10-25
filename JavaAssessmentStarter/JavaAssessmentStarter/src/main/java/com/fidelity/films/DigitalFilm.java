package com.fidelity.films;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class DigitalFilm extends Film {
	
	private BigDecimal duration;
	
	

	public DigitalFilm(int id, String title, LocalDate dateAdded, LocalTime timeAdded, BigDecimal duration) {
		super(id, title, dateAdded, timeAdded);
		if(duration.compareTo(BigDecimal.ZERO)<=0)
		{
			throw new IllegalArgumentException("Duration cannot be negative or zero");
		}
		
		this.duration = duration.setScale(2);
	}
	
	@Override
	public int getCapacity() {
		return 0;
	}

	@Override
	public BigDecimal calculateDuration() {
		// TODO Auto-generated method stub
		return duration;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(duration);
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DigitalFilm other = (DigitalFilm) obj;
		return Objects.equals(duration, other.duration);
	}



	@Override
	public String toString() {
		return "DigitalFilm [duration=" + duration + "]";
	}



	
	

}
