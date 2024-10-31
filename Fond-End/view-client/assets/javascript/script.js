document.addEventListener('DOMContentLoaded', () => {
    const bar = document.getElementById('bar');
    const close = document.getElementById('close');
    const nav = document.getElementById('navbar');
    const qtyInput = document.querySelector('.input-qty');
    const minusButton = document.querySelector('.minus');
    const plusButton = document.querySelector('.plus');

    if (bar) {
        bar.addEventListener('click', () => {
            nav.classList.add('active');
        });
    }

    if (close) {
        close.addEventListener('click', () => {
            nav.classList.remove('active');
        });
    }

    // Hàm để cập nhật giá trị
    function updateValue(delta) {
        let currentValue = parseInt(qtyInput.value);
        if (isNaN(currentValue)) currentValue = 1; // Đảm bảo giá trị mặc định là 1
        currentValue += delta;

        // Đảm bảo giá trị không vượt quá giới hạn
        if (currentValue < 1) currentValue = 1;
        if (currentValue > 100) currentValue = 100;

        qtyInput.value = currentValue;
    }

    // Sự kiện click cho button giảm
    if (minusButton) {
        minusButton.addEventListener('click', () => {
            updateValue(-1);
        });
    }

    // Sự kiện click cho button tăng
    if (plusButton) {
        plusButton.addEventListener('click', () => {
            updateValue(1);
        });
    }
});
