package spring.securitybasic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest
public class SecurityContextTest extends TestControllerConfig {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/username")
                        .with(httpBasic("john", "12345")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("john"));
    }

    @Test
    void printUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/print-username")
                        .with(httpBasic("john", "12345")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        InitializingBean initializingBean() {
            return () -> SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        }

        @Bean
        UserDetailsService userDetailsService() {
            var userDetailsManager = new InMemoryUserDetailsManager();
            userDetailsManager.createUser(User.withUsername("john")
                    .password("12345")
                    .roles("USER")
                    .build());
            return userDetailsManager;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }
    }
}

