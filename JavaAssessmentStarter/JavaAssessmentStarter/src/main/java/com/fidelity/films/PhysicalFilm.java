package com.fidelity.films;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PhysicalFilm extends Film {
	
	private int numberOfReels;
	private BigDecimal durationOfReel;
	
	
	

	public PhysicalFilm(int id, String title, LocalDate dateAdded, LocalTime timeAdded, int numberOfReels,
			BigDecimal durationOfReel) {
		super(id, title, dateAdded, timeAdded);
		if(numberOfReels<=0)
		{
			throw new IllegalArgumentException("Number of reels can't be negative");
		}
		if(durationOfReel.compareTo(BigDecimal.ZERO)<0)
		{
			throw new IllegalArgumentException("Duration of reel can't be negative");
		}
		
		this.numberOfReels = numberOfReels;
		this.durationOfReel = durationOfReel.setScale(2);
	}
	
	@Override
	public int getCapacity()
	{
		return numberOfReels;
	}



	@Override
	public BigDecimal calculateDuration() {
		// TODO Auto-generated method stub
		BigDecimal numReels = new BigDecimal(numberOfReels);

		return durationOfReel.multiply(numReels).setScale(2);
	}



	public int getNumberOfReels() {
		return numberOfReels;
	}



	public BigDecimal getDurationOfReel() {
		return durationOfReel;
	}



	public void setNumberOfReels(int numberOfReels) {
		this.numberOfReels = numberOfReels;
	}



	public void setDurationOfReel(BigDecimal durationOfReel) {
		this.durationOfReel = durationOfReel;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(durationOfReel, numberOfReels);
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
		PhysicalFilm other = (PhysicalFilm) obj;
		return Objects.equals(durationOfReel, other.durationOfReel) && numberOfReels == other.numberOfReels;
	}



	@Override
	public String toString() {
		return "PhysicalFilm [numberOfReels=" + numberOfReels + ", durationOfReel=" + durationOfReel + "]";
	}
	
	
}
