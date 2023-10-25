package com.fidelity.films;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Film {
	
	private int id;
	private String title;
	private LocalDate dateAdded;
	private LocalTime timeAdded;
	
	
	public Film(int id, String title, LocalDate dateAdded, LocalTime timeAdded) {
		
		if(id<0)
		{
			throw new IllegalArgumentException("id cannot be negative");
		}
		if(title==null)
		{
			throw new NullPointerException("Title of film cannot be null");
		}
		if(title=="")
		{
			throw new IllegalArgumentException("Title of film cannot be empty");
		}
		if(dateAdded==null)
		{
			throw new NullPointerException("Date Added cannot be null");
		}
		if(timeAdded==null)
		{
			throw new NullPointerException("Time Added of film cannot be null");
		}
		this.id = id;
		this.title = title;
		this.dateAdded = dateAdded;
		this.timeAdded = timeAdded;
	}
	
	public abstract BigDecimal calculateDuration();
	public abstract int getCapacity();

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public LocalTime getTimeAdded() {
		return timeAdded;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setTimeAdded(LocalTime timeAdded) {
		this.timeAdded = timeAdded;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateAdded, id, timeAdded, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		return Objects.equals(dateAdded, other.dateAdded) && id == other.id
				&& Objects.equals(timeAdded, other.timeAdded) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Film [id=" + id + ", title=" + title + ", dateAdded=" + dateAdded + ", timeAdded=" + timeAdded + "]";
	}

	

	
	
	
	
	
	

}
