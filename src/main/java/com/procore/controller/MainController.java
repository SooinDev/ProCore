package com.procore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String home() {
    return "index";
  }

  @GetMapping("/main")
  public String main() {
    return "index";
  }
}