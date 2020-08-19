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

    @Override
    String getUsername(HttpServletRequest request) {
        System.out.println(request.getHeader("Api-Key"));
        return request.getHeader("Api-Key");
    }

    @Override
    String getPassword(HttpServletRequest request) {
        System.out.println(request.getHeader("Api-Secret"));
        return request.getHeader("Api-Secret");
    }
}
