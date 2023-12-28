package com.example.mangostore;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class MangoStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangoStoreApplication.class, args);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("classpath:databaseTools/changelog-master.xml");
        springLiquibase.setDataSource(dataSource);
        return springLiquibase;
    }
}
