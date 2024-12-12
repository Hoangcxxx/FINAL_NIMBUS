window.QuanLynguoidungController = function ($scope, $http, $window) {
    const baseURL = "http://127.0.0.1:8080/api/admin/nguoi_dung";


    $scope.customSearch = function (user) {
        if (!$scope.searchUser) return true; // Hiển thị tất cả nếu không nhập gì

        const keyword = $scope.searchUser.toLowerCase();
        return (
            user.tenNguoiDung.toLowerCase().includes(keyword) ||
            user.email.toLowerCase().includes(keyword) ||
            user.sdt.includes(keyword)
        );
    };


    // Hàm gọi API chung để giảm mã lặp lại
    const callApi = function (method, url, data) {
        return $http({ method, url, data });
    };


    // Tải danh sách người dùng
    $scope.loadUsers = function () {
        $http.get(baseURL)
            .then(function (response) {
                $scope.users = response.data;
            })
            .catch(function (error) {
                console.error("Lỗi khi tải danh sách người dùng:", error);
            });
    };

    // Chọn người dùng để chỉnh sửa
    $scope.editUser = function (user) {
        $scope.selectedUser = angular.copy(user); // Sao chép đối tượng người dùng được chọn
    };
    // Cập nhật thông tin người dùng
    $scope.updateUser = function () {
        if ($scope.updateUserForm.$valid) {
            const url = `${baseURL}/${$scope.selectedUser.idNguoiDung}`; // Sử dụng idNguoiDung từ đối tượng đã chọn
            $http.put(url, $scope.selectedUser)
                .then(function (response) {
                    alert("Cập nhật thành công!");
                    $("#updateUserModal").modal("hide"); // Đóng modal
                    window.location.reload(); // Reload lại toàn bộ trang
                })
                .catch(function (error) {
                    console.error("Lỗi khi cập nhật:", error);
                    alert("Cập nhật thất bại, vui lòng thử lại!");
                });
        } else {
            alert("Vui lòng điền đầy đủ thông tin hợp lệ.");
        }
    };


    // Khởi tạo dữ liệu ban đầu
    $scope.loadUsers();

    // Lấy danh sách người dùng
    const getUsers = function () {
        callApi('GET', `${baseURL}/list/nguoidung`)
            .then(response => {
                $scope.users = response.data;
            })
            .catch(error => {
                console.error("Lỗi khi lấy danh sách người dùng:", error);
            });
    };

    // Lấy danh sách nhân viên
    const getEmployees = function () {
        callApi('GET', `${baseURL}/list/nhanvien`)
            .then(response => {
                $scope.employees = response.data;
            })
            .catch(error => {
                console.error("Lỗi khi lấy danh sách nhân viên:", error);
            });
    };

    $scope.changeUserStatus = function (userId, action) {
console.log('Thay đổi trạng thái:', { userId, action });

        const endpoint = action === 'lock' ? 'khoa' : 'mo_khoa';
        $http.put(`${baseURL}/${endpoint}/${userId}`)
            .then(response => {
                if (response.status === 200) {
                    // Xử lý dữ liệu trả về dạng chuỗi
                    let message = typeof response.data === 'string' ? response.data : response.data.message;

                    // Cập nhật trạng thái trong danh sách nhân viên
                    let employee = $scope.employees.find(emp => emp.idNguoiDung === userId);
                    if (employee) {
                        employee.trangThai = action === 'unlock'; // true nếu mở khóa, false nếu khóa
                    }

                    let user = $scope.users.find(u => u.idNguoiDung === userId);
                    if (user) {
                        user.trangThai = action === 'unlock';
                    }
                    Swal.fire({
                        title: 'Thành công!',
                        text: message,
                        icon: 'success',
                        confirmButtonText: 'OK'
                    });
                }
            })
            .catch(error => {
                console.error('API error:', error);
                Swal.fire({
                    title: 'Thất bại!',
                    text: `Không thể ${action === 'lock' ? 'khóa' : 'mở khóa'} người dùng.`,
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };









    // Xóa người dùng (khách hàng)

    $scope.deleteUser = function (idNguoiDung) {
        if (confirm("Bạn có chắc muốn xóa người dùng này?")) {
            callApi('DELETE', `${baseURL}/${idNguoiDung}`)
                .then(() => {
                    alert("Xóa người dùng thành công!");
                    // Cập nhật lại danh sách người dùng sau khi xóa
                    $scope.users = $scope.users.filter(user => user.idNguoiDung !== idNguoiDung);
                })
                .catch((error) => {
                    console.error("Lỗi khi xóa người dùng:", error);
                    alert("Đã xảy ra lỗi khi xóa người dùng!");
                });
        }
    };



    $scope.deleteNhanvien = function (idNguoiDung) {
        if (confirm("Bạn có chắc muốn xóa người dùng này?")) {
            callApi('DELETE', `${baseURL}/${idNguoiDung}`)
                .then(() => {
                    alert("Xóa người dùng thành công!");
                    // Cập nhật lại danh sách người dùng sau khi xóa
                    $scope.employees = $scope.employees.filter(employee => employee.idNguoiDung !== idNguoiDung);
                })
                .catch((error) => {
                    console.error("Lỗi khi xóa người dùng:", error);
alert("Đã xảy ra lỗi khi xóa người dùng!");
                });
        }
    };
    $scope.registerUser = function () {
        const registerData = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.email,
            matKhau: $scope.password,
            vaiTro: { idVaiTro: $scope.role },
            sdt: $scope.phone,
            gioiTinh: $scope.gender
        };

        callApi('POST', `${baseURL}/dang_ky`, registerData)
            .then(function () {
                Swal.fire({
                    icon: 'success',
                    title: 'Đăng ký thành công!',
                    html: '<p>Bạn đã tạo tài khoản thành công. Hãy đăng nhập để bắt đầu trải nghiệm!</p>',
                    confirmButtonText: 'Đăng nhập ngay'
                }).then(() => {
                    window.location.reload(); // Reload lại toàn bộ trang
                });
            })
            .catch(function (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Đăng ký thất bại!',
                    text: error.data && error.data.message ? `Lỗi: ${error.data.message}` : 'Đã xảy ra lỗi không xác định. Vui lòng thử lại!',
                    confirmButtonText: 'Thử lại'
                });
            });
    };


    // Lắng nghe sự kiện submit form
    document.getElementById('addUserForm').addEventListener('submit', function (event) {
        event.preventDefault(); // Ngừng hành động mặc định của form
        $scope.tenNguoiDung = document.getElementById('username').value;
        $scope.email = document.getElementById('email').value;
        $scope.password = document.getElementById('password').value;
        $scope.role = parseInt(document.getElementById('role').value); // Lấy vai trò từ select
        $scope.phone = document.getElementById('phone').value; // Lấy số điện thoại
        $scope.gender = document.getElementById('gender').value; // Lấy giới tính
        $scope.registerUser();
    });

    // Khởi tạo dữ liệu khi trang load
    getUsers();
    getEmployees();
};