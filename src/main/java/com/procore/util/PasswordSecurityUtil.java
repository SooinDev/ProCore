package com.procore.util;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class PasswordSecurityUtil {

  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MAX_PASSWORD_LENGTH = 128;
  private static final int SALT_LENGTH = 32;

  // 일반적인 비밀번호 패턴들
  private static final Set<String> COMMON_PASSWORDS = new HashSet<>(Arrays.asList(
          "password", "12345678", "qwerty123", "abc12345", "password123",
          "1q2w3e4r", "admin123", "welcome123", "letmein123", "monkey123",
          "dragon123", "111111", "123123", "sunshine", "master123",
          "shadow123", "ashley123", "football123", "jesus123", "michael123",
          "ninja123", "mustang123", "password1", "123456789", "princess123"
  ));

  // 정규표현식 패턴들
  private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
  private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
  private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
  private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
  private static final Pattern SEQUENTIAL_PATTERN = Pattern.compile("(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz|123|234|345|456|567|678|789|890)");
  private static final Pattern BIRTH_DATE_PATTERN = Pattern.compile("\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])");
  private static final Pattern YEAR_PATTERN = Pattern.compile("(19|20)\\d{2}");

  /**
   * 비밀번호 강도를 평가합니다.
   * @param password 평가할 비밀번호
   * @return PasswordStrength 객체
   */
  public PasswordStrength evaluatePasswordStrength(String password) {
    if (password == null || password.isEmpty()) {
      return new PasswordStrength(0, StrengthLevel.NONE, "비밀번호를 입력하세요");
    }

    int score = 0;
    StringBuilder feedback = new StringBuilder();

    // 기본 요구사항 체크
    boolean hasMinLength = password.length() >= MIN_PASSWORD_LENGTH;
    boolean hasLowercase = LOWERCASE_PATTERN.matcher(password).find();
    boolean hasUppercase = UPPERCASE_PATTERN.matcher(password).find();
    boolean hasDigit = DIGIT_PATTERN.matcher(password).find();
    boolean hasSpecial = SPECIAL_CHAR_PATTERN.matcher(password).find();
    boolean notCommon = !isCommonPassword(password);

    // 점수 계산
    if (hasMinLength) score++;
    if (hasLowercase) score++;
    if (hasUppercase) score++;
    if (hasDigit) score++;
    if (hasSpecial) score++;
    if (notCommon) score++;

    // 추가 보너스 점수
    if (password.length() >= 12) score++;
    if (password.length() >= 16) score++;
    if (hasGoodEntropy(password)) score++;

    // 점수를 5점 만점으로 정규화
    double normalizedScore = Math.min(5.0, score * 5.0 / 9.0);

    // 레벨 결정
    StrengthLevel level;
    String message;

    if (normalizedScore < 1) {
      level = StrengthLevel.VERY_WEAK;
      message = "매우 약함";
    } else if (normalizedScore < 2) {
      level = StrengthLevel.WEAK;
      message = "약함";
    } else if (normalizedScore < 3) {
      level = StrengthLevel.FAIR;
      message = "보통";
    } else if (normalizedScore < 4) {
      level = StrengthLevel.GOOD;
      message = "좋음";
    } else {
      level = StrengthLevel.EXCELLENT;
      message = "매우 강함";
    }

    return new PasswordStrength(normalizedScore, level, message);
  }

  /**
   * 비밀번호가 유효한지 검증합니다.
   * @param password 검증할 비밀번호
   * @return 유효성 검증 결과
   */
  public PasswordValidationResult validatePassword(String password) {
    if (password == null) {
      return new PasswordValidationResult(false, "비밀번호는 필수입니다.");
    }

    if (password.length() < MIN_PASSWORD_LENGTH) {
      return new PasswordValidationResult(false,
              "비밀번호는 최소 " + MIN_PASSWORD_LENGTH + "자 이상이어야 합니다.");
    }

    if (password.length() > MAX_PASSWORD_LENGTH) {
      return new PasswordValidationResult(false,
              "비밀번호는 최대 " + MAX_PASSWORD_LENGTH + "자 이하여야 합니다.");
    }

    // 기본 문자 조합 체크
    if (!LOWERCASE_PATTERN.matcher(password).find()) {
      return new PasswordValidationResult(false, "비밀번호에 영문 소문자가 포함되어야 합니다.");
    }

    if (!UPPERCASE_PATTERN.matcher(password).find()) {
      return new PasswordValidationResult(false, "비밀번호에 영문 대문자가 포함되어야 합니다.");
    }

    if (!DIGIT_PATTERN.matcher(password).find()) {
      return new PasswordValidationResult(false, "비밀번호에 숫자가 포함되어야 합니다.");
    }

    if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
      return new PasswordValidationResult(false, "비밀번호에 특수문자가 포함되어야 합니다.");
    }

    // 일반적인 비밀번호 체크
    if (isCommonPassword(password)) {
      return new PasswordValidationResult(false, "너무 일반적인 비밀번호입니다. 더 복잡한 비밀번호를 사용해주세요.");
    }

    // 강도 체크 (최소 보통 이상)
    PasswordStrength strength = evaluatePasswordStrength(password);
    if (strength.getScore() < 3) {
      return new PasswordValidationResult(false,
              "비밀번호가 너무 약합니다. 현재 강도: " + strength.getMessage() + " (최소 '보통' 이상 필요)");
    }

    return new PasswordValidationResult(true, "유효한 비밀번호입니다.");
  }

  /**
   * 비밀번호를 해시화합니다 (Salt 포함)
   * @param password 원본 비밀번호
   * @return 해시화된 비밀번호 (salt + hash)
   */
  public String hashPassword(String password) {
    try {
      // Salt 생성
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[SALT_LENGTH];
      random.nextBytes(salt);

      // 비밀번호 해시화
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt);
      byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));

      // Salt + Hash를 Base64로 인코딩하여 저장
      byte[] saltAndHash = new byte[salt.length + hashedPassword.length];
      System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
      System.arraycopy(hashedPassword, 0, saltAndHash, salt.length, hashedPassword.length);

      return java.util.Base64.getEncoder().encodeToString(saltAndHash);

    } catch (Exception e) {
      throw new RuntimeException("비밀번호 해시화 중 오류가 발생했습니다.", e);
    }
  }

  /**
   * 비밀번호를 검증합니다
   * @param password 입력된 비밀번호
   * @param hashedPassword 저장된 해시화된 비밀번호
   * @return 일치 여부
   */
  public boolean verifyPassword(String password, String hashedPassword) {
    try {
      // 저장된 hash에서 salt와 hash 분리
      byte[] saltAndHash = java.util.Base64.getDecoder().decode(hashedPassword);

      byte[] salt = new byte[SALT_LENGTH];
      byte[] hash = new byte[saltAndHash.length - SALT_LENGTH];

      System.arraycopy(saltAndHash, 0, salt, 0, SALT_LENGTH);
      System.arraycopy(saltAndHash, SALT_LENGTH, hash, 0, hash.length);

      // 입력된 비밀번호를 같은 salt로 해시화
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(salt);
      byte[] testHash = md.digest(password.getBytes("UTF-8"));

      // 해시 비교
      return MessageDigest.isEqual(hash, testHash);

    } catch (Exception e) {
      System.err.println("비밀번호 검증 중 오류: " + e.getMessage());
      return false;
    }
  }

  /**
   * 일반적인 비밀번호인지 확인
   */
  private boolean isCommonPassword(String password) {
    String lowerPassword = password.toLowerCase();

    // 정확히 일치하는 일반적인 비밀번호
    if (COMMON_PASSWORDS.contains(lowerPassword)) {
      return true;
    }

    // 일반적인 비밀번호의 변형 체크
    for (String common : COMMON_PASSWORDS) {
      if (lowerPassword.contains(common) && lowerPassword.length() <= common.length() + 3) {
        return true;
      }
    }

    // 순차적 패턴
    if (hasSequentialPattern(password)) {
      return true;
    }

    // 반복 패턴
    if (hasRepeatingPattern(password)) {
      return true;
    }

    // 개인정보 패턴
    if (hasPersonalInfoPattern(password)) {
      return true;
    }

    return false;
  }

  /**
   * 순차적 패턴 체크
   */
  private boolean hasSequentialPattern(String password) {
    String lowerPassword = password.toLowerCase();

    // 알파벳 순서
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    String numbers = "0123456789";
    String keyboard = "qwertyuiopasdfghjklzxcvbnm";

    return checkSequence(lowerPassword, alphabet) ||
            checkSequence(lowerPassword, numbers) ||
            checkSequence(lowerPassword, keyboard);
  }

  /**
   * 시퀀스 체크 헬퍼 메서드
   */
  private boolean checkSequence(String password, String sequence) {
    for (int i = 0; i <= sequence.length() - 4; i++) {
      String subSeq = sequence.substring(i, i + 4);
      String reverseSeq = new StringBuilder(subSeq).reverse().toString();

      if (password.contains(subSeq) || password.contains(reverseSeq)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 반복 패턴 체크
   */
  private boolean hasRepeatingPattern(String password) {
    // 3글자 이상 반복
    for (int i = 0; i < password.length() - 2; i++) {
      char c = password.charAt(i);
      if (password.charAt(i + 1) == c && password.charAt(i + 2) == c) {
        return true;
      }
    }

// 패턴 반복
    for (int len = 2; len <= password.length() / 2; len++) {
      String pattern = password.substring(0, len);
      StringBuilder sb = new StringBuilder();
      int repeatCount = password.length() / len;
      for (int i = 0; i < repeatCount; i++) {
        sb.append(pattern);
      }
      String repeated = sb.toString();
      if (password.startsWith(repeated) && repeated.length() >= 4) {
        return true;
      }
    }

    return false;
  }

  /**
   * 개인정보 패턴 체크
   */
  private boolean hasPersonalInfoPattern(String password) {
    return BIRTH_DATE_PATTERN.matcher(password).find() ||
            YEAR_PATTERN.matcher(password).find() ||
            password.matches(".*\\d{6}.*"); // 6자리 연속 숫자
  }

  /**
   * 엔트로피 체크
   */
  private boolean hasGoodEntropy(String password) {
    Set<Character> uniqueChars = new HashSet<>();
    for (char c : password.toCharArray()) {
      uniqueChars.add(c);
    }

    // 문자 다양성과 길이 고려
    double entropy = uniqueChars.size() * Math.log(password.length()) / Math.log(2);
    return entropy > 30; // 임계값
  }

  // 내부 클래스들
  public static class PasswordStrength {
    private final double score;
    private final StrengthLevel level;
    private final String message;

    public PasswordStrength(double score, StrengthLevel level, String message) {
      this.score = score;
      this.level = level;
      this.message = message;
    }

    public double getScore() { return score; }
    public StrengthLevel getLevel() { return level; }
    public String getMessage() { return message; }
  }

  public static class PasswordValidationResult {
    private final boolean valid;
    private final String message;

    public PasswordValidationResult(boolean valid, String message) {
      this.valid = valid;
      this.message = message;
    }

    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
  }

  public enum StrengthLevel {
    NONE("none"),
    VERY_WEAK("very-weak"),
    WEAK("weak"),
    FAIR("fair"),
    GOOD("good"),
    EXCELLENT("excellent");

    private final String value;

    StrengthLevel(String value) {
      this.value = value;
    }

    public String getValue() { return value; }
  }
}