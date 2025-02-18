package blaybus.hair_mvp.domain.kakao_Payment.service;

import blaybus.hair_mvp.domain.kakao_Payment.dto.*;

import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.KakaoPayProperties;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import blaybus.hair_mvp.domain.kakao_Payment.mapper.KakaoPaymentMapper;
import blaybus.hair_mvp.domain.kakao_Payment.repository.PaymentRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private static final String KAKAO_PAY_API_HOST = "https://open-api.kakaopay.com";
    private static final String KAKAO_TRANSACTION_API_LINK = "https://link.kakaopay.com/_/PofpGNf";

    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;
    private final KakaoPaymentMapper kakaoPaymentMapper;

    RestTemplate restTemplate = new RestTemplate();

    public KakaoReadyResponse kakaoPayReadyDuplicate(PaymentRequest request) {

        Optional<Payment> existingPayment = paymentRepository.findByOrderIdAndStatus(request.getOrderId(), Status.READY);
        if (existingPayment.isPresent()) {

            paymentRepository.delete(existingPayment.get());  //
        }

        // 새로운 결제 요청 수행
        return sendKakaoPayRequest(request);
    }
//    public void cancelReadyPayment(){
//        paymentRepository.delete();
//    }

    // 결제 요청
    public KakaoReadyResponse sendKakaoPayRequest(PaymentRequest request) {
        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디
        Map<String, String> params = new HashMap<>();

        params.put("cid", kakaoPayProperties.getCid());
        params.put("partner_order_id", request.getOrderId());
        params.put("partner_user_id", request.getUserId());
        params.put("item_name", request.getItemName());
        params.put("quantity", "1");
        params.put("total_amount", String.valueOf(request.getAmount().getTotal()));
        params.put("tax_free_amount", "0");
        params.put("approval_url", "http://localhost:8080/online/v1/payment/approve?orderId=" + request.getOrderId());
        params.put("cancel_url", "http://localhost:8080/online/v1/payment/cancel?orderId=" + request.getOrderId());
        params.put("fail_url", "http://localhost:8080/online/v1/payment/fail");

        // 헤더와 바디 붙이기
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity(params, headers);
        try {
            KakaoReadyResponse kakaoReadyResponse = restTemplate.postForObject(
                    KAKAO_PAY_API_HOST + "/online/v1/payment/ready", requestEntity, KakaoReadyResponse.class
            );

            Payment payment = Payment.builder()
                    .orderId(request.getOrderId())
                    .userId(request.getUserId())
                    .tid((kakaoReadyResponse.getTid()))
                    .cid(kakaoPayProperties.getCid())
                    .item_name(request.getItemName())
                    .status(Status.READY)
                    .amount(request.getAmount())
                    .created_at(LocalDateTime.parse(kakaoReadyResponse.getCreated_at()))
                    .build();

            paymentRepository.save(payment);
            System.out.println("결제 승인 정보 : " + kakaoReadyResponse);

            return kakaoReadyResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("카카오페이 결제 준비 요청 중 오류 발생 : " + e.getMessage());
        }
    }

    // 결제 승인
    public KakaoApproveResponse kakaoPayApprove(String tid,String pgToken){
        //tid 를 받아야할듯
        // tid 가 생성되는 시점은 ready api 호출 시 응답으로 (유효시간 15분정도)

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);


        Payment payment = OptionalUtil.getOrElseThrow(paymentRepository.findByTid(tid),"해당 주문의 결제 내역이 존재하지 않습니다.");
        log.info("payment {}", payment);

        Map<String, String> params = new HashMap<>();
        params.put("cid", kakaoPayProperties.getCid());
        params.put("tid", tid);
        params.put("partner_order_id", payment.getOrderId());
        params.put("partner_user_id", payment.getUserId());
        params.put("pg_token",pgToken);

        HttpEntity<Map<String,String>> request = new HttpEntity<>(params,headers);
        // JSON 데이터로 변경해야 하므로 Map 사용
        try {
            KakaoApproveResponse response = restTemplate.postForObject(
                    KAKAO_PAY_API_HOST + "/online/v1/payment/approve", request, KakaoApproveResponse.class);

            log.info("결제 승인 응답 객체 :" + KakaoApproveResponse.class);
            payment.approvePayment(response);
            paymentRepository.save(payment);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("카카오페이 결제 승인 요청 중 오류 발생: " + e.getMessage());
        }
    }

    // 결제 조회(단건 조회)
    public OrderResponse findByTidAndCid(String tid,String cid){
        Payment payment = OptionalUtil.getOrElseThrow(paymentRepository.findByTidAndCid(tid,cid),"해당 주문의 결제 내역이 존재하지 않습니다.");
        return kakaoPaymentMapper.toDto(payment);
    }

    //  결제 취소
    public KakaoCancelResponse cancelPayment(String tid) {

        Payment payment = paymentRepository.findByTid(tid)
                .orElseThrow(() -> new RuntimeException("해당 주문의 결제 내역이 존재하지 않습니다."));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> params = new HashMap<>();
        params.put("cid", kakaoPayProperties.getCid());
        params.put("tid", payment.getTid());
        params.put("cancel_amount", String.valueOf(payment.getAmount().getTotal())); // cancel_amount 는 객체가 아니라 정수타입으로
        params.put("cancel_tax_free_amount", "0");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        KakaoCancelResponse response = restTemplate.postForObject(
                KAKAO_PAY_API_HOST + "/online/v1/payment/cancel", requestEntity, KakaoCancelResponse.class
        );

        payment.cancelPayment(response);
        paymentRepository.save(payment);

        log.info("기존 결제 취소 완료: " + response);

        return response;
    }
    // 이미 결제 취소한 건에 대해서 다시 취소하지 못하도록 예외발생


    // 수정 중
    public KakaoDepositResponse depositPayment(String orderId){
        Payment payment = OptionalUtil.getOrElseThrow(paymentRepository.findByOrderId(orderId),"해당 주문의 결제 내역이 존재하지 않습니다.");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,String> params = new HashMap<>();

        params.put("partner_user_id", payment.getUserId());
        params.put("partner_order_id", orderId);
        params.put("amount", String.valueOf(payment.getAmount().getTotal()));

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        KakaoDepositResponse response = restTemplate.postForObject(
                KAKAO_TRANSACTION_API_LINK + "/connect/api/v1/money-transaction",requestEntity,KakaoDepositResponse.class
        );

        Payment depositPayment = Payment.builder()
                .tid(payment.getTid())
                .aid(payment.getAid())
                .cid(payment.getCid())
                .orderId(orderId)
                .userId(payment.getUserId())
                .item_name(payment.getItem_name())
                .status(Status.DEPOSIT_WAITING)
                .amount(payment.getAmount())
                .build();

        paymentRepository.save(depositPayment);

        return response;

    }

}
