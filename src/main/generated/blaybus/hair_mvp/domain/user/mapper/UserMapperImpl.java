package blaybus.hair_mvp.domain.user.mapper;

import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T15:28:06+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserSignupRequest userSignupRequest) {
        if ( userSignupRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( userSignupRequest.getEmail() );
        user.name( userSignupRequest.getName() );
        user.profileUrl( userSignupRequest.getProfileUrl() );

        user.role( blaybus.hair_mvp.domain.user.entity.Role.CLIENT );

        return user.build();
    }
}
