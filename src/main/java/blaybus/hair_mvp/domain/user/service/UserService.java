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

    public void save(final UserSignupRequest request){
        User user = userMapper.toEntity(request);
        userRepository.save(user);
    }

    public boolean isExistUser(final String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public UserSurveyResponse getUserSurvey(String email) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(email), NOT_EXIST_USER_EMAIL_MESSAGE);
        return userMapper.toUserSurveyResponse(user);
    }


    @Transactional
    public void updateUserSurvey(String email, UserSurveyRequest request, String styling) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(email), NOT_EXIST_USER_EMAIL_MESSAGE);
        user.updateSurvey(request, styling);
    }


    public MyPageResponse findMyPageInformation(final String email) {
        // 사용자 조회
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(email), NOT_EXIST_USER_EMAIL_MESSAGE);

        // 예약, 취소된 예약, 리뷰 정보 가져오기
        List<ReservationResponse> reservations = getReservations(user);
        List<ReservationResponse> cancelReservations = getCancelReservations(user);
        List<ReviewResponse> reviews = getReviews(user);

        // 최종 응답 변환
        return userMapper.toMyPageResponse(user, reservations, cancelReservations, reviews);
    }

    private List<ReservationResponse> getReservations(User user) {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.findNotCancelReservationByUserId(user.getId()).stream()
                .map(reservation -> {
                    boolean isUpcoming = reservation.getReservationAt().isAfter(now);
                    return reservationMapper.toResponseWithCurrentState(reservation, reservation.getDesigner(), isUpcoming);
                })
                .collect(Collectors.toList());
    }

    private List<ReservationResponse> getCancelReservations(User user) {
        return reservationRepository.findCancelReservationByUserId(user.getId()).stream()
                .map(reservation -> reservationMapper.toResponseWithCurrentState(reservation, reservation.getDesigner(), false))
                .collect(Collectors.toList());
    }

    private List<ReviewResponse> getReviews(User user) {
        return reviewRepository.findAllByUserId(user.getId()).stream()
                .map(review -> reviewMapper.toResponse(review, review.getDesigner()))
                .collect(Collectors.toList());
    }

}
