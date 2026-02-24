new Swiper('.heroSwiper', {
  loop: true,
  autoplay: {
    delay: 5000,
    disableOnInteraction: false,
    pauseOnMouseEnter: true
  },
  pagination: {
    el: '.swiper-pagination',
    clickable: true
  },
  breakpoints: {
    640: { slidesPerView: 1 },
    1024: { slidesPerView: 1 }
  }
});

new Swiper('.categorySwiper', {
  slidesPerView: 2,
  loop: true,
  spaceBetween: 20,
  autoplay: {
    delay: 3000,
    disableOnInteraction: false,
    pauseOnMouseEnter: true
  },
  pagination: {
    el: '.swiper-pagination-category',
    clickable: true
  },
  breakpoints: {
    640: { slidesPerView: 3 },
    1024: { slidesPerView: 5 }
  }
});

new Swiper('.featuredSwiper', {
  slidesPerView: 1,
  loop: true,
  spaceBetween: 20,
  autoplay: {
    delay: 3000,
    disableOnInteraction: false,
    pauseOnMouseEnter: true
  },
  pagination: {
    el: '.swiper-pagination-featured',
    clickable: true
  },
  breakpoints: {
    640: { slidesPerView: 2 },
    1024: { slidesPerView: 3 }
  }
});