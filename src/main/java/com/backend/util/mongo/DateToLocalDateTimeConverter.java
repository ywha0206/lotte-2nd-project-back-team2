package com.backend.util.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    @Override
    public LocalDateTime convert(Date source) {
        if (source == null) {
            return null;
        }
        ZonedDateTime utcZoned = source.toInstant().atZone(ZoneId.of("UTC"));
        ZonedDateTime kstZoned = utcZoned.withZoneSameInstant(KST_ZONE);
        return kstZoned.toLocalDateTime();
    }
}
