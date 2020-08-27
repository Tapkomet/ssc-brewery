package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Tapkomet on 8/17/2020
 */
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;


    @Test
    void listBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void getBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tests where a beer is accessed by id")
    @Nested
    class BeerWithIdTests {

        public Beer randomIdBeer() {
            Random random = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Deleted beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(10)
                    .quantityToBrew(200)
                    .upc(String.valueOf(random.nextInt(9999999)))
                    .build());
        }

        @Test
        void getBeerById() throws Exception {
            mockMvc.perform(get("/api/v1/beer/" + randomIdBeer().getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerWithHeaders() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .header("Api-Key", "admin").header("Api-Secret", "admin"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerWithParams() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .param("username", "admin").param("password", "admin"))
                    .andExpect(status().isOk());
        }


        @Test
        void deleteBeerBadCredsParams() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .param("username", "admin").param("password", "falsePassword"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .with(httpBasic("admin", "admin")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .with(httpBasic("user", "user")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId())
                    .with(httpBasic("customer", "customer")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + randomIdBeer().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }
}