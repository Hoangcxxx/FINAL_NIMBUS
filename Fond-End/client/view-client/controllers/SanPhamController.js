window.SanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedCategoryId = null;
    $scope.selectedProduct = null;
    $scope.selectedDanhMuc = null;
    $scope.selectedChatLieu = null;
    $scope.selectedMauSac = null;
    $scope.selectedKichThuoc = null;

    // Hàm để lưu giá trị đã chọn
    $scope.selectChatLieu = function (chatLieu) {
        $scope.selectedChatLieu = chatLieu;
        console.log("Chất liệu được chọn:", chatLieu);
    };

    $scope.selectMauSac = function (mauSac) {
        $scope.selectedMauSac = mauSac;
        console.log("Màu sắc được chọn:", mauSac);
    };

    $scope.selectKichThuoc = function (kichThuoc) {
        $scope.selectedKichThuoc = kichThuoc;
        console.log("Kích thước được chọn:", kichThuoc);
    };

    // Hàm để theo dõi danh mục được chọn
    $scope.selectDanhMuc = function (danhMuc) {
        $scope.selectedDanhMuc = danhMuc;
        console.log("Danh mục được chọn:", danhMuc);
    };


    // Hàm lấy dữ liệu từ API
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Hàm click vào danh mục
    $scope.onclickDanhMuc = function (idDanhMuc) {
        console.log("Danh mục được click: " + idDanhMuc);
        if (idDanhMuc === "null") {
            // Nếu chọn "Tất cả", lấy tất cả sản phẩm
            $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham', 'dsSanPham', "Dữ liệu API trả về:");
        } else {
            $scope.selectedCategoryId = idDanhMuc;
            // Lấy danh sách sản phẩm theo danh mục đã chọn
            $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham/findDanhMuc/' + idDanhMuc, 'dsSanPham', "Dữ liệu API trả về:");
        }
    };

    // Hàm click vào sản phẩm
    $scope.onclickSanPham = function (idSanPham) {
        console.log('ID sản phẩm:', idSanPham);
        // Lấy chi tiết sản phẩm đã chọn
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/' + idSanPham).then(function (response) {
            $scope.dsSanPham = response.data; // Gán dữ liệu sản phẩm chi tiết vào biến selectedProduct
            console.log("Dữ liệu sản phẩm chi tiết:", response.data);
            window.location.href = '#!/san_pham_ct/' + idSanPham;
        }, function (error) {
            console.error('Error fetching product details:', error);
        });
    };

    // Hàm lấy dữ liệu sản phẩm và danh mục khi khởi tạo controller
    function initializeData() {
        // Lấy danh sách sản phẩm
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham', 'dsSanPham');
        // Lấy danh sách danh mục
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/danh_muc', 'dsDanhMuc');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/chat_lieu', 'dsChatLieu');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/mau_sac', 'dsMauSac');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/kich_thuoc', 'dsKichThuoc');
    }

    // Gọi hàm khởi tạo
    initializeData();
};
