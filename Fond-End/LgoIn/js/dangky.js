document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn việc gửi form mặc định

    var formData = {
        tenNguoiDung: document.getElementById('tenNguoiDung').value,
        email: document.getElementById('email').value,
        matKhau: document.getElementById('matKhau').value,
        sdtNguoiDung: document.getElementById('sdtNguoiDung').value,
        diaChi: document.getElementById('diaChi').value,
        gioiTinh: document.getElementById('gioiTinh').value,
        trangThai: true // Đặt trạng thái mặc định là true
    };

    fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Có lỗi xảy ra!'); // Đối phó với lỗi
        }
        return response.json();
    })
    .then(data => {
        // Lưu thông tin đăng ký vào localStorage
        localStorage.setItem('tenNguoiDung', formData.tenNguoiDung);
        localStorage.setItem('email', formData.email);
        localStorage.setItem('matKhau', formData.matKhau); // Lưu mật khẩu chỉ để thử nghiệm, không nên làm như vậy trong thực tế

        document.getElementById('responseMessage').innerText = 'Đăng ký thành công!';
        setTimeout(() => {
            window.location.href = 'Login.html'; // Chuyển hướng về trang đăng nhập sau 2 giây
        }, 2000); // Thay đổi thời gian (tính bằng mili giây) nếu cần
        document.getElementById('registerForm').reset(); // Reset form sau khi đăng ký thành công
    })
    .catch(error => {
        document.getElementById('responseMessage').innerText = error.message;
    });
});
