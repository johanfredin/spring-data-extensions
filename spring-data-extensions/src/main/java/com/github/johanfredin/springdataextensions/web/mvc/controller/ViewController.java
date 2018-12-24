package com.github.johanfredin.springdataextensions.web.mvc.controller;


import com.github.johanfredin.springdataextensions.web.MvcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping(value = "/")
    public String index() {
        return MvcUtils.redirectTo("login");
    }

}
