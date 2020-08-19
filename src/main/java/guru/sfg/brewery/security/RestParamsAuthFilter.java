package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Tapkomet on 8/18/2020
 */

@Slf4j
public class RestParamsAuthFilter extends AuthFilter {


    public RestParamsAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    String getUsername(HttpServletRequest request) {
        System.out.println(request.getParameter("username"));
        return request.getParameter("username");
    }

    String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
