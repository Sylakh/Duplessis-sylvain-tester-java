package com.parkit.parkingsystem;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

			// when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicleTest() {
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
		
		parkingService.processExitingVehicle();

		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
		verify(ticketDAO, Mockito.times(1)).getNbTicket("ABCDEF");
		verify(ticketDAO, Mockito.times(1)).updateTicket(ticketDAO.getTicket(anyString()));

	}

	@Test
	public void processIncomingVehicleTest() {
		// test de l’appel de la méthode processIncomingVehicle() où tout se déroule
		// comme attendu.

		// Arrange
		ParkingSpot mockParkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
		when(parkingService.getNextParkingNumberIfAvailable()).thenReturn(mockParkingSpot);
		when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(false);
		when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
		when(ticketDAO.getNbTicket(any(String.class))).thenReturn(2);

		// Act
		parkingService.processIncomingVehicle();

		// verify
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

	}
	/**
	 * @Test public void processExitingVehicleUnableUpdateTest() { // exécution du
	 *       test dans le cas où la méthode updateTicket() de ticketDAO // renvoie
	 *       false lors de l’appel de processExitingVehicle() }
	 * 
	 * @Test public void getNextParkingNumberIfAvailableTest() { // test de l’appel
	 *       de la méthode getNextParkingNumberIfAvailable() avec pour // résultat
	 *       l’obtention d’un spot dont l’ID est 1 et qui est disponible. }
	 * 
	 * @Test public void getNextParkingNumberIfAvailableParkingNumberNotFoundTest()
	 *       { // test de l’appel de la méthode getNextParkingNumberIfAvailable()
	 *       avec pour // résultat aucun spot disponible (la méthode renvoie null).
	 *       }
	 * 
	 * @Test public void
	 *       getNextParkingNumberIfAvailableParkingNumberWrongArgumentTest() { //
	 *       test de l’appel de la méthode getNextParkingNumberIfAvailable() avec
	 *       pour // résultat aucun spot // (la méthode renvoie null) car l’argument
	 *       saisi par l’utilisateur concernant // le type de véhicule est erroné
	 *       (par exemple, // l’utilisateur a saisi 3). }
	 */
}
