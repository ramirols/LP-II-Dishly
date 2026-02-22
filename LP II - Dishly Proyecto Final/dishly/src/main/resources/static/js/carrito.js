function toggleCart() {
    const cart = document.getElementById('cartSidebar');
    const overlay = document.getElementById('cartOverlay');
<<<<<<< HEAD
=======

>>>>>>> 3d1df5a539d2d7489d6edec0b93316de935d2f47
    const isOpen = !cart.classList.contains('translate-x-full');

    if (isOpen) {
        cart.classList.add('translate-x-full');
        overlay.classList.add('opacity-0', 'pointer-events-none');
<<<<<<< HEAD
        localStorage.setItem('cartState', 'closed');
    } else {
        cart.classList.remove('translate-x-full');
        overlay.classList.remove('opacity-0', 'pointer-events-none');
        localStorage.setItem('cartState', 'open');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname;
    
    const isCheckoutPage = path.includes('/cliente/checkout');
    const isLoginPage = path.includes('/auth/login');

    if (isCheckoutPage || isLoginPage) {
        // si esta en login o checkout, forzamos el cierre y limpiamos estado
        localStorage.setItem('cartState', 'closed');
        
        const cart = document.getElementById('cartSidebar');
        const overlay = document.getElementById('cartOverlay');
        if (cart && overlay) {
            cart.classList.add('translate-x-full');
            overlay.classList.add('opacity-0', 'pointer-events-none');
        }
    } else {
        if (localStorage.getItem('cartState') === 'open') {
            const cart = document.getElementById('cartSidebar');
            const overlay = document.getElementById('cartOverlay');
            if (cart && overlay) {
                cart.classList.remove('translate-x-full');
                overlay.classList.remove('opacity-0', 'pointer-events-none');
            }
        }
    }
});

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        const cart = document.getElementById('cartSidebar');
        if (cart && !cart.classList.contains('translate-x-full')) {
            toggleCart();
        }
    }
});

function updateQty(val, precioBase, platoId) {
    const input = document.getElementById('inputQty');
    const displayPrecio = document.getElementById('displayPrecio');
    const btn = document.getElementById('btnAddToCart');

    if(!input || !displayPrecio || !btn) return;

    let currentQty = parseInt(input.value) + val;
    if (currentQty < 1) currentQty = 1;

    input.value = currentQty;
    displayPrecio.innerText = (precioBase * currentQty).toFixed(2);
    
    btn.href = `/carrito/agregar/${platoId}?cantidad=${currentQty}`;
}

function updateCartServer(id, delta) {
    localStorage.setItem('cartState', 'open');
    const url = delta > 0 ? `/carrito/agregar/${id}` : `/carrito/restar/${id}`;
    window.location.href = url;
}
=======
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
>>>>>>> 3d1df5a539d2d7489d6edec0b93316de935d2f47
