window.ThanhCongController = function ($scope, $http) {
    // Lấy mã hóa đơn từ URL hoặc localStorage nếu có
    var maHoaDon = new URLSearchParams(window.location.search).get("maHoaDon");
    console.log("maHoaDon từ URL:", maHoaDon);

    if (!maHoaDon) {
        maHoaDon = localStorage.getItem("maHoaDon");
        console.log("maHoaDon từ localStorage:", maHoaDon);
    }

    if (!maHoaDon) {
        console.error("Không tìm thấy mã hóa đơn trong URL hoặc localStorage.");
        alert("Mã hóa đơn không hợp lệ! Vui lòng kiểm tra lại URL hoặc đăng nhập lại.");
    } else {
        console.log("Mã hóa đơn hợp lệ:", maHoaDon);
    }


    // Lấy chi tiết hóa đơn từ API
    function getOrderDetails(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;
        console.log("Gửi yêu cầu đến API chi tiết hóa đơn với URL:", apiUrl);

        $http.get(apiUrl)
            .then(function (response) {
                console.log("Phản hồi từ API hóa đơn:", response.data);

                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    console.log("Dữ liệu hóa đơn đã xử lý:", hoaDon);

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
                        giaTriMavoucher: hoaDon.giaTriMavoucher || 0,
                        kieugiamgia: hoaDon.kieugiamgia,
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []
                    };

                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        console.log("Chi tiết sản phẩm:", hoaDon.listSanPhamChiTiet);

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

                        $scope.orderDetails.listSanPhamChiTiet.forEach((item) => {
                            console.log("Đang lấy hình ảnh cho sản phẩm với ID:", item.idSanPham);

                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
                                    console.log("Phản hồi hình ảnh cho sản phẩm ID", item.idSanPham, ":", imageResponse.data);
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
        angular.forEach($scope.orderDetails?.listSanPhamChiTiet || [], function (item) {
            total += item.tongtien || 0;
        });
        console.log("Tổng giá trị sản phẩm:", total);
        return total;
    };

    // Hàm xử lý thanh toán
    $scope.payment = function () {
        const user = JSON.parse(localStorage.getItem("user"));
        const totalAmount = JSON.parse(localStorage.getItem("totalAmount"));
        const storedData = JSON.parse(localStorage.getItem("shippingData"));
        const cart = JSON.parse(localStorage.getItem("cart"));
        const idTinh = localStorage.getItem("idTinh");
        const idHuyen = localStorage.getItem("idHuyen");
        const idXa = localStorage.getItem("idXa");

        // Kiểm tra nếu `storedData` hoặc các dữ liệu quan trọng khác không tồn tại
        if (!user || !totalAmount || !cart || !storedData || !idTinh || !idHuyen || !idXa) {
            alert("Dữ liệu thanh toán không hợp lệ. Vui lòng kiểm tra lại!");
            console.error("Dữ liệu bị thiếu:", {
                user,
                totalAmount,
                cart,
                storedData,
                idTinh,
                idHuyen,
                idXa
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
            idvoucher: $scope.selectedVoucher && $scope.selectedVoucher.idVoucher ? $scope.selectedVoucher.idVoucher : null
        };

        console.log("orderData:", orderData);

        // Gửi request thanh toán
        $http.post("http://localhost:8080/api/nguoi_dung/hoa_don/thanh_toan_vnpay", orderData)
            .then(function (response) {
                const maHoaDon = response.data.maHoaDon;
                // Lưu mã hóa đơn và chuyển hướng tới cổng thanh toán
                localStorage.setItem("maHoaDon", maHoaDon);
                getOrderDetails(maHoaDon);
            })
            .catch(function (error) {
                console.error("Lỗi khi thực hiện thanh toán:", error);
                alert("Thanh toán không thành công. Vui lòng thử lại!");
            });
    };


    // Gọi thanh toán và lấy chi tiết đơn hàng
    $scope.payment();
    getOrderDetails(maHoaDon);
};