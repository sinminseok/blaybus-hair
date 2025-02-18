package blaybus.hair_mvp.domain.user.mapper;

import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import blaybus.hair_mvp.domain.review.entity.Review;
import blaybus.hair_mvp.domain.user.dto.MyPageResponse;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * (componentMode = 'spring') 을 안하면 빈 등록이 안된다. 꼭 해주자.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(blaybus.hair_mvp.domain.user.entity.Role.CLIENT)")
    @Mapping(target = "profileImage", source = "userSignupRequest.profileUrl")
    User toEntity(UserSignupRequest userSignupRequest);

    @Mapping(target = "userNickName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userProfileUrl", source = "user.profileImage")
    @Mapping(target = "reservations", source = "reservations")
    @Mapping(target = "reviews", source = "reviews")
    @Mapping(target = "cancelReservations", source = "cancelReservations")
    MyPageResponse toMyPageResponse(User user, List<ReservationResponse> reservations, List<ReservationResponse> cancelReservations, List<ReviewResponse> reviews);
}
