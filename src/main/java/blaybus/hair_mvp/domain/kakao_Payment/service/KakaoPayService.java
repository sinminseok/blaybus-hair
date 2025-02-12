package blaybus.hair_mvp.domain.kakao_Payment.service;

import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoReadyResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.PaymentRequest;
import blaybus.hair_mvp.domain.kakao_Payment.entity.KakaoPayProperties;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import blaybus.hair_mvp.domain.kakao_Payment.repository.PaymentRepository;
import blaybus.hair_mvp.utils.OptionalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    private static final String KAKAO_API_HOST = "https://open-api.kakao.com";

    private final KakaoPayProperties kakaoPayProperties;
    private final PaymentRepository paymentRepository;

    RestTemplate restTemplate = new RestTemplate();

    // 결제 요청
    public KakaoReadyResponse kakaoPayReady(PaymentRequest request) {

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","KakaoAK " + kakaoPayProperties.getSecretKey());


        // 요청 바디
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("cid","TC0ONETIME");
        params.add("partner_order_id", request.getOrderId());
        params.add("partner_user_id", request.getUserId());
        params.add("item_name",request.getItemName());
        params.add("quantity","1");
        params.add("total_amount", String.valueOf(request.getAmount()));
        params.add("tax_free_amount","0");
        params.add("approval_url","http://localhost::8080/kakao/success");
        params.add("cancel_url","http://localhost::8080/kakao/cancel");
        params.add("fail_url","http://localhost::8080/kakao/fail");

        // 헤더와 바디 붙이기
        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,headers);

        KakaoReadyResponse response = restTemplate.postForObject(
                KAKAO_API_HOST + "/online/v1/payment/ready",requestEntity,KakaoReadyResponse.class
        );

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .tid(response.getTid())
                .status(Status.READY)
                .amount(request.getAmount())
                .build();

        paymentRepository.save(payment);

        return response;
    }
    public KakaoApproveResponse kakaoPayApprove(String pgToken, String orderId){
        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","KakaoAK " + kakaoPayProperties.getSecretKey());

        Payment payment = OptionalUtil.getOrElseThrow(paymentRepository.findByOrderId(orderId),"존재하지 않은 결제건입니다");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", kakaoPayProperties.getCid());
        params.add("tid",payment.getTid());
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", payment.getUserId());
        params.add("pg_token",pgToken);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);

        KakaoApproveResponse response = restTemplate.postForObject(
                KAKAO_API_HOST,request,KakaoApproveResponse.class
        );

        payment.setStatus(Status.SUCCESS);
        paymentRepository.save(payment);
        return response;
    }

    /**
     * http 에러 코드 이후에 추가
     */
}
