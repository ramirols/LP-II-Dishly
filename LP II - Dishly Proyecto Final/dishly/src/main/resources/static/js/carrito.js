function toggleCart() {
    const cart = document.getElementById('cartSidebar');
    const overlay = document.getElementById('cartOverlay');

    const isOpen = !cart.classList.contains('translate-x-full');

    if (isOpen) {
        cart.classList.add('translate-x-full');
        overlay.classList.add('opacity-0', 'pointer-events-none');
    } else {
        cart.classList.remove('translate-x-full');
        overlay.classList.remove('opacity-0', 'pointer-events-none');
    }
}

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        const cart = document.getElementById('cartSidebar');
        if (!cart.classList.contains('translate-x-full')) {
            toggleCart();
        }
    }
});