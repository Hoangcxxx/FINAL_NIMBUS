window.SanPhamCTController = function ($scope, $http, $routeParams) {
    // Biến để lưu thông tin sản phẩm chi tiết
    $scope.sanPhamChiTiet = null;
    $scope.anhSanPham = []; // Biến lưu trữ danh sách ảnh sản phẩm

    // Lấy ID sản phẩm từ URL
    var idSanPham = $routeParams.id;

    // Hàm lấy thông tin chi tiết sản phẩm
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/san_pham/findSanPham/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.sanPhamChiTiet = response.data[0]; // Lấy sản phẩm đầu tiên trong mảng
                    $scope.anhSanPham = response.data; // Giả sử tất cả ảnh có trong mảng này
                    console.log("Ảnh sản phẩm:", $scope.anhSanPham);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }

    // Gọi hàm để lấy thông tin chi tiết sản phẩm khi controller được khởi tạo
    fetchSanPhamChiTiet();

    // Gán hàm currentDiv vào scope để có thể sử dụng trong HTML
    $scope.currentDiv = currentDiv;
};

// JavaScript còn lại không thay đổi

function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");

    if (n > x.length) { slideIndex = 1; }
    if (n < 1) { slideIndex = x.length; }

    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }

    // Kiểm tra số lượng dots trước khi truy cập
    if (dots.length > 0 && slideIndex > 0 && slideIndex <= dots.length) {
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" w3-opacity-off", "");
        }
        x[slideIndex - 1].style.display = "block";
        dots[slideIndex - 1].className += " w3-opacity-off";
    } else {
        console.error("Không có phần tử dots nào hoặc slideIndex không hợp lệ.");
    }
}
