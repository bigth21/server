package spring.securitysmallsafeapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class ProjectConfigTest {

    @Test
    void encode() {
        var encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("12345");
        System.out.println("encoded = " + encoded);
    }

}