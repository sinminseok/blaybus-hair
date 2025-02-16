package blaybus.hair_mvp.domain.kakao_Payment.mapper;

import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper {
    // 날짜 타입을 String 타입으로 변환하기 위한 mapper
    @Named("stringToLocalDateTime")
    public LocalDateTime stringToLocalDateTime(String dateTime) {
        return (dateTime != null) ?
                LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                : null;
    }
}
