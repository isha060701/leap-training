package com.fidelity.films;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class FilmArchive {
	private Set<Film> films;
	private int capacity;
	
	
	public FilmArchive(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity cannot be negative or zero");
		}
		this.films = new HashSet<>();
		this.capacity = capacity;
	}
	
	public int getInitialStorageCapacity()
	{
		return capacity;
	}
	
	public int getNumberOfFilms()
	{
		return films.size();
	}
	
	public int getRemainingStorageCapacity() {
		int filledSpaces=0;
		for(Film film:films)
		{
			filledSpaces+=film.getCapacity();
		}
		return capacity-filledSpaces;
	}
	
	public void addFilm(Film film) {
		if(film.getCapacity()>getRemainingStorageCapacity())
		{
			throw new IllegalArgumentException("This capacity of film cannot be added, it will exceed the maximum capacit");
		}
		if (getRemainingStorageCapacity() == 0) {
			throw new IllegalArgumentException("Film archive is at capacity");
		}
		
		films.add(film);
	}
	
	public BigDecimal calculateTotalRunningTime() {
		BigDecimal totalRunningTime = BigDecimal.ZERO.setScale(2);
		for (Film film : films) {
			BigDecimal runningTime = film.calculateDuration();
			totalRunningTime = totalRunningTime.add(runningTime);
		}
		return totalRunningTime.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public void removeFilm(int id) {
		Iterator<Film> iter = films.iterator();
		while (iter.hasNext()) {
			Film film = iter.next();
			if (film.getId()==id) {
				iter.remove();
				return;
			}
		}
		String msg = "'" + id+ "' is not in the archive";
		throw new NoSuchElementException(msg);
	}
	
	public Set<Film> getFilms() {
		return Collections.unmodifiableSet(films);
	}
}
