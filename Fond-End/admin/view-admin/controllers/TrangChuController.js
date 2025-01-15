window.TrangChuController = function ($scope, $http) {
    $scope.updateInvoiceStatus = function () {
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/" + selectedInvoice.idHoaDon + "/update-status", { trangThai: true })
            .then(function (response) {
                console.log("Kết quả cập nhật:", response.data);
            })
    };
    $scope.createPaymentHistory = function (amount) {
        // Lấy thông tin từ localStorage
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        const user = JSON.parse(localStorage.getItem('user'));
        // Kiểm tra tính hợp lệ
        // Dữ liệu gửi lên server
        const paymentData = {
            idHoaDon: selectedInvoice.idHoaDon,
            idNguoiDung: selectedUser.idNguoiDung,
            idNhanVien: user.idNguoiDung,
            soTienThanhToan: totalPriceInfo.totalPrice,
        };

        // Gửi yêu cầu POST tạo lịch sử thanh toán
        $http.post('http://localhost:8080/api/admin/lich_su_thanh_toan/create', paymentData)
            .then(function (response) {
                console.log('Tạo lịch sử thanh toán thành công:', response.data);
            })
            .catch(function (error) {
                console.error('Lỗi khi tạo lịch sử thanh toán:', error);
            });
    };
    $scope.deleteGioHangChiTietByUserId = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));
        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa chi tiết giỏ hàng
        $http.delete("http://localhost:8080/api/admin/gio_hang/delete/user/" + userId)
            .then(function (response) {
            })
            .catch(function (error) {
                console.error("Lỗi khi xóa chi tiết giỏ hàng:", error);
            });
    };
    $scope.createPaymentMethod = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        const paymentMethodFromStorage = JSON.parse(localStorage.getItem('selectedPaymentMethod'));

        var paymentMethod = {
            phuongThucThanhToan: { idPTThanhToan: paymentMethodFromStorage.id },
            soTienThanhToan: Number($scope.customerPaid),
            ngayGiaoDich: new Date().toISOString(),
            ghiChu: "Thanh toán hóa đơn " + selectedInvoice.maHoaDon
        };

        // Tạo phương thức thanh toán
        $http.post("http://localhost:8080/api/admin/phuong_thuc_thanh_toan_hoa_don/" + selectedInvoice.idHoaDon + "/thanh-toan", paymentMethod)

            .then(function (response) {
                console.log("Phản hồi từ POST:", response.data);
                if (response.data && response.data.idThanhToanHoaDon) {
                    const selectedVoucher = JSON.parse(localStorage.getItem('voucherdachon'));
                    // Phương thức thanh toán đã được tạo thành công, tiếp tục cập nhật hóa đơn
                    var updatedInvoice = {
                        phiShip: 0,
                        ngayThanhToan: new Date().toISOString(),
                        thanhTien: totalPriceInfo.totalPrice,
                        idThanhToanHoaDon: response.data.idThanhToanHoaDon,
                        setSdtNguoiNhan: userFromStorage.sdt, // Lấy số điện thoại từ localStorage
                        trangThaiHoaDon: $scope.selectedPhuongThucThanhToan === 2 ? 4 : 5,
                        idVoucher: selectedVoucher ? selectedVoucher.idVoucher : null
                    };

                    var user = JSON.parse(localStorage.getItem("user"));
                    console.log("Cấu trúc của updatedInvoice trước khi gửi:", updatedInvoice);
                    return $http.put("http://localhost:8080/api/admin/hoa_don_ban_hang/cap-nhat/" + selectedInvoice.idHoaDon + "?idNhanVien=" + user.idNguoiDung, updatedInvoice)
                        .then(function (putResponse) {
                            console.log("Hóa đơn đã được cập nhật thành công:", putResponse.data);
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi cập nhật hóa đơn:", error);
                            alert("Có lỗi xảy ra khi cập nhật hóa đơn.");
                        });
                } else {
                    alert("Có lỗi xảy ra khi tạo phương thức thanh toán.");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi tạo phương thức thanh toán:", error);
                alert("Có lỗi xảy ra khi tạo phương thức thanh toán.");
            });
    };
    $scope.deleteCart = function () {
        // Lấy thông tin người dùng từ localStorage
        let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

        const userId = userFromStorage.idNguoiDung;

        // Gửi yêu cầu xóa giỏ hàng
        $http.delete('http://localhost:8080/api/admin/gio_hang/xoa-tat-ca/' + userId)
            .then(function (response) {
                console.log('Giỏ hàng đã được xóa:', response.data);
                // Có thể thêm các tác vụ sau khi xóa giỏ hàng nếu cần
            })
            .catch(function (error) {
                console.error('Lỗi khi xóa giỏ hàng:', error);
            });
    };
    $scope.processCartItems = async function () {
        const paymentInfo = JSON.parse(localStorage.getItem('paymentInfo'));
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
        const selectedPaymentMethod = JSON.parse(localStorage.getItem('selectedPaymentMethod'));
        $scope.hoaDonChiTietDTO = [];
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice'));
        // Lấy giỏ hàng từ localStorage
        const cartItemsFromStorage = JSON.parse(localStorage.getItem('cartItems'));

        // Kiểm tra xem có dữ liệu giỏ hàng hay không
        if (!cartItemsFromStorage || cartItemsFromStorage.length === 0) {
            console.log("Giỏ hàng trống hoặc không tìm thấy giỏ hàng trong localStorage");
            return;
        }

        // Duyệt qua các sản phẩm trong giỏ hàng
        cartItemsFromStorage.forEach(function (item) {
            const detail = {
                idHoaDon: selectedInvoice.idHoaDon,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                soLuong: item.soLuong,
                tongTien: totalPriceInfo.totalPrice,
                soTienThanhToan: selectedPaymentMethod.id === 3
                    ? totalPriceInfo.totalPrice
                    : parseFloat(paymentInfo.tienKhachDua),
                tienTraLai: selectedPaymentMethod.id === 3
                    ? 0
                    : $scope.tienThua,
                moTa: item.moTa || ''
            };
            $scope.hoaDonChiTietDTO.push(detail);
        });
    };
    $scope.deductVoucherQuantity = async function () {
        try {
            // Gửi yêu cầu trừ số lượng voucher, sử dụng tổng tiền trước giảm giá
            const selectedVoucher = JSON.parse(localStorage.getItem('voucherdachon'));
            const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo'));
            const response = await $http.post(
                `http://localhost:8080/api/admin/ban_hang/use/${selectedVoucher.maVoucher}`,
                totalPriceInfo.originalTotalPrice
                // Sử dụng tổng tiền chưa giảm giá
            );
            console.log("Số lượng voucher đã được trừ.");


        } catch (error) {
            console.error('Lỗi khi trừ số lượng voucher:', error);
        }
    };
    $scope.thanhtoanck = async function () {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const status = urlParams.get('status'); // Lấy giá trị của tham số "status"

        // Chỉ thực thi nếu trạng thái là "PAID"
        if (status !== 'PAID') {
            console.log("Trạng thái không hợp lệ hoặc thanh toán chưa hoàn tất. Không thực hiện hành động.");
            return;
        }
        try {
            // Các bước thanh toá
            // Lấy dữ liệu từ sessionStorage
            let userFromStorage = JSON.parse(localStorage.getItem('selectedUser'));

            // Kiểm tra dữ liệu đã lấy được
            console.log("User from sessionStorage: ", userFromStorage);


            const userId = userFromStorage.idNguoiDung;

            // Cập nhật trạng thái hóa đơn và các thao tác khác
            await $scope.updateInvoiceStatus();
            await $scope.createPaymentHistory();

            await Promise.all([
                $scope.deleteGioHangChiTietByUserId(),
                $scope.createPaymentMethod(),
                $scope.deleteCart(),
                $scope.deductVoucherQuantity()
            ]);

            await $scope.processCartItems();

            // Tạo hóa đơn chi tiết
            const response = await $http.post(
                "http://localhost:8080/api/admin/hoa_don_chi_tiet/create",
                $scope.hoaDonChiTietDTO,
                { params: { userId: userId } }
            );
            console.log("Hóa đơn chi tiết:", response.data);


            Swal.fire({
                title: 'Thành công!',
                text: 'Khách hàng đã thanh toán thành công!',
                icon: 'success',
                timer: 2400,
                showConfirmButton: false
            }).then(() => {
                // Sau khi hiển thị thông báo thành công, hỏi người dùng có muốn in hóa đơn không
                Swal.fire({
                    title: 'Bạn có muốn in hóa đơn không?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'In Hóa Đơn',
                    cancelButtonText: 'Không'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Nếu người dùng chọn "In Hóa Đơn", gọi hàm in hóa đơn
                        $scope.printInvoice();
                    }
                });
            });


        } catch (error) {
            console.error("Lỗi xảy ra:", error);
            Swal.fire({
                title: 'Lỗi!',
                text: 'Có lỗi xảy ra trong quá trình tạo hóa đơn.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    };
    $scope.cartItems = JSON.parse(localStorage.getItem('giohang')) || [];
    $scope.totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo')) || {};
    $scope.paymentInfo = JSON.parse(sessionStorage.getItem('paymentInfo')) || {};
    $scope.selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice')) || {};
    $scope.selectedUser = JSON.parse(localStorage.getItem('selectedUser')) || {};


    // Định dạng số thành tiền tệ
    // Định dạng số thành tiền tệ (VNĐ) không có từ 'VND' phía sau
    $scope.formatCurrency = function (value) {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' })
            .format(value)
            .replace('₫', ' VNĐ'); // Chuyển ký hiệu "₫" thành " VNĐ"
    };
    $scope.printInvoice = function () {
        // Lấy dữ liệu từ localStorage
        const cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
        const totalPriceInfo = JSON.parse(localStorage.getItem('totalPriceInfo')) || {};
        const selectedInvoice = JSON.parse(localStorage.getItem('selectedInvoice')) || {};
        const selectedUser = JSON.parse(localStorage.getItem('selectedUser')) || {};
        const user = JSON.parse(localStorage.getItem('user')) || {};
        const selectedPaymentMethod = JSON.parse(localStorage.getItem('selectedPaymentMethod')) || {};
        const paymentInfo = JSON.parse(localStorage.getItem('paymentInfo')) || {};
        const discount = localStorage.getItem('discount') || 0;
        const voucherCode = localStorage.getItem('voucherCode') || 'N/A';
        let formattedDate = 'N/A'; // Mặc định là 'N/A'
        if (selectedInvoice.ngayTao) {
            const dateObj = new Date(selectedInvoice.ngayTao);
            formattedDate = dateObj.getDate().toString().padStart(2, '0') + '/' +
                (dateObj.getMonth() + 1).toString().padStart(2, '0') + '/' +
                dateObj.getFullYear();
        }

        // Tạo HTML hóa đơn từ dữ liệu
        const invoiceHtml = `
       <div style="font-family: 'Roboto', sans-serif; padding: 20px; color: #333; background-color: #fff; width: 100%; max-width: 800px; margin: 0 auto; border: 1px solid #ddd; border-radius: 15px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);">
    <!-- Thông tin cửa hàng -->
    <div style="text-align: center; margin-bottom: 30px; color: #333;">
        <img src="/view-admin/assets/image/favicon1.png" alt="Logo" style="max-width: 150px; margin-bottom: 10px;">
        <h2 style="font-size: 32px; font-weight: bold; color: #1E3D58; margin-bottom: 5px;">NB Fashion</h2>
        <p style="font-size: 16px; margin: 5px 0; color: #555;">Địa Chỉ: 32 Cổ Nhuế 2 - Bắc Từ Liêm - Hà Nội</p>
        <p style="font-size: 16px; margin: 5px 0; color: #555;">Số Điện Thoại: 0989899999</p>
    </div>
    
    <hr style="border: 1px solid #1E3D58; margin-bottom: 30px;">
    
    <!-- Thông tin khách hàng và nhân viên -->
    <div style="display: flex; justify-content: space-between; font-size: 16px; color: #444; margin-bottom: 20px;">
    <!-- Thông tin khách hàng và nhân viên bên trái -->
    <div style="width: 48%; padding-right: 20px;">
        <p><strong style="color: #1E3D58;">Tên Nhân Viên:</strong> ${user.tenNguoiDung || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Tên Khách Hàng:</strong> ${selectedUser.tenNguoiDung || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Số Điện Thoại:</strong> ${selectedUser.sdt || 'N/A'}</p>
        <p><strong style="color: #1E3D58;">Mã Voucher:</strong> ${voucherCode}</p>
    </div>
    
    <!-- Thông tin hóa đơn bên phải -->
    <div style="width: 48%; padding-left: 20px;">
<p><strong style="color: #1E3D58;">Mã Hóa Đơn:</strong> ${selectedInvoice.maHoaDon || 'N/A'}</p>
           <p><strong style="color: #1E3D58;">Ngày Tạo:</strong> ${formattedDate}</p>
        <p><strong style="color: #1E3D58;">Hình thức thanh toán:</strong> ${selectedPaymentMethod.tenPhuongThuc || 'Chưa rõ'}</p>
       
    </div>
</div>


    <!-- Chi tiết sản phẩm -->
    <h3 style="font-size: 20px; font-weight: bold; color: #1E3D58; text-align: center; margin-top: 30px;">Chi Tiết Sản Phẩm</h3>
    <table style="width: 100%; border-collapse: collapse; margin-top: 20px; background-color: #f9f9f9; border-radius: 8px; overflow: hidden;">
        <thead style="background-color: #1E3D58; color: #fff;">
            <tr>
                <th style="padding: 12px; text-align: center; font-size: 14px;">#</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Sản Phẩm</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Chất Liệu</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Màu Sắc</th>
                <th style="padding: 12px; text-align: left; font-size: 14px;">Kích Thước</th>
                <th style="padding: 12px; text-align: center; font-size: 14px;">Số Lượng</th>
                <th style="padding: 12px; text-align: right; font-size: 14px;">Đơn Giá</th>
                <th style="padding: 12px; text-align: right; font-size: 14px;">Tổng Tiền</th>
            </tr>
        </thead>
        <tbody>
            ${cartItems.map((item, index) => `
                <tr style="border-bottom: 1px solid #ddd; text-align: center;">
                    <td style="padding: 10px; font-size: 14px;">${index + 1}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.tenSanPham || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.chatLieu || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.mauSac || 'N/A'}</td>
                    <td style="padding: 10px; font-size: 14px;">${item.kichThuoc || 'N/A'}</td>
                    <td style="padding: 10px; text-align: center; font-size: 14px;">${item.soLuong || 0}</td>
                    <td style="padding: 10px; text-align: right; font-size: 14px;">${$scope.formatCurrency(item.giaBan)}</td>
                    <td style="padding: 10px; text-align: right; font-size: 14px;">${$scope.formatCurrency(item.soLuong * item.giaBan)}</td>
                </tr>
            `).join('')}
        </tbody>
    </table>

    <!-- Tổng tiền và các khoản phí -->
    <div style="text-align: right; font-weight: bold; margin-top: 30px; font-size: 18px; color: #1E3D58;">
<p><strong>Giảm Giá:</strong> ${$scope.formatCurrency(discount)}</p>
        <p><strong>Tổng Cộng:</strong> ${$scope.formatCurrency(totalPriceInfo.totalPrice || 0)}</p>
    </div>

    <!-- Lời cảm ơn -->
    <div style="text-align: center; margin-top: 40px; font-size: 16px; font-style: italic; color: #333;">
        <p>Cảm ơn bạn đã mua sắm tại NB Fashion. Chúc bạn có một ngày tuyệt vời!</p>
    </div>
</div>

    `;

        // Tạo iframe tạm thời để in
        const iframe = document.createElement('iframe');
        iframe.style.position = 'absolute';
        iframe.style.top = '-10000px';
        document.body.appendChild(iframe);

        const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;

        // Ghi nội dung hóa đơn vào iframe
        iframeDoc.open();
        iframeDoc.write(`
        <html>
            <head>
                <style>
                    @page { 
                        size: A3;
                        margin: 15mm;
                    }
                    body { 
                        margin: 0;
                        font-family: 'Roboto', sans-serif;
                    }
                </style>
            </head>
            <body>
                ${invoiceHtml}
            </body>
        </html>
    `);
        iframeDoc.close();

        // Gọi lệnh in từ iframe
        iframe.contentWindow.focus();
        iframe.contentWindow.print();

        // Thông báo in hóa đơn thành công với Swal.fire
        Swal.fire({
            icon: 'success',
            title: 'Hóa đơn đã được in thành công!',
            text: 'Cảm ơn bạn đã mua sắm tại NB Fashion.',
            confirmButtonText: 'Đóng'
        });
        localStorage.removeItem('voucherCode');

        // Xóa giỏ hàng trong sessionStorage sau khi in
        sessionStorage.removeItem('giohang');

        // Xóa iframe sau khi in
        document.body.removeChild(iframe);
    };



    $scope.thanhtoanck();
};


