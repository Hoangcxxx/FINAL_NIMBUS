window.addSanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.sanPhamChiTietList = []; // Initialize as an array
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedProduct = [];
    $scope.selectedMaterials = [];
    $scope.selectedColors = [];
    $scope.selectedSizes = [];

    // Fetch data function
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(response => {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }).catch(error => console.error('Error fetching data:', error));
    };

    // Initialize data
    function initializeData() {
        const urls = [
            { url: 'http://localhost:8080/api/ad_san_pham', target: 'dsSanPham', log: 'Fetched products:' },
            { url: 'http://localhost:8080/api/ad_danh_muc', target: 'dsDanhMuc', log: 'Fetched categories:' },
            { url: 'http://localhost:8080/api/ad_chat_lieu', target: 'dsChatLieu', log: 'Fetched materials:' },
            { url: 'http://localhost:8080/api/ad_mau_sac', target: 'dsMauSac', log: 'Fetched colors:' },
            { url: 'http://localhost:8080/api/ad_kich_thuoc', target: 'dsKichThuoc', log: 'Fetched sizes:' }
        ];

        urls.forEach(({ url, target, log }) => $scope.fetchData(url, target, log));
    }

    // Initial data fetch
    initializeData();

    // Update selected material
    $scope.updateMaterial = function (material) {
        if (material.selected) {
            $scope.selectedMaterials.push(material);
        } else {
            $scope.selectedMaterials = $scope.selectedMaterials.filter(m => m.id !== material.id);
        }
        document.getElementById('material').value = $scope.selectedMaterials.map(m => m.tenChatLieu).join(', ');
    };

    // Update selected color
    $scope.updateColor = function (color) {
        if (color.selected) {
            $scope.selectedColors.push(color);
        } else {
            $scope.selectedColors = $scope.selectedColors.filter(c => c.id !== color.id);
        }
        document.getElementById('color').value = $scope.selectedColors.map(c => c.tenMauSac).join(', ');
    };

    // Update selected size
    $scope.updateSize = function (size) {
        if (size.selected) {
            $scope.selectedSizes.push(size);
        } else {
            $scope.selectedSizes = $scope.selectedSizes.filter(s => s.id !== size.id);
        }
        document.getElementById('size').value = $scope.selectedSizes.map(s => s.tenKichThuoc).join(', ');
    };

    // Save or edit a product
    $scope.saveProduct = function () {
        if (!$scope.productData.idSanPham) {
            alert("Vui lòng chọn sản phẩm!");
            return;
        }

        // Lấy ID sản phẩm từ dữ liệu sản phẩm
        const idSanPham = parseInt($scope.productData.idSanPham, 10);

        // Tạo một danh sách sản phẩm chi tiết
        $scope.selectedMaterials.forEach((material, index) => {
            // Tạo đối tượng SanPhamChiTiet cho mỗi sự chọn lựa của chất liệu, màu sắc, kích thước
            const sanPhamChiTiet = {
                sanPham: {
                    idSanPham: idSanPham
                },
                chatLieuChiTiet: {
                    idChatLieuChiTiet: material.id // ID chất liệu
                },
                mauSacChiTiet: {
                    idMauSacChiTiet: $scope.selectedColors[index]?.id // ID màu sắc (có thể không có nếu không chọn)
                },
                kichThuocChiTiet: {
                    idKichThuocChiTiet: $scope.selectedSizes[index]?.id // ID kích thước (có thể không có nếu không chọn)
                }
            };

            // Đẩy vào danh sách sản phẩm chi tiết
            $scope.sanPhamChiTietList.push(sanPhamChiTiet);
        });

        console.log("Payload to send:", JSON.stringify($scope.sanPhamChiTietList, null, 2));

        // Gửi yêu cầu POST tới server
        $http.post('http://localhost:8080/api/ad_san_pham/multiple', $scope.sanPhamChiTietList)
            .then(function (response) {
                alert("Thêm sản phẩm chi tiết thành công!");
                console.log(response.data);
                // Xóa danh sách sản phẩm chi tiết và lựa chọn
                $scope.sanPhamChiTietList = [];
                $scope.selectedMaterials = [];
                $scope.selectedColors = [];
                $scope.selectedSizes = [];
            })
            .catch(function (error) {
                alert("Có lỗi xảy ra khi thêm sản phẩm chi tiết.");
                if (error.data) {
                    console.error("Error response data:", error.data);
                }
                console.error("Error details:", error);
            });
    };




    // Edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        // Set selected materials, colors, and sizes
        $scope.selectedMaterials = item.materials || [];
        $scope.selectedColors = item.colors || [];
        $scope.selectedSizes = item.sizes || [];

        // Update selection status for chatLieu, mauSac, kichThuoc
        $scope.dsChatLieu.forEach(mat => mat.selected = $scope.selectedMaterials.some(m => m.id === mat.id));
        $scope.dsMauSac.forEach(color => color.selected = $scope.selectedColors.some(c => c.id === color.id));
        $scope.dsKichThuoc.forEach(size => size.selected = $scope.selectedSizes.some(s => s.id === size.id));

        $('#addProductModal').modal('show');
    };

    // Delete a product
    $scope.xoaSanPham = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            $http.delete(`http://localhost:8080/api/ad_san_pham/${id}`).then(response => {
                console.log('Sản phẩm được xóa thành công:', response.data);
                initializeData();
            }).catch(error => console.error('Error deleting product:', error));
        }
    };

    // Reset the form
    $scope.resetModal = function () {
        // Giữ nguyên dữ liệu đã chọn
        $('#addProductModal').modal('hide');
        $('#materialModal').modal('hide');
        $('#colorModal').modal('hide');
        $('#sizeModal').modal('hide');
    };




    $scope.SanPham = {
        idDanhMuc: null,
        tenSanPham: "",
        moTa: "",
        giaBan: 100000,
        ngayTao: new Date(),
        ngayCapNhat: new Date(),
        trangThai: true
    };

    $scope.chinhSuaSanPham = function (item) {
        $scope.SanPham = angular.copy(item); // Sao chép dữ liệu để sửa
        $('#addProductModal').modal('show'); // Hiển thị modal
        $scope.isEditing = true; // Đánh dấu là đang chỉnh sửa
    };

    $scope.onCreate = function () {
        $scope.SanPham.idDanhMuc = parseInt($scope.SanPham.idDanhMuc, 10);
        if (!$scope.SanPham.idDanhMuc || !$scope.SanPham.tenSanPham || !$scope.SanPham.moTa) {
            alert('Vui lòng điền đầy đủ thông tin sản phẩm.');
            return;
        }
        $http.post("http://localhost:8080/api/ad_san_pham", $scope.SanPham).then(response => {
            alert('Chúc mừng bạn tạo mới thành công');
            initializeData(); // Load lại dsSanPham
            $scope.resetModal(); // Reset modal
        }).catch(error => console.error('Error creating product:', error));
    };




};
