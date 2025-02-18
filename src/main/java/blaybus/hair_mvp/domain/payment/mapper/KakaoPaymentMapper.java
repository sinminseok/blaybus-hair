package blaybus.hair_mvp.domain.payment.mapper;

import blaybus.hair_mvp.domain.payment.dto.OrderResponse;
import blaybus.hair_mvp.domain.payment.entity.Payment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KakaoPaymentMapper {

    KakaoPaymentMapper INSTANCE = Mappers.getMapper(KakaoPaymentMapper.class);

     //결제 승인 응답
//    @Mapping(target = "status", constant = "SUCCESS_PAYMENT")
//    @Mapping(target = "approved_at", source = "approved_at", qualifiedByName = "stringToLocalDateTime") //  수정
//    @Mapping(target = "userId",source = "partner_user_id")
//    @Mapping(target = "orderId",source = "partner_order_id")
//    @Mapping(target = "canceled_at",ignore = true)
//    @Mapping(target = "created_at",source = "created_at",qualifiedByName = "stringToLocalDateTime")
//    void toApproveEntity(KakaoApproveResponse approveResponse,@MappingTarget Payment payment);


//    @Mapping(target = "status", constant = "CANCEL_PAYMENT")
//    @Mapping(target = "canceled_amount", source = "canceled_amount")
//    @Mapping(target = "canceled_at", source = "canceled_at", qualifiedByName = "stringToLocalDateTime")
//    void updatePaymentFromDto(KakaoCancelResponse cancelResponse, @MappingTarget Payment payment);


    @Mapping(target = "approved_at", expression = "java(payment.getApproved_at().toString())")
    @Mapping(target = "canceled_at", ignore = true) // 결제 취소 내역을 어디에 저장?
    OrderResponse toDto(Payment payment);

}
