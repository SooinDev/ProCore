// ProCore 강화된 비밀번호 보안 시스템
class EnhancedPasswordValidator {
  constructor() {
    this.passwordField = null;
    this.confirmPasswordField = null;
    this.strengthIndicator = null;
    this.requirementsContainer = null;
    this.init();
  }

  init() {
    this.passwordField = document.getElementById('password');
    this.confirmPasswordField = document.getElementById('confirmPassword');
    this.createStrengthIndicator();
    this.createRequirementsDisplay();
    this.setupEventListeners();
  }

  createStrengthIndicator() {
    const passwordGroup = this.passwordField.closest('.form-group');
    const existingStrength = passwordGroup.querySelector('.password-strength');

    if (existingStrength) {
      existingStrength.remove();
    }

    const strengthHTML = `
      <div class="password-strength">
        <div class="strength-bar">
          <div class="strength-fill"></div>
        </div>
        <div class="strength-info">
          <span class="strength-text">비밀번호 강도</span>
          <span class="strength-score">0/5</span>
        </div>
      </div>
    `;

    passwordGroup.insertAdjacentHTML('beforeend', strengthHTML);
    this.strengthIndicator = passwordGroup.querySelector('.password-strength');
  }

  createRequirementsDisplay() {
    const passwordGroup = this.passwordField.closest('.form-group');

    const requirementsHTML = `
      <div class="password-requirements">
        <div class="requirement" data-rule="length">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">8자 이상</span>
        </div>
        <div class="requirement" data-rule="lowercase">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">영문 소문자</span>
        </div>
        <div class="requirement" data-rule="uppercase">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">영문 대문자</span>
        </div>
        <div class="requirement" data-rule="numbers">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">숫자</span>
        </div>
        <div class="requirement" data-rule="special">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">특수문자 (!@#$%^&*)</span>
        </div>
        <div class="requirement" data-rule="noCommon">
          <span class="requirement-icon">○</span>
          <span class="requirement-text">일반적이지 않은 비밀번호</span>
        </div>
      </div>
    `;

    this.strengthIndicator.insertAdjacentHTML('afterend', requirementsHTML);
    this.requirementsContainer = passwordGroup.querySelector('.password-requirements');
  }

  setupEventListeners() {
    this.passwordField.addEventListener('input', () => {
      this.validatePassword();
    });

    this.passwordField.addEventListener('focus', () => {
      this.requirementsContainer.style.display = 'block';
    });

    this.passwordField.addEventListener('blur', () => {
      setTimeout(() => {
        if (!this.passwordField.matches(':focus')) {
          this.requirementsContainer.style.display = 'none';
        }
      }, 200);
    });
  }

  // 일반적인 비밀번호 패턴들
  getCommonPasswords() {
    return [
      'password', '12345678', 'qwerty123', 'abc12345', 'password123',
      '1q2w3e4r', 'admin123', 'welcome123', 'letmein123', 'monkey123',
      'dragon123', '111111', '123123', 'sunshine', 'master123',
      'shadow123', 'ashley123', 'football123', 'jesus123', 'michael123',
      'ninja123', 'mustang123', 'password1', '123456789', 'princess123'
    ];
  }

  // 순차적 패턴 체크
  hasSequentialPattern(password) {
    const sequences = [
      'abcdefghijklmnopqrstuvwxyz',
      '0123456789',
      'qwertyuiopasdfghjklzxcvbnm'
    ];

    const lowerPassword = password.toLowerCase();

    for (let sequence of sequences) {
      for (let i = 0; i <= sequence.length - 4; i++) {
        const subSeq = sequence.substring(i, i + 4);
        const reverseSeq = subSeq.split('').reverse().join('');

        if (lowerPassword.includes(subSeq) || lowerPassword.includes(reverseSeq)) {
          return true;
        }
      }
    }
    return false;
  }

  // 반복 패턴 체크
  hasRepeatingPattern(password) {
    // 3글자 이상 반복
    for (let i = 0; i < password.length - 2; i++) {
      const char = password[i];
      if (password[i + 1] === char && password[i + 2] === char) {
        return true;
      }
    }

    // 패턴 반복 (예: abcabc, 123123)
    for (let len = 2; len <= password.length / 2; len++) {
      const pattern = password.substring(0, len);
      const repeated = pattern.repeat(Math.floor(password.length / len));
      if (password.startsWith(repeated) && repeated.length >= 4) {
        return true;
      }
    }

    return false;
  }

  // 개인정보 기반 패턴 체크 (간단한 버전)
  hasPersonalInfo(password) {
    const commonPersonalPatterns = [
      /\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])/, // 생년월일 패턴
      /\d{6}/, // 6자리 연속 숫자
      /(19|20)\d{2}/, // 연도 패턴
    ];

    return commonPersonalPatterns.some(pattern => pattern.test(password));
  }

  validatePassword() {
    const password = this.passwordField.value;
    const requirements = {
      length: password.length >= 8,
      lowercase: /[a-z]/.test(password),
      uppercase: /[A-Z]/.test(password),
      numbers: /\d/.test(password),
      special: /[!@#$%^&*(),.?":{}|<>]/.test(password),
      noCommon: !this.isCommonPassword(password)
    };

    // 요구사항 UI 업데이트
    Object.entries(requirements).forEach(([rule, passed]) => {
      const requirementElement = this.requirementsContainer.querySelector(`[data-rule="${rule}"]`);
      const icon = requirementElement.querySelector('.requirement-icon');

      if (passed) {
        requirementElement.classList.add('passed');
        icon.textContent = '✓';
        icon.style.color = '#16a34a';
      } else {
        requirementElement.classList.remove('passed');
        icon.textContent = '○';
        icon.style.color = '#dc2626';
      }
    });

    // 강도 계산
    const strength = this.calculateStrength(password, requirements);
    this.updateStrengthIndicator(strength);

    return strength;
  }

  isCommonPassword(password) {
    if (!password || password.length < 4) return true;

    const lowerPassword = password.toLowerCase();
    const commonPasswords = this.getCommonPasswords();

    // 정확히 일치하는 일반적인 비밀번호
    if (commonPasswords.includes(lowerPassword)) {
      return true;
    }

    // 일반적인 비밀번호의 변형 (숫자 추가 등)
    for (let common of commonPasswords) {
      if (lowerPassword.includes(common) && lowerPassword.length <= common.length + 3) {
        return true;
      }
    }

    // 순차적 패턴
    if (this.hasSequentialPattern(password)) {
      return true;
    }

    // 반복 패턴
    if (this.hasRepeatingPattern(password)) {
      return true;
    }

    // 개인정보 패턴
    if (this.hasPersonalInfo(password)) {
      return true;
    }

    return false;
  }

  calculateStrength(password, requirements) {
    if (!password) {
      return { score: 0, level: 'none', text: '비밀번호를 입력하세요' };
    }

    let score = 0;
    const passedCount = Object.values(requirements).filter(Boolean).length;

    // 기본 요구사항 점수 (각 1점, 총 6점)
    score += passedCount;

    // 추가 보너스 점수
    if (password.length >= 12) score += 1; // 긴 비밀번호
    if (password.length >= 16) score += 1; // 매우 긴 비밀번호
    if (/[가-힣]/.test(password)) score += 0.5; // 한글 포함
    if (this.hasGoodEntropy(password)) score += 0.5; // 엔트로피 좋음

    // 점수를 5점 만점으로 정규화
    const normalizedScore = Math.min(5, Math.round(score * 10) / 10);

    // 레벨 결정
    let level, text;
    if (normalizedScore < 2) {
      level = 'very-weak';
      text = '매우 약함';
    } else if (normalizedScore < 3) {
      level = 'weak';
      text = '약함';
    } else if (normalizedScore < 4) {
      level = 'fair';
      text = '보통';
    } else if (normalizedScore < 5) {
      level = 'good';
      text = '좋음';
    } else {
      level = 'excellent';
      text = '매우 강함';
    }

    return { score: normalizedScore, level, text };
  }

  hasGoodEntropy(password) {
    // 문자 다양성 체크
    const charSets = [
      /[a-z]/, /[A-Z]/, /\d/, /[!@#$%^&*(),.?":{}|<>]/, /[가-힣]/
    ];

    const diversityScore = charSets.filter(set => set.test(password)).length;
    const lengthEntropy = password.length > 10;

    return diversityScore >= 4 && lengthEntropy;
  }

  updateStrengthIndicator(strength) {
    const fillElement = this.strengthIndicator.querySelector('.strength-fill');
    const textElement = this.strengthIndicator.querySelector('.strength-text');
    const scoreElement = this.strengthIndicator.querySelector('.strength-score');

    // 바 채우기 및 색상
    const percentage = (strength.score / 5) * 100;
    fillElement.style.width = `${percentage}%`;
    fillElement.className = `strength-fill ${strength.level}`;

    // 텍스트 업데이트
    textElement.textContent = strength.text;
    scoreElement.textContent = `${strength.score}/5`;

    // 색상 적용
    const colors = {
      'none': '#e5e7eb',
      'very-weak': '#dc2626',
      'weak': '#ea580c',
      'fair': '#ca8a04',
      'good': '#16a34a',
      'excellent': '#059669'
    };

    textElement.style.color = colors[strength.level];
    scoreElement.style.color = colors[strength.level];
  }

  // 외부에서 호출할 수 있는 검증 메서드
  isPasswordValid() {
    const strength = this.validatePassword();
    return strength.score >= 3; // 보통 이상이어야 유효
  }

  getPasswordStrength() {
    return this.validatePassword();
  }
}

// 기존 ProCoreRegisterForm 클래스 수정
class ProCoreRegisterForm {
  constructor() {
    this.form = document.getElementById('registerForm');
    this.fields = {
      email: document.getElementById('email'),
      password: document.getElementById('password'),
      confirmPassword: document.getElementById('confirmPassword'),
      nickname: document.getElementById('nickname'),
      phone: document.getElementById('phone')
    };

    // 강화된 비밀번호 검증기 추가
    this.passwordValidator = new EnhancedPasswordValidator();

    this.init();
  }

  init() {
    this.setupEventListeners();
    this.setupPhoneFormatter();
    console.log('ProCore 강화된 회원가입 폼 초기화 완료');
  }

  setupEventListeners() {
    // 실시간 유효성 검사
    this.fields.email.addEventListener('blur', () => this.validateEmail());
    this.fields.password.addEventListener('input', () => this.validatePassword());
    this.fields.confirmPassword.addEventListener('input', () => this.validateConfirmPassword());
    this.fields.nickname.addEventListener('blur', () => this.validateNickname());
    this.fields.phone.addEventListener('blur', () => this.validatePhone());

    // 폼 제출 이벤트
    this.form.addEventListener('submit', (e) => this.handleSubmit(e));

    // 입력 중 에러 상태 제거
    Object.values(this.fields).forEach(field => {
      field.addEventListener('input', () => {
        this.clearFieldError(field);
      });
    });

    // 중복확인 버튼들
    const checkEmailBtn = document.getElementById('checkEmailBtn');
    if (checkEmailBtn) {
      checkEmailBtn.addEventListener('click', () => this.checkEmailDuplicate());
    }

    const checkNicknameBtn = document.getElementById('checkNicknameBtn');
    if (checkNicknameBtn) {
      checkNicknameBtn.addEventListener('click', () => this.checkNicknameDuplicate());
    }
  }

  // 강화된 비밀번호 검증
  validatePassword() {
    const password = this.fields.password.value;
    const strength = this.passwordValidator.validatePassword();

    if (!password) {
      this.setFieldError(this.fields.password, '비밀번호를 입력해주세요.');
      return false;
    }

    if (strength.score < 3) {
      this.setFieldError(this.fields.password,
          `비밀번호가 너무 약합니다. 현재 강도: ${strength.text} (최소 "보통" 이상 필요)`);
      return false;
    }

    this.setFieldSuccess(this.fields.password);
    return true;
  }

  validateConfirmPassword() {
    const password = this.fields.password.value;
    const confirmPassword = this.fields.confirmPassword.value;

    if (!confirmPassword) {
      this.setFieldError(this.fields.confirmPassword, '비밀번호 확인을 입력해주세요.');
      return false;
    }

    if (password !== confirmPassword) {
      this.setFieldError(this.fields.confirmPassword, '비밀번호가 일치하지 않습니다.');
      return false;
    }

    this.setFieldSuccess(this.fields.confirmPassword);
    return true;
  }

  // 나머지 메서드들은 기존과 동일...
  validateEmail() {
    const email = this.fields.email.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
      this.setFieldError(this.fields.email, '이메일을 입력해주세요.');
      return false;
    }

    if (!emailRegex.test(email)) {
      this.setFieldError(this.fields.email, '올바른 이메일 형식을 입력해주세요.');
      return false;
    }

    this.setFieldSuccess(this.fields.email);
    return true;
  }

  validateNickname() {
    const nickname = this.fields.nickname.value.trim();

    if (!nickname) {
      this.setFieldError(this.fields.nickname, '닉네임을 입력해주세요.');
      return false;
    }

    if (nickname.length < 2 || nickname.length > 10) {
      this.setFieldError(this.fields.nickname, '닉네임은 2-10자 사이여야 합니다.');
      return false;
    }

    const nicknameRegex = /^[a-zA-Z0-9가-힣_]+$/;
    if (!nicknameRegex.test(nickname)) {
      this.setFieldError(this.fields.nickname, '닉네임은 영문, 숫자, 한글, 언더스코어만 사용 가능합니다.');
      return false;
    }

    this.setFieldSuccess(this.fields.nickname);
    return true;
  }

  setupPhoneFormatter() {
    const phoneField = this.fields.phone;

    phoneField.addEventListener('input', (e) => {
      let value = e.target.value.replace(/\D/g, '');

      if (value.length >= 11) {
        value = value.substring(0, 11);
        value = value.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
      } else if (value.length >= 7) {
        value = value.replace(/(\d{3})(\d{4})/, '$1-$2');
      } else if (value.length >= 3) {
        value = value.replace(/(\d{3})/, '$1-');
      }

      e.target.value = value;
    });
  }

  validatePhone() {
    const phone = this.fields.phone.value.trim();

    if (!phone) {
      this.clearFieldError(this.fields.phone);
      return true;
    }

    const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;
    if (!phoneRegex.test(phone)) {
      this.setFieldError(this.fields.phone, '올바른 전화번호 형식을 입력해주세요. (예: 010-1234-5678)');
      return false;
    }

    this.setFieldSuccess(this.fields.phone);
    return true;
  }

  checkEmailDuplicate() {
    const email = this.fields.email.value.trim();
    const checkResult = document.getElementById('emailCheckResult');
    const checkBtn = document.getElementById('checkEmailBtn');

    if (!this.validateEmail()) {
      return;
    }

    checkBtn.disabled = true;
    checkBtn.textContent = '확인중...';
    checkResult.textContent = '';

    fetch('/member/check-email', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `email=${encodeURIComponent(email)}`
    })
        .then(response => response.json())
        .then(data => {
          if (data.success) {
            checkResult.style.color = '#16a34a';
            checkResult.textContent = data.message;
            this.setFieldSuccess(this.fields.email);
          } else {
            checkResult.style.color = '#dc2626';
            checkResult.textContent = data.message;
            this.setFieldError(this.fields.email, '이미 사용 중인 이메일입니다.');
          }
        })
        .catch(error => {
          console.error('Error:', error);
          checkResult.style.color = '#dc2626';
          checkResult.textContent = '중복확인 중 오류가 발생했습니다.';
        })
        .finally(() => {
          checkBtn.disabled = false;
          checkBtn.textContent = '중복확인';
        });
  }

  checkNicknameDuplicate() {
    const nickname = this.fields.nickname.value.trim();
    const checkResult = document.getElementById('nicknameCheckResult');
    const checkBtn = document.getElementById('checkNicknameBtn');

    if (!this.validateNickname()) {
      return;
    }

    checkBtn.disabled = true;
    checkBtn.textContent = '확인중...';
    checkResult.textContent = '';

    fetch('/member/check-nickname', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `nickname=${encodeURIComponent(nickname)}`
    })
        .then(response => response.json())
        .then(data => {
          if (data.success) {
            checkResult.style.color = '#16a34a';
            checkResult.textContent = data.message;
            this.setFieldSuccess(this.fields.nickname);
          } else {
            checkResult.style.color = '#dc2626';
            checkResult.textContent = data.message;
            this.setFieldError(this.fields.nickname, '이미 사용 중인 닉네임입니다.');
          }
        })
        .catch(error => {
          console.error('Error:', error);
          checkResult.style.color = '#dc2626';
          checkResult.textContent = '중복확인 중 오류가 발생했습니다.';
        })
        .finally(() => {
          checkBtn.disabled = false;
          checkBtn.textContent = '중복확인';
        });
  }

  handleSubmit(e) {
    e.preventDefault();

    const isEmailValid = this.validateEmail();
    const isPasswordValid = this.validatePassword();
    const isConfirmPasswordValid = this.validateConfirmPassword();
    const isNicknameValid = this.validateNickname();
    const isPhoneValid = this.validatePhone();

    const isFormValid = isEmailValid && isPasswordValid && isConfirmPasswordValid &&
        isNicknameValid && isPhoneValid;

    if (!isFormValid) {
      const firstError = document.querySelector('.form-group.error input');
      if (firstError) {
        firstError.focus();
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      return;
    }

    // 중복확인 여부 체크
    const emailCheckResult = document.getElementById('emailCheckResult');
    const nicknameCheckResult = document.getElementById('nicknameCheckResult');

    if (!emailCheckResult || !emailCheckResult.textContent.includes('사용 가능')) {
      alert('이메일 중복확인을 해주세요.');
      this.fields.email.focus();
      return;
    }

    if (!nicknameCheckResult || !nicknameCheckResult.textContent.includes('사용 가능')) {
      alert('닉네임 중복확인을 해주세요.');
      this.fields.nickname.focus();
      return;
    }

    this.showLoading();
    setTimeout(() => {
      this.form.submit();
    }, 500);
  }

  // UI 업데이트 메서드들
  setFieldError(field, message) {
    const formGroup = field.closest('.form-group');
    const errorElement = formGroup.querySelector('.form-error');

    formGroup.classList.remove('success', 'loading');
    formGroup.classList.add('error');
    field.classList.add('error');
    field.classList.remove('success');

    if (errorElement) {
      errorElement.textContent = message;
    }

    // 중복확인 결과 초기화
    if (field === this.fields.email) {
      const checkResult = document.getElementById('emailCheckResult');
      if (checkResult) checkResult.textContent = '';
    }
    if (field === this.fields.nickname) {
      const checkResult = document.getElementById('nicknameCheckResult');
      if (checkResult) checkResult.textContent = '';
    }
  }

  setFieldSuccess(field) {
    const formGroup = field.closest('.form-group');
    const errorElement = formGroup.querySelector('.form-error');

    formGroup.classList.remove('error', 'loading');
    formGroup.classList.add('success');
    field.classList.add('success');
    field.classList.remove('error');

    if (errorElement) {
      errorElement.textContent = '';
    }
  }

  clearFieldError(field) {
    const formGroup = field.closest('.form-group');
    const errorElement = formGroup.querySelector('.form-error');

    formGroup.classList.remove('error', 'success', 'loading');
    field.classList.remove('error', 'success');

    if (errorElement) {
      errorElement.textContent = '';
    }

    // 중복확인 결과 초기화
    if (field === this.fields.email) {
      const checkResult = document.getElementById('emailCheckResult');
      if (checkResult) checkResult.textContent = '';
    }
    if (field === this.fields.nickname) {
      const checkResult = document.getElementById('nicknameCheckResult');
      if (checkResult) checkResult.textContent = '';
    }
  }

  showLoading() {
    const submitBtn = this.form.querySelector('button[type="submit"]');
    const btnText = submitBtn.querySelector('.btn-text');
    const btnLoading = submitBtn.querySelector('.btn-loading');

    submitBtn.disabled = true;
    btnText.style.opacity = '0';
    btnLoading.style.display = 'block';
  }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  new ProCoreRegisterForm();
});

window.ProCoreRegisterForm = ProCoreRegisterForm;
window.EnhancedPasswordValidator = EnhancedPasswordValidator;