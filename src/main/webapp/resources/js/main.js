// ProCore 메인페이지 JavaScript
class ProCoreLanding {
  constructor() {
    this.init();
  }

  init() {
    this.setupNavbar();
    this.setupAnimations();
    this.setupInteractions();
    this.setupAccessibility();
    console.log('ProCore 랜딩페이지가 로드되었습니다.');
  }

  // Navbar scroll effects
  setupNavbar() {
    const navbar = document.querySelector('.navbar');
    let lastScrollY = window.scrollY;
    let ticking = false;

    const updateNavbar = () => {
      const scrollY = window.scrollY;
      const scrollDirection = scrollY > lastScrollY ? 'down' : 'up';

      // Add/remove scrolled class for styling
      if (scrollY > 20) {
        navbar.classList.add('navbar-scrolled');
      } else {
        navbar.classList.remove('navbar-scrolled');
      }

      // Hide navbar on scroll down, show on scroll up
      if (scrollY > 100) {
        if (scrollDirection === 'down' && scrollY - lastScrollY > 5) {
          navbar.style.transform = 'translateY(-100%)';
        } else if (scrollDirection === 'up' && lastScrollY - scrollY > 5) {
          navbar.style.transform = 'translateY(0)';
        }
      } else {
        navbar.style.transform = 'translateY(0)';
      }

      lastScrollY = scrollY;
      ticking = false;
    };

    const onScroll = () => {
      if (!ticking) {
        requestAnimationFrame(updateNavbar);
        ticking = true;
      }
    };

    window.addEventListener('scroll', onScroll, { passive: true });

    // Add transition after initial load
    setTimeout(() => {
      navbar.style.transition = 'transform 0.3s cubic-bezier(0.4, 0.0, 0.2, 1)';
    }, 100);
  }

  // Intersection Observer for animations
  setupAnimations() {
    const observerOptions = {
      threshold: 0.1,
      rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.style.opacity = '1';
          entry.target.style.transform = 'translateY(0)';

          // Special handling for feature cards
          if (entry.target.classList.contains('feature-card')) {
            const delay = Array.from(entry.target.parentNode.children).indexOf(entry.target) * 100;
            setTimeout(() => {
              entry.target.style.opacity = '1';
              entry.target.style.transform = 'translateY(0)';
            }, delay);
          }
        }
      });
    }, observerOptions);

    // Observe elements for animation
    const animatedElements = document.querySelectorAll('.feature-card, .visual-card');
    animatedElements.forEach(el => {
      el.style.opacity = '0';
      el.style.transform = 'translateY(30px)';
      el.style.transition = 'opacity 0.6s cubic-bezier(0.4, 0.0, 0.2, 1), transform 0.6s cubic-bezier(0.4, 0.0, 0.2, 1)';
      observer.observe(el);
    });
  }

  // Interactive elements
  setupInteractions() {
    // Smooth scroll for internal links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
      anchor.addEventListener('click', (e) => {
        e.preventDefault();
        const target = document.querySelector(anchor.getAttribute('href'));
        if (target) {
          target.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
          });
        }
      });
    });

    // Enhanced button interactions
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary, .nav-cta');
    buttons.forEach(button => {
      // Ripple effect
      button.addEventListener('click', (e) => {
        const ripple = document.createElement('span');
        const rect = button.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = e.clientX - rect.left - size / 2;
        const y = e.clientY - rect.top - size / 2;

        ripple.style.cssText = `
                    position: absolute;
                    width: ${size}px;
                    height: ${size}px;
                    left: ${x}px;
                    top: ${y}px;
                    background: rgba(255, 255, 255, 0.3);
                    border-radius: 50%;
                    transform: scale(0);
                    animation: ripple 0.6s linear;
                    pointer-events: none;
                `;

        button.style.position = 'relative';
        button.style.overflow = 'hidden';
        button.appendChild(ripple);

        setTimeout(() => {
          ripple.remove();
        }, 600);
      });

      // Hover effects with proper touch handling
      button.addEventListener('mouseenter', () => {
        if (!window.matchMedia('(hover: none)').matches) {
          button.style.transform = 'translateY(-2px)';
        }
      });

      button.addEventListener('mouseleave', () => {
        button.style.transform = 'translateY(0)';
      });

      // Touch feedback
      button.addEventListener('touchstart', () => {
        button.style.transform = 'translateY(-1px) scale(0.98)';
      });

      button.addEventListener('touchend', () => {
        setTimeout(() => {
          button.style.transform = 'translateY(0) scale(1)';
        }, 150);
      });
    });

    // Visual card hover effect
    const visualCard = document.querySelector('.visual-card');
    if (visualCard) {
      let cardTiltTimeout;

      visualCard.addEventListener('mouseenter', () => {
        if (!window.matchMedia('(hover: none)').matches) {
          visualCard.style.transform = 'translateY(-8px) rotate(2deg) scale(1.02)';
        }
      });

      visualCard.addEventListener('mouseleave', () => {
        visualCard.style.transform = 'translateY(0) rotate(0deg) scale(1)';
      });

      // Parallax effect on mouse move
      visualCard.addEventListener('mousemove', (e) => {
        if (!window.matchMedia('(hover: none)').matches) {
          const rect = visualCard.getBoundingClientRect();
          const x = e.clientX - rect.left;
          const y = e.clientY - rect.top;

          const centerX = rect.width / 2;
          const centerY = rect.height / 2;

          const rotateX = (y - centerY) / centerY * -10;
          const rotateY = (x - centerX) / centerX * 10;

          clearTimeout(cardTiltTimeout);
          visualCard.style.transform = `translateY(-8px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale(1.02)`;

          cardTiltTimeout = setTimeout(() => {
            visualCard.style.transform = 'translateY(-8px) rotate(0deg) scale(1.02)';
          }, 1000);
        }
      });
    }
  }

  // Accessibility enhancements
  setupAccessibility() {
    // Keyboard navigation
    document.addEventListener('keydown', (e) => {
      if (e.key === 'Tab') {
        document.body.classList.add('keyboard-navigation');
      }
    });

    document.addEventListener('mousedown', () => {
      document.body.classList.remove('keyboard-navigation');
    });

    // Reduce motion for users who prefer it
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
      const style = document.createElement('style');
      style.textContent = `
                *, *::before, *::after {
                    animation-duration: 0.01ms !important;
                    animation-iteration-count: 1 !important;
                    transition-duration: 0.01ms !important;
                }
            `;
      document.head.appendChild(style);
    }

    // Enhanced focus indicators
    const focusableElements = document.querySelectorAll('a, button, [tabindex]:not([tabindex="-1"])');
    focusableElements.forEach(element => {
      element.addEventListener('focus', () => {
        element.style.outline = '2px solid #007aff';
        element.style.outlineOffset = '2px';
      });

      element.addEventListener('blur', () => {
        element.style.outline = '';
        element.style.outlineOffset = '';
      });
    });
  }

  // Performance monitoring
  measurePerformance() {
    if ('performance' in window) {
      window.addEventListener('load', () => {
        setTimeout(() => {
          const perf = performance.getEntriesByType('navigation')[0];
          console.log(`ProCore 페이지 로드 시간: ${Math.round(perf.loadEventEnd - perf.navigationStart)}ms`);

          if (perf.loadEventEnd - perf.navigationStart > 3000) {
            console.warn('페이지 로드 시간이 예상보다 느립니다');
          }
        }, 0);
      });
    }
  }
}

// CSS for ripple animation
const rippleCSS = `
    @keyframes ripple {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }
    
    .keyboard-navigation *:focus {
        outline: 2px solid #007aff !important;
        outline-offset: 2px !important;
    }
`;

const style = document.createElement('style');
style.textContent = rippleCSS;
document.head.appendChild(style);

// Initialize when DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', () => {
    new ProCoreLanding();
  });
} else {
  new ProCoreLanding();
}

// Export for potential use in other modules
window.ProCoreLanding = ProCoreLanding;