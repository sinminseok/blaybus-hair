package blaybus.hair_mvp.domain.user.mapper;

import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * (componentMode = 'spring') 을 안하면 빈 등록이 안된다. 꼭 해주자.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(blaybus.hair_mvp.domain.user.entity.Role.CLIENT)")
    User toEntity(UserSignupRequest userSignupRequest);

}
