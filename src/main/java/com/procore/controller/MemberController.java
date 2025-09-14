package com.procore.controller;

import com.procore.service.MemberService;
import com.procore.util.PasswordSecurityUtil;
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

  @Autowired
  private PasswordSecurityUtil passwordSecurityUtil;

  // 회원가입 페이지 보여주기
  @GetMapping("/member/register")
  public String registerForm() {
    return "member/register";
  }

  // 회원가입 처리 (강화된 보안 검증 포함)
  @PostMapping("/member/register")
  public String register(MemberVO memberVO, Model model) {
    System.out.println("=== 강화된 보안 회원가입 요청 수신 ===");
    System.out.println("받은 회원정보: " + memberVO);

    try {
      // 1. 입력값 기본 검증
      if (!isValidMemberInput(memberVO, model)) {
        return "member/register";
      }

      // 2. 비밀번호 보안 강화 검증
      PasswordSecurityUtil.PasswordValidationResult passwordValidation =
              passwordSecurityUtil.validatePassword(memberVO.getPassword());

      if (!passwordValidation.isValid()) {
        System.out.println("회원가입 실패 - 비밀번호 보안: " + passwordValidation.getMessage());
        model.addAttribute("error", passwordValidation.getMessage());
        return "member/register";
      }

      // 3. Status 기본값 설정
      memberVO.setStatus(MemberStatus.ACTIVE);

      // 4. 서비스 호출하여 회원가입 처리
      boolean success = memberService.registerMember(memberVO);

      if (success) {
        System.out.println("강화된 보안 회원가입 성공: " + memberVO.getEmail());
        model.addAttribute("message", "회원가입이 완료되었습니다! 강력한 보안으로 계정이 보호됩니다.");
        return "member/login";
      } else {
        System.out.println("회원가입 실패: " + memberVO.getEmail());
        model.addAttribute("error", "회원가입에 실패했습니다. 입력 정보를 다시 확인해주세요.");
        return "member/register";
      }

    } catch (Exception e) {
      System.err.println("회원가입 처리 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("error", "회원가입 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
      return "member/register";
    }
  }

  // 비밀번호 강도 평가 API (AJAX용)
  @PostMapping("/member/check-password-strength")
  @ResponseBody
  public Map<String, Object> checkPasswordStrength(@RequestParam String password) {
    System.out.println("=== 비밀번호 강도 평가 API 요청 ===");

    Map<String, Object> result = new HashMap<>();

    try {
      if (password == null || password.isEmpty()) {
        result.put("score", 0);
        result.put("level", "none");
        result.put("message", "비밀번호를 입력하세요");
        return result;
      }

      PasswordSecurityUtil.PasswordStrength strength =
              passwordSecurityUtil.evaluatePasswordStrength(password);

      result.put("score", strength.getScore());
      result.put("level", strength.getLevel().getValue());
      result.put("message", strength.getMessage());
      result.put("isValid", strength.getScore() >= 3); // 보통 이상이어야 유효

      System.out.println("비밀번호 강도 평가 결과: " + strength.getMessage() + " (" + strength.getScore() + "/5)");

    } catch (Exception e) {
      System.err.println("비밀번호 강도 평가 중 예외: " + e.getMessage());
      result.put("score", 0);
      result.put("level", "error");
      result.put("message", "강도 평가 중 오류가 발생했습니다");
      result.put("isValid", false);
    }

    return result;
  }

  // 비밀번호 유효성 검사 API (AJAX용)
  @PostMapping("/member/validate-password")
  @ResponseBody
  public Map<String, Object> validatePassword(@RequestParam String password) {
    System.out.println("=== 비밀번호 유효성 검사 API 요청 ===");

    Map<String, Object> result = new HashMap<>();

    try {
      PasswordSecurityUtil.PasswordValidationResult validation =
              passwordSecurityUtil.validatePassword(password);

      result.put("valid", validation.isValid());
      result.put("message", validation.getMessage());

      if (validation.isValid()) {
        PasswordSecurityUtil.PasswordStrength strength =
                passwordSecurityUtil.evaluatePasswordStrength(password);

        Map<String, Object> strengthMap = new HashMap<>();
        strengthMap.put("score", strength.getScore());
        strengthMap.put("level", strength.getLevel().getValue());
        strengthMap.put("message", strength.getMessage());

        result.put("strength", strengthMap);
      }

      System.out.println("비밀번호 유효성 검사 결과: " + (validation.isValid() ? "유효" : "무효"));

    } catch (Exception e) {
      System.err.println("비밀번호 유효성 검사 중 예외: " + e.getMessage());
      result.put("valid", false);
      result.put("message", "유효성 검사 중 오류가 발생했습니다");
    }

    return result;
  }

  // 이메일 중복 확인 API
  @PostMapping("/member/check-email")
  @ResponseBody
  public Map<String, Object> checkEmailDuplicate(@RequestParam String email) {
    System.out.println("=== 이메일 중복확인 API 요청 ===");
    System.out.println("받은 이메일: [" + email + "]");

    Map<String, Object> result = new HashMap<>();

    try {
      if (email == null || email.trim().isEmpty()) {
        result.put("success", false);
        result.put("message", "이메일을 입력해주세요.");
        return result;
      }

      String trimmedEmail = email.trim();

      if (!isValidEmailFormat(trimmedEmail)) {
        result.put("success", false);
        result.put("message", "올바른 이메일 형식을 입력해주세요.");
        return result;
      }

      boolean isAvailable = memberService.checkDuplicateEmail(trimmedEmail);

      if (isAvailable) {
        result.put("success", true);
        result.put("message", "사용 가능한 이메일입니다.");
      } else {
        result.put("success", false);
        result.put("message", "이미 사용 중인 이메일입니다.");
      }

    } catch (Exception e) {
      System.err.println("이메일 중복확인 API 처리 중 예외: " + e.getMessage());
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
      if (nickname == null || nickname.trim().isEmpty()) {
        result.put("success", false);
        result.put("message", "닉네임을 입력해주세요.");
        return result;
      }

      String trimmedNickname = nickname.trim();

      if (trimmedNickname.length() < 2 || trimmedNickname.length() > 10) {
        result.put("success", false);
        result.put("message", "닉네임은 2-10자 사이여야 합니다.");
        return result;
      }

      if (!trimmedNickname.matches("^[a-zA-Z0-9가-힣_]+$")) {
        result.put("success", false);
        result.put("message", "닉네임은 영문, 숫자, 한글, 언더스코어만 사용 가능합니다.");
        return result;
      }

      boolean isAvailable = memberService.checkDuplicateNickname(trimmedNickname);

      if (isAvailable) {
        result.put("success", true);
        result.put("message", "사용 가능한 닉네임입니다.");
      } else {
        result.put("success", false);
        result.put("message", "이미 사용 중인 닉네임입니다.");
      }

    } catch (Exception e) {
      System.err.println("닉네임 중복확인 API 처리 중 예외: " + e.getMessage());
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

  // 로그인 처리 (강화된 보안 검증)
  @PostMapping("/member/login")
  public String login(@RequestParam String email,
                      @RequestParam String password,
                      Model model) {
    System.out.println("=== 강화된 보안 로그인 요청 수신 ===");
    System.out.println("이메일: " + email);

    try {
      if (email == null || email.trim().isEmpty() ||
              password == null || password.trim().isEmpty()) {
        model.addAttribute("error", "이메일과 비밀번호를 모두 입력해주세요.");
        return "member/login";
      }

      MemberVO member = memberService.login(email.trim(), password);

      if (member != null) {
        System.out.println("강화된 보안 로그인 성공: " + member.getEmail());
        // TODO: 세션에 사용자 정보 저장
        return "redirect:/";
      } else {
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

  // 비밀번호 변경 페이지 (새로운 기능)
  @GetMapping("/member/change-password")
  public String changePasswordForm() {
    return "member/change-password";
  }

  // 비밀번호 변경 처리 (새로운 기능)
  @PostMapping("/member/change-password")
  public String changePassword(@RequestParam String currentPassword,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               Model model) {
    System.out.println("=== 비밀번호 변경 요청 수신 ===");

    try {
      // 1. 기본 입력값 검증
      if (currentPassword == null || newPassword == null || confirmPassword == null ||
              currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
        model.addAttribute("error", "모든 필드를 입력해주세요.");
        return "member/change-password";
      }

      // 2. 새 비밀번호 확인
      if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("error", "새 비밀번호가 일치하지 않습니다.");
        return "member/change-password";
      }

      // 3. 새 비밀번호 보안 검증
      PasswordSecurityUtil.PasswordValidationResult validation =
              passwordSecurityUtil.validatePassword(newPassword);

      if (!validation.isValid()) {
        model.addAttribute("error", validation.getMessage());
        return "member/change-password";
      }

      // TODO: 실제 구현시 세션에서 사용자 ID 가져와서 처리
      // boolean success = memberService.changePassword(memberId, currentPassword, newPassword);

      model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
      return "member/change-password";

    } catch (Exception e) {
      System.err.println("비밀번호 변경 중 예외 발생: " + e.getMessage());
      model.addAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
      return "member/change-password";
    }
  }

  // === Private Helper Methods ===

  /**
   * 회원가입 입력값 검증 (강화된 비밀번호 검증 제외)
   */
  private boolean isValidMemberInput(MemberVO memberVO, Model model) {
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

    if (!isValidEmailFormat(memberVO.getEmail().trim())) {
      model.addAttribute("error", "올바른 이메일 형식을 입력해주세요.");
      return false;
    }

    String nickname = memberVO.getNickname().trim();
    if (nickname.length() < 2 || nickname.length() > 10) {
      model.addAttribute("error", "닉네임은 2-10자 사이여야 합니다.");
      return false;
    }

    if (!nickname.matches("^[a-zA-Z0-9가-힣_]+$")) {
      model.addAttribute("error", "닉네임은 영문, 숫자, 한글, 언더스코어만 사용 가능합니다.");
      return false;
    }

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