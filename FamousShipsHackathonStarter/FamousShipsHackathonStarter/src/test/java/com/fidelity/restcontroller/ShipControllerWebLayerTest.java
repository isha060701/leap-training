package com.fidelity.restcontroller;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fidelity.business.Ship;
import com.fidelity.integration.mapper.ShipMapper;
import com.fidelity.restservices.ShipDatabaseException;
import com.fidelity.restservices.ShipService;

@WebMvcTest(ShipController.class)
class ShipControllerWebLayerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ShipService mockService;
	
	@MockBean
	private ShipMapper mapper;
	
	private static final List<Ship> ships = Arrays.asList(
			new Ship(1,"RMS Titanic", null, "Captain Edward J. Smith", "Undisputedly the most famous ship in maritime history to encounter the most tragic event could be this luxury cruise from the British White Star liner with a connotation to showcase mankind�s technological brilliance.  On its maiden voyage on April 10, 1912 from Southampton to New York, it had struck an iceberg five days later and sank in the North Atlantic, failing to evacuate about 1500 passengers onboard. Rediscovered 1985, this historic ship with its equally historic tale has become the inspiration of multitude of documentaries and also the backdrop to one of the most successful Hollywood flick in 1999.", "Luxury liner"),
			new Ship(2,"Bismarck", null, "Otto Ernst Lindeman", "With a length of 823 feet and a top speed of 30 knots, this giant historic ship was undoubtedly the largest and fastest warship afloat in 1941 to have struck a terror at the heart of the British Navy. After inflicting enough damage to the British fleet of battle ships it was sunk at the bottom of the sea. However, after it was recovered in 1989, the founding indicated that this epitome of warship in the maritime history might have been scuttled rather than sunk by the British.", "German battleship"));
	

	@Test
    public void testQueryAllShips_Positive() throws Exception {

        when(mockService.queryAllShips()).thenReturn(ships);

        mockMvc.perform(MockMvcRequestBuilders.get("/ships")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
	
	@Test
	public void testQueryForAllShipsEmptyList() throws Exception {
		when(mockService.queryAllShips())
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/ships"))
			    //.andDo(print())
		       .andExpect(status().isNoContent())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForAllShipsNullList() throws Exception {
		when(mockService.queryAllShips())
			.thenReturn(null);
		
		mockMvc.perform(get("/ships"))
		       //.andDo(print())
		       .andExpect(status().isNoContent())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
	public void testQueryForAllShipsDaoException() throws Exception {
		when(mockService.queryAllShips())
			.thenThrow(new ShipDatabaseException());
		
		mockMvc.perform(get("/ships"))
		       //.andDo(print())
		       .andExpect(status().isInternalServerError())
		       .andExpect(content().string(is(emptyOrNullString())));
	}
	
	@Test
    public void testGetShipById_Positive() throws Exception {
        int id = 1;
        Ship ship = new Ship(1,"RMS Titanic", null, "Captain Edward J. Smith", "Undisputedly the most famous ship in maritime history to encounter the most tragic event could be this luxury cruise from the British White Star liner with a connotation to showcase mankind�s technological brilliance.  On its maiden voyage on April 10, 1912 from Southampton to New York, it had struck an iceberg five days later and sank in the North Atlantic, failing to evacuate about 1500 passengers onboard. Rediscovered 1985, this historic ship with its equally historic tale has become the inspiration of multitude of documentaries and also the backdrop to one of the most successful Hollywood flick in 1999.", "Luxury liner");

        when(mockService.queryShipById(id)).thenReturn(ship);

        mockMvc.perform(MockMvcRequestBuilders.get("/ships/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));
    }
	
	@Test
    public void testGetCaptainByShip_Positive() throws Exception {
        String shipName = "Ship1";
        String captain = "Captain1";

        when(mockService.queryCaptainByShipName(shipName)).thenReturn(captain);

        mockMvc.perform(MockMvcRequestBuilders.get("/ships/name/{name}", shipName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.captain").value(captain));
    }
	
	@Test
    public void testGetShipById_ThrowsException() throws Exception {
        int id = 1;

        when(mockService.queryShipById(id)).thenThrow(new RuntimeException("mock exception"));

        mockMvc.perform(get("/ships/id/" + id))
	       //.andDo(print())
	       .andExpect(status().isInternalServerError())
	       .andExpect(content().string(is(emptyOrNullString())));
    }
	
	@Test
    public void testGetShipById_InvalidId() throws Exception {
        int id = -1;
		
		mockMvc.perform(get("/ships/id/" + id))
		       //.andDo(print())
		       .andExpect(status().isBadRequest())
		       .andExpect(content().string(is(emptyOrNullString())));
    }
	
	@Test
    public void testGetShipById_NonExistantId() throws Exception {
        int id = 99;

        when(mockService.queryShipById(id)).thenReturn(null);

        mockMvc.perform(get("/ships/id/" + id))
	       //.andDo(print())
	       .andExpect(status().isNoContent())
	       .andExpect(content().string(is(emptyOrNullString())));
    }
}
