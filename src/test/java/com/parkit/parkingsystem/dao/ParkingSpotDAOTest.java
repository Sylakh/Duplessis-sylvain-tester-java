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
		int nextSlot = 0;

		nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(1, nextSlot);
	}

	@Test
	public void updateParkingSpotTest() {

		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		boolean updateVerification = false;

		updateVerification = parkingSpotDAO.updateParking(parkingSpot);
		assertEquals(true, updateVerification);

		int nextSlot = 0;
		nextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

		assertEquals(2, nextSlot);
	}

	@Test
	public void getNextAvailableSlotIfNoSlotAvailable() {

		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		parkingSpotDAO.updateParking(parkingSpot);
		parkingSpot.setId(5);
		parkingSpotDAO.updateParking(parkingSpot);

		int noNextSlot = 0;
		noNextSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

		assertEquals(0, noNextSlot);

	}

}
