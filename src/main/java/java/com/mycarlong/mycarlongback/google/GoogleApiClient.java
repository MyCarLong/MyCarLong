package java.com.mycarlong.mycarlongback.google;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GoogleApiClient {
    @PostExchange(url = "https://oauth2.googleapis.com/token")
    GoogleToken fetchToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange("https://www.googleapis.com/userinfo/v2/me")
    GoogleMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
