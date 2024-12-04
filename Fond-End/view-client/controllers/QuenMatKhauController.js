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
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Vui lòng nhập email!',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Gửi yêu cầu tới API để gửi mã khôi phục
        fetch('http://localhost:8080/api/nguoi_dung/quen_mat_khau?email=' + encodeURIComponent(email), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Mã khôi phục đã được gửi đến email của bạn. Vui lòng kiểm tra email và nhập mã khôi phục.',
                    confirmButtonText: 'OK'
                });
                // Ẩn form email và hiển thị form nhập mã khôi phục
                emailForm.style.display = 'none';
                verifyForm.style.display = 'block';
            } else {
                throw new Error('Không thể tìm thấy tài khoản với email đã cung cấp');
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: error.message,
                confirmButtonText: 'OK'
            });
        });
    });

    // Xác nhận mã khôi phục
    const verifyCodeForm = document.getElementById('verify-code-form');
    verifyCodeForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const code = recoveryCodeInput.value.trim();

        if (!code) {
            Swal.fire({
                icon: 'warning',
                title: 'Cảnh báo!',
                text: 'Vui lòng nhập mã khôi phục!',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Gửi mã khôi phục và kiểm tra mã đó
        fetch('http://localhost:8080/api/nguoi_dung/xac-nhan-ma-khoi-phuc?makhophuc=' + encodeURIComponent(code), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                recoveryCode = code; // Lưu mã khôi phục
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Mã khôi phục hợp lệ. Vui lòng đặt mật khẩu mới.',
                    confirmButtonText: 'OK'
                });
                verifyForm.style.display = 'none';
                resetPasswordForm.style.display = 'block';
            } else {
                throw new Error('Mã khôi phục không hợp lệ');
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: error.message,
                confirmButtonText: 'OK'
            });
        });
    });

    // Đổi mật khẩu mới
    const resetPasswordFormElement = document.getElementById('reset-password-form');
    resetPasswordFormElement.addEventListener('submit', function (e) {
        e.preventDefault();
        const newPassword = newPasswordInput.value.trim();
        
        if (!newPassword) {
            Swal.fire({
                icon: 'warning',
                title: 'Cảnh báo!',
                text: 'Vui lòng nhập mật khẩu mới!',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra xem mã khôi phục có hợp lệ không
        if (!recoveryCode) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Vui lòng xác nhận mã khôi phục trước!',
                confirmButtonText: 'OK'
            });
            return;
        }

        fetch('http://localhost:8080/api/nguoi_dung/doi-mat-khau?email=' + encodeURIComponent(emailInput.value) + '&matKhauMoi=' + encodeURIComponent(newPassword), {
            method: 'POST',
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Mật khẩu đã được thay đổi thành công!',
                    confirmButtonText: 'OK'
                }).then(() => {
                    $window.location.href = "#!user"; // Đường dẫn cần thay đổi phù hợp với cấu trúc ứng dụng
                });
            } else {
                throw new Error('Có lỗi xảy ra khi đổi mật khẩu');
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: error.message,
                confirmButtonText: 'OK'
            });
        });
    });
};
