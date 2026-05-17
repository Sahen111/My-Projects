/* =============================================
   BITTER BITES — site.js
   Cart, Animations, Enquiry System
   ============================================= */

'use strict';

/* ─────────────────────────────────────────────
   PRODUCT DATA
───────────────────────────────────────────── */
const PRODUCTS = [
    {
        id: 1,
        name: "6 Cupcake Bouquet",
        category: "bouquets",
        categoryLabel: "Cupcake Bouquets",
        price: 230,
        desc: "Beautifully arranged cupcakes shaped into a stunning floral bouquet. Available in vanilla or chocolate.",
        badge: "Best Seller",
        img: "https://images.unsplash.com/photo-1614707267537-b85aaf00c4b7?w=500&q=80"
    },
    {
        id: 2,
        name: "12 Cupcake Bouquet",
        category: "bouquets",
        categoryLabel: "Cupcake Bouquets",
        price: 420,
        desc: "A grand bouquet of 12 stunning cupcakes — the perfect centrepiece gift for any occasion.",
        badge: "Popular",
        img: "https://images.unsplash.com/photo-1557806767-1f1a9df8e5a3?w=500&q=80"
    },
    {
        id: 3,
        name: "Classic Cupcakes × 6",
        category: "cupcakes",
        categoryLabel: "Classic Cupcake Boxes",
        price: 130,
        desc: "Six delicious cupcakes in a pretty box. Vanilla or chocolate. Optional fillings available.",
        badge: null,
        img: "https://images.unsplash.com/photo-1587668178277-295251f900ce?w=500&q=80"
    },
    {
        id: 4,
        name: "Classic Cupcakes × 12",
        category: "cupcakes",
        categoryLabel: "Classic Cupcake Boxes",
        price: 190,
        desc: "A dozen gorgeous cupcakes. Perfect for parties, celebrations, or just treating yourself.",
        badge: "Value",
        img: "https://images.unsplash.com/photo-1576618148400-f54bed99fcfd?w=500&q=80"
    },
    {
        id: 5,
        name: "Classic Cupcakes × 24",
        category: "cupcakes",
        categoryLabel: "Classic Cupcake Boxes",
        price: 250,
        desc: "Two dozen cupcakes — ideal for office events, birthdays, and big celebrations.",
        badge: "Party Pack",
        img: "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=500&q=80"
    },
    {
        id: 6,
        name: "Brownie Box × 6",
        category: "brownies",
        categoryLabel: "Brownies",
        price: 100,
        desc: "Rich, fudgy chocolate brownies baked to perfection. Six indulgent pieces per box.",
        badge: null,
        img: "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=500&q=80"
    },
    {
        id: 7,
        name: "Brownie Box × 12",
        category: "brownies",
        categoryLabel: "Brownies",
        price: 180,
        desc: "Twelve decadent brownies for serious chocolate lovers. Great for sharing… or not.",
        badge: "Best Value",
        img: "https://images.unsplash.com/photo-1617309568012-5d4066a26f48?w=500&q=80"
    },
    {
        id: 8,
        name: "Cookie Box × 6",
        category: "cookies",
        categoryLabel: "Cookies",
        price: 80,
        desc: "Freshly baked, golden cookies packed with flavour. A classic treat for all ages.",
        badge: null,
        img: "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=500&q=80"
    },
    {
        id: 9,
        name: "Cookie Box × 12",
        category: "cookies",
        categoryLabel: "Cookies",
        price: 150,
        desc: "A baker's dozen of freshly baked cookies in a pretty gift box. Perfect as a thoughtful gift.",
        badge: "Gift Idea",
        img: "https://images.unsplash.com/photo-1548365328-8c6db3220e4d?w=500&q=80"
    },
    {
        id: 10,
        name: "Mini Cake — Small",
        category: "cakes",
        categoryLabel: "Mini Cakes",
        price: 220,
        desc: "Serves 6–8 people. A beautifully decorated small cake, customisable in flavour and colour.",
        badge: "From R220",
        img: "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=500&q=80"
    },
    {
        id: 11,
        name: "Mini Cake — Medium",
        category: "cakes",
        categoryLabel: "Mini Cakes",
        price: 300,
        desc: "Serves 10–12 people. A stunning centrepiece cake perfect for any celebration or gathering.",
        badge: "From R300",
        img: "https://images.unsplash.com/photo-1535141192574-5d4897c12636?w=500&q=80"
    },
    {
        id: 12,
        name: "Treat Box — Small",
        category: "treats",
        categoryLabel: "Treat Boxes",
        price: 180,
        desc: "Six items — a mixed assortment of cupcakes, brownies & cookies in a gorgeous gift box.",
        badge: "Mixed",
        img: "https://images.unsplash.com/photo-1559620192-032c4bc4674e?w=500&q=80"
    },
    {
        id: 13,
        name: "Treat Box — Large",
        category: "treats",
        categoryLabel: "Treat Boxes",
        price: 320,
        desc: "Twelve items of mixed cupcakes, brownies & cookies. The ultimate gift for someone special.",
        badge: "Perfect Gift",
        img: "https://images.unsplash.com/photo-1586985288123-2495f577dd14?w=500&q=80"
    }
];

/* ─────────────────────────────────────────────
   CART STATE
───────────────────────────────────────────── */
let cart = [];

function getCartTotal() {
    return cart.reduce((sum, item) => sum + item.price * item.qty, 0);
}

function getCartCount() {
    return cart.reduce((sum, item) => sum + item.qty, 0);
}

/* ─────────────────────────────────────────────
   RENDER PRODUCTS
───────────────────────────────────────────── */
function renderProducts() {
    const grid = document.getElementById('productsGrid');
    if (!grid) return;

    grid.innerHTML = '';

    PRODUCTS.forEach((p, idx) => {
        const card = document.createElement('div');
        card.className = 'product-card reveal';
        card.dataset.category = p.category;
        card.style.transitionDelay = `${(idx % 4) * 0.08}s`;

        card.innerHTML = `
      <div class="product-img-wrap">
        <img src="${p.img}" alt="${p.name}" class="product-img" loading="lazy" onerror="this.src='https://images.unsplash.com/photo-1486427944299-d1955d23e34d?w=500&q=80'">
        ${p.badge ? `<span class="product-badge">${p.badge}</span>` : ''}
        <button class="product-wishlist" aria-label="Favourite" title="Add to favourites">🤍</button>
      </div>
      <div class="product-body">
        <div class="product-category">${p.categoryLabel}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-desc">${p.desc}</div>
        <div class="product-footer">
          <div class="product-price">R${p.price}<span>  ea.</span></div>
          <button class="add-to-cart-btn" data-id="${p.id}">
            <span>🛒</span> Add
          </button>
        </div>
      </div>
    `;

        grid.appendChild(card);
    });

    // Add to cart listeners
    grid.querySelectorAll('.add-to-cart-btn').forEach(btn => {
        btn.addEventListener('click', () => handleAddToCart(Number(btn.dataset.id), btn));
    });

    // Wishlist toggle
    grid.querySelectorAll('.product-wishlist').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.textContent = btn.textContent === '🤍' ? '❤️' : '🤍';
        });
    });

    // Reveal on mount
    setTimeout(() => setupScrollReveal(), 100);
}

/* ─────────────────────────────────────────────
   ADD TO CART
───────────────────────────────────────────── */
function handleAddToCart(productId, btn) {
    const product = PRODUCTS.find(p => p.id === productId);
    if (!product) return;

    const existing = cart.find(i => i.id === productId);
    if (existing) {
        existing.qty += 1;
    } else {
        cart.push({ ...product, qty: 1 });
    }

    // Button feedback
    btn.classList.add('added');
    btn.innerHTML = '<span>✓</span> Added!';
    setTimeout(() => {
        btn.classList.remove('added');
        btn.innerHTML = '<span>🛒</span> Add';
    }, 1400);

    updateCartUI();
    showToast(`${product.name} added to cart! 🛒`);
    bumpCartBadge();
}

/* ─────────────────────────────────────────────
   UPDATE CART UI
───────────────────────────────────────────── */
function updateCartUI() {
    const count = getCartCount();
    const total = getCartTotal();

    // Update badge
    document.querySelectorAll('.cart-badge, .nav-cart-count').forEach(el => {
        el.textContent = count;
    });

    // Cart items
    const itemsEl = document.getElementById('cartItems');
    const emptyEl = document.getElementById('cartEmpty');
    const totalEl = document.getElementById('cartTotal');
    const enquireBtn = document.getElementById('cartEnquireBtn');

    if (!itemsEl) return;

    if (cart.length === 0) {
        itemsEl.innerHTML = '';
        if (emptyEl) emptyEl.style.display = 'block';
        if (totalEl) totalEl.textContent = 'R0';
        if (enquireBtn) enquireBtn.disabled = true;
        return;
    }

    if (emptyEl) emptyEl.style.display = 'none';
    if (enquireBtn) enquireBtn.disabled = false;
    if (totalEl) totalEl.textContent = `R${total}`;

    itemsEl.innerHTML = '';
    cart.forEach(item => {
        const el = document.createElement('div');
        el.className = 'cart-item';
        el.innerHTML = `
      <img src="${item.img}" alt="${item.name}" class="cart-item-img" onerror="this.src='https://images.unsplash.com/photo-1486427944299-d1955d23e34d?w=100&q=60'">
      <div class="cart-item-info">
        <div class="cart-item-name">${item.name}</div>
        <div class="cart-item-price">R${item.price} × ${item.qty} = R${item.price * item.qty}</div>
      </div>
      <div class="cart-item-qty">
        <button class="qty-btn" data-action="dec" data-id="${item.id}">−</button>
        <span class="qty-num">${item.qty}</span>
        <button class="qty-btn" data-action="inc" data-id="${item.id}">+</button>
      </div>
    `;
        itemsEl.appendChild(el);
    });

    itemsEl.querySelectorAll('.qty-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = Number(btn.dataset.id);
            const action = btn.dataset.action;
            const item = cart.find(i => i.id === id);
            if (!item) return;

            if (action === 'inc') {
                item.qty += 1;
            } else {
                item.qty -= 1;
                if (item.qty <= 0) cart = cart.filter(i => i.id !== id);
            }
            updateCartUI();
        });
    });
}

function bumpCartBadge() {
    document.querySelectorAll('.cart-badge').forEach(el => {
        el.classList.remove('bump');
        void el.offsetWidth;
        el.classList.add('bump');
    });
}

/* ─────────────────────────────────────────────
   CART SIDEBAR TOGGLE
───────────────────────────────────────────── */
function openCart() {
    document.getElementById('cartSidebar')?.classList.add('open');
    document.getElementById('cartOverlay')?.classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeCart() {
    document.getElementById('cartSidebar')?.classList.remove('open');
    document.getElementById('cartOverlay')?.classList.remove('open');
    document.body.style.overflow = '';
}

/* ─────────────────────────────────────────────
   ENQUIRY MODAL
───────────────────────────────────────────── */
function openEnquiryModal() {
    closeCart();
    const modal = document.getElementById('enquiryModal');
    if (!modal) return;

    // Populate cart summary
    const summaryEl = document.getElementById('modalCartItems');
    if (summaryEl) {
        if (cart.length === 0) {
            summaryEl.innerHTML = '<div class="modal-cart-item"><span class="name">No items selected</span></div>';
        } else {
            summaryEl.innerHTML = cart.map(item => `
        <div class="modal-cart-item">
          <span class="name">${item.name} × ${item.qty}</span>
          <span class="price">R${item.price * item.qty}</span>
        </div>
      `).join('');
        }
    }

    const totalEl = document.getElementById('modalTotal');
    if (totalEl) totalEl.textContent = `R${getCartTotal()}`;

    modal.classList.add('open');
    document.body.style.overflow = 'hidden';

    // Reset form
    const form = modal.querySelector('#enquiryForm');
    if (form) form.reset();
    const successEl = modal.querySelector('.success-state');
    const formEl = modal.querySelector('.enquiry-form-wrap');
    if (successEl) successEl.classList.remove('visible');
    if (formEl) formEl.style.display = '';
}

function closeEnquiryModal() {
    document.getElementById('enquiryModal')?.classList.remove('open');
    document.body.style.overflow = '';
}

function handleEnquirySubmit(e) {
    e.preventDefault();

    const nameEl = document.getElementById('enquiryName');
    const phoneEl = document.getElementById('enquiryPhone');
    const commentsEl = document.getElementById('enquiryComments');

    const name = nameEl?.value.trim() || '';
    const phone = phoneEl?.value.trim() || '';
    const comments = commentsEl?.value.trim() || '';

    if (!name || !phone) {
        showToast('Please fill in your name and phone number 💕');
        return;
    }

    // Build email body
    const cartLines = cart.length
        ? cart.map(i => `  • ${i.name} × ${i.qty} = R${i.price * i.qty}`).join('%0A')
        : '  • No items selected';

    const totalLine = `%0A%0ATotal Estimate: R${getCartTotal()}`;
    const noteLine = `%0A%0ANote: Prices may vary for custom designs. 50%25 deposit required to confirm.`;
    const customerLine = `Customer: ${name}%0APhone: ${phone}`;
    const commentLine = comments ? `%0AComments: ${comments}` : '';

    const subject = `Bitter Bites Enquiry — ${name}`;
    const body = `Hello Bitter Bites!%0A%0AI'd like to enquire about the following items:%0A%0A${cartLines}${totalLine}${noteLine}%0A%0A${customerLine}${commentLine}%0A%0AKind regards,%0A${name}`;

    const mailtoLink = `mailto:ramble.comp@gmail.com?subject=${encodeURIComponent(subject)}&body=${body}`;

    // Show success state
    const formEl = document.querySelector('.enquiry-form-wrap');
    const successEl = document.querySelector('.success-state');
    if (formEl) formEl.style.display = 'none';
    if (successEl) successEl.classList.add('visible');

    // Open mail client after brief delay for UX
    setTimeout(() => {
        window.location.href = mailtoLink;
    }, 800);

    // Clear cart
    setTimeout(() => {
        cart = [];
        updateCartUI();
    }, 2000);
}

/* ─────────────────────────────────────────────
   CATEGORY FILTER TABS
───────────────────────────────────────────── */
function setupCategoryTabs() {
    const tabs = document.querySelectorAll('.cat-tab');
    const grid = document.getElementById('productsGrid');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            const cat = tab.dataset.cat;
            const cards = grid?.querySelectorAll('.product-card');

            cards?.forEach(card => {
                if (cat === 'all' || card.dataset.category === cat) {
                    card.classList.remove('hidden');
                } else {
                    card.classList.add('hidden');
                }
            });
        });
    });
}

/* ─────────────────────────────────────────────
   SCROLL ANIMATIONS
───────────────────────────────────────────── */
function setupScrollReveal() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, { threshold: 0.1, rootMargin: '0px 0px -40px 0px' });

    document.querySelectorAll('.reveal').forEach(el => observer.observe(el));
}

/* ─────────────────────────────────────────────
   NAVBAR SCROLL EFFECT
───────────────────────────────────────────── */
function setupNavbar() {
    const navbar = document.getElementById('mainNav');
    const onScroll = () => {
        if (window.scrollY > 60) {
            navbar?.classList.add('scrolled');
        } else {
            navbar?.classList.remove('scrolled');
        }
    };
    window.addEventListener('scroll', onScroll, { passive: true });
}

/* ─────────────────────────────────────────────
   SPRINKLE PARTICLE EFFECT
───────────────────────────────────────────── */
function setupSprinkles() {
    const canvas = document.getElementById('sprinklesCanvas');
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    let W = canvas.width = window.innerWidth;
    let H = canvas.height = window.innerHeight;

    const COLORS = ['#F06292', '#F48FB1', '#E91E8C', '#F8BBD9', '#C2185B', '#FFB3C8', '#FF69B4'];
    const COUNT = 55;

    const sprinkles = Array.from({ length: COUNT }, () => ({
        x: Math.random() * W,
        y: Math.random() * H,
        w: 4 + Math.random() * 6,
        h: 1.5 + Math.random() * 2,
        angle: Math.random() * Math.PI,
        speed: 0.2 + Math.random() * 0.5,
        drift: (Math.random() - 0.5) * 0.4,
        color: COLORS[Math.floor(Math.random() * COLORS.length)],
        spin: (Math.random() - 0.5) * 0.04
    }));

    function draw() {
        ctx.clearRect(0, 0, W, H);
        sprinkles.forEach(s => {
            ctx.save();
            ctx.translate(s.x, s.y);
            ctx.rotate(s.angle);
            ctx.fillStyle = s.color;
            ctx.beginPath();
            ctx.roundRect(-s.w / 2, -s.h / 2, s.w, s.h, s.h / 2);
            ctx.fill();
            ctx.restore();

            s.y += s.speed;
            s.x += s.drift;
            s.angle += s.spin;

            if (s.y > H + 10) { s.y = -10; s.x = Math.random() * W; }
            if (s.x < -10) s.x = W + 10;
            if (s.x > W + 10) s.x = -10;
        });
        requestAnimationFrame(draw);
    }

    draw();

    window.addEventListener('resize', () => {
        W = canvas.width = window.innerWidth;
        H = canvas.height = window.innerHeight;
    });
}

/* ─────────────────────────────────────────────
   TOAST NOTIFICATION
───────────────────────────────────────────── */
function showToast(message) {
    let toast = document.getElementById('toastEl');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toastEl';
        toast.className = 'toast';
        document.body.appendChild(toast);
    }

    toast.textContent = message;
    toast.classList.add('show');

    clearTimeout(toast._timer);
    toast._timer = setTimeout(() => toast.classList.remove('show'), 2800);
}

/* ─────────────────────────────────────────────
   SMOOTH SCROLL FOR ANCHOR LINKS
───────────────────────────────────────────── */
function setupSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(link => {
        link.addEventListener('click', e => {
            const target = document.querySelector(link.getAttribute('href'));
            if (target) {
                e.preventDefault();
                const offset = 80;
                const top = target.getBoundingClientRect().top + window.scrollY - offset;
                window.scrollTo({ top, behavior: 'smooth' });
            }
        });
    });
}

/* ─────────────────────────────────────────────
   INIT
───────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
    renderProducts();
    setupCategoryTabs();
    setupNavbar();
    setupSmoothScroll();
    setupSprinkles();
    updateCartUI();

    // Cart toggle buttons
    document.getElementById('navCartBtn')?.addEventListener('click', openCart);
    document.getElementById('cartOverlay')?.addEventListener('click', closeCart);
    document.getElementById('cartCloseBtn')?.addEventListener('click', closeCart);

    // Enquire button
    document.getElementById('cartEnquireBtn')?.addEventListener('click', openEnquiryModal);

    // Modal close
    document.getElementById('enquiryModal')?.addEventListener('click', e => {
        if (e.target === document.getElementById('enquiryModal')) closeEnquiryModal();
    });
    document.getElementById('modalCloseBtn')?.addEventListener('click', closeEnquiryModal);

    // Form submit
    document.getElementById('enquiryForm')?.addEventListener('submit', handleEnquirySubmit);

    // CTA order button
    document.getElementById('heroOrderBtn')?.addEventListener('click', () => {
        document.querySelector('#products')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    });

    // Close modal success
    document.getElementById('modalSuccessClose')?.addEventListener('click', closeEnquiryModal);

    // Scroll reveal
    setTimeout(setupScrollReveal, 200);
});