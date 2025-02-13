package blaybus.hair_mvp.domain.kakao_Payment;

import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.kakao_Payment.entity.ApprovedPayment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ApprovedPaymentMapper {

    ApprovedPaymentMapper INSTANCE = Mappers.getMapper(ApprovedPaymentMapper.class);

    @Mapping(target = "tid",source = "response.tid")
    @Mapping(target = "amount",source = "response.amount")
    @Mapping(target = "payment", source = "payment")
    ApprovedPayment toEntity(KakaoApproveResponse response, Payment payment);

}
