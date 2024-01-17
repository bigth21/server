package spring.securitybasic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BasicAuthTest extends TestControllerConfig {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    SecurityProperties securityProperties;

    @Test
    void test() throws Exception {
        mockMvc.perform(get("/hello")
                        .with(httpBasic("user", securityProperties.getUser().getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"))
                .andDo(print());
    }

    @Test
    void test_401() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
