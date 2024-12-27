package com.backend.config;

import com.backend.util.mongo.DateToLocalDateTimeConverter;
import com.backend.util.mongo.LocalDateTimeToDateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.MongoConfigurationSupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(this.connectionString);
    }

    @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new LocalDateTimeToDateConverter(),
                new DateToLocalDateTimeConverter()
        ));
    }

//    @Override
//    public CustomConversions customConversions() {
//        return new CustomConversions(List.of(
//                new Converter[]<LocalDate, Date>() {
//                    @Override
//                    public Date convert(LocalDate source) {
//                        return Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
//                    }
//                },
//                new Converter<Date, LocalDate>() {
//                    @Override
//                    public LocalDate convert(Date source) {
//                        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                    }
//                }
//        ));
//    }
}
