package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestParamsAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Tapkomet on 8/17/2020
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public RestParamsAuthFilter restParamsAuthFilter(AuthenticationManager authenticationManager) {
        RestParamsAuthFilter filter = new RestParamsAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restParamsAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers/find", "/beers*", "/beers/*").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER");
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
