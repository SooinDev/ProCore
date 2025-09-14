// ProCore 로그인 폼 유효성 검사 및 인터랙션
class ProCoreLoginForm {
  constructor() {
    this.form = document.getElementById('loginForm');
    this.emailField = document.getElementById('email');
    this.passwordField = document.getElementById('password');
    this.init();
  }

  init() {
    this.setupEventListeners();
    this.setupRememberMe();
    console.log('ProCore 로그인 폼 초기화 완료');
  }

  setupEventListeners() {
    // 실시간 유효성 검사
    this.emailField.addEventListener('blur', () => this.validateEmail());
    this.passwordField.addEventListener('blur', () => this.validatePassword());

    // 폼 제출 이벤트
    this.form.addEventListener('submit', (e) => this.handleSubmit(e));

    // 입력 중 에러 상태 제거
    [this.emailField, this.passwordField].forEach(field => {
      field.addEventListener('input', () => {
        this.clearFieldError(field);
      });
    });

    // Enter 키로 폼 제출
    this.form.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        this.handleSubmit(e);
      }
    });
  }

  setupRememberMe() {
    const rememberCheckbox = document.getElementById('rememberMe');
    const savedEmail = localStorage.getItem('procoreRememberedEmail');

    // 저장된 이메일이 있으면 복원
    if (savedEmail) {
      this.emailField.value = savedEmail;
      rememberCheckbox.checked = true;
    }

    // 체크박스 상태 변경시 처리
    rememberCheckbox.addEventListener('change', () => {
      if (!rememberCheckbox.checked) {
        localStorage.removeItem('procoreRememberedEmail');
      }
    });
  }

  validateEmail() {
    const email = this.emailField.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!email) {
      this.setFieldError(this.emailField, '이메일을 입력해주세요.');
      return false;
    }

    if (!emailRegex.test(email)) {
      this.setFieldError(this.emailField, '올바른 이메일 형식을 입력해주세요.');
      return false;
    }

    this.setFieldSuccess(this.emailField);
    return true;
  }

  validatePassword() {
    const password = this.passwordField.value;

    if (!password) {
      this.setFieldError(this.passwordField, '비밀번호를 입력해주세요.');
      return false;
    }

    this.setFieldSuccess(this.passwordField);
    return true;
  }

  handleSubmit(e) {
    e.preventDefault();

    // 유효성 검사
    const isEmailValid = this.validateEmail();
    const isPasswordValid = this.validatePassword();

    if (!isEmailValid || !isPasswordValid) {
      // 첫 번째 에러 필드로 포커스
      const firstError = document.querySelector('.form-group.error input');
      if (firstError) {
        firstError.focus();
      }
      return;
    }

    // 이메일 기억하기 처리
    const rememberCheckbox = document.getElementById('rememberMe');
    if (rememberCheckbox.checked) {
      localStorage.setItem('procoreRememberedEmail', this.emailField.value);
    } else {
      localStorage.removeItem('procoreRememberedEmail');
    }

    // 로딩 상태 표시
    this.showLoading();

    // 폼 제출
    setTimeout(() => {
      this.form.submit();
    }, 500);
  }

  setFieldError(field, message) {
    const formGroup = field.closest('.form-group');
    const errorElement = formGroup.querySelector('.form-error');

    formGroup.classList.remove('success');
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

    formGroup.classList.remove('error');
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

    formGroup.classList.remove('error', 'success');
    field.classList.remove('error', 'success');

    if (errorElement) {
      errorElement.textContent = '';
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
  new ProCoreLoginForm();
});

// 전역 함수로 내보내기
window.ProCoreLoginForm = ProCoreLoginForm;