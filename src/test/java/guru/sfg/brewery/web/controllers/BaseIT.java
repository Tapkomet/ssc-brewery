package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Created by Tapkomet on 8/17/2020
 */
public abstract class BaseIT {
    public static final String ADMIN = "admin";
    public static final String CUSTOMER = "customer";
    public static final String USER = "user";
    @Autowired
    WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of(ADMIN, ADMIN),
                Arguments.of(CUSTOMER, CUSTOMER),
                Arguments.of(USER, USER));
    }

    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of(CUSTOMER, CUSTOMER),
                Arguments.of(USER, USER));
    }

    public static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of(CUSTOMER, CUSTOMER),
                Arguments.of(ADMIN, ADMIN));
    }
}