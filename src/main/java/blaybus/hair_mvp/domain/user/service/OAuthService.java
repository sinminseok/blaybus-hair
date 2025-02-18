package blaybus.hair_mvp.domain.user.service;

import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.AuthExceptionCode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.http.HttpRequestInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

import static blaybus.hair_mvp.constants.ErrorMessages.INVALID_GOOGLE_ID_TOKEN;
import static blaybus.hair_mvp.infra.google.GoogleProfileHelper.getGoogleProfileName;
import static blaybus.hair_mvp.infra.google.GoogleProfileHelper.getGoogleProfilePicture;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Value("${oauth.google-client-id}")
    private String clientId;

    public UserSignupRequest getGoogleProfile(GoogleAuthRequest request) throws GeneralSecurityException, IOException {
        // Access token을 통해 Google API에 요청하여 사용자 프로필을 가져옵니다.
        String accessToken = request.getAccessToken();

        // Google API를 통해 사용자 프로필 정보 가져오기
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        // People API를 사용하여 사용자 정보 가져오기
        PeopleService peopleService = new PeopleService.Builder(new NetHttpTransport(), new GsonFactory(), requestInitializer)
                .setApplicationName("Google OAuth2 Example")
                .build();

        // 사용자 정보 가져오기 - resourceName을 people/me로 수정
        com.google.api.services.people.v1.model.Person profile = peopleService.people().get("people/me")
                .setPersonFields("names,emailAddresses,photos")
                .execute();

        return UserSignupRequest.builder()
                .email(profile.getEmailAddresses().get(0).getValue())
                .name(profile.getNames().get(0).getDisplayName())
                .profileUrl(profile.getPhotos().get(0).getUrl())
                .build();
    }
}
