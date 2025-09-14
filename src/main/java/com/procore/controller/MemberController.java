package com.procore.controller;

import com.procore.service.MemberService;
import com.procore.vo.MemberStatus;
import com.procore.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberController {

  @Autowired
  private MemberService memberService;

  // 회원가입 페이지 보여주기
  @GetMapping("/member/register")
  public String registerForm() {
    return "member/register";
  }

  // 회원가입 처리
  @PostMapping("/member/register")
  public String register(MemberVO memberVO, Model model) {
    System.out.println("=== 회원가입 요청 수신 ===");
    System.out.println("받은 회원정보: " + memberVO);

    try {
      // 1. 입력값 기본 검증
      if (!isValidMemberInput(memberVO, model)) {
        return "member/register";
      }

      // 2. Status 기본값 설정 (명시적으로 ACTIVE 설정)
      memberVO.setStatus(MemberStatus.ACTIVE);

      // 3. 서비스 호출하여 회원가입 처리
      boolean success = memberService.registerMember(memberVO);

      if (success) {
        // 성공 시 - 로그인 페이지로 이동
        System.out.println("회원가입 성공: " + memberVO.getEmail());
        model.addAttribute("message", "회원가입이 완료되었습니다! 로그인해주세요.");
        return "member/login";
      } else {
        // 실패 시 - 중복 등의 이유
        System.out.println("회원가입 실패: " + memberVO.getEmail());
        model.addAttribute("error", "이미 사용 중인 이메일, 닉네임 또는 전화번호입니다.");
        return "member/register";
      }

    } catch (Exception e) {
      // 예외 발생 시
      System.err.println("회원가입 처리 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
      return "member/register";
    }
  }

  // 이메일 중복 확인 API
  @PostMapping("/member/check-email")
  @ResponseBody
  public Map<String, Object> checkEmailDuplicate(@RequestParam String email) {
    System.out.println("=== 이메일 중복확인 API 요청 ===");
    System.out.println("받은 이메일: [" + email + "]");

    Map<String, Object> result = new HashMap<>();

    try {
      // 1. 입력값 기본 검증
      if (email == null || email.trim().isEmpty()) {
        result.put("success", false);
        result.put("message", "이메일을 입력해주세요.");
        return result;
      }

      String trimmedEmail = email.trim();

      // 2. 이메일 형식 검증
      if (!isValidEmailFormat(trimmedEmail)) {
        result.put("success", false);
        result.put("message", "올바른 이메일 형식을 입력해주세요.");
        return result;
      }

      // 3. 중복 확인 서비스 호출
      boolean isAvailable = memberService.checkDuplicateEmail(trimmedEmail);
      System.out.println("서비스 결과: " + isAvailable);

      if (isAvailable) {
        // 사용 가능
        result.put("success", true);
        result.put("message", "사용 가능한 이메일입니다.");
        System.out.println("API 응답: 사용 가능");
      } else {
        // 중복됨
        result.put("success", false);
        result.put("message", "이미 사용 중인 이메일입니다.");
        System.out.println("API 응답: 중복됨");
      }

    } catch (Exception e) {
      System.err.println("이메일 중복확인 API 처리 중 예외: " + e.getMessage());
      e.printStackTrace();
      result.put("success", false);
      result.put("message", "중복확인 중 오류가 발생했습니다. 다시 시도해주세요.");
    }

    return result;
  }

  // 닉네임 중복 확인 API
  @PostMapping("/member/check-nickname")
  @ResponseBody
  public Map<String, Object> checkNicknameDuplicate(@RequestParam String nickname) {
    System.out.println("=== 닉네임 중복확인 API 요청 ===");
    System.out.println("받은 닉네임: [" + nickname + "]");

    Map<String, Object> result = new HashMap<>();

    try {
      // 1. 입력값 기본 검증
      if (nickname == null || nickname.trim().isEmpty()) {
        result.put("success", false);
        result.put("message", "닉네임을 입력해주세요.");
        return result;
      }

      String trimmedNickname = nickname.trim();

      // 2. 닉네임 길이 검증
      if (trimmedNickname.length() < 2 || trimmedNickname.length() > 10) {
        result.put("success", false);
        result.put("message", "닉네임은 2-10자 사이여야 합니다.");
        return result;
      }

      // 3. 닉네임 형식 검증
      if (!trimmedNickname.matches("^[a-zA-Z0-9가-힣_]+$")) {
        result.put("success", false);
        result.put("message", "닉네임은 영문, 숫자, 한글, 언더스코어만 사용 가능합니다.");
        return result;
      }

      // 4. 중복 확인 서비스 호출
      boolean isAvailable = memberService.checkDuplicateNickname(trimmedNickname);
      System.out.println("서비스 결과: " + isAvailable);

      if (isAvailable) {
        // 사용 가능
        result.put("success", true);
        result.put("message", "사용 가능한 닉네임입니다.");
        System.out.println("API 응답: 사용 가능");
      } else {
        // 중복됨
        result.put("success", false);
        result.put("message", "이미 사용 중인 닉네임입니다.");
        System.out.println("API 응답: 중복됨");
      }

    } catch (Exception e) {
      System.err.println("닉네임 중복확인 API 처리 중 예외: " + e.getMessage());
      e.printStackTrace();
      result.put("success", false);
      result.put("message", "중복확인 중 오류가 발생했습니다. 다시 시도해주세요.");
    }

    return result;
  }

  // 로그인 페이지
  @GetMapping("/member/login")
  public String loginForm() {
    return "member/login";
  }

  // 로그인 처리
  @PostMapping("/member/login")
  public String login(@RequestParam String email,
                      @RequestParam String password,
                      Model model) {
    System.out.println("=== 로그인 요청 수신 ===");
    System.out.println("이메일: " + email);

    try {
      // 입력값 검증
      if (email == null || email.trim().isEmpty() ||
              password == null || password.trim().isEmpty()) {
        model.addAttribute("error", "이메일과 비밀번호를 모두 입력해주세요.");
        return "member/login";
      }

      // 로그인 시도
      MemberVO member = memberService.login(email.trim(), password);

      if (member != null) {
        // 로그인 성공
        System.out.println("로그인 성공: " + member.getEmail());
        // TODO: 세션에 사용자 정보 저장
        return "redirect:/"; // 메인 페이지로 리다이렉트
      } else {
        // 로그인 실패
        System.out.println("로그인 실패: " + email);
        model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
        return "member/login";
      }

    } catch (Exception e) {
      System.err.println("로그인 처리 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("error", "로그인 처리 중 오류가 발생했습니다.");
      return "member/login";
    }
  }

  // 테스트용 컨트롤러
  @GetMapping("/test-controller")
  @ResponseBody
  public String testController() {
    System.out.println("테스트 컨트롤러 호출됨!");
    return "컨트롤러 정상 작동중!";
  }

  // === Private Helper Methods ===

  /**
   * 회원가입 입력값 검증
   */
  private boolean isValidMemberInput(MemberVO memberVO, Model model) {
    // 필수 필드 체크
    if (memberVO.getEmail() == null || memberVO.getEmail().trim().isEmpty()) {
      model.addAttribute("error", "이메일을 입력해주세요.");
      return false;
    }

    if (memberVO.getPassword() == null || memberVO.getPassword().trim().isEmpty()) {
      model.addAttribute("error", "비밀번호를 입력해주세요.");
      return false;
    }

    if (memberVO.getNickname() == null || memberVO.getNickname().trim().isEmpty()) {
      model.addAttribute("error", "닉네임을 입력해주세요.");
      return false;
    }

    // 이메일 형식 체크
    if (!isValidEmailFormat(memberVO.getEmail().trim())) {
      model.addAttribute("error", "올바른 이메일 형식을 입력해주세요.");
      return false;
    }

    // 비밀번호 길이 체크
    if (memberVO.getPassword().length() < 8) {
      model.addAttribute("error", "비밀번호는 8자 이상이어야 합니다.");
      return false;
    }

    // 닉네임 길이 및 형식 체크
    String nickname = memberVO.getNickname().trim();
    if (nickname.length() < 2 || nickname.length() > 10) {
      model.addAttribute("error", "닉네임은 2-10자 사이여야 합니다.");
      return false;
    }

    if (!nickname.matches("^[a-zA-Z0-9가-힣_]+$")) {
      model.addAttribute("error", "닉네임은 영문, 숫자, 한글, 언더스코어만 사용 가능합니다.");
      return false;
    }

    // 전화번호 형식 체크 (있는 경우만)
    if (memberVO.getPhone() != null && !memberVO.getPhone().trim().isEmpty()) {
      if (!memberVO.getPhone().matches("^\\d{3}-\\d{4}-\\d{4}$")) {
        model.addAttribute("error", "올바른 전화번호 형식을 입력해주세요. (예: 010-1234-5678)");
        return false;
      }
    }

    return true;
  }

  /**
   * 이메일 형식 검증
   */
  private boolean isValidEmailFormat(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    return email.matches(emailRegex);
  }
}