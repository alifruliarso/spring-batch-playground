package com.galapea.belajar.basicbatch;

import org.springframework.batch.item.ItemProcessor;
import com.google.common.flogger.FluentLogger;

public class CustomerItemProcessor implements ItemProcessor<Customer,Customer> {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    
    @Override
    public Customer process(Customer cust) throws Exception {
        String firstName = cust.getFirstName().toUpperCase();
        String lastName = cust.getLastName().toUpperCase();
        Customer transformed = new Customer(firstName, lastName);
        logger.atInfo().log("Converting %s into %s", cust, transformed);
        return transformed;
    }
    
}
