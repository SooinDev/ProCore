package com.procore.vo;

import java.time.LocalDateTime;

public class MemberVO {

  /** 회원 고유 번호 */
  Long memberId;

  /** 회원 이메일 (로그인 ID) */
  String email;

  /** 회원 비밀번호 */
  String password;

  /** 회원 닉네임 */
  String nickname;

  /** 회원 닉네임 변경 일자 */
  LocalDateTime nicknameChangedAt;

  /** 회원 이름 */
  String name;

  /** 회원 전화번호 */
  String phone;

  /** 회원 주소 */
  String address;

  /** 회원 상태 */
  MemberStatus status;

  /** 회원 가입 일자 */
  LocalDateTime createdAt;

  /** 회원 정보 수정 일자 */
  LocalDateTime updatedAt;

  public MemberVO() {
  }

  public MemberVO(String email, String password, String nickname) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.status = MemberStatus.ACTIVE;  // 기본값 설정
  }

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
    this.email = email;
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
    this.nickname = nickname;
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
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public MemberStatus getStatus() {  // 리턴 타입 변경
    return status;
  }

  public void setStatus(MemberStatus status) {  // 매개변수 타입 변경
    this.status = status;
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

  @Override
  public String toString() {
    return "MemberVO{" +
            "memberId=" + memberId +
            ", email='" + email + '\'' +
            ", password='****'" +
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
}