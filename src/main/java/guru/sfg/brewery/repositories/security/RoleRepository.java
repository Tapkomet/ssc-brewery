package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Tapkomet on 8/28/2020
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
