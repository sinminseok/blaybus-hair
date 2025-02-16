package blaybus.hair_mvp.domain.designer.service;

import static org.junit.jupiter.api.Assertions.*;

import blaybus.hair_mvp.domain.designer.dto.DesignerResponse;
import blaybus.hair_mvp.domain.designer.entity.MeetingType;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DesignerServiceTest {
    @Autowired DesignerService designerService;

    @Test
    void findDesignerByMtAndSty() {
        // given
        int page = 0;
        int size = 5;
        // when
        List<DesignerResponse> designers = designerService.findDesignerByMtAndSty(page, size, MeetingType.OFFLINE, "펌");
        // then
        for (DesignerResponse designer : designers) {
            System.out.println(designer.getName() + " " + designer.getOfflineConsultFee() + " " + designer.getOnlineConsultFee());
            assertEquals("펌", designer.getStyling());
        }
    }

    @Test
    void findDesignerBySty() {
        // given
        int page = 0;
        int size = 5;
        // when
        List<DesignerResponse> designers = designerService.findDesignerBySty(page, size, "펌");
        // then
        for (DesignerResponse designer : designers) {
            assertEquals("펌", designer.getStyling());
        }
    }
}