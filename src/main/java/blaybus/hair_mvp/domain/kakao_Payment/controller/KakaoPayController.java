package blaybus.hair_mvp.domain.kakao_Payment.controller;

import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoApproveResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.KakaoReadyResponse;
import blaybus.hair_mvp.domain.kakao_Payment.dto.PaymentRequest;
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

    @PostMapping("/ready")
    public ResponseEntity<?> requestPayment(@RequestBody PaymentRequest request){
        log.info(request.toString());
        KakaoReadyResponse kakaoReadyResponse = kakaoPayService.kakaoPayReady(request);
        SuccessResponse response = new SuccessResponse(true,"결제 요청 성공", kakaoReadyResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/approve")
    public ResponseEntity<?> successPayment(@RequestParam("pg_token") String pgToken,@RequestParam("orderId") String orderId){
        KakaoApproveResponse kakaoApproveResponse = kakaoPayService.kakaoPayApprove(pgToken, orderId);
        SuccessResponse response = new SuccessResponse(true,"결제 승인 성공",kakaoApproveResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayment(){
        SuccessResponse response = new SuccessResponse(true,"결제를 휘소합니다",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/fail")
    public ResponseEntity<?> failPayment(){
        SuccessResponse response = new SuccessResponse(true,"결제를 실패했습니다",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
