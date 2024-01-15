package spring.security;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CustomAuthenticationProviderTest extends TestControllerConfig {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        .with(httpBasic("john", "12345")))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @Test
    void test_401_UsernameNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        .with(httpBasic("johnaa", "12345")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void test_401_BadCredentialException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello")
                        .with(httpBasic("john", "123456")))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        UserDetailsService userDetailsService() {
            var userDetailsService = new InMemoryUserDetailsManager();
            var user = User.withUsername("john")
                    .password("12345")
//                    .roles("USER") // same with follow configuration
                    .authorities("ROLE_USER")
                    .build();
            userDetailsService.createUser(user);
            return userDetailsService;
        }

        @Bean
        AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
            var authenticationProvider = new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
            return new ProviderManager(authenticationProvider);
        }
    }

    @RequiredArgsConstructor
    static class CustomAuthenticationProvider implements AuthenticationProvider {
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Error in authentication");
            }
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
