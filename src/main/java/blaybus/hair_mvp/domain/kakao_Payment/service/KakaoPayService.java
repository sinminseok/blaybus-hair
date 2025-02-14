package blaybus.hair_mvp.domain.kakao_Payment.service;

import blaybus.hair_mvp.domain.kakao_Payment.ApprovedPaymentMapper;
import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoReadyResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.PaymentRequest;
import blaybus.hair_mvp.domain.kakao_Payment.entity.ApprovedPayment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.KakaoPayProperties;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import blaybus.hair_mvp.domain.kakao_Payment.repository.ApprovedPaymentRepository;
import blaybus.hair_mvp.domain.kakao_Payment.repository.PaymentRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private static final String KAKAO_PAY_API_HOST = "https://open-api.kakaopay.com";

    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;
    private final ApprovedPaymentRepository approvedPaymentRepository;
    private final ApprovedPaymentMapper approvedPaymentMapper;

    RestTemplate restTemplate = new RestTemplate();

    // 결제 요청
    public KakaoReadyResponse kakaoPayReady(PaymentRequest request) {

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디
       Map<String,String> params = new HashMap<>();

        params.put("cid",kakaoPayProperties.getCid());
        params.put("partner_order_id", request.getOrderId());
        params.put("partner_user_id", request.getUserId());
        params.put("item_name", request.getItemName());
        params.put("quantity","1");
        params.put("total_amount", String.valueOf(request.getAmount().getTotal()));
        params.put("tax_free_amount","0");
        params.put("approval_url","http://localhost:8080/online/v1/payment/approve?orderId="+request.getOrderId());
        params.put("cancel_url","http://localhost:8080/online/v1/payment/cancel");
        params.put("fail_url","http://localhost:8080/online/v1/payment/fail");

        // 헤더와 바디 붙이기
        HttpEntity<Map<String,String>> requestEntity = new HttpEntity(params,headers);

        KakaoReadyResponse kakaoReadyResponse = restTemplate.postForObject(
                KAKAO_PAY_API_HOST + "/online/v1/payment/ready",requestEntity,KakaoReadyResponse.class
        );

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .tid((kakaoReadyResponse.getTid()))
                .status(Status.READY)
                .amount(request.getAmount())
                .build();

        paymentRepository.save(payment);

        return kakaoReadyResponse;
    }
    // 결제 승인
    public KakaoApproveResponse kakaoPayApprove(String tid,String pgToken){
        //tid 를 받아야할듯
        // tid 가 생성되는 시점은 ready api 호출 시 응답으로 (유효시간 15분정도)

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","SECRET_KEY " + kakaoPayProperties.getSecretKey());
        headers.setContentType(MediaType.APPLICATION_JSON);


        Payment payment = OptionalUtil.getOrElseThrow(paymentRepository.findByTid(tid),"존재하지 않은 결제건입니다");
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
            ApprovedPayment approvedPayment = approvedPaymentMapper.toEntity(response,payment);
            payment.setStatus(Status.SUCCESS);
            paymentRepository.save(payment);
            approvedPaymentRepository.save(approvedPayment);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("카카오페이 결제 승인 요청 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * http 에러 코드 이후에 추가
     */
}
