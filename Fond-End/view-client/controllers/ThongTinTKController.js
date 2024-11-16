window.ThongTinTKController = function ($scope, $http) {
    var iduser = 3;  

    // Lấy thông tin người dùng
    $http.get('http://localhost:8080/api/auth/user/' + iduser)
        .then(function (response) {
            var user = response.data;
            $scope.tenKhachHang = user.tenNguoiDung;
            $scope.emailKhachHang = user.email;
            $scope.diaChiKhachHang = user.diaChi;
            $scope.sdtKhachHang = user.sdtNguoiDung;
        }, function (error) {
            console.error('Lỗi khi lấy dữ liệu người dùng:', error);
        });

    // Mở modal chỉnh sửa thông tin
    $scope.openEditModal = function () {
        $scope.tempTenKhachHang = $scope.tenKhachHang;
        $scope.tempEmailKhachHang = $scope.emailKhachHang;
        $scope.tempDiaChiKhachHang = $scope.diaChiKhachHang;
        $scope.tempSdtKhachHang = $scope.sdtKhachHang;

        $('#editCustomerModal').modal('show'); 
    };
    $scope.getCartItems = function () {
        $http.get(`http://localhost:8080/api/giohang/${cartId}`)
            .then(function (response) {
                $scope.cart = response.data;
                $scope.cartItemCount = $scope.cart.length; // Cập nhật số lượng sản phẩm trong giỏ hàng

                // Lấy hình ảnh cho từng sản phẩm
                $scope.cart.forEach((element) => {
                    $http.get(`http://localhost:8080/api/hinh_anh/${element.idSanPham}`)
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
   
    $scope.saveCustomerDetails = function () {
        var updatedCustomer = {
            tenNguoiDung: $scope.tempTenKhachHang,
            email: $scope.tempEmailKhachHang,
            diaChi: $scope.tempDiaChiKhachHang,
            sdtNguoiDung: $scope.tempSdtKhachHang
        };

        $http.put('http://localhost:8080/api/auth/update/' + iduser, updatedCustomer)
            .then(function (response) {
                // Cập nhật thông tin sau khi thành công
                $scope.tenKhachHang = updatedCustomer.tenNguoiDung;
                $scope.emailKhachHang = updatedCustomer.email;
                $scope.diaChiKhachHang = updatedCustomer.diaChi;
                $scope.sdtKhachHang = updatedCustomer.sdtNguoiDung;

                alert('Cập nhật thông tin thành công!');
                $('#editCustomerModal').modal('hide'); 
            }, function (error) {
                console.error('Lỗi khi cập nhật dữ liệu người dùng:', error);
                alert('Cập nhật không thành công! Vui lòng thử lại.');
            });
    };
};
