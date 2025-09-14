package com.procore.vo;

import java.time.LocalDateTime;

public class MemberVO {

  /** 회원 고유 번호 */
  private Long memberId;

  /** 회원 이메일 (로그인 ID) */
  private String email;

  /** 회원 비밀번호 */
  private String password;

  /** 회원 닉네임 */
  private String nickname;

  /** 회원 닉네임 변경 일자 */
  private LocalDateTime nicknameChangedAt;

  /** 회원 이름 */
  private String name;

  /** 회원 전화번호 */
  private String phone;

  /** 회원 주소 */
  private String address;

  /** 회원 상태 */
  private MemberStatus status;

  /** 회원 가입 일자 */
  private LocalDateTime createdAt;

  /** 회원 정보 수정 일자 */
  private LocalDateTime updatedAt;

  // 기본 생성자
  public MemberVO() {
    this.status = MemberStatus.ACTIVE; // 기본값을 ACTIVE로 설정
  }

  // 필수 필드 생성자
  public MemberVO(String email, String password, String nickname) {
    this();  // 기본 생성자 호출하여 status 기본값 설정
    this.email = email;
    this.password = password;
    this.nickname = nickname;
  }

  // 모든 필드를 포함한 생성자
  public MemberVO(String email, String password, String nickname, String name, String phone, String address) {
    this(email, password, nickname);
    this.name = name;
    this.phone = phone;
    this.address = address;
  }

  // Getter/Setter Methods
  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email != null ? email.trim() : null;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname != null ? nickname.trim() : null;
  }

  public LocalDateTime getNicknameChangedAt() {
    return nicknameChangedAt;
  }

  public void setNicknameChangedAt(LocalDateTime nicknameChangedAt) {
    this.nicknameChangedAt = nicknameChangedAt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name != null ? name.trim() : null;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone != null ? phone.trim() : null;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address != null ? address.trim() : null;
  }

  public MemberStatus getStatus() {
    return status;
  }

  public void setStatus(MemberStatus status) {
    this.status = status != null ? status : MemberStatus.ACTIVE;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  // 비즈니스 로직 메서드들

  /**
   * 활성 회원인지 확인
   */
  public boolean isActive() {
    return MemberStatus.ACTIVE.equals(this.status);
  }

  /**
   * 닉네임 변경 가능한지 확인 (7일 제한)
   */
  public boolean canChangeNickname() {
    if (nicknameChangedAt == null) {
      return true;
    }
    return nicknameChangedAt.plusDays(7).isBefore(LocalDateTime.now());
  }

  /**
   * 입력값 검증 메서드
   */
  public boolean isValid() {
    return email != null && !email.trim().isEmpty() &&
            password != null && !password.trim().isEmpty() &&
            nickname != null && !nickname.trim().isEmpty() &&
            status != null;
  }

  /**
   * 이메일 유효성 검사
   */
  public boolean isValidEmail() {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    return email.matches(emailRegex);
  }

  /**
   * 닉네임 유효성 검사 (2-10자, 한글/영문/숫자/언더스코어)
   */
  public boolean isValidNickname() {
    if (nickname == null || nickname.trim().isEmpty()) {
      return false;
    }
    String trimmedNickname = nickname.trim();
    return trimmedNickname.length() >= 2 &&
            trimmedNickname.length() <= 10 &&
            trimmedNickname.matches("^[a-zA-Z0-9가-힣_]+$");
  }

  /**
   * 전화번호 유효성 검사
   */
  public boolean isValidPhone() {
    if (phone == null || phone.trim().isEmpty()) {
      return true; // 전화번호는 선택사항이므로 비어있어도 유효
    }
    return phone.matches("^\\d{3}-\\d{4}-\\d{4}$");
  }

  @Override
  public String toString() {
    return "MemberVO{" +
            "memberId=" + memberId +
            ", email='" + email + '\'' +
            ", password='[PROTECTED]'" +
            ", nickname='" + nickname + '\'' +
            ", nicknameChangedAt=" + nicknameChangedAt +
            ", name='" + name + '\'' +
            ", phone='" + phone + '\'' +
            ", address='" + address + '\'' +
            ", status=" + status +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MemberVO memberVO = (MemberVO) o;

    if (memberId != null ? !memberId.equals(memberVO.memberId) : memberVO.memberId != null) return false;
    return email != null ? email.equals(memberVO.email) : memberVO.email == null;
  }

  @Override
  public int hashCode() {
    int result = memberId != null ? memberId.hashCode() : 0;
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
  }
}