window.SanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedCategoryId = null;
    $scope.selectedChatLieuId = null;
    $scope.selectedMauSacId = null;
    $scope.selectedKichThuocId = null;
    $scope.selectedProduct = null;
    $scope.selectedDanhMuc = null;
    $scope.selectedChatLieu = null;
    $scope.selectedMauSac = null;
    $scope.selectedKichThuoc = null;
    $scope.searchTerm = ''; // Thêm biến tìm kiếm
    // Khởi tạo khoảng giá với giá trị mặc định min = 0 và max = 1000000
    $scope.priceRange = {
        min: 0, // Giá trị mặc định của min
        max: 1000000 // Giá trị mặc định của max
    };

    // Biến lưu trữ timeout để debounce
    let priceRangeTimeout;

    // Biến để xác định khi nào thay đổi khoảng giá
    let isPriceRangeChanged = false;

    // Function to fetch filtered products
    function fetchFilteredProducts() {
        const minPrice = $scope.priceRange.min || 0; // Lấy giá trị min từ priceRange hoặc mặc định là 0
        const maxPrice = $scope.priceRange.max || 1000000; // Lấy giá trị max từ priceRange hoặc mặc định là 1000000

        // Construct the filter URL
        let filterUrl = `http://localhost:8080/api/nguoi_dung/san_pham/search?minPrice=${minPrice}&maxPrice=${maxPrice}`;
        if ($scope.selectedCategoryId) {
            filterUrl += `&danhMucId=${$scope.selectedCategoryId}`;
        }
        if ($scope.selectedChatLieu) {
            filterUrl += `&chatLieuId=${$scope.selectedChatLieuId}`;
        }
        if ($scope.selectedMauSac) {
            filterUrl += `&mauSacId=${$scope.selectedMauSacId}`;
        }
        if ($scope.selectedKichThuoc) {
            filterUrl += `&kichThuocId=${$scope.selectedKichThuocId}`;
        }
        // Thêm tham số tìm kiếm theo tên sản phẩm nếu có
        if ($scope.searchTerm) {
            filterUrl += `&tenSanPham=${$scope.searchTerm}`;
        }
        // Fetch products from the API
        $http.get(filterUrl).then(function (response) {
            $scope.dsSanPham = response.data;
            console.log("Filtered products:", response.data);
        }, function (error) {
            console.error('Error fetching filtered products:', error);
        });
    }

    // Cập nhật hàm để thay đổi khoảng giá khi người dùng thay đổi giá trị trên slider
    $scope.updatePriceRange = function () {
        // Đảm bảo min và max là hợp lệ
        $scope.priceRange = {
            min: $scope.priceRange.min || 0,
            max: $scope.priceRange.max || 1000000
        };

        // Hủy bỏ timeout cũ nếu có
        if (priceRangeTimeout) {
            clearTimeout(priceRangeTimeout);
        }

        // Đặt flag là true khi khoảng giá thay đổi
        isPriceRangeChanged = true;

        // Tạo độ trễ 1 giây trước khi gọi lại hàm fetchFilteredProducts cho khoảng giá
        priceRangeTimeout = setTimeout(function () {
            if (isPriceRangeChanged) {
                fetchFilteredProducts();
                isPriceRangeChanged = false;  // Reset flag sau khi đã xử lý
            }
        }, 500);  // 500ms = 0.5 giây
    };
    // Hàm tìm kiếm sản phẩm
    $scope.searchProducts = function () {
        fetchFilteredProducts(); // Gọi hàm để lấy sản phẩm theo các bộ lọc và tên tìm kiếm
    };
    // Hàm xóa bộ lọc
    $scope.removeFilter = function (filterType) {
        switch (filterType) {
            case 'DanhMuc':
                $scope.selectedDanhMuc = null;
                $scope.selectedCategoryId = null;
                break;
            case 'ChatLieu':
                $scope.selectedChatLieu = null;
                $scope.selectedChatLieuId = null;
                break;
            case 'MauSac':
                $scope.selectedMauSac = null;
                $scope.selectedMauSacId = null;
                break;
            case 'KichThuoc':
                $scope.selectedKichThuoc = null;
                $scope.selectedKichThuocId = null;
                break;
        }

        // Gọi lại hàm fetchFilteredProducts để cập nhật danh sách sản phẩm sau khi xóa bộ lọc
        fetchFilteredProducts();
    };

    // Category selection
    $scope.selectDanhMuc = function (category) {
        $scope.selectedDanhMuc = category.tenDanhMuc;
        $scope.selectedCategoryId = category.idDanhMuc;
        console.log("Category selected:", category);
        fetchFilteredProducts();
    };

    // Material selection
    $scope.selectChatLieu = function (chatLieu) {
        $scope.selectedChatLieu = chatLieu.tenChatLieu;
        $scope.selectedChatLieuId = chatLieu.id;
        console.log("Material selected:", chatLieu);
        fetchFilteredProducts();
    };

    // Color selection
    $scope.selectMauSac = function (mauSac) {
        $scope.selectedMauSac = mauSac.tenMauSac;
        $scope.selectedMauSacId = mauSac.id;
        console.log("Color selected:", mauSac);
        fetchFilteredProducts();
    };

    // Size selection
    $scope.selectKichThuoc = function (kichThuoc) {
        $scope.selectedKichThuoc = kichThuoc.tenKichThuoc;
        $scope.selectedKichThuocId = kichThuoc.id;
        console.log("Size selected:", kichThuoc);
        fetchFilteredProducts();
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

    // Hàm lấy dữ liệu sản phẩm và danh mục khi khởi tạo controller
    function initializeData() {
        // Lấy danh sách sản phẩm
        // $scope.fetchData('http://localhost:8080/api/nguoi_dung/san_pham', 'dsSanPham');
        // Lấy danh sách danh mục
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/danh_muc', 'dsDanhMuc');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/chat_lieu', 'dsChatLieu');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/mau_sac', 'dsMauSac');
        $scope.fetchData('http://localhost:8080/api/nguoi_dung/kich_thuoc', 'dsKichThuoc');
    }
    $scope.page = 0; // Trang hiện tại
    $scope.size = 8; // Số sản phẩm mỗi trang
    $scope.totalPages = 1; // Tổng số trang
    $scope.dsSanPham = []; // Danh sách sản phẩm

    // Hàm lấy danh sách sản phẩm có phân trang
    $scope.getSanPhams = function (page) {
        if (page < 0 || page >= $scope.totalPages) return;
        $http.get("http://localhost:8080/api/nguoi_dung/san_pham/phan_trang?page=" + page + "&size=" + $scope.size)
            .then(function (response) {
                $scope.dsSanPham = response.data.data; // Sử dụng key 'data' từ response
                $scope.totalPages = response.data.totalPages;
                $scope.page = response.data.currentPage;
            });
    };

    // Hàm chuyển trang
    $scope.changePage = function (newPage) {
        $scope.getSanPhams(newPage);
    };

    // Gọi dữ liệu trang đầu tiên
    $scope.getSanPhams(0);

    // Hiển thị các số trang gần trang hiện tại
    $scope.getPageNumbers = function () {
        let range = [];
        let start = Math.max(1, $scope.page - 1);
        let end = Math.min($scope.totalPages - 2, $scope.page + 1);

        for (let i = start; i <= end; i++) {
            range.push(i);
        }
        return range;
    };


    // Gọi hàm khởi tạo
    initializeData();
    $scope.isValidDiscountPeriod = function (item) {
        // Lấy ngày hiện tại
        const today = new Date();

        // Lấy ngày bắt đầu và ngày kết thúc từ dữ liệu sản phẩm
        const startDate = new Date(item.ngayBatDau);
        const endDate = item.ngayKetThuc ? new Date(item.ngayKetThuc) : null; // Nếu không có ngày kết thúc thì không có điều kiện

        // Kiểm tra xem ngày hiện tại có nằm trong khoảng ngày bắt đầu và ngày kết thúc
        if (startDate && endDate) {
            return today >= startDate && today <= endDate;
        } else if (startDate) {
            return today >= startDate; // Nếu chỉ có ngày bắt đầu
        } else if (endDate) {
            return today <= endDate; // Nếu chỉ có ngày kết thúc
        }

        return false; // Không có ngày nào, không hợp lệ
    };

};
