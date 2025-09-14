<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>회원가입 - ProCore</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/form.css'/>">
</head>
<body>
<!-- 네비게이션 -->
<nav class="navbar">
  <div class="nav-container">
    <div class="nav-brand">
      <a href="<c:url value='/'/>">
        <h1>ProCore</h1>
      </a>
    </div>
    <div class="nav-links">
      <a href="<c:url value='/'/>">홈</a>
      <a href="<c:url value='/member/login'/>">로그인</a>
    </div>
  </div>
</nav>

<!-- 회원가입 섹션 -->
<section class="auth-section">
  <div class="auth-container">
    <div class="auth-form-wrapper">
      <div class="auth-header">
        <h1>회원가입</h1>
        <p>ProCore와 함께 시작해보세요</p>
      </div>

      <c:if test="${not empty error}">
        <div class="alert alert-error">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <line x1="15" y1="9" x2="9" y2="15" stroke="currentColor" stroke-width="2"/>
            <line x1="9" y1="9" x2="15" y2="15" stroke="currentColor" stroke-width="2"/>
          </svg>
            ${error}
        </div>
      </c:if>

      <form id="registerForm" action="<c:url value='/member/register'/>" method="post" class="auth-form">
        <div class="form-group">
          <label for="email">이메일 주소 *</label>
          <input type="email"
                 id="email"
                 name="email"
                 required
                 placeholder="이메일을 입력해주세요"
                 autocomplete="email">
          <div class="form-error" id="emailError"></div>
        </div>

        <div class="form-group">
          <label for="password">비밀번호 *</label>
          <input type="password"
                 id="password"
                 name="password"
                 required
                 placeholder="8자 이상, 영문/숫자/특수문자 조합"
                 autocomplete="new-password">
          <div class="form-error" id="passwordError"></div>
          <div class="password-strength">
            <div class="strength-bar">
              <div class="strength-fill"></div>
            </div>
            <span class="strength-text">비밀번호 강도</span>
          </div>
        </div>

        <div class="form-group">
          <label for="confirmPassword">비밀번호 확인 *</label>
          <input type="password"
                 id="confirmPassword"
                 name="confirmPassword"
                 required
                 placeholder="비밀번호를 다시 입력해주세요"
                 autocomplete="new-password">
          <div class="form-error" id="confirmPasswordError"></div>
        </div>

        <div class="form-group">
          <label for="nickname">닉네임 *</label>
          <input type="text"
                 id="nickname"
                 name="nickname"
                 required
                 placeholder="2-10자의 닉네임을 입력해주세요"
                 maxlength="10">
          <div class="form-error" id="nicknameError"></div>
        </div>

        <div class="form-divider">
          <span>선택 정보</span>
        </div>

        <div class="form-group">
          <label for="name">이름</label>
          <input type="text"
                 id="name"
                 name="name"
                 placeholder="실명을 입력해주세요"
                 maxlength="20">
        </div>

        <div class="form-group">
          <label for="phone">전화번호</label>
          <input type="tel"
                 id="phone"
                 name="phone"
                 placeholder="010-1234-5678"
                 pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}">
          <div class="form-error" id="phoneError"></div>
        </div>

        <div class="form-group">
          <label for="address">주소</label>
          <textarea id="address"
                    name="address"
                    placeholder="주소를 입력해주세요"
                    rows="3"
                    maxlength="200"></textarea>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary btn-large">
            <span class="btn-text">계정 만들기</span>
            <div class="btn-loading" style="display: none;">
              <div class="loading-spinner"></div>
            </div>
          </button>
        </div>
      </form>

      <div class="auth-footer">
        <p>이미 계정이 있으신가요? <a href="<c:url value='/member/login'/>">로그인하기</a></p>
      </div>
    </div>

    <!-- 사이드 정보 -->
    <div class="auth-info">
      <div class="info-card">
        <h3>안전한 회원가입</h3>
        <ul class="info-list">
          <li>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            개인정보 암호화 보호
          </li>
          <li>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            스팸 메일 발송 안함
          </li>
          <li>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
              <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            언제든지 계정 삭제 가능
          </li>
        </ul>
      </div>
    </div>
  </div>
</section>

<script src="<c:url value='/js/register.js'/>"></script>
</body>
</html>