package com.procore.service.impl;

import com.procore.mapper.MemberMapper;
import com.procore.service.MemberService;
import com.procore.vo.MemberStatus;
import com.procore.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  @Autowired
  private MemberMapper memberMapper;

  @Override
  public boolean registerMember(MemberVO memberVO) {
    System.out.println("=== 회원가입 처리 시작 ===");
    System.out.println("요청 회원 정보: " + memberVO.getEmail() + ", " + memberVO.getNickname());

    // 1. Status 기본값 설정 (null인 경우)
    if (memberVO.getStatus() == null) {
      memberVO.setStatus(MemberStatus.ACTIVE);
      System.out.println("Status 기본값 설정: ACTIVE");
    }

    // 2. 이메일 중복 체크
    if (isEmailDuplicate(memberVO.getEmail())) {
      System.out.println("회원가입 실패: 이메일 중복 - " + memberVO.getEmail());
      return false;
    }

    // 3. 닉네임 중복 체크
    if (isNicknameDuplicate(memberVO.getNickname())) {
      System.out.println("회원가입 실패: 닉네임 중복 - " + memberVO.getNickname());
      return false;
    }

    // 4. 전화번호 중복 체크 (전화번호가 있는 경우만)
    if (memberVO.getPhone() != null && !memberVO.getPhone().trim().isEmpty()) {
      if (isPhoneDuplicate(memberVO.getPhone())) {
        System.out.println("회원가입 실패: 전화번호 중복 - " + memberVO.getPhone());
        return false;
      }
    }

    try {
      // 5. 회원 등록 실행
      int result = memberMapper.insertMember(memberVO);

      if (result > 0) {
        System.out.println("회원가입 성공: " + memberVO.getEmail());
        return true;
      } else {
        System.out.println("회원가입 실패: DB 삽입 결과 0");
        return false;
      }
    } catch (Exception e) {
      System.err.println("회원가입 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public MemberVO login(String email, String password) {
    if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
      System.out.println("로그인 실패: 이메일 또는 비밀번호가 비어있음");
      return null;
    }

    try {
      // 이메일로 회원 조회
      MemberVO member = memberMapper.selectMemberByEmail(email.trim());

      if (member == null) {
        System.out.println("로그인 실패: 존재하지 않는 이메일 - " + email);
        return null;
      }

      // 계정 상태 체크
      if (member.getStatus() != MemberStatus.ACTIVE) {
        System.out.println("로그인 실패: 비활성 계정 - " + email + " (상태: " + member.getStatus() + ")");
        return null;
      }

      // 비밀번호 검증 (실제 환경에서는 암호화된 비밀번호와 비교해야 함)
      if (member.getPassword().equals(password)) {
        System.out.println("로그인 성공: " + email);
        return member;
      } else {
        System.out.println("로그인 실패: 비밀번호 불일치 - " + email);
        return null;
      }
    } catch (Exception e) {
      System.err.println("로그인 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public int updateMember(MemberVO memberVO) {
    if (memberVO == null || memberVO.getMemberId() == null) {
      return 0;
    }

    try {
      return memberMapper.updateMember(memberVO);
    } catch (Exception e) {
      System.err.println("회원정보 수정 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      return 0;
    }
  }

  @Override
  public boolean checkDuplicateEmail(String email) {
    System.out.println("=== 이메일 중복 체크 시작 ===");
    System.out.println("체크할 이메일: " + email);

    if (email == null || email.trim().isEmpty()) {
      System.out.println("결과: 이메일이 비어있음 (false 리턴)");
      return false;
    }

    try {
      // 이메일 중복 여부 확인 (사용 가능하면 true, 중복이면 false)
      boolean isAvailable = !isEmailDuplicate(email.trim());
      System.out.println("결과: " + (isAvailable ? "사용 가능" : "중복됨") + " (" + isAvailable + " 리턴)");
      return isAvailable;
    } catch (Exception e) {
      System.err.println("이메일 중복 체크 중 에러 발생: " + e.getMessage());
      e.printStackTrace();
      // 에러 발생시 안전하게 중복으로 처리
      return false;
    }
  }

  @Override
  public boolean checkDuplicateNickname(String nickname) {
    System.out.println("=== 닉네임 중복 체크 시작 ===");
    System.out.println("체크할 닉네임: " + nickname);

    if (nickname == null || nickname.trim().isEmpty()) {
      System.out.println("결과: 닉네임이 비어있음 (false 리턴)");
      return false;
    }

    try {
      // 닉네임 중복 여부 확인 (사용 가능하면 true, 중복이면 false)
      boolean isAvailable = !isNicknameDuplicate(nickname.trim());
      System.out.println("결과: " + (isAvailable ? "사용 가능" : "중복됨") + " (" + isAvailable + " 리턴)");
      return isAvailable;
    } catch (Exception e) {
      System.err.println("닉네임 중복 체크 중 에러 발생: " + e.getMessage());
      e.printStackTrace();
      // 에러 발생시 안전하게 중복으로 처리
      return false;
    }
  }

  // === Private Helper Methods ===

  /**
   * 이메일 중복 여부 확인
   * @param email 확인할 이메일
   * @return 중복이면 true, 사용 가능하면 false
   */
  private boolean isEmailDuplicate(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }

    try {
      Long count = memberMapper.countByEmail(email.trim());
      return count != null && count > 0;
    } catch (Exception e) {
      System.err.println("이메일 중복 체크 DB 조회 실패: " + e.getMessage());
      return true; // 에러시 안전하게 중복으로 처리
    }
  }

  /**
   * 닉네임 중복 여부 확인
   * @param nickname 확인할 닉네임
   * @return 중복이면 true, 사용 가능하면 false
   */
  private boolean isNicknameDuplicate(String nickname) {
    if (nickname == null || nickname.trim().isEmpty()) {
      return false;
    }

    try {
      int count = memberMapper.countByNickname(nickname.trim());
      return count > 0;
    } catch (Exception e) {
      System.err.println("닉네임 중복 체크 DB 조회 실패: " + e.getMessage());
      return true; // 에러시 안전하게 중복으로 처리
    }
  }

  /**
   * 전화번호 중복 여부 확인
   * @param phone 확인할 전화번호
   * @return 중복이면 true, 사용 가능하면 false
   */
  private boolean isPhoneDuplicate(String phone) {
    if (phone == null || phone.trim().isEmpty()) {
      return false;
    }

    try {
      int count = memberMapper.countByPhone(phone.trim());
      return count > 0;
    } catch (Exception e) {
      System.err.println("전화번호 중복 체크 DB 조회 실패: " + e.getMessage());
      return true; // 에러시 안전하게 중복으로 처리
    }
  }
}