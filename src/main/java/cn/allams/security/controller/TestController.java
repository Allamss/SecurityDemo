package cn.allams.security.controller;

import cn.allams.security.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/sale")
    @Secured({"ROLE_sale", "ROLE_manager"})
    public String sale() {
        return "sale";
    }

    @GetMapping("/system")
    @PreAuthorize("hasAnyAuthority('system')")
    public String system() {
        return "system";
    }

    @GetMapping("/see")
    @PostAuthorize("hasAnyAuthority('nobody')")
    public String see() {
        System.out.println("truly in but no return");
        return "see";
    }

    @GetMapping("/filterResult")
    @Secured("ROLE_sale")
    @PostFilter("filterObject.username == 'pass'")
    public List<Users> filterResult() {
        List<Users> list = new ArrayList<>();
        list.add(new Users(11, "pass", "123"));
        list.add(new Users(12, "noPass", "123"));
        return list;
    }

}
