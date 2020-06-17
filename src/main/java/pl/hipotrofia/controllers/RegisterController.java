package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {


    @PostMapping("/api/registration")
    public boolean registerUser(){

        boolean result;

        try {

            result = true;
        } catch (Exception e) {

            result = false;
        }

        return result;
    }


}
