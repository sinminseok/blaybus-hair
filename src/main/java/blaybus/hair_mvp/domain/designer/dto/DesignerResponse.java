package blaybus.hair_mvp.domain.designer.dto;

import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class DesignerResponse {
    private UUID id;
    private String name;
    private String shopAddress;
    private String region;
    private String styling;
    private int offlineConsultFee;
    private int onlineConsultFee;
    private String bio;
    private float rating;
}
