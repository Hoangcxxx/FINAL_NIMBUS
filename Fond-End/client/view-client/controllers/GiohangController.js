window.GiohangController = function ($scope, $http, $window) {
    $scope.products = [];
    $scope.dsDanhMuc = [];
    $scope.selectedCategoryId = null; // Lưu ID danh mục đã chọn
    $scope.cart = []; // Khởi tạo mảng giỏ hàng trống
    const userId = $scope.userId; // Thay thế bằng ID người dùng thực tế
    const cartId = $scope.userId; // Thay thế bằng ID giỏ hàng thực tế
    $scope.cartItemCount = 0;
    $scope.isValidCart = true; // Mặc định giỏ hàng hợp lệ
    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');

    if (userInfo) {
        // Nếu có thông tin người dùng, chuyển thành đối tượng và lấy ID người dùng
        userInfo = JSON.parse(userInfo);  // Parse thông tin người dùng từ JSON
        $scope.userId = $scope.infoUser.idNguoiDung;  // Lấy ID người dùng từ thông tin đã lưu
    } else {
        // Nếu không có thông tin người dùng (người dùng chưa đăng nhập), gán userId là null
        $scope.userId = null;
        // Xóa idGioHang trong localStorage nếu người dùng chưa đăng nhập
        localStorage.removeItem("idGioHang");
    }

    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/nguoi_dung/gio_hang/${$scope.userId}`)
            .then(function (response) {
                $scope.cart = response.data;
                $scope.cartItemCount = $scope.cart.length; // Cập nhật số lượng sản phẩm trong giỏ hàng
                // Lấy hình ảnh cho từng sản phẩm
                $scope.cart.forEach((element) => {
                    $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${element.idSanPham}`)
                        .then(function (response) {
                            element.urlAnh = response.data[0]?.urlAnh || ''; // Lấy url hình ảnh
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                        });
                });

                // Kiểm tra xem có sản phẩm mới nào trong localStorage không
                const newCartItem = localStorage.getItem("cartItem");
                if (newCartItem) {
                    const item = JSON.parse(newCartItem);
                    $scope.cart.push(item);
                    $scope.cartItemCount++; // Tăng số lượng sản phẩm khi thêm sản phẩm mới
                    localStorage.removeItem("cartItem");
                    alert(`${item.tenSanPham} đã được thêm vào giỏ hàng!`);
                }
            })
            .catch(function (error) {
                console.error("Error fetching cart items:", error);
            });
    };

    // Hàm kiểm tra ngày hiện tại có nằm trong khoảng ngày bắt đầu và ngày kết thúc hay không
    $scope.isValidDiscountPeriod = function (item) {
        const today = new Date();
        const startDate = new Date(item.ngayBatDau);
        const endDate = item.ngayKetThuc ? new Date(item.ngayKetThuc) : null;

        // Kiểm tra nếu có ngày bắt đầu và ngày kết thúc
        if (startDate && endDate) {
            return today >= startDate && today <= endDate;
        } else if (startDate) {
            return today >= startDate; // Nếu chỉ có ngày bắt đầu
        } else if (endDate) {
            return today <= endDate; // Nếu chỉ có ngày kết thúc
        }
        return false; // Nếu không có ngày bắt đầu hoặc kết thúc
    };



    $scope.removeFromCart = function (idSanPhamChiTiet) {
        $http.delete(`http://localhost:8080/api/nguoi_dung/gio_hang/delete?idGioHang=${$scope.userId}&idSanPhamChiTiet=${idSanPhamChiTiet}`)
            .then(function (response) {
                Swal.fire({
                    icon: 'success', // Loại thông báo thành công
                    title: 'Thành công!',
                    text: response.data.message,
                    confirmButtonText: 'Đồng ý'
                });
                $scope.getCartItems(); // Lấy lại giỏ hàng sau khi xóa
            })
            .catch(function (error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Không thể xóa sản phẩm khỏi giỏ hàng.',
                    confirmButtonText: 'Đồng ý'
                });
                console.error("Lỗi xóa sản phẩm khỏi giỏ hàng:", error);
            });
    };


    $scope.updateCart = function (item) {
        // Kiểm tra nếu có giá khuyến mãi, nếu không thì dùng giá gốc
        let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;

        // Tính thành tiền
        let thanhTien = item.soLuongGioHang * donGia;

        let value = {
            soLuong: item.soLuongGioHang,
            donGia: donGia,
            idSanPhamChiTiet: item.idSanPhamCT,
            thanhTien: thanhTien,
        };

        $http({
            method: "PUT",
            url: `http://localhost:8080/api/nguoi_dung/gio_hang/update?idNguoiDung=${$scope.userId}`,
            data: value,
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(function (response) {
                console.log("Cập nhật giỏ hàng thành công:", response.data);
            })
            .catch(function (error) {
                console.error("Lỗi cập nhật sản phẩm trong giỏ hàng:", error);
            });
    };



    $scope.getTotal = function () {
        // Kiểm tra tất cả sản phẩm trong giỏ hàng trước khi tính tổng
        const invalidItems = $scope.cart.filter(item => item.soLuongGioHang < 1 || item.soLuongGioHang > 20);
        if (invalidItems.length > 0) {
            // Nếu có sản phẩm có số lượng không hợp lệ
            $scope.isValidCart = false;
        } else {
            // Nếu tất cả số lượng đều hợp lệ
            $scope.isValidCart = true;
        }

        return $scope.cart.reduce((total, item) => {
            let donGia = item.giaKhuyenMai != null ? item.giaKhuyenMai : item.giaBan;
            return total + (item.soLuongGioHang * donGia);
        }, 0);
    };

    $scope.checkout = function () {
        // Hàm kiểm tra tính hợp lệ của giỏ hàng
        function validateCartItems() {
            const invalidItems = $scope.cart.filter(item => item.soLuongGioHang < 1 || item.soLuongGioHang > 20);
            if (invalidItems.length > 0) {
                invalidItems.forEach(item => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Giỏ hàng không hợp lệ!',
                        text: `Số lượng sản phẩm "${item.tenSanPham}" phải từ 1 đến 20. Vui lòng kiểm tra lại.`,
                        confirmButtonText: 'Đồng ý'
                    });
                });
                return false; // Giỏ hàng không hợp lệ
            }
            return true; // Giỏ hàng hợp lệ
        }

        function checkStockAvailability() {
            const promises = $scope.cart.map(item => {
                return $http.get(`http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/check-so-luong/${item.idSanPhamCT}?soLuongGioHang=${item.soLuongGioHang}`)
                    .then(response => {
                        const message = response.data.message;

                        if (message.includes('hết hàng')) {
                            Swal.fire({
                                icon: 'error',
                                title: 'Sản phẩm hết hàng!',
                                text: `Sản phẩm "${item.tenSanPham}" đã hết hàng.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Sản phẩm "${item.tenSanPham}" hết hàng.`);
                        } else if (message.includes('không khớp')) {
                            const [systemQuantity] = message.match(/Hệ thống: (\d+)/).slice(1);
                            Swal.fire({
                                icon: 'warning',
                                title: 'Số lượng không đủ!',
                                text: `Sản phẩm "${item.tenSanPham}" hiện không đủ số lượng trong kho. Chúng tôi chỉ có ${systemQuantity} sản phẩm.`,
                                confirmButtonText: 'Đồng ý'
                            });

                            throw new Error(`Sản phẩm "${item.tenSanPham}" không đủ số lượng trong kho.`);
                        }
                    })
                    .catch(error => {
                        console.error(`Lỗi kiểm tra tồn kho cho sản phẩm "${item.tenSanPham}":`, error);
                        throw error;
                    });
            });

            return Promise.all(promises);
        }

        // Kiểm tra trạng thái sản phẩm
        function checkProductStatuses() {
            const promises = $scope.cart.map(item => {
                return $http.get(`http://localhost:8080/api/nguoi_dung/san_pham/${item.idSanPham}/trang-thai`)
                    .then(response => {
                        if (!response.data) {
                            Swal.fire({
                                icon: 'error',
                                title: "Sản phẩm đã ngừng bán!",
                                text: `Sản phẩm "${item.tenSanPham}" đã ngừng bán.`,
                                confirmButtonText: 'Đồng ý'
                            });
                            throw new Error(`Sản phẩm "${item.tenSanPham}" không hợp lệ.`);
                        }
                    })
                    .catch(error => {
                        console.error(`Lỗi kiểm tra trạng thái sản phẩm "${item.tenSanPham}":`, error);
                        throw error;
                    });
            });

            return Promise.all(promises);
        }

        // Kiểm tra trạng thái người dùng
        function checkUserStatus() {
            return $http.get(`http://localhost:8080/api/admin/nguoi_dung/check_trang_thai/${$scope.idNguoiDung}`)
                .then(response => {
                    if (!response.data.trangThai) {
                        Swal.fire({
                            title: 'Tài khoản của bạn đã bị khóa!',
                            text: 'Rất tiếc, tài khoản của bạn đã bị tạm khóa do phát hiện hoạt động bất thường hoặc vi phạm chính sách sử dụng.',
                            icon: 'error',
                            confirmButtonText: 'Đồng ý'
                        });
                        return false;
                    }
                    return true;
                })
                .catch(error => {
                    console.error('Lỗi khi kiểm tra trạng thái người dùng:', error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Đã xảy ra lỗi khi kiểm tra trạng thái người dùng. Vui lòng thử lại sau.',
                        confirmButtonText: 'Đồng ý'
                    });
                    throw error;
                });
        }

        // Bắt đầu quá trình kiểm tra giỏ hàng
        if (!validateCartItems()) {
            $scope.isValidCart = false;
            return;
        }

        // Thực hiện kiểm tra tồn kho, trạng thái sản phẩm và trạng thái người dùng
        Promise.allSettled([checkStockAvailability(), checkProductStatuses(), checkUserStatus()])
            .then(results => {
                const hasError = results.some(result => result.status === 'rejected');
                const userStatusValid = results[2].status === 'fulfilled' && results[2].value === true;

                if (hasError || !userStatusValid) {
                    $scope.isValidCart = false;
                    return;
                }

                // Nếu tất cả kiểm tra thành công
                $scope.isValidCart = true;
                $window.location.href = '/#!thanh_toan';
            })
            .catch(error => {
                console.error('Lỗi xảy ra trong quá trình kiểm tra:', error);
            });
    };










    // Lấy dữ liệu giỏ hàng ban đầu
    $scope.getCartItems();

    $scope.onclickDanhMuc = function (id) {
        console.log("Danh mục được click: " + id);
        $scope.selectedCategoryId = id; // Cập nhật ID danh mục đã chọn

        // Xây dựng URL với ID danh mục
        var url = 'http://localhost:8080/api/nguoi_dung/san_pham/findDanhMuc/' + id;

        $http({
            method: 'GET',
            url: url // Sử dụng URL đã xây dựng
        }).then(function (response) {
            $scope.products = response.data;
            console.log("Dữ liệu API trả về: ", response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Hàm lấy dữ liệu sản phẩm từ API
    function fetchData(url, target) {
        $http({
            method: 'GET',
            url: url
        }).then(function (response) {
            $scope[target] = response.data;
            console.log($scope.products);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    }
    $scope.checkQuantity = function (item) {
        // Đảm bảo item.soLuongGioHang là một chuỗi
        item.soLuongGioHang = String(item.soLuongGioHang);

        // Chỉ giữ lại các ký tự số hợp lệ, xóa tất cả ký tự không phải là số
        let validQuantity = item.soLuongGioHang.replace(/[^0-9]/g, '');

        // Nếu validQuantity không phải một chuỗi rỗng, chuyển nó thành số
        if (validQuantity !== '') {
            item.soLuongGioHang = parseInt(validQuantity, 10);
        }

        // Kiểm tra nếu số lượng nhỏ hơn 1 hoặc lớn hơn 20
        if (item.soLuongGioHang < 1) {
            item.soLuongGioHang = 1; // Đảm bảo số lượng không nhỏ hơn 1
            item.errorMessage = "Số lượng sản phẩm không thể nhỏ hơn 1!";
            Swal.fire({
                icon: 'error',
                title: 'Số lượng sản phẩm không thể nhỏ hơn 1!',
                text: item.errorMessage,
                confirmButtonText: 'Đồng ý'
            });
        } else if (item.soLuongGioHang > 20) {
            item.soLuongGioHang = 20; // Đảm bảo số lượng không lớn hơn 20
            item.errorMessage = "Số lượng sản phẩm không được vượt quá 20.";
            Swal.fire({
                icon: 'error',
                title: 'Số lượng sản phẩm không được vượt quá 20!',
                text: item.errorMessage,
                confirmButtonText: 'Đồng ý'
            });
        } else if (item.soLuongGioHang > item.soLuong) {
            // Kiểm tra nếu số lượng giỏ hàng lớn hơn số lượng tồn kho
            item.soLuongGioHang = item.soLuong; // Điều chỉnh lại số lượng giỏ hàng về số lượng tồn kho
            item.errorMessage = `Chỉ còn ${item.soLuong} sản phẩm trong kho.`;
            Swal.fire({
                icon: 'error',
                title: 'Sản phẩm trong giỏ hàng vượt quá số lượng có sẵn!',
                text: item.errorMessage,
                confirmButtonText: 'Đồng ý'
            });
        } else {
            item.errorMessage = ''; // Xóa thông báo lỗi nếu số lượng hợp lệ
        }

        // Cập nhật giỏ hàng sau khi chỉnh sửa
        $scope.updateCart(item);

        // Kiểm tra tổng số lượng sản phẩm trong giỏ hàng
        $scope.validateTotalQuantity();
    };



    // Phương thức validate tổng số lượng giỏ hàng
    $scope.validateTotalQuantity = function () {
        // Tính tổng số lượng sản phẩm trong giỏ hàng
        let totalQuantity = $scope.cart.reduce((sum, item) => {
            return sum + item.soLuongGioHang;
        }, 0);

        // Kiểm tra nếu tổng số lượng vượt quá giới hạn (ví dụ: 100 sản phẩm)
        if (totalQuantity > 100) {
            $scope.isValidCart = false; // Giỏ hàng không hợp lệ
            Swal.fire({
                icon: 'error',
                title: 'Giỏ hàng không hợp lệ!',
                text: 'Tổng số lượng sản phẩm trong giỏ hàng vượt quá giới hạn cho phép. Vui lòng kiểm tra lại.',
                confirmButtonText: 'Đồng ý'
            });
        } else {
            $scope.isValidCart = true; // Giỏ hàng hợp lệ
        }
    };



    fetchData('http://localhost:8080/api/nguoi_dung/san_pham', 'products');
    fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc');
}

