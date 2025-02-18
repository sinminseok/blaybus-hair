package blaybus.hair_mvp.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSurveyResponse {
    private String faceShape;
    private String styling;
    private String personalColor;
    private String hairCondition;
}
