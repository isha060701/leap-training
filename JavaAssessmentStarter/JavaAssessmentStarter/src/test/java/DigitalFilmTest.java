import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.films.DigitalFilm;
import com.fidelity.films.Film;

class DigitalFilmTest {
	
	private Film digitalFilm1;
	@BeforeEach
	void setUp() throws Exception {
		digitalFilm1 = new DigitalFilm(1, "digitalFilm1", LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("10"));
	}

	@AfterEach
	void tearDown() throws Exception {
		digitalFilm1=null;
	}

	@Test
	void idCannotBeNegative()
	{
		assertThrows(IllegalArgumentException.class,()->{
			new DigitalFilm(-1, "digitalFilm1", LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("10"));
		});
	}
	
	@Test
	void titleCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new DigitalFilm(1, null, LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("10"));
		});
	}
	
	@Test
	void titleCannotBeEmpty()
	{
		assertThrows(IllegalArgumentException.class,()->{
			new DigitalFilm(1, "", LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("10"));
		});
	}
	
	@Test
	void dateAddedCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new DigitalFilm(1, "isha",null,LocalTime.now(), new BigDecimal("10"));
		});
	}
	
	@Test
	void timeAddedCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new DigitalFilm(1, "isha",LocalDate.now(),null, new BigDecimal("10"));
		});
	}
	
	@Test
	void durationCannotBeNegative()
	{
		assertThrows(IllegalArgumentException.class,()->{
			new DigitalFilm(1, "digitalFilm1", LocalDate.of(2022, 9, 21),LocalTime.now(), new BigDecimal("-10"));
		});
	}
	
	@Test
	void getCapacityRequirementAsZero() {
		assertEquals(0,digitalFilm1.getCapacity());
	}
	
	@Test
	void calculateCorrectRunningTime() {
		assertEquals(BigDecimal.valueOf(10).setScale(2),digitalFilm1.calculateDuration());
	}

}
