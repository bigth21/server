package spring.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InMemoryUserDetailsTest.TestController.class)
class InMemoryUserDetailsTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .with(httpBasic("user", "12345")))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .httpBasic(withDefaults());
            http
                    .authorizeHttpRequests(c -> c
                            .requestMatchers("/test").hasRole("USER")
                            .anyRequest().authenticated());
            return http.build();
        }

        @Bean
        UserDetailsService userDetailsService() {
            var userDetailsService = new InMemoryUserDetailsManager();
            var user = User.withUsername("user")
                    .password("12345")
//                    .roles("USER") // same with follow configuration
                    .authorities("ROLE_USER")
                    .build();
            userDetailsService.createUser(user);
            return userDetailsService;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {
        @GetMapping("/test")
        public String test() {
            return "test";
        }
    }

}