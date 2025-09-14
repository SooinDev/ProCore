<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>로그인 - ProCore</title>
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
      <a href="<c:url value='/member/register'/>">회원가입</a>
    </div>
  </div>
</nav>

<!-- 로그인 섹션 -->
<section class="auth-section">
  <div class="auth-container auth-container-simple">
    <div class="auth-form-wrapper auth-form-centered">
      <div class="auth-header">
        <h1>로그인</h1>
        <p>ProCore 계정으로 로그인하세요</p>
      </div>

      <c:if test="${not empty message}">
        <div class="alert alert-success">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
            ${message}
        </div>
      </c:if>

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

      <form id="loginForm" action="<c:url value='/member/login'/>" method="post" class="auth-form">
        <div class="form-group">
          <label for="email">이메일 주소</label>
          <input type="email"
                 id="email"
                 name="email"
                 required
                 placeholder="이메일을 입력해주세요"
                 autocomplete="email"
                 autofocus>
          <div class="form-error" id="emailError"></div>
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password"
                 id="password"
                 name="password"
                 required
                 placeholder="비밀번호를 입력해주세요"
                 autocomplete="current-password">
          <div class="form-error" id="passwordError"></div>
        </div>

        <div class="form-options">
          <label class="checkbox-label">
            <input type="checkbox" id="rememberMe" name="rememberMe">
            <span class="checkmark"></span>
            로그인 상태 유지
          </label>
          <a href="#" class="forgot-password">비밀번호를 잊으셨나요?</a>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary btn-large">
            <span class="btn-text">로그인</span>
            <div class="btn-loading" style="display: none;">
              <div class="loading-spinner"></div>
            </div>
          </button>
        </div>
      </form>

      <div class="auth-divider">
        <span>또는</span>
      </div>

      <div class="social-login">
        <button type="button" class="btn-social btn-google" disabled>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
            <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
            <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" fill="#FBBC05"/>
            <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
          </svg>
          Google로 로그인 (준비중)
        </button>
        <button type="button" class="btn-social btn-kakao" disabled>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M12 3C6.48 3 2 6.48 2 10.8c0 2.7 1.68 5.1 4.26 6.54l-1.08 3.96c-.12.42.36.78.72.54L9.84 19.8C10.56 19.92 11.28 20 12 20c5.52 0 10-3.48 10-7.8S17.52 3 12 3z" fill="#FEE500"/>
          </svg>
          카카오로 로그인 (준비중)
        </button>
      </div>

      <div class="auth-footer">
        <p>아직 계정이 없으신가요? <a href="<c:url value='/member/register'/>">회원가입하기</a></p>
      </div>
    </div>
  </div>
</section>

<script src="<c:url value='/js/login.js'/>"></script>
</body>
</html>