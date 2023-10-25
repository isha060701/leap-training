package com.fidelity.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fidelity.model.Car;
import com.fidelity.model.CarBodyType;
import com.fidelity.model.CarEngine;
import com.fidelity.model.CarEngineType;

class CarDaoOracleImplTest {
	
	private JdbcTemplate jdbcTemplate;
	private DbTestUtils dbTestUtils;
	private CarDao dao;
	private SimpleDataSource dataSource;
	private Connection connection;
	private TransactionManager transactionManager;

	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleDataSource();
		connection = dataSource.getConnection();
		dao=new CarDaoOracleImpl(dataSource);
		
						
		// Start the transaction
		transactionManager=new TransactionManager(dataSource);
		transactionManager.startTransaction();
		
		dbTestUtils = new DbTestUtils(connection);		
		jdbcTemplate = dbTestUtils.initJdbcTemplate();
	}

	@AfterEach
	void tearDown() throws Exception {
		transactionManager.rollbackTransaction();
		
		// Shutdown the DataSource
		dataSource.shutdown();	
	}

	@Test
	void NotNull() {
		assertNotNull(dao.getCars());
	}
	
	@Test
	void getCarsSize()
	{
		List<Car> cars = dao.getCars();
		assertEquals(8, cars.size());
	}
	
	@Test
	void getAllCarsFirstElement() {
		CarEngine carEngine = new CarEngine(101, CarEngineType.of(3), 266, 0);
		Car car = new Car(101, "Ford", "Mustang Mach-E", "Convertible", carEngine, LocalDate.of(2022, 1, 04));
		assertEquals(car, dao.getCars().get(0));
	}

	@Test
	void getAllCarsLastElement() {
		CarEngine carEngine = new CarEngine(108, CarEngineType.of(3), 670, 0);
		Car car = new Car(108, "Tesla", "Model S", "Sedan", carEngine, LocalDate.of(2022, 1, 22));
		assertEquals(car, dao.getCars().get(7));
	}
	
	@Test
    public void cannotInsertNullCar() {
        Car car = null;
        
        assertThrows(NullPointerException.class, () -> {
        	dao.insertCar(car);
        });
    }
	
	@Test
	void testInsertCar_Duplicate_ThrowsException() {
		int id = 101;
		Car dupeCar = new Car(id, "VW", "Bug", CarBodyType.CONVERTIBLE.getName(),
			       new CarEngine(id, CarEngineType.GASOLINE, 2.0f, 135),
			       LocalDate.of(2022, 1, 4));
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
				
		assertThrows(DatabaseException.class, () -> {
			dao.insertCar(dupeCar);
		});

		assertEquals(eCarOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", 
				"id = " + id + " and make = 'Ford'"));
	} 
	
	@Test
	void insertDuplicateCar() {
		CarEngine carEngine = new CarEngine(109, CarEngineType.of(2), 0, 680);
		Car car = new Car(109, "Tesla", "Model Y", "Sedan", carEngine, LocalDate.of(2022, 1, 15));
		dao.insertCar(car);
		assertThrows(DatabaseException.class, ()-> {
			dao.insertCar(car);
		});
	}
	
//	@Test
//	void insertCar() {
//		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
//		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id=109"));
//		CarEngine carEngine = new CarEngine(109, CarEngineType.of(2), 680, 0);
//		Car car = new Car(109, "Tesla", "Model Y", "Sedan", carEngine, LocalDate.of(2022, 1, 15));
//		dao.insertCar(car);
//		assertEquals(oldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
//	}
//
//	@Test
//	void insertEngineCar() {
//		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
//		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id=109"));
//		CarEngine carEngine = new CarEngine(109, CarEngineType.of(2), 680, 0);
//		Car car = new Car(109, "Tesla", "Model Y", "Sedan", carEngine, LocalDate.of(2022, 1, 15));
//		dao.insertCar(car);
//		assertEquals(oldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
//	}
	
	@Test
	void testInsertCar_WithEngine_Success() {
		int id = 109;
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
		
		Car newcar = new Car(id, "Aeromobil", "Flying Car", CarBodyType.OTHER.getName(),
	            new CarEngine(id, CarEngineType.GASOLINE, 3.5f, 300),
	            LocalDate.of(2023, 1, 1));
		
		dao.insertCar(newcar);
		
		assertEquals(eCarOldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", 
				"id = " + id + " and make = 'Aeromobil'"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", 
				"id = " + id + " and horsepower = 300"));
	}

	@Test
	void testInsertCar_WithoutEngine_Success() {
		int id = 109;
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
		
		Car newcar = new Car(id, "Berg", "X-ITE Pedal Car", CarBodyType.OTHER.getName(),
	            null, LocalDate.of(2023, 1, 1));
		
		dao.insertCar(newcar);
		
		assertEquals(eCarOldSize + 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", 
				"id = " + id + " and make = 'Berg' and model = 'X-ITE Pedal Car'"));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
	}
	
	@Test
	void testDeleteCar(){
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int id=101;
		dao.deleteCar(id);
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		assertEquals(oldSize-1,newSize);
		int rowsWithDeletedId=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car","id="+id);
		assertEquals(0,rowsWithDeletedId);
		
	}
	
	@Test
	void testDeleteCarEngine(){
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		int id=101;
		dao.deleteCar(id);
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(oldSize-1,newSize);
		int rowsWithDeletedId=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine","id="+id);
		assertEquals(0,rowsWithDeletedId);
		
	}
	
	@Test
	void testUpdateCarBeforeOperation() {
		int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		
		String whereCondition="make = 'Maruti' and model = 'F-Type' and id=101";
		
		int rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car",whereCondition);
		assertEquals(0, rowCount);
		
	}
	
	@Test
	void testUpdateCarAfterOperation() {
		int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		
		String whereCondition="make = 'Maruti' and model = 'F-Type' and id=101";
		
		CarEngine carEngine = new CarEngine(101, CarEngineType.of(3), 0, 266f);
		Car car = new Car(101, "Maruti", "F-Type", "Sedan", carEngine, LocalDate.of(2022, 1, 15));
		
		dao.updateCar(car);
		
		int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		
		assertEquals(expectedRows,actualRows);
		
		int rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car",whereCondition);
		assertEquals(1, rowCount);
	}
	
	@Test
	void testUpdateCarEngineBeforeOperation() {
		int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		
		String whereCondition="engine_size_l = 3 and horsepower = 277 and id=101";
		
		int rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine",whereCondition);
		assertEquals(0, rowCount);
	}
	
	@Test
	void testUpdateCarEngineAfterOperation() {
		int expectedRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");

		String whereCondition="engine_size_l = 3 and horsepower = 277 and id=101";


		CarEngine carEngine = new CarEngine(101, CarEngineType.of(3), 3, 277);
		Car car = new Car(101, "Maruti", "F-Type", "Sedan", carEngine, LocalDate.of(2022, 1, 15));

		dao.updateCar(car);

		int actualRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");

		assertEquals(expectedRows,actualRows);

		int rowCount=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine",whereCondition);
		assertEquals(1, rowCount);
	}
	
	@Test
	void testUpdateCar_WithEngine_Success() {
		int id = 108;
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
		
		Car updatedCar = new Car(id, "Tesla", "Roadster", CarBodyType.CONVERTIBLE.getName(),
			       new CarEngine(id, CarEngineType.ELECTRIC, 0.0f, 1360),
			       LocalDate.of(2023, 1, 1));

		dao.updateCar(updatedCar);
		
		assertEquals(eCarOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", 
				"id = " + id + " and make = 'Tesla' and model = 'Roadster'"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", 
				"id = " + id + " and horsepower = 1360"));
	}

	@Test
	void testUpdateCar_WithoutEngine_Success() {
		int id = 106;
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
		
		Car updatedCar = new Car(id, "Berg", "X-ITE Pedal Car", CarBodyType.OTHER.getName(),
			       null, LocalDate.of(2022, 3, 4));

		dao.updateCar(updatedCar);
		
		assertEquals(eCarOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", 
				"id = " + id + " and make = 'Berg' and model = 'X-ITE Pedal Car'"));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
	}

	@Test
	void testUpdateCar_NotPresent_ThrowsException() {
		int id = 999;
		int eCarOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		int eCarEngineOldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
		
		Car updatedCar = new Car(id, "Tesla", "Roadster", CarBodyType.CONVERTIBLE.getName(),
			       new CarEngine(id, CarEngineType.ELECTRIC, 0.0f, 1360),
			       LocalDate.of(2023, 1, 1));
		
		assertThrows(DatabaseException.class, () -> {
			dao.updateCar(updatedCar);
		});
		
		assertEquals(eCarOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
		assertEquals(eCarEngineOldSize, JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
		assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
	}
	
	@Test
	void deleteCarTwice() {
		dao.deleteCar(101);
		assertThrows(DatabaseException.class, ()-> {
			dao.deleteCar(101);
		});
	}

	@Test
	void testDeleteCar1()  {
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		dao.deleteCar(101);
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
		assertEquals(oldSize - 1, newSize);
	}

	@Test
	void testDeleteCarRow(){
		dao.deleteCar(101);
		int rowsWithDeletedId=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car","id=101");
		assertEquals(0,rowsWithDeletedId);

 

	}

	@Test
	void testDeleteCarEngine1()  {
		int oldSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		dao.deleteCar(101);
		int newSize = JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");
		assertEquals(oldSize - 1, newSize);
	}

	@Test
	void testDeleteCarEngineRow(){
		dao.deleteCar(101);
		int rowsWithDeletedId=JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine","id=101");
		assertEquals(0,rowsWithDeletedId);

 

	}

	@Test
    void testDeleteCar_WithoutEngine_Success() {
        int id = 106;
        int oldCarsSize =  JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car");
        int oldCarEnginesSize =  JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine");

 

        dao.deleteCar(id);

        assertEquals(oldCarsSize - 1,  JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car"));
        assertEquals(0,  JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car", "id = " + id));
        assertEquals(oldCarEnginesSize,  JdbcTestUtils.countRowsInTable(jdbcTemplate, "e_car_engine"));
        assertEquals(0,  JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "e_car_engine", "id = " + id));
    }

	@Test
	void testDeleteCarNegativeId() {
		int id = -101;
		assertThrows(DatabaseException.class,()->dao.deleteCar(id));	
	}

	
			
}
