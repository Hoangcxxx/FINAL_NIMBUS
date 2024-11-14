window.ThanhCongController = function ($scope, $http, $window) {
    const maHoaDon = HD001;  // You can replace this with a dynamic value as needed.

    const apiUrl = `http://localhost:8080/api/hoa-don/${maHoaDon}`;

    // Making the GET request to fetch order data
    $http.get(apiUrl)
        .then(function (response) {
            const orderData = response.data[0];  // We expect an array, so take the first item
            $scope.orderDetails = {
                maHoaDon: orderData.maHoaDon,
                tenPhuongThucThanhToan: orderData.tenPhuongThucThanhToan,
                listSanPhamChiTiet: orderData.listSanPhamChiTiet,
                thanhTien: orderData.thanhTien,
                phiShip: orderData.phiShip,
                tenNguoiNhan: orderData.tenNguoiNhan,
                sdtNguoiNhan: orderData.sdtNguoiNhan,
                diaChi: orderData.diaChi,
                tinh: orderData.tinh,
                huyen: orderData.huyen,
                xa: orderData.xa,
                ghiChu: orderData.ghiChu
            };
        })
        .catch(function (error) {
            console.error("Error fetching order details:", error);
        });
};
