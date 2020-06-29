package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/user/test")

    public String test(){
        return "test";
    }

    @GetMapping("/admin/test2")

    public String test2(){
        return "test2";
    }

}
