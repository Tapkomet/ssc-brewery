package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * Created by Tapkomet on 8/18/2020
 */
public class PasswordEncodingTest {

    static final String password = "password";

    @Test
    void bcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder();

        System.out.println(bcrypt.encode("user"));
        System.out.println(bcrypt.encode(password));
    }

    @Test
    void noOp() {
        PasswordEncoder noop = NoOpPasswordEncoder.getInstance();

        System.out.println(noop.encode(password));
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(password.getBytes()));

        String salt = "salt";
        System.out.println(DigestUtils.md5DigestAsHex((password + salt).getBytes()));
    }

}
