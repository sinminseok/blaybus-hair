package blaybus.hair_mvp.domain.kakao_Payment.controller;

import blaybus.hair_mvp.domain.kakao_Payment.dto.*;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Payment;
import blaybus.hair_mvp.domain.kakao_Payment.entity.Status;
import blaybus.hair_mvp.domain.kakao_Payment.repository.PaymentRepository;
import blaybus.hair_mvp.domain.kakao_Payment.service.KakaoPayService;
import blaybus.hair_mvp.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/online/v1/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final PaymentRepository paymentRepository;


    @PostMapping("/ready")
    public ResponseEntity<?> requestPayment(@RequestBody PaymentRequest request){
        System.out.println("결제 요청 도착 :" + request );
        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.sendKakaoPayRequest(request);
        System.out.println("tid : "+ kakaoReadyResponse.getTid());
        SuccessResponse response = new SuccessResponse(true,"결제 요청 성공", kakaoReadyResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/approve")
    public ResponseEntity<?> successPayment( @RequestParam("pg_token") String pgToken,@RequestParam("orderId") String orderId){
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->  new RuntimeException("존재하지 않은 결제 정보입니다"));
        String tid = payment.getTid();
        KakaoApproveResponse kakaoApproveResponse = kakaoPayService.kakaoPayApprove(tid, pgToken);
        System.out.println("pgToken : "+ pgToken);

        SuccessResponse response = new SuccessResponse(true,"결제 승인 성공",kakaoApproveResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
        // 추후에 리다이렉트 경로 추가
    }

    // 결제내역 조회
    // 결제 조회 권한 필요
    @GetMapping("/order")
    public ResponseEntity<?> getPayment(@RequestParam("tid") String tid,@RequestParam("cid") String cid ){
        OrderResponse orderResponse = kakaoPayService.findByTidAndCid(tid,cid);
        SuccessResponse response = new SuccessResponse(true,"결제 내역을 조회합니다",orderResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 중복 결제 요청 취소

    // 결제 승인 후 결제 취소
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam("orderId") String orderId){
        Payment approvedPayment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->  new RuntimeException("존재하지 않은 주문아이디"));
        String tid = approvedPayment.getTid();
        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.cancelPayment(tid);
        SuccessResponse response = new SuccessResponse(true,"결제를 휘소합니다",kakaoCancelResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/fail")
    public ResponseEntity<?> failPayment(){
        SuccessResponse response = new SuccessResponse(true,"결제를 실패했습니다",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
