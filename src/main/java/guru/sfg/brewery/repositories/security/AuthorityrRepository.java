package guru.sfg.brewery.repositories.security;


import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tapkomet on 8/19/2020
 */
public interface AuthorityrRepository extends JpaRepository<Authority, Integer> {
}
