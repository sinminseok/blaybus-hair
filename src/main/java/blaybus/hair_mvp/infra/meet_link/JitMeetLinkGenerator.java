package blaybus.hair_mvp.infra.meet_link;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JitMeetLinkGenerator implements MeetLinkGenerator{
    @Override
    public String generateMeetLink() {
        String randomRoomName = UUID.randomUUID().toString().replace("-", "");
        return  "https://meet.jit.si/" + randomRoomName;
    }
}
