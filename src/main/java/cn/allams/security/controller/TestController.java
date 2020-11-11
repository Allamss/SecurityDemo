package cn.allams.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/echo")
    public String echo() {
        return "welcome on";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
