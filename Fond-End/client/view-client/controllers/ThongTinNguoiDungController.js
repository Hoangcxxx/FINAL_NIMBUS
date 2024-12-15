window.ThongTinNguoiDungController = function ($scope, $http, $window) {
    $scope.dsTinh = [];
    $scope.dsHuyen = [];
    $scope.dsXa = [];

    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');

    if (userInfo) {
        // Nếu có thông tin người dùng, parse và gán vào scope để hiển thị
        $scope.infoUser = JSON.parse(userInfo);
        $scope.idNguoiDung = $scope.infoUser.idNguoiDung;
        $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
        $scope.emailNguoiDung = $scope.infoUser.email;
        $scope.diaChiNguoiDung = $scope.infoUser.diaChi;
        $scope.sdtNguoiDung = $scope.infoUser.sdt;
        $scope.ngaySinhNguoiDung = $scope.infoUser.ngaySinh;
        $scope.gioiTinhNguoiDung = $scope.infoUser.gioiTinh;
    } else {
        // Nếu không có thông tin người dùng trong localStorage
        $scope.infoUser = null;
    }

    // Mở modal chỉnh sửa thông tin
    $scope.openEditModal = function () {
        // Thông tin sẽ tự động điền vào modal khi mở
        $scope.tenNguoiDung = $scope.infoUser.tenNguoiDung;
        $scope.emailNguoiDung = $scope.infoUser.email;
        $scope.diaChiNguoiDung = $scope.infoUser.diaChi;
        $scope.sdtNguoiDung = $scope.infoUser.sdt;
        $scope.ngaySinhNguoiDung = $scope.infoUser.ngaySinh;
        $scope.gioiTinh = $scope.infoUser.gioiTinh;
        console.log('Ngày sinh trong modal sau khi chuyển định dạng: ', $scope.ngaySinhNguoiDung);
        // Log thông tin người dùng hiện tại khi mở modal
        console.log('Dữ liệu người dùng khi mở modal: ', $scope.infoUser);
    };

    // Hàm cập nhật thông tin người dùng
    $scope.saveCustomerDetails = function () {
        // Chuyển đổi ngày sinh từ đối tượng Date thành định dạng dd-MM-yyyy
        var ngaySinhFormat = $scope.ngaySinhNguoiDung;
        if (ngaySinhFormat instanceof Date) {
            var day = ngaySinhFormat.getDate().toString().padStart(2, '0');  // Lấy ngày và đảm bảo có 2 chữ số
            var month = (ngaySinhFormat.getMonth() + 1).toString().padStart(2, '0'); // Lấy tháng và đảm bảo có 2 chữ số
            var year = ngaySinhFormat.getFullYear();  // Lấy năm

            // Định dạng lại ngày sinh theo dd-MM-yyyy
            $scope.ngaySinhNguoiDung = day + '-' + month + '-' + year;
        }

        // Validate dữ liệu
        if (!$scope.tenNguoiDung || $scope.tenNguoiDung.trim() === "") {
            Swal.fire('Lỗi', 'Tên người dùng không được để trống!', 'error');
            return;
        }
        if (!$scope.emailNguoiDung || !$scope.emailNguoiDung.match(/^\S+@\S+\.\S+$/)) {
            Swal.fire('Lỗi', 'Email không hợp lệ!', 'error');
            return;
        }
        if (!$scope.sdtNguoiDung || !$scope.sdtNguoiDung.match(/^\d{10,11}$/)) {
            Swal.fire('Lỗi', 'Số điện thoại phải từ 10-11 chữ số!', 'error');
            return;
        }
        if (!$scope.ngaySinhNguoiDung || !$scope.ngaySinhNguoiDung.match(/^\d{2}-\d{2}-\d{4}$/)) {
            Swal.fire('Lỗi', 'Ngày sinh không hợp lệ! Định dạng đúng: dd-MM-yyyy.', 'error');
            return;
        }
        if (!$scope.diaChiNguoiDung || $scope.diaChiNguoiDung.trim() === "") {
            Swal.fire('Lỗi', 'Địa chỉ không được để trống!', 'error');
            return;
        }

        // Chuẩn bị dữ liệu người dùng mới để gửi đi
        var updatedUser = {
            tenNguoiDung: $scope.tenNguoiDung,
            email: $scope.emailNguoiDung,
            sdt: $scope.sdtNguoiDung,
            ngaySinh: $scope.ngaySinhNguoiDung,
            diaChi: $scope.diaChiNguoiDung,
            gioiTinh: $scope.gioiTinh,
        };

        // Gửi yêu cầu PUT đến API cập nhật người dùng
        $http.put('http://localhost:8080/api/nguoi_dung/' + $scope.infoUser.idNguoiDung, updatedUser)
            .then(function (response) {
                // Cập nhật thông tin trong localStorage sau khi thành công
                localStorage.setItem('user', JSON.stringify(response.data));

                // Cập nhật lại thông tin trong scope
                $scope.infoUser = response.data;
                $scope.tenNguoiDung = response.data.tenNguoiDung;
                $scope.emailNguoiDung = response.data.email;
                $scope.diaChiNguoiDung = response.data.diaChi;
                $scope.sdtNguoiDung = response.data.sdt;
                $scope.ngaySinhNguoiDung = response.data.ngaySinh;
                $scope.gioiTinh = response.data.gioiTinh;

                // Đóng modal và hiển thị thông báo thành công
                $('#editCustomerModal').modal('hide');
                Swal.fire('Thành công', 'Thông tin đã được cập nhật!', 'success')
                    .then(() => {
                        $window.location.href = "/#!thong_tin_nguoi_dung"; // Chuyển hướng sang trang thông tin
                    });
            })
            .catch(function (error) {
                console.log('Cập nhật thông tin thất bại:', error);
                Swal.fire('Lỗi', 'Có lỗi xảy ra khi cập nhật thông tin!', 'error');
            });
    };

    $scope.formatDate = function () {
        let date = $scope.ngaySinhNguoiDung;

        // Loại bỏ mọi ký tự không phải là số (bao gồm cả dấu gạch nối)
        date = date.replace(/[^0-9]/g, "");

        // Nếu có ít hơn 8 ký tự, xử lý thêm dấu gạch nối theo từng bước
        if (date.length >= 2) {
            // Nếu có 2 ký tự, thêm dấu gạch nối sau ngày
            date = date.substring(0, 2) + (date.length >= 2 ? '-' : '') + date.substring(2);
        }

        if (date.length >= 5) {
            // Nếu có 5 ký tự, thêm dấu gạch nối sau tháng
            date = date.substring(0, 5) + (date.length >= 5 ? '-' : '') + date.substring(5);
        }

        // Hiển thị lại giá trị đã định dạng
        $scope.ngaySinhNguoiDung = date;

        // Nếu đủ 10 ký tự, không thay đổi gì thêm
        if (date.length === 10) {
            return;
        }
    };

    function getOrderDetails() {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/user/${$scope.idNguoiDung}`;

        $http.get(apiUrl)
            .then(function (response) {
                // Kiểm tra và gán dữ liệu đơn hàng
                const hoaDonList = response.data.hoaDon && Array.isArray(response.data.hoaDon)
                    ? response.data.hoaDon
                    : [];

                if (hoaDonList.length > 0) {
                    // Gán danh sách hóa đơn vào $scope
                    $scope.orderData = hoaDonList;
                    console.log($scope.orderData);
                } else {
                    console.warn("Không tìm thấy đơn hàng nào.");
                    $scope.orderData = [];
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
                $scope.orderData = []; // Gán rỗng nếu lỗi
            });
    }
    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails();

    // Lấy danh sách tỉnh
    $http.get('http://localhost:8080/api/nguoi_dung/dia_chi/tinh').then(function (response) {
        $scope.tinhList = response.data;
    });

    // Khi chọn tỉnh
    $scope.onTinhChange = function () {
        if ($scope.selectedTinh) {
            $http.get('http://localhost:8080/api/nguoi_dung/dia_chi/huyen?idTinh=' + $scope.selectedTinh)
                .then(function (response) {
                    $scope.huyenList = response.data;
                    $scope.xaList = []; // Reset danh sách xã khi thay đổi tỉnh
                    $scope.selectedHuyen = null; // Reset huyện đã chọn
                });
        } else {
            $scope.huyenList = [];
            $scope.xaList = [];
            $scope.selectedHuyen = null;
        }
    };

    // Khi chọn huyện
    $scope.onHuyenChange = function () {
        if ($scope.selectedHuyen) {
            $http.get('http://localhost:8080/api/nguoi_dung/dia_chi/xa?idHuyen=' + $scope.selectedHuyen)
                .then(function (response) {
                    $scope.xaList = response.data;
                });
        } else {
            $scope.xaList = [];
        }
    };
    $scope.addresses = []; // Khởi tạo mảng địa chỉ

    // Sử dụng thông tin người dùng đã lấy từ localStorage
    if ($scope.infoUser) {  // Đảm bảo rằng thông tin người dùng đã được lấy
        var idNguoiDung = $scope.infoUser.idNguoiDung;  // Lấy id người dùng từ thông tin người dùng

        // Gọi API để lấy địa chỉ vận chuyển của người dùng
        $http.get('http://localhost:8080/api/nguoi_dung/dia_chi/' + idNguoiDung)
            .then(function (response) {
                // Lưu dữ liệu trả về vào scope
                if (response.data && Array.isArray(response.data)) {
                    $scope.addresses = response.data.map(function (address) {
                        return {
                            idDiaChiVanChuyen: address.idDiaChiVanChuyen,
                            diaChiCuThe: address.diaChiCuThe,
                            moTa: address.moTa,
                            tinh: address.tinh ? address.tinh.tenTinh : 'N/A',
                            huyen: address.huyen ? address.huyen.tenHuyen : 'N/A',
                            xa: address.xa ? address.xa.tenXa : 'N/A',
                            trangThai: address.trangThai ? 'Đang sử dụng' : 'Không sử dụng'
                        };
                    });
                    // In dữ liệu dưới dạng chuỗi JSON dễ đọc
                    console.log('Dữ liệu:', JSON.stringify($scope.addresses, null, 2));
                } else {
                    console.error('Dữ liệu trả về không hợp lệ hoặc không có địa chỉ.');
                    $scope.addresses = [];
                }
            }, function (error) {
                console.error('Lỗi khi gọi API:', error);
                $scope.addresses = []; // Gán rỗng nếu có lỗi
            });
    } else {
        console.log('Không tìm thấy thông tin người dùng trong localStorage.');
    }


    $scope.openAddAddressModal = function () {
        // Reset các trường trong modal về trạng thái ban đầu
        $scope.selectedTinh = null;  // Reset Tỉnh
        $scope.selectedHuyen = null; // Reset Huyện
        $scope.selectedXa = null;    // Reset Xã
        $scope.specificAddress = ""; // Reset Địa chỉ cụ thể
        $scope.description = "";     // Reset Mô tả

        // Reset danh sách huyện và xã để người dùng không nhìn thấy danh sách cũ khi chưa chọn tỉnh
        $scope.huyenList = [];
        $scope.xaList = [];

        // Log thông tin để kiểm tra
        console.log("Modal thêm địa chỉ được mở. Dữ liệu đã được reset.");
    };

    $scope.saveAddress = function () {
        // Kiểm tra xem các trường có được điền đầy đủ hay không
        if (!$scope.selectedTinh || !$scope.selectedHuyen || !$scope.selectedXa || !$scope.specificAddress) {
            alert('Vui lòng điền đầy đủ thông tin địa chỉ!');
            return;
        }

        // Chuẩn bị dữ liệu để gửi
        var addressData = {
            idTinh: $scope.selectedTinh,
            idHuyen: $scope.selectedHuyen,
            idXa: $scope.selectedXa,
            diaChi: $scope.diaChiNguoiDung,
            moTa: $scope.description, // Mô tả
            idNguoiDung: $scope.idNguoiDung // ID người dùng từ localStorage hoặc từ $scope
        };

        // Gửi yêu cầu POST để thêm địa chỉ mới
        $http.post('http://localhost:8080/api/nguoi_dung/dia_chi/add', addressData)
            .then(function (response) {
                // Nếu thêm thành công
                $scope.addresses = response.data.map(function (address) {
                    return {
                        idDiaChiVanChuyen: address.idDiaChiVanChuyen,
                        diaChiCuThe: address.diaChiCuThe,
                        moTa: address.moTa,
                        tinh: address.tinh ? address.tinh.tenTinh : 'N/A',
                        huyen: address.huyen ? address.huyen.tenHuyen : 'N/A',
                        xa: address.xa ? address.xa.tenXa : 'N/A',
                        trangThai: address.trangThai ? 'Đang sử dụng' : 'Không sử dụng'
                    };
                });
                alert('Địa chỉ đã được thêm thành công!');
                $('#addAddressModal').modal('hide'); // Đóng modal

                console.log('Dữ liệu địa chỉ sau khi thêm:', JSON.stringify($scope.addresses, null, 2));

                // Sau khi thêm thành công, có thể gọi lại API để lấy danh sách địa chỉ mới
                $http.get('http://localhost:8080/api/nguoi_dung/dia_chi/' + $scope.idNguoiDung)
                    .then(function (response) {
                        $scope.addresses = response.data.map(function (address) {
                            return {
                                idDiaChiVanChuyen: address.idDiaChiVanChuyen,
                                diaChiCuThe: address.diaChiCuThe,
                                moTa: address.moTa,
                                tinh: address.tinh ? address.tinh.tenTinh : 'N/A',
                                huyen: address.huyen ? address.huyen.tenHuyen : 'N/A',
                                xa: address.xa ? address.xa.tenXa : 'N/A',
                                trangThai: address.trangThai ? 'Đang sử dụng' : 'Không sử dụng'
                            };
                        });
                        console.log('Dữ liệu địa chỉ sau khi thêm:', JSON.stringify($scope.addresses, null, 2));

                        // Gọi lại hàm reset modal sau khi thêm thành công
                        $scope.openAddAddressModal();
                    })
                    .catch(function (error) {
                        console.error('Lỗi khi lấy lại danh sách địa chỉ:', error);
                    });
            })
            .catch(function (error) {
                console.log('Lỗi khi thêm địa chỉ:', error);
                alert('Có lỗi xảy ra khi thêm địa chỉ! Vui lòng thử lại.');
            });
    };


    // Khởi tạo dữ liệu ban đầu
    $scope.cities = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.shippingInfo = {
        province: null,
        district: null,
        ward: null,
        address: ''
    };
  

    // Hàm gọi API lấy danh sách tỉnh thành
    function getCities() {
        if ($scope.selectedCity && $scope.selectedCity.code) {
            console.log('Province ID:', $scope.selectedCity.code);
        } else {
            console.warn('Chưa chọn tỉnh thành.');
        }

        $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/cities")
            .then(function (response) {
                if (response.data && Array.isArray(response.data)) {
                    $scope.cities = response.data;

                    // Lưu vào localStorage
                    localStorage.setItem('cities', JSON.stringify($scope.cities));
                } else {
                    console.error('Không có dữ liệu tỉnh thành trả về.');
                }
            })
            .catch(function (error) {
                console.error('Có lỗi xảy ra khi lấy tỉnh thành:', error);
            });
    }

    // Hàm gọi API lấy danh sách huyện theo mã tỉnh
    $scope.onCityChange = function () {
        $scope.districts = [];
        $scope.wards = [];
        $scope.shippingInfo.district = null;
        $scope.shippingInfo.ward = null;

        if ($scope.selectedCity && $scope.selectedCity.code) {
            console.log('ID tỉnh vừa chọn:', $scope.selectedCity.code);
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/districts/" + $scope.selectedCity.code)
                .then(function (response) {
                    if (response.data && Array.isArray(response.data)) {
                        $scope.districts = response.data;

                        // Lưu vào localStorage
                        localStorage.setItem('districts_' + $scope.selectedCity.code, JSON.stringify($scope.districts));
                    } else {
                        console.error('Không có dữ liệu huyện trả về.');
                    }
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy huyện:', error);
                });
        } else {
            console.warn('Chưa chọn tỉnh thành.');
        }
    };

    // Hàm gọi API lấy danh sách xã theo mã huyện
    $scope.onDistrictChange = function () {
        $scope.wards = [];
        $scope.shippingInfo.ward = null;

        if ($scope.selectedDistrict && $scope.selectedDistrict.code) {
            console.log('ID huyện vừa chọn:', $scope.selectedDistrict.code);
            $http.get("http://127.0.0.1:8080/api/nguoi_dung/test/wards/" + $scope.selectedDistrict.code)
                .then(function (response) {
                    if (response.data && Array.isArray(response.data)) {
                        $scope.wards = response.data;

                        // Lưu vào localStorage
                        localStorage.setItem('wards_' + $scope.selectedDistrict.code, JSON.stringify($scope.wards));
                    } else {
                        console.error('Không có dữ liệu xã trả về.');
                    }
                })
                .catch(function (error) {
                    console.error('Có lỗi xảy ra khi lấy xã:', error);
                });
        } else {
            console.warn('Chưa chọn huyện.');
        }
    };





    
    // Gọi API lấy danh sách tỉnh thành khi trang được tải
    getCities();

};
