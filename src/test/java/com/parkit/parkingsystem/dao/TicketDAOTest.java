package com.parkit.parkingsystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
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
	public void saveTicketTest() {
		// Arrange
		Ticket ticketTest = new Ticket();
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, true);
		boolean savedVerification;
		Date dateInTime = new Date();
		Date dateOutTime = new Date();

		dateInTime.setTime(System.currentTimeMillis() - (120 * 60 * 1000));
		dateOutTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticketTest.setParkingSpot(parkingSpotTest);
		ticketTest.setVehicleRegNumber("AZERTY");
		ticketTest.setPrice(0);
		ticketTest.setInTime(dateInTime);
		ticketTest.setOutTime(dateOutTime);

		// Act
		savedVerification = ticketDAO.saveTicket(ticketTest);

		// Assert
		assertEquals("AZERTY", ticketTest.getVehicleRegNumber());
		assertEquals(0, ticketTest.getPrice());
		assertNotNull(ticketTest.getInTime());
		assertNotNull(ticketTest.getOutTime());
		assertEquals(true, savedVerification);
	}

	@Test
	public void getTickektTest() {
		// Arrange
		saveTicketTest();
		Ticket getTicketTest = new Ticket();

		// Act
		getTicketTest = ticketDAO.getTicket("AZERTY");

		// Assert
		assertEquals("AZERTY", getTicketTest.getVehicleRegNumber());
		assertEquals(0, getTicketTest.getPrice());
		assertNotNull(getTicketTest.getInTime());
		assertNotNull(getTicketTest.getOutTime());
	}

	@Test
	public void updateTicketTest() {
		// Arrange
		saveTicketTest();

		Ticket ticket = new Ticket();
		Date updateInTime = new Date();
		Date updateOutTime = new Date();

		updateInTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		updateOutTime.setTime(System.currentTimeMillis());

		ticket = ticketDAO.getTicket("AZERTY");

		ticket.setPrice(10);
		ticket.setInTime(updateInTime);
		ticket.setOutTime(updateOutTime);
		ticketDAO.updateTicket(ticket);

		// Act
		Ticket updatedTicket = new Ticket();
		updatedTicket = ticketDAO.getTicket("AZERTY");

		// Assert
		assertEquals(10, updatedTicket.getPrice());
		assertNotNull(updatedTicket.getInTime().getTime());
		assertNotNull(updatedTicket.getOutTime().getTime());
	}

	@Test
	public void getNbTicketTest() {
		// Arrange
		saveTicketTest();
		saveTicketTest();

		int countNbTicket = 0;

		// Act
		countNbTicket = ticketDAO.getNbTicket("AZERTY");

		// Assert
		assertNotEquals(0, countNbTicket);
		assertNotEquals(1, countNbTicket);
	}
}
