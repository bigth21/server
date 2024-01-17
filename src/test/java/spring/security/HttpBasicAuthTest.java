package spring.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class HttpBasicAuthTest extends TestControllerConfig {
    @Autowired
    MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized())
//                .andExpect(header().string("WWW-Authenticate", "Basic realm=\"OTHER\""))
                .andExpect(header().string("message", "Luke, I am your father")) // Realm-name header disappears because of custom authentication entry point registration
                .andDo(print());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .httpBasic(c -> c
                            .realmName("OTHER")
                            .authenticationEntryPoint(new CustomEntryPoint()))
                    .authorizeHttpRequests(c -> c
                            .anyRequest().authenticated());

            return http.build();
        }
    }

    static class CustomEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.addHeader("message", "Luke, I am your father");
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
