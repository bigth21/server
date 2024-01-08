package com.example.server.web.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @GetMapping
    public String home(@ModelAttribute TestModel testModel) {
        testModel.setName("MeMeMe");
        return "home";
    }

    @Getter
    @Setter
    public static class TestModel {
        private String name;
        private String desc;

        public TestModel(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }
}
