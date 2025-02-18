package blaybus.hair_mvp.domain.designer.service;

import blaybus.hair_mvp.domain.designer.dto.TimeSlot;
import blaybus.hair_mvp.domain.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ReservationRepository reservationRepository;

    // 3개월 간의 데이터만 추출
    private final int RESERVATION_PERIOD_MONTHS = 3;
    // 10:00부터 20:00까지
    private final LocalTime START_TIME = LocalTime.of(10, 0);
    private final LocalTime END_TIME = LocalTime.of(20, 0);
    private final int SLOT_COUNT = 20;
    private final int INTERVAL_MINUTES = 30;

    public Map<LocalDate, List<TimeSlot>> getDesignerSchedule(UUID designerId) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(RESERVATION_PERIOD_MONTHS);
        // 오늘부터 3개월 뒤까지의 디자이너 예약 날짜 + 시간 조회
        List<LocalDateTime> reservedDateTimes = reservationRepository.findByDesignerIdAndDateRange(designerId, today.atStartOfDay(), endDate.atTime(END_TIME));
        Map<LocalDate, List<TimeSlot>> schedule = new LinkedHashMap<>();
        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<TimeSlot> timeSlots = generateTimeSlots(date, reservedDateTimes);
            schedule.put(date, timeSlots);
        }
        return schedule;
    }

    // 예약 가능 or 불가능 시간 생성, TimeSlot isAvailable 필드 세팅
    private List<TimeSlot> generateTimeSlots(LocalDate date, List<LocalDateTime> reservedTimes) {
        return IntStream.range(0, SLOT_COUNT)
                // 10:00부터 20:00까지 30분 간격으로 TimeSlot 생성 (20번 반복)
                .mapToObj(i -> START_TIME.plusMinutes((long) i * INTERVAL_MINUTES))
                .map(time -> TimeSlot.builder()
                        // date + time 합쳐서 LocalDateTime 생성
                        .dateTime(date.atTime(time))
                        // reservedTimes 안에 포함되어 있으면 isAvailable false
                        .isAvailable(!reservedTimes.contains(date.atTime(time)))
                        .build())
                .collect(Collectors.toList());
    }
}
