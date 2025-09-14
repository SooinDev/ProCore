<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ProCore</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<c:url value='/css/main.css'/>">
</head>
<body>
<!-- 네비게이션 -->
<nav class="navbar">
  <div class="nav-container">
    <div class="nav-brand">
      <h1>ProCore</h1>
    </div>
    <div class="nav-links">
      <a href="<c:url value='/'/>">홈</a>
      <a href="<c:url value='/member/login'/>">로그인</a>
      <a href="<c:url value='/member/register'/>" class="nav-cta">시작하기</a>
    </div>
  </div>
</nav>

<!-- 메인 섹션 -->
<section class="hero">
  <div class="hero-container">
    <div class="hero-content">
      <h1 class="hero-title">
        프로필 관리를
        <span class="hero-subtitle">더 스마트하게</span>
      </h1>
      <p class="hero-description">
        안전하고 직관적인 프로필 코어 시스템으로 개인 정보를 체계적으로 관리하세요.
        개인정보 보호와 사용자 경험을 최우선으로 합니다.
      </p>
      <div class="hero-actions">
        <a href="<c:url value='/member/register'/>" class="btn-primary">
          무료로 시작하기
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
            <path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </a>
        <a href="<c:url value='/member/login'/>" class="btn-secondary">로그인</a>
      </div>
    </div>
    <div class="hero-visual">
      <div class="visual-card">
        <div class="visual-header">
          <div class="visual-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
        <div class="visual-content">
          <div class="visual-row">
            <div class="visual-avatar"></div>
            <div class="visual-info">
              <div class="visual-line"></div>
              <div class="visual-line short"></div>
            </div>
          </div>
          <div class="visual-row">
            <div class="visual-avatar"></div>
            <div class="visual-info">
              <div class="visual-line"></div>
              <div class="visual-line short"></div>
            </div>
          </div>
          <div class="visual-row">
            <div class="visual-avatar"></div>
            <div class="visual-info">
              <div class="visual-line"></div>
              <div class="visual-line short"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<!-- 기능 섹션 -->
<section class="features">
  <div class="features-container">
    <div class="features-header">
      <h2>개인 정보를 안전하게 관리</h2>
      <p>프로필 관리의 새로운 기준</p>
    </div>
    <div class="features-grid">
      <div class="feature-card">
        <div class="feature-icon">
          🔒
        </div>
        <h3>강력한 보안</h3>
        <p>개인 정보를 보호하는 엔터프라이즈급 보안과 암호화 시스템을 제공합니다.</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">
          🛡️
        </div>
        <h3>프라이버시 우선</h3>
        <p>개인정보 보호법을 준수하며 사용자의 프라이버시를 최우선으로 보장합니다.</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">
          👤
        </div>
        <h3>간편한 관리</h3>
        <p>직관적인 인터페이스로 프로필 정보를 쉽고 빠르게 관리할 수 있습니다.</p>
      </div>
    </div>
  </div>
</section>

<!-- 푸터 -->
<footer class="footer">
  <div class="footer-container">
    <div class="footer-content">
      <div class="footer-brand">
        <h3>ProCore</h3>
        <p>프로필 관리의 새로운 기준</p>
      </div>
      <div class="footer-links">
        <div class="footer-column">
          <h4>제품</h4>
          <a href="#">기능</a>
          <a href="#">보안</a>
          <a href="#">요금제</a>
        </div>
        <div class="footer-column">
          <h4>지원</h4>
          <a href="#">문서</a>
          <a href="#">문의</a>
          <a href="#">서비스 상태</a>
        </div>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2024 ProCore. 모든 권리 보유.</p>
    </div>
  </div>
</footer>

<script src="<c:url value='/js/main.js'/>"></script>
</body>
</html>