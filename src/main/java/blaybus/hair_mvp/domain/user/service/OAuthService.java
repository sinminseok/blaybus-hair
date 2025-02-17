package blaybus.hair_mvp.domain.user.service;

import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import blaybus.hair_mvp.domain.user.dto.UserSignupRequest;
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
        GoogleIdTokenVerifier verifier = createVerifier();
        GoogleIdToken googleIdToken = verifier.verify(request.getIdToken());
        if (googleIdToken == null) {
            throw new IllegalBlockSizeException(INVALID_GOOGLE_ID_TOKEN);
        }
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return UserSignupRequest.builder()
                .email(payload.getEmail())
                .name(getGoogleProfileName(request.getAccessToken()))
                .profileUrl(getGoogleProfilePicture(request.getAccessToken()))
                .build();
    }

    private GoogleIdTokenVerifier createVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

}
