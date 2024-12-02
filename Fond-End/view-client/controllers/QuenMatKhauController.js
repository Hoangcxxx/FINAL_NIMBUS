window.QuenMatKhauController = function ($scope, $http, $window) {
    const emailForm = document.getElementById('forgot-password-form');
    const verifyForm = document.getElementById('verify-code-form');
    const resetPasswordForm = document.getElementById('reset-password-form');
    const emailInput = document.getElementById('email');
    const errorMessage = document.getElementById('error-message');
    const recoveryCodeInput = document.getElementById('code');
    const newPasswordInput = document.getElementById('new-password');
    
    let recoveryCode = ''; // Lưu mã khôi phục khi xác nhận

    // Gửi yêu cầu quên mật khẩu
    emailForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const email = emailInput.value.trim();
        
        if (!email) {
            errorMessage.innerHTML = `<div class="alert">Vui lòng nhập email</div>`;
            return;
        }

        // Gửi yêu cầu tới API để gửi mã khôi phục
        fetch('http://localhost:8080/api/nguoi_dung/quen_mat_khau?email=' + encodeURIComponent(email), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                // Sau khi gửi mã khôi phục thành công, hiển thị thông báo
                errorMessage.innerHTML = `<div class="alert success">Mã khôi phục đã được gửi đến email của bạn. Vui lòng kiểm tra email và nhập mã dưới đây.</div>`;
                // Ẩn form email và hiển thị form nhập mã khôi phục
                emailForm.style.display = 'none';
                verifyForm.style.display = 'block';
            } else {
                throw new Error('Không thể tìm thấy tài khoản với email đã cung cấp');
            }
        })
        .catch(error => {
            errorMessage.innerHTML = `<div class="alert">${error.message}</div>`;
        });
    });

    // Xác nhận mã khôi phục
    const verifyCodeForm = document.getElementById('verify-code-form');
    verifyCodeForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const code = recoveryCodeInput.value.trim();

        if (!code) {
            alert('Vui lòng nhập mã khôi phục');
            return;
        }

        // Gửi mã khôi phục và kiểm tra mã đó
        fetch('http://localhost:8080/api/nguoi_dung/xac-nhan-ma-khoi-phuc?makhophuc=' + encodeURIComponent(code), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                recoveryCode = code; // Lưu mã khôi phục
                verifyForm.style.display = 'none';
                resetPasswordForm.style.display = 'block';
            } else {
                throw new Error('Mã khôi phục không hợp lệ');
            }
        })
        .catch(error => {
            alert(error.message);
        });
    });

    // Đổi mật khẩu mới
    const resetPasswordFormElement = document.getElementById('reset-password-form');
    resetPasswordFormElement.addEventListener('submit', function (e) {
        e.preventDefault();
        const newPassword = newPasswordInput.value.trim();
        
        if (!newPassword) {
            alert('Vui lòng nhập mật khẩu mới');
            return;
        }

        // Kiểm tra xem mã khôi phục có hợp lệ không
        if (!recoveryCode) {
            alert('Vui lòng xác nhận mã khôi phục trước');
            return;
        }

        fetch('http://localhost:8080/api/nguoi_dung/doi-mat-khau?email=' + encodeURIComponent(emailInput.value) + '&matKhauMoi=' + encodeURIComponent(newPassword), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                alert('Mật khẩu đã được thay đổi thành công!');
                // Chuyển hướng tới trang đăng nhập hoặc trang người dùng
                $window.location.href = "#!user"; // Đường dẫn cần thay đổi phù hợp với cấu trúc ứng dụng
            } else {
                throw new Error('Có lỗi xảy ra khi đổi mật khẩu');
            }
        })
        .catch(error => {
            alert(error.message);
        });
        
    });
};
