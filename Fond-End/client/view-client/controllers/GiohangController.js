window.GiohangController = function ($scope, $http) {
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

                // Đảm bảo mỗi item có soLuongGioHang mặc định là 1 nếu chưa có
                $scope.cart.forEach(item => {
                    if (item.soLuongGioHang === undefined || item.soLuongGioHang === null) {
                        item.soLuongGioHang = 1; // Mặc định là 1 nếu không có giá trị
                    }
                });

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


    $scope.removeFromCart = function (idSanPhamChiTiet) {
        $http.delete(`http://localhost:8080/api/nguoi_dung/gio_hang/delete?idGioHang=${$scope.userId}&idSanPhamChiTiet=${idSanPhamChiTiet}`)
            .then(function (response) {
                alert(response.data.message);
                $scope.getCartItems(); // Lấy lại giỏ hàng sau khi xóa
                window.location.reload();  // Làm mới route để giao diện được cập nhật
            })
            .catch(function (error) {
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
            url: `http://localhost:8080/api/nguoi_dung/gio_hang/update?idGioHang=${$scope.userId}`,
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
        if ($scope.isValidCart) {
            $window.location.href = "/#!thanh_toan"; // Chuyển hướng sang trang thanh toán
        } else {
            alert("Giỏ hàng của bạn có sản phẩm với số lượng không hợp lệ. Vui lòng kiểm tra lại.");
        }
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
        // Đảm bảo item.soLuongGioHang luôn là một chuỗi
        if (isNaN(item.soLuongGioHang) || item.soLuongGioHang === '') {
            item.soLuongGioHang = 1; // Gán lại giá trị mặc định nếu không phải số
        }

        // Chỉ giữ lại các ký tự số hợp lệ, xóa tất cả ký tự không phải là số
        let validQuantity = item.soLuongGioHang.replace(/[^0-9]/g, '');

        // Nếu validQuantity không phải một chuỗi rỗng, chuyển nó thành số
        if (validQuantity !== '') {
            item.soLuongGioHang = parseInt(validQuantity, 10);
        }

        // Kiểm tra nếu số lượng nhỏ hơn 1 hoặc lớn hơn 20
        if (item.soLuongGioHang < 1) {
            item.soLuongGioHang = 1; // Đảm bảo số lượng không nhỏ hơn 1
            item.errorMessage = "Số lượng sản phẩm không thể nhỏ hơn 1.";
        } else if (item.soLuongGioHang > 20) {
            item.soLuongGioHang = 20; // Đảm bảo số lượng không lớn hơn 20
            item.errorMessage = "Số lượng sản phẩm không được vượt quá 20.";
        } else {
            item.errorMessage = ''; // Xóa thông báo lỗi nếu số lượng hợp lệ
        }

        // Cập nhật giỏ hàng sau khi chỉnh sửa
        $scope.updateCart(item);
    };


    fetchData('http://localhost:8080/api/nguoi_dung/san_pham', 'products');
    fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc');
}
