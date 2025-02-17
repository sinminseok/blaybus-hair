package blaybus.hair_mvp.domain.reservation.mapper;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.reservation.dto.ReservationCreateResponse;
import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "designer", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "googleMeetLink", ignore = true)
    Reservation toEntity(ReservationRequest reservationRequest);

    @Mapping(target = "reservationAt", source = "reservation.reservationAt")
    @Mapping(target = "meetingType", source = "reservation.meetingType")
    @Mapping(target = "designerName", source = "designer.name")
    @Mapping(target = "shopAddress", source = "designer.shopAddress")
    ReservationCreateResponse toCreateResponse(Reservation reservation, Designer designer);

    @Mapping(target = "id", source = "reservation.id")
    @Mapping(target = "reservationAt", source = "reservation.reservationAt")
    @Mapping(target = "designerName", source = "designer.name")
    @Mapping(target = "shopAddress", source = "designer.shopAddress")
    @Mapping(target = "meetingType", source = "reservation.meetingType")
    @Mapping(target = "price", source = "reservation.price")
    @Mapping(target = "status", source = "reservation.paymentStatus")
    @Mapping(target = "googleMeetLink", source = "reservation.googleMeetLink")
    ReservationResponse toResponse(Reservation reservation, Designer designer);
}
