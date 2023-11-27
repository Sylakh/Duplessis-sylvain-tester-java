package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static final double NB_MILLISECONDS_IN_AN_HOUR = 3600000;

	public void calculateFare(Ticket ticket, boolean isDiscount) {

		double discountForRegularcustomer = 1;

		if (isDiscount == true)
			discountForRegularcustomer = 0.95;

		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		double duration = (outHour - inHour) / NB_MILLISECONDS_IN_AN_HOUR;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration > 0.5) {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * discountForRegularcustomer);
			} else {
				ticket.setPrice(0);
			}
			break;
		}
		case BIKE: {
			if (duration > 0.5) {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR * discountForRegularcustomer);
			} else {
				ticket.setPrice(0);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

	public void calculateFare(Ticket ticket) {

		calculateFare(ticket, false);

	}
}