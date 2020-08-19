package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tapkomet on 8/19/2020
 */

@Slf4j
public class RestParamsAuthFilter extends AuthFilter {


    public RestParamsAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    String getUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
