window.DangkyController = function($scope) {
    document.getElementById('registerForm').addEventListener('submit', function(event) {
        event.preventDefault();

     
        var formData = {
            tenNguoiDung: document.getElementById('tenNguoiDung').value,
            email: document.getElementById('email').value,
            matKhau: document.getElementById('matKhau').value,
            sdtNguoiDung: document.getElementById('sdtNguoiDung').value,
            diaChi: document.getElementById('diaChi').value,
            gioiTinh: document.getElementById('gioiTinh').value,
            trangThai: true // Default status
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
                throw new Error('Có lỗi xảy ra!'); // Handle errors
            }
            return response.json();
        })
        .then(data => {
          
            localStorage.setItem('tenNguoiDung', formData.tenNguoiDung);
            localStorage.setItem('email', formData.email);
         
            localStorage.setItem('matKhau', formData.matKhau);

          
            document.getElementById('responseMessage').innerText = 'Đăng ký thành công!';
            setTimeout(() => {
                window.location.href = 'Login.html'; 
            }, 2000);
            document.getElementById('registerForm').reset();
        })
        .catch(error => {
            document.getElementById('responseMessage').innerText = error.message; 
        });
    });
};
