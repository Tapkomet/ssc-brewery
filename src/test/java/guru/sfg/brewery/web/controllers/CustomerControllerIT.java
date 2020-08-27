package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Tapkomet on 8/27/2020
 */
@SpringBootTest
public class CustomerControllerIT extends BaseIT {

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("guru.sfg.brewery.web.controllers.CustomerControllerIT#getStreamAdminCustomer")
    void listCustomers(String user, String password) throws Exception {
        mockMvc.perform(get("/customers")
                .with(httpBasic(user, password)))
                .andExpect(status().isOk());
    }

    @Test
    void listCustomersIncorrectUserAuth() throws Exception {
        mockMvc.perform(get("/customers")
                .with(httpBasic(USER, USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void listCustomersNoAuth() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
    }
}
