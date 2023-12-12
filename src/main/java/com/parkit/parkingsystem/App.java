package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/**
 * Main class of the project ParkIt
 * <p>
 * load the shell of the application
 * 
 * @version 1.0
 */
public class App {
	private static final Logger logger = LogManager.getLogger("App");

	/**
	 * Main class of the project ParkIt
	 * <p>
	 * load the shell of the application
	 * 
	 * @version 1.0
	 */
	public static void main(String args[]) {

		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}
