package com.procore.controller;

import com.procore.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

  @Autowired
  private MemberService memberService;

  // 회원가입 페이지 보여주기
  @GetMapping("/member/register")
  public String registerForm() {
    return "register";  // JSP 파일명
  }

  // 회원가입 처리
  @PostMapping("/member/register")
  public String register() {
    // 처리 로직
    return "register";
  }
}
