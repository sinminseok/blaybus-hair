package blaybus.hair_mvp.infra.google;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.util.Arrays;

public class GoogleProfileHelper {

    public static String getGoogleProfileName(String accessToken) throws IOException {
        AccessToken token = new AccessToken(accessToken, null);
        GoogleCredentials credentials = GoogleCredentials.create(token)
                .createScoped(Arrays.asList(
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "openid"
                ));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        PeopleService peopleService = new PeopleService.Builder(
                new NetHttpTransport(), new GsonFactory(), requestInitializer)
                .build();
        Person profile = peopleService.people().get("people/me").setPersonFields("names").execute();
        return profile.getNames().get(0).getDisplayName();
    }

    public static String getGoogleProfilePicture(String accessToken) throws IOException {
        AccessToken token = new AccessToken(accessToken, null);
        GoogleCredentials credentials = GoogleCredentials.create(token)
                .createScoped(Arrays.asList(
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "openid"
                ));

        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        PeopleService peopleService = new PeopleService.Builder(
                new NetHttpTransport(), new GsonFactory(), requestInitializer)
                .build();
        Person profile = peopleService.people().get("people/me").setPersonFields("photos").execute();
        return profile.getPhotos().get(0).getUrl();
    }
}
