window.KhoHangController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.productData = {}; // Initialize productData
    $scope.filteredProducts = []; // Initialize for filtered products
    $scope.quantity = 1; // Default quantity

    // Fetch categories
    const fetchCategories = () => {
        console.log('Fetching categories...');
        $http.get('http://localhost:8080/api/ad_kho_hang/categories')
            .then(response => {
                console.log('Categories fetched successfully:', response.data);
                $scope.dsDanhMuc = response.data;
            })
            .catch(error => {
                console.error('Error fetching categories:', error);
                alert('Failed to fetch categories. Please check the console for details.');
            });
    };

    // On category change, fetch products
    $scope.onCategoryChange = function () {
        const idDanhMuc = $scope.productData.idDanhMuc;
        console.log('Category changed to ID:', idDanhMuc);
        if (idDanhMuc) {
            $http.get(`http://localhost:8080/api/ad_kho_hang/products?idDanhMuc=${idDanhMuc}`)
                .then(response => {
                    console.log('Products fetched successfully:', response.data);
                    $scope.dsSanPham = response.data;
                    resetAttributes();
                })
                .catch(error => {
                    console.error('Error fetching products by category:', error);
                    alert('Failed to fetch products. Please check the console for details.');
                    $scope.dsSanPham = [];
                });
        } else {
            resetAttributes();
        }
    };

    // Reset attributes
    const resetAttributes = () => {
        $scope.dsChatLieu = [];
        $scope.dsMauSac = [];
        $scope.dsKichThuoc = [];
        $scope.productData.idSanPham = null;
        $scope.filteredProducts = []; // Reset filtered products
    };

    // On product change, fetch materials
    $scope.onSanPhamChange = function () {
        const idSanPham = $scope.productData.idSanPham;
        console.log('Product changed to ID:', idSanPham);
        if (idSanPham) {
            $http.get(`http://localhost:8080/api/ad_kho_hang/materials?idSanPham=${idSanPham}`)
                .then(response => {
                    console.log('Materials fetched successfully:', response.data);
                    $scope.dsChatLieu = response.data;
                    resetColorAndSize(); // Reset color and size when product changes
                })
                .catch(error => {
                    console.error('Error fetching materials:', error);
                    resetAttributes();
                });
        } else {
            resetAttributes();
        }
    };

    // Reset color and size
    const resetColorAndSize = () => {
        $scope.dsMauSac = [];
        $scope.dsKichThuoc = [];
        $scope.productData.idChatLieu = null;
    };

    // On material change, fetch colors
    $scope.onChatLieuChange = function () {
        const idSanPham = $scope.productData.idSanPham;
        const idChatLieu = $scope.productData.idChatLieu;
        console.log('Material changed to ID:', idChatLieu);
        if (idSanPham && idChatLieu) {
            $http.get(`http://localhost:8080/api/ad_kho_hang/colors?idSanPham=${idSanPham}&idChatLieu=${idChatLieu}`)
                .then(response => {
                    console.log('Colors fetched successfully:', response.data);
                    $scope.dsMauSac = response.data;
                    resetSize(); // Reset size when color changes
                })
                .catch(error => {
                    console.error('Error fetching colors:', error);
                });
        } else {
            resetSize();
        }
    };

    // Reset size
    const resetSize = () => {
        $scope.dsKichThuoc = [];
        $scope.productData.idMauSac = null;
    };

    // On color change, fetch sizes
    $scope.onMauSacChange = function () {
        const idSanPham = $scope.productData.idSanPham;
        const idChatLieu = $scope.productData.idChatLieu;
        const idMauSac = $scope.productData.idMauSac;
        console.log('Color changed to ID:', idMauSac);
        if (idSanPham && idChatLieu && idMauSac) {
            $http.get(`http://localhost:8080/api/ad_kho_hang/sizes?idSanPham=${idSanPham}&idChatLieu=${idChatLieu}&idMauSac=${idMauSac}`)
                .then(response => {
                    console.log('Sizes fetched successfully:', response.data);
                    $scope.dsKichThuoc = response.data;
                })
                .catch(error => {
                    console.error('Error fetching sizes:', error);
                });
        } else {
            $scope.dsKichThuoc = [];
        }
    };

    // Fetch product details when all selections are made
    $scope.getProductDetails = function () {
        const { idSanPham, idChatLieu, idMauSac, idKichThuoc } = $scope.productData;
        if (idSanPham && idChatLieu && idMauSac && idKichThuoc) {
            $http.get(`http://localhost:8080/api/ad_kho_hang/product_details?idSanPham=${idSanPham}&idChatLieu=${idChatLieu}&idMauSac=${idMauSac}&idKichThuoc=${idKichThuoc}`)
                .then(response => {
                    console.log('Product details fetched successfully:', response.data);
                    $scope.filteredProducts = response.data; // Update filtered products
                })
                .catch(error => {
                    console.error('Error fetching product details:', error);
                });
        }
    };

    // Watch for changes to trigger product detail fetch
    $scope.$watchGroup(['productData.idSanPham', 'productData.idChatLieu', 'productData.idMauSac', 'productData.idKichThuoc'], function (newValues) {
        if (newValues.every(value => value !== null)) {
            $scope.getProductDetails();
        }
    });

    // Initialize data
    const initializeData = () => {
        fetchCategories();
    };

    initializeData();

    $scope.hideModal = function (modalId) {
        $('#' + modalId).modal('hide');
    };

    $scope.showQuantityModal = function (product) {
        $scope.selectedProduct = product; // Save selected product info
        $scope.quantity = 1; // Reset default quantity
        $('#quantityModal').modal('show'); // Open modal
    };

    $scope.updateQuantity = function () {
        if ($scope.quantity > 0 && $scope.selectedProduct) {
            const idSanPhamChiTiet = $scope.selectedProduct.idSanPhamCT; // Assuming you have this attribute
            console.log('Updating quantity for product ID:', idSanPhamChiTiet, 'New quantity:', $scope.quantity);
            $http.put(`http://localhost:8080/api/ad_kho_hang/update/${idSanPhamChiTiet}?soLuongThem=${$scope.quantity}`)
                .then(response => {
                    console.log('Quantity updated successfully:', response.data);
                    $('#quantityModal').modal('hide'); // Close modal after success

                    // Gọi lại hàm để lấy lại chi tiết sản phẩm
                    $scope.getProductDetails();
                })
                .catch(error => {
                    console.error('Error updating quantity:', error);
                    alert('Cập nhật số lượng không thành công. Vui lòng kiểm tra lại.');
                });
        } else {
            alert('Số lượng phải lớn hơn 0!');
        }
    };





    // Function to update product status
    $scope.updateTrangThai = function (idSanPhamCT, newStatus) {
        console.log('Updating status for product ID:', idSanPhamCT, 'New status:', newStatus);
        $http.put(`http://localhost:8080/api/ad_kho_hang/update_status/${idSanPhamCT}`, { trangThai: newStatus })
            .then(function (response) {
                console.log('Cập nhật trạng thái sản phẩm thành công:', response.data);
                // Thêm bất kỳ hành động nào bạn muốn sau khi cập nhật thành công
            })
            .catch(function (error) {
                console.error('Error updating product status:', error);
                // Log thêm thông tin chi tiết về lỗi
                if (error.status) {
                    console.error('Status:', error.status);
                }
                if (error.data) {
                    console.error('Response data:', error.data);
                }
                // Revert the checkbox state if the update fails
                const product = $scope.filteredProducts.find(p => p.idSanPhamCT === idSanPhamCT);
                if (product) {
                    product.trangThai = !newStatus; // Đảo ngược lại trạng thái
                }
            });
    };




};
