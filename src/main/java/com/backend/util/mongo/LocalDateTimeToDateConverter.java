package com.backend.util.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");


    @Override
    public Date convert(LocalDateTime source) {
        if (source == null) {
            return null;
        }
        ZonedDateTime kstZoned = source.atZone(KST_ZONE);
        ZonedDateTime utcZoned = kstZoned.withZoneSameInstant(ZoneId.of("UTC"));
        return Date.from(utcZoned.toInstant());
    }
}
