package blaybus.hair_mvp.domain.reservation.controller;

import blaybus.hair_mvp.auth.SecurityContextHelper;
import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.reservation.dto.ReservationCreateResponse;
import blaybus.hair_mvp.domain.reservation.dto.ReservationRequest;
import blaybus.hair_mvp.domain.reservation.dto.ReservationResponse;
import blaybus.hair_mvp.domain.reservation.service.ReservationService;

import blaybus.hair_mvp.domain.user.entity.User;
import blaybus.hair_mvp.utils.SuccessResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final SecurityContextHelper securityContextHelper;

    @PostMapping
    public ResponseEntity<?> reserve(@RequestBody final ReservationRequest request) throws IOException {
        ReservationCreateResponse reservationResponse = reservationService.save(request, securityContextHelper.getEmailInToken());
        SuccessResponse response = new SuccessResponse(true, "예약 성공", reservationResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentReservation(){
        List<ReservationResponse> currentReservations = reservationService.findCurrentReservations(securityContextHelper.getEmailInToken());
        SuccessResponse response = new SuccessResponse(true, "현재 예약 조회 성공", currentReservations);
        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{reservationId}")
    public ResponseEntity<?> getDesignerDetail(@PathVariable UUID reservationId) {
        reservationService.cancelReservation(reservationId);
        SuccessResponse response = new SuccessResponse(true, "예약 취소 성공", null);
        return ResponseEntity.ok(response);
    }
}
