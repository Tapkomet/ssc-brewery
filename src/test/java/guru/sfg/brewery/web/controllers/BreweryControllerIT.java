
package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Tapkomet on 8/25/2020
 */

@SpringBootTest
class BreweryControllerIT extends BaseIT {

    @Test
    void listBreweriesCUSTOMER() throws Exception {
        mockMvc.perform(get("/breweries")
                .with(httpBasic("customer", "customer")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesADMIN() throws Exception {
        mockMvc.perform(get("/breweries")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesUSER() throws Exception {
        mockMvc.perform(get("/breweries")
                .with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesNOAUTH() throws Exception {
        mockMvc.perform(get("/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getBreweriesJsonCUSTOMER() throws Exception {
        mockMvc.perform(get("/api/v1/breweries")
                .with(httpBasic("customer", "customer")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonADMIN() throws Exception {
        mockMvc.perform(get("/api/v1/breweries")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonUSER() throws Exception {
        mockMvc.perform(get("/api/v1/breweries")
                .with(httpBasic("user", "user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonNOAUTH() throws Exception {
        mockMvc.perform(get("/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }
}