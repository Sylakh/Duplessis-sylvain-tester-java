package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.text.DecimalFormat;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

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
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability

		Ticket ticketFromDataBase = new Ticket();
		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		assertNotEquals(null, ticketFromDataBase);
		assertEquals("ABCDEF", ticketFromDataBase.getVehicleRegNumber());
		assertEquals(ParkingType.CAR, ticketFromDataBase.getParkingSpot().getParkingType());
		assertNotNull(ticketFromDataBase.getInTime());
		assertNull(ticketFromDataBase.getOutTime());
		assertNotEquals(1, parkingSpotDAO.getNextAvailableSlot(ticketFromDataBase.getParkingSpot().getParkingType()));
		assertEquals(false, ticketFromDataBase.getParkingSpot().isAvailable());
	}

	@Test
	public void testParkingLotExit() {

		testParkingACar();
		Ticket ticketFromDataBase = new Ticket();
		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		Date newDate = new Date();
		newDate.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticketFromDataBase.setInTime(newDate);
		ticketFromDataBase.setOutTime(newDate);

		ticketDAO.updateTicket(ticketFromDataBase);

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database

		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		assertNotNull(ticketFromDataBase.getPrice());
		assertNotEquals(null, ticketFromDataBase.getOutTime());
	}

	@Test
	public void testParkingLotExitRecurringUser() {
		// Il doit tester le calcul du prix d’un
		// ticket via l’appel de processIncomingVehicle et processExitingVehicle
		// dans le cas d’un utilisateur récurrent.

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		Ticket ticket1 = new Ticket();
		ticket1 = ticketDAO.getTicket("ABCDEF");

		Date newDate1 = new Date();
		newDate1.setTime(System.currentTimeMillis() - (3 * 60 * 60 * 1000));
		ticket1.setInTime(newDate1);
		ticket1.setOutTime(newDate1);

		ticketDAO.updateTicket(ticket1);

		parkingService.processExitingVehicle();

		parkingService.processIncomingVehicle();

		Ticket ticket2 = new Ticket();
		ticket2 = ticketDAO.getTicket("ABCDEF");

		Date newDate2 = new Date();
		newDate2.setTime(System.currentTimeMillis() - (2 * 60 * 60 * 1000));
		ticket2.setInTime(newDate2);
		ticket2.setOutTime(newDate2);
		ticketDAO.updateTicket(ticket2);

		parkingService.processExitingVehicle();
		ticket2 = ticketDAO.getTicket("ABCDEF");

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		String roundedValue = decimalFormat.format(ticket2.getPrice());

		assertEquals("2,85", roundedValue);

	}

	@Test
	public void testParkingLotExitForOneHour() {

		testParkingACar();
		Ticket ticketFromDataBase = new Ticket();
		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		Date newDate = new Date();
		newDate.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ticketFromDataBase.setInTime(newDate);
		ticketFromDataBase.setOutTime(newDate);

		ticketDAO.updateTicket(ticketFromDataBase);

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();

		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		String roundedValue = decimalFormat.format(ticketFromDataBase.getPrice());

		assertNotNull(ticketFromDataBase.getPrice());
		assertEquals("1,5", roundedValue);
		assertNotEquals(null, ticketFromDataBase.getOutTime());

	}

	@Test
	public void testParkingLotExitForOneDay() {

		testParkingACar();
		Ticket ticketFromDataBase = new Ticket();
		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		Date newDate = new Date();
		newDate.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
		ticketFromDataBase.setInTime(newDate);
		ticketFromDataBase.setOutTime(newDate);

		ticketDAO.updateTicket(ticketFromDataBase);

		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();

		ticketFromDataBase = ticketDAO.getTicket("ABCDEF");

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		String roundedValue = decimalFormat.format(ticketFromDataBase.getPrice());

		assertNotNull(ticketFromDataBase.getPrice());
		assertEquals("36", roundedValue);
		assertNotEquals(null, ticketFromDataBase.getOutTime());

	}

}
