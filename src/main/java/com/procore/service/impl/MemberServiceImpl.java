package com.procore.service.impl;

import com.procore.mapper.MemberMapper;
import com.procore.service.MemberService;
import com.procore.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  @Autowired
  private MemberMapper memberMapper;

  @Override
  public boolean registerMember(MemberVO memberVO) {

    if (memberMapper.countByEmail(memberVO.getEmail()) > 0) {
      return false; // 이미 존재함
    }
    if (memberMapper.countByNickname(memberVO.getNickname()) > 0) {
      return false; // 이미 존재함
    }
    if (memberVO.getPhone() != null && memberMapper.countByPhone(memberVO.getPhone()) > 0) {
      return false; // 이미 존재함
    }

    int result = memberMapper.insertMember(memberVO);

    return result > 0;
  }

  @Override
  public MemberVO login(String email, String password) {
    if (email == null || password == null) {
      return null;
    }

    // 데이터베이스에서 실제 회원 조회
    MemberVO member = memberMapper.selectMemberByEmail(email);

    if (member == null) {
      return null; // 회원이 존재하지 않음
    }

    // 비밀번호 검증
    if (member.getPassword().equals(password)) {
      return member; // 로그인 성공, 실제 회원정보 리턴
    }

    return null; // 비밀번호 불일치
  }

  @Override
  public int updateMember(MemberVO memberVO) {
    return memberMapper.updateMember(memberVO);
  }

}
