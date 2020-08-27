package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    @DisplayName("Beer listing tests")
    @Nested
    class ListBeersTests {

        @Test
        void listBeersNoAuth() throws Exception {
            mockMvc.perform(get("/api/v1/beer"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void listBeers(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beer")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }


    @DisplayName("Get Beer By ID")
    @Nested
    class GetBeerById {
        @Test
        void findBeerById() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void findBeerById(String user, String password) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                    .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Find By UPC")
    class FindByUPC {
        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamAllUsers")
        void findBeerByUpc(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                    .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Beer deletion tests")
    @Nested
    class DeleteBeerTests {

        public Beer beerToDelete() {
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
        void deleteBeerWithHeaders() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .header("Api-Key", ADMIN).header("Api-Secret", ADMIN))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerWithParams() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("username", ADMIN).param("password", ADMIN))
                    .andExpect(status().isOk());
        }


        @Test
        void deleteBeerBadCredsParams() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .param("username", "admin").param("password", "falsePassword"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerHttpAdmin() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic(ADMIN, ADMIN)))
                    .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getStreamNotAdmin")
        void deleteBeerHttpNotAdmin(String user, String password) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic(user, password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }
}