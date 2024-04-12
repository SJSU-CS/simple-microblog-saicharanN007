package edu.sjsu.cmpe272.simpleblog.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

public class Ruokcontroller {

    @RestController
    public static class HelloController {

        @GetMapping("/ruok")
        public String hello() {
            return "Hello";
        }
    }
}
