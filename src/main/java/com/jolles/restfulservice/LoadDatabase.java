package com.jolles.restfulservice;

import com.jolles.restfulservice.model.Employee;
import com.jolles.restfulservice.model.Order;
import com.jolles.restfulservice.repository.IEmployeeRepository;
import com.jolles.restfulservice.repository.IOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(IEmployeeRepository employeeRepository, IOrderRepository orderRepository) {

        return args -> {
            employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
            employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));

            employeeRepository.findAll().forEach(employee -> LOGGER.info("Preloaded " + employee));


            orderRepository.save(new Order("MacBook Pro", Order.Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Order.Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                LOGGER.info("Preloaded " + order);
            });
        };
    }
}
