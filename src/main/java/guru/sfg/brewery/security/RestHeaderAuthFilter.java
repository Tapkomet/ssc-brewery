package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tapkomet on 8/18/2020
 */

@Slf4j
public class RestHeaderAuthFilter extends AuthFilter {


    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    String getUsername(HttpServletRequest request) {
        return request.getHeader("Api-Key");
    }

    String getPassword(HttpServletRequest request) {
        return request.getHeader("Api-Secret");
    }
}
