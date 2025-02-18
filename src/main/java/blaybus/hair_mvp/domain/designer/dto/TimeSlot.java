package blaybus.hair_mvp.domain.designer.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeSlot {
    private LocalDateTime dateTime;
    private boolean isAvailable;
}
