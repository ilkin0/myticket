package com.ilkinmehdiyev.myticket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

  @GetMapping("/auth")
  public String hello() {
    return "Hello World";
  }

  @GetMapping("/secure")
  public String helloSecured() {
    return "Hello Secured World";
  }
}
