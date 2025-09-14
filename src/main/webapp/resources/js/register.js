// ProCore 회원가입 폼 유효성 검사 및 인터랙션
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
    this.init();
  }

  init() {
    this.setupEventListeners();
    this.setupPasswordStrength();
    this.setupPhoneFormatter();
    console.log('ProCore 회원가입 폼 초기화 완료');
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
  }

  setupPasswordStrength() {
    const passwordField = this.fields.password;
    const strengthFill = document.querySelector('.strength-fill');
    const strengthText = document.querySelector('.strength-text');

    passwordField.addEventListener('input', () => {
      const password = passwordField.value;
      const strength = this.calculatePasswordStrength(password);

      strengthFill.className = 'strength-fill ' + strength.level;
      strengthText.textContent = strength.text;
    });
  }

  calculatePasswordStrength(password) {
    if (!password) return { level: '', text: '비밀번호 강도' };

    let score = 0;
    const checks = {
      length: password.length >= 8,
      lowercase: /[a-z]/.test(password),
      uppercase: /[A-Z]/.test(password),
      numbers: /\d/.test(password),
      special: /[!@#$%^&*(),.?":{}|<>]/.test(password)
    };

    score = Object.values(checks).filter(Boolean).length;

    if (score < 2) return { level: 'weak', text: '약함' };
    if (score < 3) return { level: 'fair', text: '보통' };
    if (score < 4) return { level: 'good', text: '좋음' };
    return { level: 'strong', text: '강함' };
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

  // 유효성 검사 메서드들
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

  validatePassword() {
    const password = this.fields.password.value;

    if (!password) {
      this.setFieldError(this.fields.password, '비밀번호를 입력해주세요.');
      return false;
    }

    if (password.length < 8) {
      this.setFieldError(this.fields.password, '비밀번호는 8자 이상이어야 합니다.');
      return false;
    }

    const hasLetter = /[a-zA-Z]/.test(password);
    const hasNumber = /\d/.test(password);

    if (!(hasLetter && hasNumber)) {
      this.setFieldError(this.fields.password, '영문과 숫자를 포함해야 합니다.');
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
  }

  // 폼 제출 처리
  handleSubmit(e) {
    e.preventDefault();

    // 모든 필수 필드 유효성 검사
    const isEmailValid = this.validateEmail();
    const isPasswordValid = this.validatePassword();
    const isConfirmPasswordValid = this.validateConfirmPassword();
    const isNicknameValid = this.validateNickname();
    const isPhoneValid = this.validatePhone();

    const isFormValid = isEmailValid && isPasswordValid && isConfirmPasswordValid &&
        isNicknameValid && isPhoneValid;

    if (!isFormValid) {
      // 첫 번째 에러 필드로 스크롤
      const firstError = document.querySelector('.form-group.error input');
      if (firstError) {
        firstError.focus();
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      return;
    }

    // 로딩 상태 표시
    this.showLoading();

    // 폼 제출 (실제 서버로 전송)
    setTimeout(() => {
      this.form.submit();
    }, 500);
  }

  showLoading() {
    const submitBtn = this.form.querySelector('button[type="submit"]');
    const btnText = submitBtn.querySelector('.btn-text');
    const btnLoading = submitBtn.querySelector('.btn-loading');

    submitBtn.disabled = true;
    btnText.style.opacity = '0';
    btnLoading.style.display = 'block';
  }

  hideLoading() {
    const submitBtn = this.form.querySelector('button[type="submit"]');
    const btnText = submitBtn.querySelector('.btn-text');
    const btnLoading = submitBtn.querySelector('.btn-loading');

    submitBtn.disabled = false;
    btnText.style.opacity = '1';
    btnLoading.style.display = 'none';
  }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  new ProCoreRegisterForm();
});

// 전역 함수로 내보내기
window.ProCoreRegisterForm = ProCoreRegisterForm;