window.SanPhamCTController = function ($scope, $http, $routeParams) {
    var idSanPham = $routeParams.id;
    var idMauSacCT = $routeParams.id; // Initialize idMauSacCT to null
    $scope.dsSanPhamChiTiet = [];
    $scope.dsMauSacChiTiet = [];
    $scope.dsKichThuocChiTiet = [];
    $scope.anhSanPham = [];
    $scope.kichThuocChiTiet = [];
    $scope.selectedColor = null;
    $scope.soluong = 1;
    $scope.slideIndex = 1;
    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');
    if (userInfo) {
        // Nếu có thông tin người dùng, chuyển thành đối tượng và lấy ID người dùng
        userInfo = JSON.parse(userInfo);  // Parse thông tin người dùng từ JSON
        $scope.userId = $scope.infoUser.idNguoiDung;  // Lấy ID người dùng từ thông tin đã lưu
        console.log("thông tin:", $scope.userId);
        localStorage.setItem('idGioHang', $scope.userId);
    } else {
        // Nếu không có thông tin người dùng (người dùng chưa đăng nhập), gán userId là null
        $scope.userId = null;
    }
    function fetchAnhSanPham() {
        $http.get('http://localhost:8080/api/nguoi_dung/hinh_anh/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.anhSanPham = response.data;
                    console.log("Ảnh sản phẩm:", $scope.anhSanPham);
                } else {
                    console.log("Không tìm thấy hình ảnh cho sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product images:', error);
            });
    }
    console.log("Thông tin sản phẩm phẩm phẩm phẩm:");
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/' + idSanPham)
            .then(function (response) {
                if (response.data) {
                    $scope.sanPhamChiTiet = response.data;
                    console.log("Thông tin sản phẩm:", $scope.sanPhamChiTiet);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }

    function fetchMauSacChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/mau_sac/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.mauSacChiTiet = response.data;
                    console.log("Màu sắc sản phẩm:", $scope.mauSacChiTiet);
                } else {
                    console.error("Không tìm thấy màu sắc cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    function fetchKichThuocChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/kich_thuoc/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.kichThuocChiTiet = response.data;
                    console.log("Kích thước sản phẩm:", $scope.kichThuocChiTiet);
                } else {
                    console.error("Không tìm thấy kích thước cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    function fetchChatLieuChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/chat_lieu/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.chatLieuChiTiet = response.data;
                    console.log("Chất liệu sản phẩm:", $scope.chatLieuChiTiet);
                } else {
                    console.error("Không tìm thấy chất liệu cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    // Fetch functions
    fetchAnhSanPham();
    fetchSanPhamChiTiet();
    fetchMauSacChiTiet();
    fetchKichThuocChiTiet();
    fetchChatLieuChiTiet();

    // Hàm chọn chất liệu
    $scope.chonChatLieu = function (chatLieu) {
        $scope.selectedMaterial = chatLieu.idChatLieu;
        console.log("Chọn chất liệu:", chatLieu);
    };

    // Hàm chọn màu sắc
    $scope.chonMauSac = function (mauSac) {
        $scope.selectedColor = mauSac.idMauSac;
        console.log("Chọn màu sắc:", mauSac);
    };

    // Hàm chọn kích thước
    $scope.chonKichThuoc = function (kichThuoc) {
        $scope.selectedSize = kichThuoc.idKichThuoc;
        console.log("Chọn kích thước:", kichThuoc);
    };


    $scope.addToCart = function () {
        if (!$scope.selectedColor || !$scope.selectedSize || !$scope.selectedMaterial) {
            alert("Vui lòng chọn đầy đủ màu sắc, kích thước và chất liệu trước khi thêm vào giỏ hàng.");
            return;
        }
        // Lấy idGioHang từ localStorage
        var idGioHang = localStorage.getItem('idGioHang');
        if (!idGioHang) {
            alert("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.");
            return;
        }
        // Chuẩn bị dữ liệu sản phẩm để gửi lên backend
        let cartItem = {
            idSanPham: $scope.sanPhamChiTiet[0].idSanPham,
            idMauSac: $scope.selectedColor,  // ID màu sắc
            idKichThuoc: $scope.selectedSize, // ID kích thước
            idChatLieu: $scope.selectedMaterial, // ID chất liệu
            soLuong: $scope.soluong // Số lượng mà người dùng chọn
        };
        // Gửi yêu cầu POST để thêm sản phẩm vào giỏ hàng
        $http.post(`http://localhost:8080/api/nguoi_dung/gio_hang/add?idUser=${idGioHang}`, cartItem)
            .then(function (response) {
                alert("Sản phẩm đã được thêm vào giỏ hàng.");
                window.location.href = '#!gio_hang';
            })
            .catch(function (error) {
                console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
                alert(error.data.message || "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.");
            });
    };


    $scope.checkout = function () {
        console.log("Đang thanh toán cho sản phẩm:", $scope.sanPhamChiTiet);
        // Thực hiện logic thanh toán tại đây
    };

    $scope.currentDiv = currentDiv;

    $scope.showProductDetail = function (index) {
        if ($scope.anhSanPham && $scope.anhSanPham[index]) {
            $scope.slideIndex = index + 1;
            showDivs($scope.slideIndex);
        }
    };
};

// Hàm điều khiển carousel
function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");

    if (n > x.length) { n = 1; }
    if (n < 1) { n = x.length; }

    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }

    if (dots.length > 0 && n > 0 && n <= dots.length) {
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" w3-opacity-off", "");
        }
        x[n - 1].style.display = "block";
        dots[n - 1].className += " w3-opacity-off";
    } else {
        console.error("Không có phần tử dots nào hoặc slideIndex không hợp lệ.");
    }
}
