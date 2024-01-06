package spring.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BasicAuthTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    SecurityProperties securityProperties;

    @Test
    void test() throws Exception {
        mockMvc.perform(get("/test")
                        .with(httpBasic("user", securityProperties.getUser().getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("test"))
                .andDo(print());
    }

    @Test
    void test_401() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @TestConfiguration
    static class TestConfig {
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
