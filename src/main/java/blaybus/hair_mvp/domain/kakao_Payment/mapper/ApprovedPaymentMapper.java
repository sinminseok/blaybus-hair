package blaybus.hair_mvp.domain.kakao_Payment.mapper;

import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.kakao_Payment.entity.ApprovedPayment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface ApprovedPaymentMapper {

    ApprovedPaymentMapper INSTANCE = Mappers.getMapper(ApprovedPaymentMapper.class);

    @Mapping(target = "tid",source = "response.tid")
    @Mapping(target = "amount",source = "response.amount")
    @Mapping(target = "approved_at", source = "response.approved_at", qualifiedByName = "stringToLocalDateTime")
    ApprovedPayment toEntity(KakaoApproveResponse response);


    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return null; // ✅ null 또는 빈 문자열이면 변환하지 않음
        }
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            return null; // ✅ 파싱 오류가 나면 예외 방지
        }
    }

}
