package com.galapea.belajar.basicbatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.common.flogger.FluentLogger;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.atInfo().log("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate
                    .query("SELECT first_name, last_name FROM customer", new CustomerRowMapper())
                    .forEach(person -> logger.atInfo().log("Found <%s> in the database.", person));
        }
    }
}
