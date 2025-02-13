package blaybus.hair_mvp.domain.designer.repository;

import blaybus.hair_mvp.domain.designer.entity.Designer;
import blaybus.hair_mvp.utils.OptionalUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class DesignerRepositoryTest {

    @Autowired DesignerRepository designerRepository;

    @Test
    @Transactional
    void test() {
        Designer designer = OptionalUtil.getOrElseThrow(designerRepository.findByName("이초 디자이너"), "디자이너 정보가 존재하지 않습니다.");
        System.out.println(designer);
    }
}