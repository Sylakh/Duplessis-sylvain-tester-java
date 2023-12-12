package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

/**
 * Service to calculate the fare price of the parking when the customer goes out
 * 
 * @version 1.0
 */
public class FareCalculatorService {

	private static final double NB_MILLISECONDS_IN_AN_HOUR = 3600000;

	/**
	 * Method to calculate the fare price of the parking when the customer goes out
	 * and update the table ticket
	 * 
	 * @param ticket     an object ticket
	 * @param isDiscount a boolean representing a regular customer or not
	 * @version 1.0
	 */
	public void calculateFare(Ticket ticket, boolean isDiscount) {

		double discountForRegularCustomer = 1;

		if (isDiscount == true)
			discountForRegularCustomer = 0.95;

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();

		double duration = (outHour - inHour) / NB_MILLISECONDS_IN_AN_HOUR;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration > 0.5) {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * discountForRegularCustomer);
			} else {
				ticket.setPrice(0);
			}
			break;
		}
		case BIKE: {
			if (duration > 0.5) {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * discountForRegularCustomer);
			} else {
				ticket.setPrice(0);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	/**
	 * Method to calculate the fare price of the parking when the customer goes out
	 * and use the parking for the first time
	 * <p>
	 * Update the table ticket
	 * 
	 * @param ticket an object ticket
	 * @version 1.0
	 */
	public void calculateFare(Ticket ticket) {

		calculateFare(ticket, false);

	}
}