package spring.security;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import spring.security.entity.Authority;
import spring.security.entity.AuthorityRepository;
import spring.security.entity.User;
import spring.security.entity.UserRepository;

import javax.sql.DataSource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JdbcUserDetailsManagerTest extends TestControllerConfig {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @BeforeEach
    void beforeEach() {
        authorityRepository.save(new Authority("john", "write"));
        userRepository.save(new User("john", "12345", 1));
    }

    @Test
    void test() throws Exception {
        mockMvc.perform(get("/hello")
                        .with(httpBasic("john", "12345")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserDetailsService userDetailsService(DataSource dataSource) {
            String userByUsernameQuery =
                    "select username, password, enabled from user where username = ?";
            String authByUsernameQuery =
                    "select username, authority from authority where username = ?";
            var userDetailsManager = new JdbcUserDetailsManager(dataSource);
            userDetailsManager.setUsersByUsernameQuery(userByUsernameQuery);
            userDetailsManager.setAuthoritiesByUsernameQuery(authByUsernameQuery);
            return userDetailsManager;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }
    }
}
