package blaybus.hair_mvp.domain.payment.controller;

import blaybus.hair_mvp.domain.payment.dto.KakaoDepositResponse;
import blaybus.hair_mvp.domain.payment.service.KakaoPayService;
import blaybus.hair_mvp.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/connect/api/v1/money-transaction")
public class DepositController {

    private final KakaoPayService kakaoPayService;
    private static final String KAKAO_TRANSACTION_API_LINK = "https://link.kakaopay.com/_/PofpGNf";

    // 코드 발급
    @GetMapping("/link")
    public ResponseEntity<?> deposit(){
        String redirectUrl = KAKAO_TRANSACTION_API_LINK;
        SuccessResponse response = new SuccessResponse(true,"발급 성공", redirectUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
