package com.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by sdrahnea
 */
@SpringBootApplication
@EnableJpaRepositories
public class CustomerManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementSystemApplication.class, args);
    }
}
