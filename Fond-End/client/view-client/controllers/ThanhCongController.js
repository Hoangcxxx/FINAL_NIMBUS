window.ThanhCongController = function ($scope, $http) {
    // Lấy mã hóa đơn từ URL hoặc localStorage nếu có
    var maHoaDon = new URLSearchParams(window.location.search).get("maHoaDon") || localStorage.getItem("maHoaDon");

    // Nếu không có mã hóa đơn thì thông báo lỗi
    if (!maHoaDon) {
        alert("Mã hóa đơn không hợp lệ!");
        return;
    }



    // Lấy chi tiết hóa đơn từ API
    function getOrderDetails(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;

        $http.get(apiUrl)
            .then(function (response) {
                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    // Cập nhật thông tin đơn hàng vào scope
                    $scope.orderData = {
                        maHoaDon: hoaDon.maHoaDon,
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        phiShip: hoaDon.phiShip,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        thanhTien: hoaDon.thanhTien,
                        tinh: hoaDon.tenTinh,
                        huyen: hoaDon.tenHuyen,
                        xa: hoaDon.tenXa,
                        giaTriMavoucher: hoaDon.giaTriMavoucher,
                        kieuGiamGia: hoaDon.kieuGiamGia,
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []
                    };

                    console.log("dsads", $scope.giaTriMavoucher)

                    // Cập nhật chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderDetails = {

                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            giaTien: hoaDon.giaTien,
                            maSPCT: hoaDon.maSPCT,
                            tenSanPham: hoaDon.tenSanPham,
                            tenmausac: hoaDon.tenmausac,
                            tenchatlieu: hoaDon.tenchatlieu,
                            tenkichthuoc: hoaDon.tenkichthuoc,
                            tongtien: hoaDon.tongtien,
                            giaKhuyenMai: hoaDon.giaKhuyenMai
                        };

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderDetails.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
                                    item.urlAnh = imageResponse.data[0]?.urlAnh || '';
                                })
                                .catch(function (error) {
                                    console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                                });
                        });
                    }
                } else {
                    console.error("Dữ liệu hóa đơn không hợp lệ:", response.data);
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
            });
    }

    $scope.getTotalProductPrice = function () {
        let total = 0;

        // Kiểm tra xem $scope.orderDetails và listSanPhamChiTiet có tồn tại không
        if ($scope.orderDetails && Array.isArray($scope.orderDetails.listSanPhamChiTiet)) {
            angular.forEach($scope.orderDetails.listSanPhamChiTiet, function (item) {
                total += item.tongtien || 0; // Thêm giá của mỗi sản phẩm vào tổng
            });
        } else {
            console.log("orderDetails or listSanPhamChiTiet is undefined or not an array.");
        }

        return total;
    };

    $scope.calculateDiscount = function () {
        let discount = 0;
        if ($scope.orderData) {
            if ($scope.orderData.kieuGiamGia === false) {
                // Giảm giá theo phần trăm
                discount = ($scope.getTotalProductPrice() * $scope.orderData.giaTriMavoucher) / 100;
            } else if ($scope.orderData.kieuGiamGia === true) {
                // Giảm giá trực tiếp bằng số tiền
                discount = $scope.orderData.giaTriMavoucher;
            }
        }
        return discount;
    };

    $scope.calculateTotalAfterDiscount = function () {
        const totalPrice = $scope.getTotalProductPrice(); // Tổng tiền sản phẩm
        const discount = $scope.calculateDiscount(); // Số tiền giảm giá
        return totalPrice - discount; // Tổng tiền sau khi trừ giảm giá
    };

    $scope.payment = function () {
        const user = JSON.parse(localStorage.getItem("user"));
        const totalAmount = JSON.parse(localStorage.getItem("totalAmount"));
        const storedData = JSON.parse(localStorage.getItem("shippingData"));
        const cart = JSON.parse(localStorage.getItem("cart"));
        const idTinh = JSON.parse(localStorage.getItem("idTinh"));
        const idHuyen = JSON.parse(localStorage.getItem("idHuyen"));
        const idXa = JSON.parse(localStorage.getItem("idXa"));
        
        const selectedVoucher = JSON.parse(localStorage.getItem("selectedVoucher"));
    
        // Kiểm tra các ID có hợp lệ không
        if (!idTinh || !idHuyen || !idXa) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Thông tin địa chỉ không hợp lệ hoặc thiếu.',
                confirmButtonText: 'Đồng ý'
            });
            return;
        }
    
        // Tạo danh sách sản phẩm chi tiết từ giỏ hàng
        const listSanPhamChiTiet = cart.map(item => ({
            idspct: item.idSanPhamCT, // ID sản phẩm chi tiết
            soLuong: item.soLuongGioHang, // Số lượng sản phẩm trong giỏ hàng
            giaTien: item.giaKhuyenMai !== null ? item.giaKhuyenMai : item.giaBan // Giá tiền ưu tiên giá khuyến mãi
        }));
    
        // Tạo đối tượng orderData
        const orderData = {
            idNguoiDung: user.idNguoiDung,
            tinh: idTinh,
            huyen: idHuyen,
            xa: idXa,
            email: storedData.email || "",
            tenNguoiNhan: storedData.name || "",
            diaChi: storedData.address || "",
            sdtNguoiNhan: storedData.phone || "",
            ghiChu: storedData.note || "",
            cartId: user.idNguoiDung,
            tenPhuongThucThanhToan: "vnpay",
            listSanPhamChiTiet: listSanPhamChiTiet,
            thanhTien: totalAmount,
            idvoucher: selectedVoucher && selectedVoucher.idVoucher ? selectedVoucher.idVoucher : null, // Lấy idVoucher từ voucher nếu có
        };
    
        console.log("Order Data being sent:", orderData);
    
        // Gửi request thanh toán
        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/thanh_toan_vnpay", orderData)
            .then(function (response) {
                const maHoaDon = response.data.maHoaDon;
                // Lưu mã hóa đơn và chuyển hướng tới cổng thanh toán
                localStorage.setItem("maHoaDon", maHoaDon);
                getOrderDetails(maHoaDon);
    
                // Kiểm tra nếu có email trước khi gửi email xác nhận
                const recipientEmail = storedData.email || $scope.userInfo.email;
                if (recipientEmail) {
                    // Gửi email xác nhận sau khi đặt hàng thành công
                    $http.post(`http://localhost:8080/api/nguoi_dung/email/send?recipientEmail=${recipientEmail}`, orderData)
                        .then(response => console.log("Email đã được gửi thành công"))
                        .catch(error => console.error("Lỗi khi gửi email:", error));
                } else {
                    console.error("Email không hợp lệ, không thể gửi email xác nhận.");
                }
    
                // Xóa voucher sau khi thanh toán thành công
                localStorage.removeItem("selectedVoucher");
                localStorage.removeItem("totalAmount");
                localStorage.removeItem("shippingData");
                localStorage.removeItem("cart");
                localStorage.removeItem("idTinh");
                localStorage.removeItem("idHuyen");
                localStorage.removeItem("idXa");
    
                // Hiển thị thông báo thanh toán thành công
                Swal.fire({
                    icon: 'success',
                    title: 'Thanh toán thành công!',
                    text: 'Cảm ơn bạn đã tin tưởng và đặt hàng. Mã hóa đơn của bạn là: ' + maHoaDon + '. Chúng tôi sẽ nhanh chóng xử lý đơn hàng của bạn!',
                    confirmButtonText: 'OK'
                });
    
                // Xóa dữ liệu trong localStorage
                console.log("Dữ liệu đã được xóa khỏi localStorage.");
            }, function (error) {
                // Hiển thị thông báo lỗi thanh toán
                console.error("Error details:", error);
                Swal.fire({
                    icon: 'error',
                    title: 'Thanh toán thất bại!',
                    text: 'Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại. Lỗi: ' + (error.data ? error.data.error : error.message),
                    confirmButtonText: 'Đồng ý'
                });
            });
    };
    
    $scope.payment();


    // Gọi API để lấy chi tiết đơn hàng
    getOrderDetails(maHoaDon);
};
