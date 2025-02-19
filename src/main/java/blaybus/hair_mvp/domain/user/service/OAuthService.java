package blaybus.hair_mvp.domain.user.service;

import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
import blaybus.hair_mvp.exception.ErrorResponseCode;
import blaybus.hair_mvp.exception.code.AuthExceptionCode;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.http.HttpRequestInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Value("${oauth.google-client-secret}")
    private String clientSecret;

    //todo 수정
    private static final String REDIRECT_URI = "http://localhost:5173/oauth/callback";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public UserSignupRequest getGoogleProfile(String authorizationCode) throws GeneralSecurityException, IOException {

        String decodedCode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                HTTP_TRANSPORT, JSON_FACTORY,
                clientId, clientSecret,
                decodedCode, REDIRECT_URI)
                .setGrantType("authorization_code")
                .execute();


        String accessToken = tokenResponse.getAccessToken();
        String idTokenString = tokenResponse.getIdToken();

        // Step 2: ID Token 검증
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new GeneralSecurityException("Invalid ID Token");
        }

        // Step 3: Google API를 통해 사용자 프로필 정보 가져오기
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));
        HttpCredentialsAdapter requestInitializer = new HttpCredentialsAdapter(credentials);

        PeopleService peopleService = new PeopleService.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName("Google OAuth2 Example")
                .build();

        // 사용자 정보 가져오기
        Person profile = peopleService.people().get("people/me")
                .setPersonFields("names,emailAddresses,photos")
                .execute();

        return UserSignupRequest.builder()
                .email(profile.getEmailAddresses().get(0).getValue())
                .name(profile.getNames().get(0).getDisplayName())
                .profileUrl(profile.getPhotos().get(0).getUrl())
                .build();
    }
}
