package blaybus.hair_mvp.domain.review.mapper;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.domain.reservation.mapper.ReservationMapper;
import blaybus.hair_mvp.domain.review.dto.ReviewRequest;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import blaybus.hair_mvp.domain.review.entity.Review;
import blaybus.hair_mvp.domain.user.entity.User;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "designer", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toEntity(ReviewRequest reviewRequest);

    @Mapping(target = "createdAt", source = "review.createdAt")
    @Mapping(target = "starPoint", source = "review.starPoint")
    @Mapping(target = "content", source = "review.content")
    @Mapping(target = "designerName", source = "designer.name")
    //todo S3 연동 후 리팩토링
    @Mapping(target = "designerProfile", ignore = true)
    ReviewResponse toResponse(Review review, Designer designer);

}
