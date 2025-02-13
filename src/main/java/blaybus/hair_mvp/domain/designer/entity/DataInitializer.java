package blaybus.hair_mvp.domain.designer.entity;

import blaybus.hair_mvp.domain.designer.repository.DesignerRepository;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            if (designerRepository.count() == 0) { // 데이터가 없을 때만 mock 데이터 삽입
                try (CSVReader csvReader = new CSVReader(new InputStreamReader(
                        new ClassPathResource("디자이너 목록 리스트 업.csv").getInputStream(), StandardCharsets.UTF_8))) {

                    List<String[]> designerList = csvReader.readAll();
                    designerList.remove(0);

                    for (String[] values : designerList) {
                        String name = values[0].trim();
                        String shopAddress = values[1].trim();
                        String region = values[2].trim();
                        String category = values[3].trim();
                        if (values[4] == null || values[4].trim().isEmpty()) {
                            System.out.println(values[4]);
                        }
                        int f2fConsultFee = Integer.parseInt(values[4].replace(",", "").trim());
                        int onlineConsultFee = Integer.parseInt(values[5].replace(",", "").trim());
                        String[] splitMeetingType = values[6].split(",");
                        MeetingType meetingType = null;
                        if (splitMeetingType.length == 1) {
                            meetingType = values[6].equals("대면") ? MeetingType.MEETING : MeetingType.VIDEO_MEETING;
                        } else if (splitMeetingType.length == 2) {
                            meetingType = MeetingType.BOTH;

                        }
//                        MeetingType meetingType = values[6].contains("대면") && values[6].contains("비대면")
//                                ? MeetingType.BOTH
//                                : values[6].equals("대면") ? MeetingType.MEETING
//                                        : MeetingType.VIDEO_MEETING;
                        String bio = values[7];

                        designerRepository.save(
                                Designer.builder()
                                        .name(name)
                                        .shopAddress(shopAddress)
                                        .region(region)
                                        .category(category)
                                        .f2fConsultFee(f2fConsultFee)
                                        .onlineConsultFee(onlineConsultFee)
                                        .meetingType(meetingType)
                                        .bio(bio)
                                        .build());
                    }
                } catch (Exception e) {
                    log.error("csv 파일 읽는 중 오류 발생", e);
                }
            }
        };
    }
}