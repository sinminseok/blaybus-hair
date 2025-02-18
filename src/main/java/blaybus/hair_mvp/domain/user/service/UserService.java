package blaybus.hair_mvp.domain.user.service;


import blaybus.hair_mvp.domain.user.dto.UserSurveyRequest;
import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.reservation.mapper.ReservationMapper;
import blaybus.hair_mvp.domain.reservation.repository.ReservationRepository;
import blaybus.hair_mvp.domain.review.dto.ReviewResponse;
import blaybus.hair_mvp.domain.review.mapper.ReviewMapper;
import blaybus.hair_mvp.domain.review.repository.ReviewRepository;
import blaybus.hair_mvp.domain.user.dto.MyPageResponse;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.domain.user.dto.UserSurveyResponse;
import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.domain.user.mapper.UserMapper;
import blaybus.hair_mvp.domain.user.repository.UserRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import static blaybus.hair_mvp.constants.ErrorMessages.NOT_EXIST_USER_EMAIL_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;
    private final ReservationMapper reservationMapper;

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


    public UserSurveyResponse getUserSurvey(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        return UserSurveyResponse.builder()
                .faceShape(user.getFaceShape())
                .styling(user.getStyling())
                .personalColor(user.getPersonalColor())
                .hairCondition(user.getHairCondition())
                .build();
    }


    @Transactional
    public void updateUserSurvey(String email, UserSurveyRequest request, String styling) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        user.updateSurvey(request, styling);
    }


    public MyPageResponse findMyPageInformation(final String email){
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(email), NOT_EXIST_USER_EMAIL_MESSAGE);
        List<ReservationResponse> reservations = new ArrayList<>();
        //예약 내역
        reservationRepository.findByUserId(user.getId()).stream()
                .forEach(reservation -> {
                    if (reservation.getReservationAt().isBefore(LocalDateTime.now())){
                        reservations.add(reservationMapper.toResponseWithCurrentState(reservation, reservation.getDesigner(), false));
                    }
                    if (!reservation.getReservationAt().isBefore(LocalDateTime.now())){
                        reservations.add(reservationMapper.toResponseWithCurrentState(reservation, reservation.getDesigner(), true));
                    }
                });

        List<ReservationResponse> cancelReservations = reservationRepository.findCancelReservationByUserId(user.getId()).stream()
                .map(reservation -> reservationMapper.toResponseWithCurrentState(reservation, reservation.getDesigner(), false))
                .collect(Collectors.toList());

        List<ReviewResponse> reviews = reviewRepository.findAllByUserId(user.getId()).stream()
                .map(review -> reviewMapper.toResponse(review, review.getDesigner()))
                .collect(Collectors.toList());
        return userMapper.toMyPageResponse(user, reservations, cancelReservations, reviews);
    }
}
