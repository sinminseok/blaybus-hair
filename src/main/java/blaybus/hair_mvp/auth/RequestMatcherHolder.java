package blaybus.hair_mvp.auth;

import static org.springframework.http.HttpMethod.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import blaybus.hair_mvp.domain.user.entity.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;


import jakarta.annotation.Nullable;

@Component
public class RequestMatcherHolder {

    private static final List<RequestInfo> REQUEST_INFO_LIST = List.of(
            //회원가입, 로그인
            new RequestInfo(GET, "/auth/*", null),
            new RequestInfo(POST, "/auth/*", null),
            // admin (관리자)
            new RequestInfo(POST, "/clients/*", Role.CLIENT),
            new RequestInfo(GET, "/clients/*", Role.CLIENT),
            // owner (가게 사장)
            new RequestInfo(POST, "/designers/*", Role.DESIGNER),
            new RequestInfo(GET, "/designers/*", Role.DESIGNER),
            // 카카오페이
            new RequestInfo(POST,"/online/v1/payment/*",null),
            new RequestInfo(GET,"/online/v1/payment/*",null)

    );

    private final ConcurrentHashMap<String, RequestMatcher> reqMatcherCacheMap = new ConcurrentHashMap<>();

    /**
     * if role == null, return permitAll Path
     */
    public RequestMatcher getRequestMatchersByMinRole(@Nullable Role minRole) {
        var key = getKeyByRole(minRole);
        if (!reqMatcherCacheMap.containsKey(key)) {
            var requestMatcherByMinRole = new OrRequestMatcher(REQUEST_INFO_LIST.stream()
                    .filter(reqInfo -> reqInfo.minRole() == null || reqInfo.minRole().equals(minRole))
                    .map(reqInfo -> new AntPathRequestMatcher(reqInfo.pattern(), reqInfo.method().name()))
                    .toArray(AntPathRequestMatcher[]::new));
            reqMatcherCacheMap.put(key, requestMatcherByMinRole);
        }
        return reqMatcherCacheMap.get(key);
    }

    private String getKeyByRole(@Nullable Role minRole) {
        if (minRole == null) {
            return "VISITOR";
        }
        return minRole.name();
    }

    private record RequestInfo(HttpMethod method, String pattern, Role minRole) {
    }
}