window.ThanhCongttController = function($scope, $http, $window){
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
                        giaTriMavoucher: hoaDon.giaTriMavoucher || 0,  // Giá trị mặc định là 0
                        kieugiamgia: hoaDon.giaTriMavoucher < 0 ? true : false,  // Kiểm tra nếu giaTriMavoucher âm (phần trăm giảm giá)
                                                                         
                        idlichsuhoadon: hoaDon.idlichsuhoadon || []

                        
                    };

                    console.log("Giá trị mã voucher:", $scope.orderData.giaTriMavoucher);

                    console.log("Kieeur Giam Gia :", $scope.orderData.kieugiamgia);
                      // Tính toán giảm giá
                    
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
 
     $scope.getTotalProductPrice = function() {
         let total = 0;
         // Lặp qua tất cả các sản phẩm và cộng tiền
         angular.forEach($scope.orderDetails.listSanPhamChiTiet, function(item) {
             total += item.tongtien || 0; // Thêm giá của mỗi sản phẩm vào tổng
         });
         return total;
     };
     

     // Gọi API để lấy chi tiết đơn hàng
     getOrderDetails(maHoaDon);
}