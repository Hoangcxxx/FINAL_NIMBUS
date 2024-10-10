window.SanPhamCTController = function ($scope, $http, $routeParams) {
    $scope.sanPhamChiTiet = null;
    $scope.anhSanPham = [];
    $scope.mauSacChiTiet = [];
    var idSanPham = $routeParams.id;
    $scope.slideIndex = 1;

    function fetchAnhSanPham() {
        $http.get('http://localhost:8080/api/hinh_anh/' + idSanPham)
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
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/' + idSanPham)
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
        $http.get('http://localhost:8080/api/san_pham_chi_tiet/mau_sac/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.mauSacChiTiet = response.data;
                    console.log("Màu sắc sản phẩm:", $scope.mauSacChiTiet);
                } else {
                    console.log("Không tìm thấy màu sắc cho sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product images:', error);
            });
    }
    $scope.addToCart = function (sanPham) {
        // Logic để thêm sản phẩm vào giỏ hàng
        console.log("Đã thêm vào giỏ hàng:", sanPham);
        // Ví dụ: Thêm sản phẩm vào local storage hoặc gọi API để lưu giỏ hàng
    };

    $scope.checkout = function (sanPham) {
        // Logic để chuyển đến trang thanh toán
        console.log("Thanh toán cho sản phẩm:", sanPham);
        // Ví dụ: Chuyển hướng đến trang thanh toán với thông tin sản phẩm
    };

    fetchAnhSanPham();
    fetchSanPhamChiTiet();
    fetchMauSacChiTiet();

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
