package guru.sfg.brewery.security.perms.customerPerms;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tapkomet on 8/28/2020
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('customer.create')")
public @interface CustomerCreatePermission {
}

