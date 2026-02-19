new Swiper('.heroSwiper', {
    loop: true,
    autoplay: { delay: 5000 },
    pagination: { el: '.swiper-pagination', clickable: true }
  });

  new Swiper('.categorySwiper', {
    slidesPerView: 2,
    spaceBetween: 20,
    breakpoints: {
      640: { slidesPerView: 3 },
      1024: { slidesPerView: 5 }
    }
});