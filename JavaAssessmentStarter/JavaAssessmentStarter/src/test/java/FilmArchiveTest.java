import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.films.DigitalFilm;
import com.fidelity.films.Film;
import com.fidelity.films.FilmArchive;
import com.fidelity.films.PhysicalFilm;

class FilmArchiveTest {
	
private FilmArchive filmArchive;
	
	private Film digitalFilm1;
	private Film physicalFilm1;
	private Film digitalFilm2;

	@BeforeEach
	void setUp() throws Exception {
		digitalFilm1 = new DigitalFilm(1, "digitalFilm1", LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("10"));
		physicalFilm1 = new PhysicalFilm(2, "phsicalFilm1", LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") );
		digitalFilm2 = new DigitalFilm(3, "digitalFilm2", LocalDate.of(2022, 11, 2),LocalTime.now(), new BigDecimal("10"));
		filmArchive = new FilmArchive(3);
	}

	@AfterEach
	void tearDown() throws Exception {
		digitalFilm1=null;
		physicalFilm1=null;
		digitalFilm2=null;
		filmArchive=null;
	}

    

	
	
	@Test
	void testConstructionOfFilmArchiveObject() {

		assertNotNull(filmArchive);
	}
	
	@Test
	void testConstructionThrowsExceptionCapacityCannotBeZero() {
		assertThrows(IllegalArgumentException.class, () ->
			filmArchive = new FilmArchive(0)
		);
	}
	
	@Test
	void testConstructionThrowsExceptionCapacityCannotBeNegative() {
		assertThrows(IllegalArgumentException.class, () ->
			filmArchive = new FilmArchive(-1)
		);
	}
	
	@Test
	void testGetInitialStorageCapacity() {
		

		assertEquals(3, filmArchive.getInitialStorageCapacity());
	}

	@Test
	void testAddFilm_Success() {

		filmArchive.addFilm(digitalFilm1);
		
		assertEquals(1, filmArchive.getNumberOfFilms());
		
	}
	
	@Test
	void getCorrectCapacityOnAddingPhysicalFilmWithCapacity2() {

		filmArchive.addFilm(physicalFilm1);
		
		assertEquals(1, filmArchive.getRemainingStorageCapacity());
		
	}
	
	@Test
	void getCorrectCapacityOnAddingDigitalFilm() {

		filmArchive.addFilm(digitalFilm1);
		
		assertEquals(3, filmArchive.getRemainingStorageCapacity());
		
	}
	
	@Test
	void cannotAddFilmWhenCapacityIsFull() {
		filmArchive.addFilm(new PhysicalFilm(4, "phsicalFilm2", LocalDate.of(2022, 5, 3),LocalTime.now(), 3, new BigDecimal("10") ));
		
		
		assertThrows(IllegalArgumentException.class,()->{
			filmArchive.addFilm(digitalFilm1);
		});
	}
	
	@Test
	void cannotAddFilmWithGreaterCapacityThanRemainingSpace() {
		filmArchive.addFilm(physicalFilm1);
		filmArchive.addFilm(digitalFilm1);
		
		assertThrows(IllegalArgumentException.class,()->{
			filmArchive.addFilm(new PhysicalFilm(4, "phsicalFilm2", LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") ));
		});
	}
	
	@Test
	void testRemoveFilmSuccess() {
		filmArchive = new FilmArchive(3);
		filmArchive.addFilm(digitalFilm1);
		filmArchive.addFilm(digitalFilm2);

		filmArchive.removeFilm(1);

		assertEquals(1, filmArchive.getNumberOfFilms());
	}
	
	@Test
	void testRemoveFilmNotFound() {
		filmArchive = new FilmArchive(3);
		filmArchive.addFilm(digitalFilm1);
		filmArchive.addFilm(physicalFilm1);
		filmArchive.addFilm(digitalFilm2);
		
		assertThrows(NoSuchElementException.class,()->{
			filmArchive.removeFilm(0);
		});
		
	}
	

 
	@Test
	void testCalculateTotalRunningTime() {
		filmArchive = new FilmArchive(3);
		filmArchive.addFilm(digitalFilm1);
		filmArchive.addFilm(physicalFilm1);
		filmArchive.addFilm(digitalFilm2);

		BigDecimal totalRunningTime = filmArchive.calculateTotalRunningTime();

		assertEquals(new BigDecimal("40").setScale(2), totalRunningTime);
	}
	
	@Test
	void testGetRemainingSpaceEmptyArchive() {
		filmArchive = new FilmArchive(3);
		
		int remainingSpaces = filmArchive.getRemainingStorageCapacity();

		assertEquals(3, remainingSpaces);
	}
	
	@Test
	void testGetRemainingSpaceFullArchive() {
		filmArchive.addFilm(new PhysicalFilm(4, "phsicalFilm2", LocalDate.of(2022, 5, 3),LocalTime.now(), 3, new BigDecimal("10") ));


		int remainingSpaces = filmArchive.getRemainingStorageCapacity();

		assertEquals(0, remainingSpaces);
	}
	
	@Test
	void calculateCorrectFilmArchiveSize()
	{
		filmArchive.addFilm(digitalFilm1);
		filmArchive.addFilm(digitalFilm2);
		
		assertEquals(2,filmArchive.getNumberOfFilms());
	}
	
	@Test
	void ableToRetrieveAllFilms() {
		filmArchive.addFilm(digitalFilm1);
		filmArchive.addFilm(digitalFilm2);
		
		Set<Film> filmStore=new HashSet<>();
		filmStore.add(digitalFilm1);
		filmStore.add(digitalFilm2);
		
		assertEquals(filmStore,filmArchive.getFilms());
	}

}
