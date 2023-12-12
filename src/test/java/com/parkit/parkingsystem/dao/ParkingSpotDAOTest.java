package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAOTest {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();

	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void getNextAvailableSlotIfAvailable() {
		// Arrange
		int nextSlot = 0;

		// Act
		nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// Assert
		assertEquals(1, nextSlot);
	}

	@Test
	public void updateParkingSpotTest() {
		// Arrange
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean updateVerification = false;
		int nextSlot = 0;

		// Act
		updateVerification = parkingSpotDAO.updateParking(parkingSpot);
		nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		// Assert
		assertEquals(true, updateVerification);
		assertEquals(2, nextSlot);
	}

	@Test
	public void getNextAvailableSlotIfNoSlotAvailable() {
		// Arrange
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		parkingSpotDAO.updateParking(parkingSpot);
		parkingSpot.setId(5);
		parkingSpotDAO.updateParking(parkingSpot);
		int noNextSlot = -1;

		// Act
		noNextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

		// Assert
		assertEquals(0, noNextSlot);

	}

}
