package spring.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomAuthenticationProviderTest.TestController.class)
public class CustomAuthenticationProviderTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .with(httpBasic("john", "12345")))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

    @Test
    void test_401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test")
                        .with(httpBasic("john", "123456")))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        AuthenticationProvider authenticationProvider() {
            return new CustomAuthenticationProvider();
        }

        @Bean
        TestController testController() {
            return new TestController();
        }
    }

    static class CustomAuthenticationProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = authentication.getName();
            String password = String.valueOf(authentication.getCredentials());

            if ("john".equals(username) && "12345".equals(password)) {
                return new UsernamePasswordAuthenticationToken(username, password, List.of());
            } else {
                throw new AuthenticationCredentialsNotFoundException("Error in authentication");
            }
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
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
