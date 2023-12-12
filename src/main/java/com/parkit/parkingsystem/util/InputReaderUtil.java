package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * All necessary tools to get information from the customer
 * 
 * @version 1.0
 */
public class InputReaderUtil {

	private static Scanner scan = new Scanner(System.in);
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	/**
	 * Method to get the selection number for parkingType from the customer
	 * 
	 * @return an integer representing the parking type
	 * @version 1.0
	 */
	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	/**
	 * Method to get the reference of the vehicle from the customer
	 * 
	 * @return a string representing the reference of the vehicle of the customer
	 * @version 1.0
	 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell", e);
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}
