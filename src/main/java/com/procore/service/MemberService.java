package com.procore.service;

import com.procore.vo.MemberVO;

public interface MemberService {

  boolean registerMember(MemberVO memberVO);

  MemberVO login(String email, String password);

  int updateMember(MemberVO memberVO);

  boolean checkDuplicateEmail(String email);

  boolean checkDuplicateNickname(String nickname);
}