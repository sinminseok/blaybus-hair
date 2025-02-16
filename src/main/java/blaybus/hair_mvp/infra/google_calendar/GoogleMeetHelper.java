package blaybus.hair_mvp.infra.google_calendar;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TimeZone;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleMeetHelper {

    private final Calendar googleCalendar;

    public String createGoogleMeetLink(String summary, String description, DateTime startDate, DateTime endDate) throws IOException {
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setConferenceData(new ConferenceData()
                        .setCreateRequest(new CreateConferenceRequest()
                                .setRequestId(UUID.randomUUID().toString())
                                .setConferenceSolutionKey(new ConferenceSolutionKey().setType("eventHangout"))
                        ));

        EventDateTime start = new EventDateTime()
                .setDateTime(startDate)
                .setTimeZone(TimeZone.getTimeZone("Asia/Seoul").getID());
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDate)
                .setTimeZone(TimeZone.getTimeZone("Asia/Seoul").getID());
        event.setEnd(end);

        Event createdEvent = googleCalendar.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return createdEvent.getHangoutLink();
    }

}
