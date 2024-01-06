package com.example.server;

import com.example.server.web.HelloController;
import com.example.server.web.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HelloController.class)
class HelloControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    SecurityProperties securityProperties;

    @Test
    void hello() throws Exception {
        mockMvc.perform(get("/api/v1/hello")
                        .with(httpBasic("user", securityProperties.getUser().getPassword())))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello!"))
                .andDo(print());
    }

    @Test
    void hello_401() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}