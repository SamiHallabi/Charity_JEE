package com.ag.charity.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {
    // MongoDB connection is configured via spring.data.mongodb.uri in application.properties.
    // Repository scanning is declared on CharityApplication via @EnableMongoRepositories.
}
