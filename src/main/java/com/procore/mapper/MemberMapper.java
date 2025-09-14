package com.procore.mapper;

import com.procore.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

  int insertMember(MemberVO memberVO);
  MemberVO selectMemberByEmail(String email);
  int updateMember(MemberVO memberVO);
  int deleteMemberById(Long memberId);
  Long countByEmail(String email);
  int countByNickname(String nickname);
  int countByPhone(String phone);
  int updateNickname(Long memberId, String nickname);
}
