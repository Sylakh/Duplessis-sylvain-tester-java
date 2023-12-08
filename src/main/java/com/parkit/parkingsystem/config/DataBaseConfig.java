package com.parkit.parkingsystem.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	public Connection getConnection() throws ClassNotFoundException, SQLException {

		try (BufferedReader reader = new BufferedReader(
				new FileReader(".\\Duplessis-sylvain-tester-java\\src\\main\\resources\\application.properties"))) {

			Properties properties = new Properties();
			properties.load(reader);

			String dbUsername = properties.getProperty("dbUsername");
			String dbPassword = properties.getProperty("dbPassword");

			logger.info("Create DB connection");
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/prod", dbUsername, dbPassword);

		} catch (IOException e) {
			System.err.println("error while reading file " + e.getMessage() + "caused by" + e.getCause());
			throw new RuntimeException("Error loading database properties", e);
		}

	}

	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}

	}

	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
