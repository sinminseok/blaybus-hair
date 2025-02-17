package blaybus.hair_mvp.domain.user.service;

import blaybus.hair_mvp.domain.designer.dto.UserSurveyRequest;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.mapper.UserMapper;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * 사용자 계정 생성 메서드
     */
    public void save(final UserSignupRequest request){
        User user = userMapper.toEntity(request);
        userRepository.save(user);
    }

    /**
     * email 존재 여부 확인 메서드
     */
    public boolean isExistUser(final String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<User> findByEmail(final String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void updateUserSurvey(String email, UserSurveyRequest request, String styling) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        user.updateSurvey(request, styling);

    }
}
