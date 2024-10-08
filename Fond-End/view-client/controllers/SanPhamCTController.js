window.SanPhamCTController = function ($scope, $http, $routeParams) {
    $scope.sanPhamChiTiet = null;
    $scope.anhSanPham = [];
    var idSanPham = $routeParams.id;
    $scope.slideIndex = 1; // Đưa slideIndex vào scope

    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/san_pham/findSanPham/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.sanPhamChiTiet = response.data[0];
                    $scope.anhSanPham = response.data; // Giả sử tất cả ảnh có trong mảng này
                    console.log("Ảnh sản phẩm:", $scope.anhSanPham);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }

    fetchSanPhamChiTiet();
    $scope.currentDiv = currentDiv;

    $scope.showProductDetail = function (index) {
        if ($scope.anhSanPham && $scope.anhSanPham[index]) {
            $scope.slideIndex = index + 1; // Cập nhật chỉ số slide
            showDivs($scope.slideIndex); // Gọi hàm với slideIndex từ scope
            $scope.sanPhamChiTiet = $scope.anhSanPham[index];
            console.log("Thông tin sản phẩm chi tiết:", $scope.sanPhamChiTiet);
        }
    };
};

// Hàm để điều khiển carousel
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

    // Kiểm tra số lượng dots trước khi truy cập
    if (dots.length > 0 && n > 0 && n <= dots.length) {
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" w3-opacity-off", "");
        }
        x[n - 1].style.display = "block"; // Hiển thị hình ảnh tương ứng
        dots[n - 1].className += " w3-opacity-off";
    } else {
        console.error("Không có phần tử dots nào hoặc slideIndex không hợp lệ.");
    }
}
