package com.galapea.belajar.basicbatch;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import com.google.common.flogger.FluentLogger;

@SpringBootApplication
public class BasicBatchApplication implements CommandLineRunner {

	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	@Autowired
	JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BasicBatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.atInfo().log("....run...");
		final List<Customer> customers = jdbcTemplate
				.query("SELECT first_name, last_name FROM customer", new CustomerRowMapper());
		customers.forEach(cust -> System.out.println(cust.toString()));
		logger.atInfo().log("....we got %d customers", customers.size());
	}
}
