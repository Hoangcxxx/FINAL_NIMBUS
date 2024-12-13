window.DonHangCuaToiController = function ($scope, $http, $window) {

    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');
    // Lắng nghe sự kiện click trên nút "Tra Cứu"
    document.getElementById("searchOrder").addEventListener("click", searchOrder);

    // Lắng nghe sự kiện nhấn phím Enter trong ô nhập mã đơn hàng
    document.getElementById("orderCode").addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            searchOrder(); // Thực hiện tra cứu khi nhấn Enter
        }
    });

    function searchOrder() {
        const orderCodeInput = document.getElementById("orderCode");
        const orderCode = orderCodeInput.value.trim();
        const orderInfoDiv = document.getElementById("orderInfo");
    
        // Ẩn thông tin đơn hàng và trạng thái lỗi trước
        orderInfoDiv.style.display = "none";
        orderCodeInput.classList.remove("is-invalid");
    
        // Kiểm tra nếu mã đơn hàng không được nhập
        if (!orderCode) {
            showError("Vui lòng nhập mã đơn hàng!");
            return;
        }
    
        // Kiểm tra mã đơn hàng hợp lệ (chỉ cho phép chữ cái, số, và độ dài từ 5-20 ký tự)
        if (!/^[A-Za-z0-9]{5,20}$/.test(orderCode)) {
            showError("Mã đơn hàng không hợp lệ! Vui lòng nhập đúng định dạng.");
            return;
        }
    
        // Hiển thị trạng thái tải
        showLoadingState(true);
    
        // Gửi yêu cầu API để tra cứu đơn hàng
        fetch(`http://127.0.0.1:8080/api/nguoi_dung/hoa_don/${orderCode}`)
            .then(response => {
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error("Không tìm thấy đơn hàng với mã này.");
                    } else {
                        throw new Error("Có lỗi xảy ra khi truy vấn API. Vui lòng thử lại.");
                    }
                }
                return response.json();
            })
            .then(data => {
                if (!data || !data.hoaDon || data.hoaDon.length === 0) {
                    throw new Error("Không tìm thấy thông tin đơn hàng!");
                }
    
                const hoaDon = data.hoaDon[0];
    
                // Hiển thị thông tin đơn hàng
                document.getElementById("infoOrderCode").textContent = hoaDon.maHoaDon || "N/A";
                document.getElementById("infoCustomerName").textContent = hoaDon.tenNguoiNhan || "N/A";
                const orderDate = hoaDon.ngayTao ? new Date(hoaDon.ngayTao).toLocaleDateString("vi-VN") : "N/A";
                document.getElementById("infoOrderDate").textContent = orderDate;
                const orderStatus = hoaDon.tenTrangThai || "Chưa có trạng thái";
                document.getElementById("infoOrderStatus").textContent = orderStatus;
    
                // Hiển thị thông tin đơn hàng nếu có
                orderInfoDiv.style.display = "block";
            })
            .catch(error => {
                showError(error.message);
                console.error("Lỗi:", error);
            })
            .finally(() => {
                // Ẩn trạng thái tải
                showLoadingState(false);
            });
    }
    
    // Hàm hiển thị trạng thái lỗi
    function showError(message) {
        Swal.fire({
            title: 'Lỗi',
            text: message,
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
    
    // Hàm hiển thị trạng thái tải
    function showLoadingState(isLoading) {
        const searchButton = document.getElementById("searchOrder");
        if (isLoading) {
            searchButton.disabled = true;
            searchButton.textContent = "Đang tra cứu...";
        } else {
            searchButton.disabled = false;
            searchButton.textContent = "Tra Cứu";
        }
    }
    
    // Thêm sự kiện click cho nút tra cứu
    document.getElementById("searchOrder").addEventListener("click", searchOrder);
    

    function getOrderDetailsByMaHoaDon(maHoaDon) {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/${maHoaDon}`;

        // Gọi API để lấy chi tiết hóa đơn
        $http.get(apiUrl)
            .then(function (response) {
                const hoaDon = response.data.hoaDon && Array.isArray(response.data.hoaDon) && response.data.hoaDon.length > 0
                    ? response.data.hoaDon[0]
                    : null;

                if (hoaDon) {
                    // Cập nhật thông tin đơn hàng
                    $scope.orderHieu = {
                        maHoaDon: hoaDon.maHoaDon,
                        tenPhuongThucThanhToan: hoaDon.tenPhuongThucThanhToan,
                        tenNguoiNhan: hoaDon.tenNguoiNhan,
                        diaChi: hoaDon.diaChi,
                        sdtNguoiNhan: hoaDon.sdtNguoiNhan,
                        ghiChu: hoaDon.ghiChu,
                        ngayTao: hoaDon.ngayTao,
                        thanhTien: hoaDon.thanhTien,
                        tinh: hoaDon.tenTinh,
                        huyen: hoaDon.tenHuyen,
                        xa: hoaDon.tenXa,
                        giaTriMavoucher: hoaDon.giaTriMavoucher || 0, // Giá trị giảm giá, mặc định là 0 nếu không có
                        kieugiamgia: hoaDon.kieugiamgia, // 0: %, 1: giá tiền
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []
                    };

                    // Cập nhật chi tiết sản phẩm
                    if (hoaDon.listSanPhamChiTiet && hoaDon.listSanPhamChiTiet.length > 0) {
                        $scope.orderlist = {
                            listSanPhamChiTiet: hoaDon.listSanPhamChiTiet,
                            thanhTien: hoaDon.thanhTien,
                            phiShip: hoaDon.phiShip,
                            giaTien:hoaDon.giaTien,
                            maSPCT: hoaDon.maSPCT,
                            tenSanPham: hoaDon.tenSanPham,
                            tenmausac: hoaDon.tenmausac, // Đảm bảo trường này tồn tại
                            tenchatlieu: hoaDon.tenchatlieu, // Đảm bảo trường này tồn tại
                            tenkichthuoc: hoaDon.tenkichthuoc, // Đảm bảo trường này tồn tại
                            tongtien: hoaDon.tongtien,
                            giaKhuyenMai: hoaDon.giaKhuyenMai
                            
                        };
         

                        // Lấy hình ảnh cho từng sản phẩm
                        $scope.orderlist.listSanPhamChiTiet.forEach((item) => {
                            $http.get(`http://localhost:8080/api/nguoi_dung/hinh_anh/${item.idSanPham}`)
                                .then(function (imageResponse) {
                                    // Gán hình ảnh cho sản phẩm nếu có
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

    // Ví dụ cách gọi hàm getOrderDetailsByMaHoaDon trong một sự kiện (ví dụ: khi người dùng nhấn nút chi tiết)
    $scope.showOrderDetails = function (hoaDon) {
        if (hoaDon && hoaDon.maHoaDon) {
            getOrderDetailsByMaHoaDon(hoaDon.maHoaDon);
        } else {
            console.error("Mã hóa đơn không hợp lệ!");
        }
    };


    // Gọi API để lấy danh sách đơn hàng khi tải trang
    $scope.getOrderDetails = function () {
        const apiUrl = `http://localhost:8080/api/nguoi_dung/hoa_don/user/${$scope.idNguoiDung}`;

        $http.get(apiUrl)
            .then(function (response) {
                const hoaDonList = response.data && Array.isArray(response.data.hoaDon)
                    ? response.data.hoaDon
                    : [];

                if (hoaDonList.length > 0) {
                    $scope.orderData = hoaDonList;
                    console.log($scope.orderData);
                } else {
                    console.warn("Không tìm thấy đơn hàng nào.");
                    $scope.orderData = [];
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy thông tin hóa đơn:", error);
                $scope.orderData = [];
            });
    };

    // Gọi hàm getOrderDetails khi tải trang
    $scope.getOrderDetails();

}