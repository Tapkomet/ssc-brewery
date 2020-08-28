/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityrRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String EXAMPLE_DISTRIBUTOR_1 = "Something-something Distributor 1";
    public static final String EXAMPLE_DISTRIBUTOR_2 = "Sample Distributor 2";
    public static final String EXAMPLE_DISTRIBUTOR_3 = "Example Distributor 3";

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final AuthorityrRepository authorityRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        if (authorityRepository.count() == 0) {
            loadAuthoritiesAndUserData();
        }
        loadBreweryData();
        loadCustomerData();
    }

    private void loadCustomerData() {

        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();

        //create customers
        Customer exampleCustomer1 = customerRepository.save(Customer.builder()
                .customerName(EXAMPLE_DISTRIBUTOR_1)
                .apiKey(UUID.randomUUID())
                .build());

        Customer exampleCustomer2 = customerRepository.save(Customer.builder()
                .customerName(EXAMPLE_DISTRIBUTOR_2)
                .apiKey(UUID.randomUUID())
                .build());

        Customer exampleCustomer3 = customerRepository.save(Customer.builder()
                .customerName(EXAMPLE_DISTRIBUTOR_3)
                .apiKey(UUID.randomUUID())
                .build());

        //create users
        User exampleUser1 = userRepository.save(User.builder().username("example1")
                .password(passwordEncoder.encode("password"))
                .customer(exampleCustomer1)
                .role(customerRole).build());

        User exampleUser2 = userRepository.save(User.builder().username("example2")
                .password(passwordEncoder.encode("password"))
                .customer(exampleCustomer2)
                .role(customerRole).build());

        User exampleUser3 = userRepository.save(User.builder().username("example3")
                .password(passwordEncoder.encode("password"))
                .customer(exampleCustomer3)
                .role(customerRole).build());

        //create orders
        createOrder(exampleCustomer1);
        createOrder(exampleCustomer2);
        createOrder(exampleCustomer3);
    }

    private BeerOrder createOrder(Customer customer) {
        return beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }


    private void loadAuthoritiesAndUserData() {
        // beer authorities
        Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());

        // brewery authorities
        Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());

        // customer authorities
        Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());

        // beer order authorities
        Authority createBeerOrder = authorityRepository.save(Authority.builder().permission("beerOrder.create").build());
        Authority readBeerOrder = authorityRepository.save(Authority.builder().permission("beerOrder.read").build());
        Authority updateBeerOrder = authorityRepository.save(Authority.builder().permission("beerOrder.update").build());
        Authority deleteBeerOrder = authorityRepository.save(Authority.builder().permission("beerOrder.delete").build());

        // beer order authorities for customer role specifically
        Authority createBeerOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.beerOrder.create").build());
        Authority readBeerOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.beerOrder.read").build());
        Authority updateBeerOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.beerOrder.update").build());
        Authority deleteBeerOrderCustomer = authorityRepository.save(Authority.builder().permission("customer.beerOrder.delete").build());

        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, readBeer, updateBeer, deleteBeer,
                createBrewery, readBrewery, updateBrewery, deleteBrewery,
                createCustomer, readCustomer, updateCustomer, deleteCustomer,
                createBeerOrder, readBeerOrder, updateBeerOrder, deleteBeerOrder)));

        customerRole.setAuthorities(new HashSet<>(Set.of(readBeer, readBrewery, readCustomer, createBeerOrderCustomer,
                readBeerOrderCustomer, updateBeerOrderCustomer, deleteBeerOrderCustomer)));

        userRole.setAuthorities(new HashSet<>(Set.of(readBeer)));

        roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("customer")
                .password(passwordEncoder.encode("customer"))
                .role(customerRole)
                .build());


        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(adminRole)
                .build());

    }
}
