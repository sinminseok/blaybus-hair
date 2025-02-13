package blaybus.hair_mvp.domain.designer.entity;

import blaybus.hair_mvp.domain.designer.repository.DesignerRegionRepository;
import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final DesignerRepository designerRepository;
    private final DesignerRegionRepository designerRegionRepository;

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            if (designerRepository.count() == 0) { // 데이터가 없을 때만 mock 데이터 삽입
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new ClassPathResource("디자이너 목록 리스트 업.csv").getInputStream(), StandardCharsets.UTF_8))) {

                    String line;
                    br.readLine();

                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(",");

                        String name = values[0].trim();
                        String address = values[1].trim();
                        String[] regions = values[2].split("/");
                        String specialty = values[3].trim();
                        int faceToFacePrice = Integer.parseInt(values[4].replace(",", "").trim());
                        int remotePrice = Integer.parseInt(values[5].replace(",", "").trim());
                        MeetingType meetingType = values[6].contains("대면") && values[6].contains("비대면")
                                ? MeetingType.BOTH
                                : values[6].contains("대면") ? MeetingType.MEETING
                                        : MeetingType.VIDEO_MEETING;
                        String intro = values[7].trim();

                        Designer designer = designerRepository.save(
                                new Designer(name, address, specialty, faceToFacePrice, remotePrice, meetingType, intro));

                        Arrays.stream(regions).map(String::trim).forEach(region -> {
                            designerRegionRepository.save(new DesignerRegion(region, designer));
                        });
                    }
                } catch (Exception e) {
                    log.error("csv 파일 읽는 중 오류 발생", e);
                }
            }
        };
    }
}