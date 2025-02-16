package blaybus.hair_mvp.domain.reservation.mapper;

import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.entity.Reservation;
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
}
