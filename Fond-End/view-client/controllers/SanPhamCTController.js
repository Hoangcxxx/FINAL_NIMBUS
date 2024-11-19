window.SanPhamCTController = function ($scope, $http, $routeParams) {
    var idSanPham = $routeParams.id;
    
    $scope.dsSanPhamChiTiet = [];
    $scope.mauSacChiTiet = [];
    $scope.kichThuocChiTiet = [];
    $scope.chatLieuChiTiet = [];
    $scope.slideIndex = 1;
    $scope.soluong = 1;

    var user = JSON.parse(localStorage.getItem("user"));
    if (user) {
        var iduser = user.idNguoiDung; 
       
    }

    // Hàm lấy ảnh sản phẩm
    function fetchAnhSanPham() {
        $http.get('http://localhost:8080/api/hinh_anh/' + idSanPham)
            .then(function (response) {
                $scope.anhSanPham = response.data || [];
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy hình ảnh sản phẩm:', error);
            });
    }

    // Hàm lấy chi tiết sản phẩm
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/' + idSanPham)
            .then(function (response) {
                $scope.sanPhamChiTiet = response.data || {};
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chi tiết sản phẩm:', error);
            });
    }

    // Hàm lấy màu sắc
    function fetchMauSacChiTiet() {
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/mau_sac/' + idSanPham)
            .then(function (response) {
                $scope.mauSacChiTiet = response.data || [];
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy màu sắc sản phẩm:', error);
            });
    }

    // Hàm lấy kích thước
    function fetchKichThuocChiTiet() {
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/kich_thuoc/' + idSanPham)
            .then(function (response) {
                $scope.kichThuocChiTiet = response.data || [];
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy kích thước sản phẩm:', error);
            });
    }

    // Hàm lấy chất liệu
    function fetchChatLieuChiTiet() {
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/chat_lieu/' + idSanPham)
            .then(function (response) {
                $scope.chatLieuChiTiet = response.data || [];
            })
            .catch(function (error) {
                console.error('Lỗi khi lấy chất liệu sản phẩm:', error);
            });
    }

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

    // Hàm chọn chất liệu
    $scope.chonChatLieu = function (chatLieu) {
        $scope.selectedMaterial = chatLieu.idChatLieu;
        console.log("Chọn chất liệu:", chatLieu);
    };

    $scope.addToCart = function () {
        if (!$scope.selectedColor || !$scope.selectedSize || !$scope.selectedMaterial) {
            alert("Vui lòng chọn đầy đủ màu sắc, kích thước và chất liệu trước khi thêm vào giỏ hàng.");
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
        $http.post(`http://localhost:8080/api/giohang/add?idUser=${iduser}`, cartItem)
            .then(function (response) {
                alert("Sản phẩm đã được thêm vào giỏ hàng.");
                window.location.href = '#!gio_hang'; 
            })
            .catch(function (error) {
                console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
                alert(error.data.message || "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng.");
            });
    };



    // Hàm thanh toán
    $scope.checkout = function () {
        console.log("Đang thanh toán cho sản phẩm:", $scope.sanPhamChiTiet);
        // Thực hiện logic thanh toán tại đây
    };

    // Hàm hiển thị hình ảnh sản phẩm trong carousel
    $scope.showProductDetail = function (index) {
        if ($scope.anhSanPham[index]) {
            $scope.slideIndex = index + 1;
            showDivs($scope.slideIndex);
        }
    };

    // Hàm khởi tạo để tải dữ liệu
    function initialize() {
        fetchAnhSanPham();
        fetchSanPhamChiTiet();
        fetchMauSacChiTiet();
        fetchKichThuocChiTiet();
        fetchChatLieuChiTiet();
    }

    // Khởi động controller
    initialize();
};

// Hàm hiển thị các slide hình ảnh
function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");

    n = n > x.length ? 1 : (n < 1 ? x.length : n);

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
