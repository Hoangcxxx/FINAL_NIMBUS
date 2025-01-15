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

    $scope.updateUser = function () {
        if ($scope.updateUserForm.$valid) {
            const url = `${baseURL}/${$scope.selectedUser.idNguoiDung}`; // Sử dụng idNguoiDung từ đối tượng đã chọn
            $http.put(url, $scope.selectedUser)
                .then(function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Cập Nhật Thành CCông !',
                        html: '<p>Bạn đã tạo tài khoản thành công. Hãy đăng nhập để bắt đầu trải nghiệm!</p>',
                        confirmButtonText: 'Đăng nhập ngay'
                    })
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


    // Lấy danh sách người dùng
    const getLeLe = function () {
        callApi('GET', `${baseURL}/list/khachle`)
            .then(response => {
                $scope.customers = response.data; // Đổi từ $scope.users thành $scope.customers
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
    }

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
        // Kiểm tra các trường đầu vào
        if (!$scope.tenNguoiDung || !$scope.email || !$scope.password || !$scope.phone || !$scope.gender) {
            Swal.fire({
                icon: 'warning',
                title: 'Thiếu thông tin!',
                text: 'Vui lòng điền đầy đủ thông tin trước khi đăng ký.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng tên người dùng (chỉ chứa ký tự và số, ít nhất 3 ký tự)
        if ($scope.tenNguoiDung.includes('@')) {
            Swal.fire({
                icon: 'error',
                title: 'Tên người dùng không hợp lệ!',
                text: 'Tên người dùng không được chứa ký tự "@"',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng email
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test($scope.email)) {
            Swal.fire({
                icon: 'error',
                title: 'Email không hợp lệ!',
                text: 'Vui lòng nhập đúng định dạng email.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if ($scope.password.length < 6) {
            Swal.fire({
                icon: 'error',
                title: 'Mật khẩu quá ngắn!',
                text: 'Mật khẩu phải có ít nhất 6 ký tự.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng số điện thoại (giả sử số điện thoại có 10 chữ số)
        const phonePattern = /^[0-9]{10}$/;
        if (!phonePattern.test($scope.phone)) {
            Swal.fire({
                icon: 'error',
                title: 'Số điện thoại không hợp lệ!',
                text: 'Số điện thoại phải bao gồm 10 chữ số.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra giới tính (giả sử chỉ có 'Nam' và 'Nữ' là hợp lệ)
        if ($scope.gender !== 'Nam' && $scope.gender !== 'Nữ') {
            Swal.fire({
                icon: 'error',
                title: 'Giới tính không hợp lệ!',
                text: 'Vui lòng chọn giới tính đúng.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Đặt vai trò mặc định là 2 (Khách hàng)
        $scope.role = 2; // Vai trò Khách hàng (mặc định)

        // Gửi yêu cầu đăng ký
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
                    title: 'Tạo tài khoản thành công!',
                    html: '<p>Bạn đã tạo tài khoản thành công. Hãy đăng nhập để bắt đầu trải nghiệm!</p>',
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
        $scope.phone = document.getElementById('phone').value; // Lấy số điện thoại
        $scope.gender = document.getElementById('gender').value; // Lấy giới tính
        $scope.registerUser();
    });
     $scope.registerEmployee = function () {
        // Kiểm tra các trường đầu vào
        if (!$scope.tenNhanVien || !$scope.email || !$scope.password || !$scope.phone || !$scope.gender || !$scope.role) {
            Swal.fire({
                icon: 'warning',
                title: 'Thiếu thông tin!',
                text: 'Vui lòng điền đầy đủ thông tin trước khi đăng ký.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng email
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test($scope.email)) {
            Swal.fire({
                icon: 'error',
                title: 'Email không hợp lệ!',
                text: 'Vui lòng nhập đúng định dạng email.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if ($scope.password.length < 6) {
            Swal.fire({
                icon: 'error',
                title: 'Mật khẩu quá ngắn!',
                text: 'Mật khẩu phải có ít nhất 6 ký tự.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra định dạng số điện thoại (giả sử số điện thoại có 10 chữ số)
        const phonePattern = /^[0-9]{10}$/;
        if (!phonePattern.test($scope.phone)) {
            Swal.fire({
                icon: 'error',
                title: 'Số điện thoại không hợp lệ!',
                text: 'Số điện thoại phải bao gồm 10 chữ số.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra giới tính (giả sử chỉ có 'Nam' và 'Nữ' là hợp lệ)
        if ($scope.gender !== 'Nam' && $scope.gender !== 'Nữ') {
            Swal.fire({
                icon: 'error',
                title: 'Giới tính không hợp lệ!',
                text: 'Vui lòng chọn giới tính đúng.',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Gửi yêu cầu kiểm tra trùng lặp email và số điện thoại
        $http.get('http://127.0.0.1:8080/api/admin/nguoi_dung/list/nhanvien')
            .then(function (response) {
                const existingUsers = response.data;  // Dữ liệu trả về từ API (Danh sách nhân viên)

                // Kiểm tra số điện thoại và email đã tồn tại trong cơ sở dữ liệu
                const isPhoneExist = existingUsers.some(user => user.sdt === $scope.phone);
                const isEmailExist = existingUsers.some(user => user.email === $scope.email);

                if (isPhoneExist) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Số điện thoại đã tồn tại!',
                        text: 'Số điện thoại này đã được đăng ký. Vui lòng sử dụng số khác.',
                        confirmButtonText: 'OK'
                    });
                    return;
                }

                if (isEmailExist) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Email đã tồn tại!',
                        text: 'Email này đã được đăng ký. Vui lòng sử dụng email khác.',
                        confirmButtonText: 'OK'
                    });
                    return;
                }

                // Nếu cả số điện thoại và email đều chưa tồn tại, tiếp tục đăng ký nhân viên
                const registerData = {
                    tenNguoiDung: $scope.tenNhanVien,
                    email: $scope.email,
                    matKhau: $scope.password,
                    vaiTro: { idVaiTro: $scope.role },
                    sdt: $scope.phone,
                    gioiTinh: $scope.gender
                };

                // Gửi yêu cầu tạo nhân viên
                callApi('POST', `${baseURL}/create`, registerData)
                    .then(function () {
                        Swal.fire({
                            icon: 'success',
                            title: 'Thêm nhân viên thành công!',
                            html: '<p>Bạn đã thêm nhân viên mới thành công.</p>',
                        }).then(() => {
                            window.location.reload(); // Reload lại trang để cập nhật danh sách
                        });
                    })
                    .catch(function (error) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Thêm nhân viên thất bại!',
                            text: error.data && error.data.message ? `Lỗi: ${error.data.message}` : 'Đã xảy ra lỗi không xác định. Vui lòng thử lại!',
                            confirmButtonText: 'Thử lại'
                        });
                    });
            })
            .catch(function (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi kiểm tra thông tin!',
                    text: 'Không thể kiểm tra email hoặc số điện thoại. Vui lòng thử lại.',
                    confirmButtonText: 'OK'
                });
            });
    };

    // Lắng nghe sự kiện submit form
    document.getElementById('addEmployeeForm').addEventListener('submit', function (event) {
        event.preventDefault(); // Ngừng hành động mặc định của form

        // Lấy các giá trị từ form
        $scope.tenNhanVien = document.getElementById('usernameEmployee').value;
        $scope.email = document.getElementById('email').value;
        $scope.password = document.getElementById('passwordEmployee').value;
        $scope.role = parseInt(document.getElementById('roleEmployee').value); // Lấy vai trò từ select
        $scope.phone = document.getElementById('phoneEmployee').value; // Lấy số điện thoại
        $scope.gender = document.getElementById('genderEmployee').value; // Lấy giới tính

        // Log dữ liệu để kiểm tra
        console.log('Tên nhân viên:', $scope.tenNhanVien);
        console.log('Email:', $scope.email);
        console.log('Mật khẩu:', $scope.password);
        console.log('Vai trò:', $scope.role);
        console.log('Số điện thoại:', $scope.phone);
        console.log('Giới tính:', $scope.gender);

        // Gọi hàm đăng ký nhân viên
        $scope.registerEmployee();
    });

    // Khởi tạo dữ liệu khi trang load
    getUsers();
    getEmployees();
    getLeLe();

};
