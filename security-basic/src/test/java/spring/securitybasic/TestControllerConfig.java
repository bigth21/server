package spring.securitybasic;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class TestControllerConfig {

    @TestConfiguration
    static class TestConfig {
        @Bean
        TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {
        @GetMapping("/hello")
        public String hello() {
            return "hello";
        }

        @GetMapping("/username")
        public String getUsername() {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            return authentication.getName();
        }

        @GetMapping("/print-username")
        public void printUsername() {
            new Thread(() -> {
                SecurityContext context = SecurityContextHolder.getContext();
                String username = context.getAuthentication().getName();
                System.out.println("name = " + username);
            }).start();
        }
    }
}
