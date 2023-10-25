import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fidelity.films.DigitalFilm;
import com.fidelity.films.Film;
import com.fidelity.films.PhysicalFilm;

class PhysicalFilmTest {
	
	private Film physicalFilm1;

	@BeforeEach
	void setUp() throws Exception {
		physicalFilm1 = new PhysicalFilm(2, "phsicalFilm1", LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") );
	}

	@AfterEach
	void tearDown() throws Exception {
		physicalFilm1=null;
	}
	
	@Test
	void idCannotBeNegative()
	{
		assertThrows(IllegalArgumentException.class,()->{
			new PhysicalFilm(-2, "phsicalFilm1", LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") );
		});
	}
	
	@Test
	void titleCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new PhysicalFilm(2, null, LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") );
		});
	}
	
	@Test
	void titleCannotBeEmpty()
	{
		assertThrows(IllegalArgumentException.class,()->{
			new PhysicalFilm(2, "", LocalDate.of(2022, 5, 3),LocalTime.now(), 2, new BigDecimal("10") );
		});
	}
	
	@Test
	void dateAddedCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new PhysicalFilm(2, "phsicalFilm1", null,LocalTime.now(), 2, new BigDecimal("10") );
		});
	}
	
	@Test
	void timeAddedCannotBeNull()
	{
		assertThrows(NullPointerException.class,()->{
			new PhysicalFilm(2, "phsicalFilm1", LocalDate.of(2022, 5, 3),null, 2, new BigDecimal("10") );
		});
	}
	
	@Test
	void numberOfReelsCannotBeNegative() {
		assertThrows(IllegalArgumentException.class,()->{
			new PhysicalFilm(2, "phsicalFilm1", LocalDate.of(2022, 5, 3),LocalTime.now(), -2, new BigDecimal("10") );
		});
	}
	
	@Test
	void durationOfReelsCannotBeNegative() {
		assertThrows(IllegalArgumentException.class,()->{
			new PhysicalFilm(2, "phsicalFilm1", LocalDate.of(2022, 5, 3),LocalTime.now(), 2,new BigDecimal("-10") );
		});
	}

	@Test
	void calculateCorrectRunningTime() {
		assertEquals(BigDecimal.valueOf(20).setScale(2),physicalFilm1.calculateDuration());
	}
	
	@Test
	void calculateCorrectRequiredCapacityForTheFilm() {
		assertEquals(2,physicalFilm1.getCapacity());
	}
	
	

}
