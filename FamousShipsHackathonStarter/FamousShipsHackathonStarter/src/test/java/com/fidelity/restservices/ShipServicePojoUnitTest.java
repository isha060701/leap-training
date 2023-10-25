package com.fidelity.restservices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fidelity.business.Ship;
import com.fidelity.integration.ShipDaoImpl;

public class ShipServicePojoUnitTest {
	
	@Mock
	private ShipDaoImpl mockDao;
	
	@InjectMocks
	private ShipServiceImpl service;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);		
	}
	
	@Test
	void testQueryAllShips() {
		List<Ship> expectedShips = List.of(
				new Ship(1,"RMS Titanic", null, "Captain Edward J. Smith", "Undisputedly the most famous ship in maritime history to encounter the most tragic event could be this luxury cruise from the British White Star liner with a connotation to showcase mankind�s technological brilliance.  On its maiden voyage on April 10, 1912 from Southampton to New York, it had struck an iceberg five days later and sank in the North Atlantic, failing to evacuate about 1500 passengers onboard. Rediscovered 1985, this historic ship with its equally historic tale has become the inspiration of multitude of documentaries and also the backdrop to one of the most successful Hollywood flick in 1999.", "Luxury liner"),
				new Ship(2,"Bismarck", null, "Otto Ernst Lindeman", "With a length of 823 feet and a top speed of 30 knots, this giant historic ship was undoubtedly the largest and fastest warship afloat in 1941 to have struck a terror at the heart of the British Navy. After inflicting enough damage to the British fleet of battle ships it was sunk at the bottom of the sea. However, after it was recovered in 1989, the founding indicated that this epitome of warship in the maritime history might have been scuttled rather than sunk by the British.", "German battleship"));
		when(mockDao.queryAllShips())
				.thenReturn(expectedShips);
		List<Ship> actualShips = service.queryAllShips();
		assertEquals(expectedShips, actualShips);
	}
	
	@Test
	void testFindAllWidgets_DaoReturnsEmptyList() {
		when(mockDao.queryAllShips())
			.thenReturn(Collections.emptyList());
		assertTrue(service.queryAllShips().isEmpty());
	}
	
	@Test
	void testFindAllWidgets_DaoThrowsException() {
		when(mockDao.queryAllShips())
        	.thenThrow(new RuntimeException("mock DAO exception"));
	
		assertThrows(ShipDatabaseException.class, () -> {
			service.queryAllShips();
		});
	}
	
	@Test
	void testFindWidgetById() {
		int id = 1;
		Ship firstWidget = new Ship(1,"RMS Titanic", null, "Captain Edward J. Smith", "Undisputedly the most famous ship in maritime history to encounter the most tragic event could be this luxury cruise from the British White Star liner with a connotation to showcase mankind�s technological brilliance.  On its maiden voyage on April 10, 1912 from Southampton to New York, it had struck an iceberg five days later and sank in the North Atlantic, failing to evacuate about 1500 passengers onboard. Rediscovered 1985, this historic ship with its equally historic tale has become the inspiration of multitude of documentaries and also the backdrop to one of the most successful Hollywood flick in 1999.", "Luxury liner");
		
		when(mockDao.queryShipById(id))
			.thenReturn(firstWidget);
		
		Ship widget = service.queryShipById(id);
		
		// verify the Widget 
		assertEquals(firstWidget, widget);
	}

	@Test
	void testFindShipById_DaoReturnNull() {
		int id = 1;
		when(mockDao.queryShipById(id))
			.thenReturn(null);
		assertNull(service.queryShipById(id));
	}

	@Test
	void testFindShipById_InvalidIdThrowsException() {
		int id = -1;
		when(mockDao.queryShipById(id))
			.thenThrow(new RuntimeException("mock DAO exception"));
		
		assertThrows(IllegalArgumentException.class, ()-> {
			service.queryShipById(id);
		});
	}
	
	@Test
	void testFindCaptainByName() {
		String name = "RMS Titanic";
		String expectedCaptain = "Captain Edward J. Smith";
		
		when(mockDao.queryCaptainByShipName(name))
			.thenReturn(expectedCaptain);
		
		String actualCaptain = service.queryCaptainByShipName(name);

		assertEquals(expectedCaptain, actualCaptain);
	}
	
	@Test
	void testFindcaptainByName_DaoReturnNull() {
		String name = "RMS Titanic";
		when(mockDao.queryCaptainByShipName(name))
			.thenReturn(null);
		assertNull(service.queryCaptainByShipName(name));
	}
	
	@Test
	void testFindCaptainByName_InvalidIdThrowsException() {
		String name = null;
		when(mockDao.queryCaptainByShipName(name))
			.thenThrow(new RuntimeException("mock DAO exception"));
		
		assertThrows(IllegalArgumentException.class, ()-> {
			service.queryCaptainByShipName(name);
		});
	}
}
