package blaybus.hair_mvp.domain.user.service;

import blaybus.hair_mvp.domain.user.dto.GoogleAuthRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Value("${oauth.google-client-id}")
    private String clientId;

    public String getGoogleEmail(GoogleAuthRequest request) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = createVerifier();
        GoogleIdToken googleIdToken = verifier.verify(request.getIdToken());

        if (googleIdToken == null) {
            //todo 커스텀 처리
            throw new IllegalBlockSizeException("Invalid ID token");
        }
        System.out.println(googleIdToken.getPayload().getEmail());

        return googleIdToken.getPayload().getEmail();
    }

    private GoogleIdTokenVerifier createVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }
}
