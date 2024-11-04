window.SanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedProduct = {};

    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };

    // Fetch data for materials, colors, sizes
    function fetchChatLieu() {
        $scope.fetchData('http://localhost:8080/api/ad_chat_lieu', 'dsChatLieu', 'Fetched materials:');
    }

    function fetchMauSac() {
        $scope.fetchData('http://localhost:8080/api/ad_mau_sac', 'dsMauSac', 'Fetched colors:');
    }

    function fetchKichThuoc() {
        $scope.fetchData('http://localhost:8080/api/ad_kich_thuoc', 'dsKichThuoc', 'Fetched sizes:');
    }

    // Function to initialize data
    function initializeData() {
        $scope.fetchData('http://localhost:8080/api/ad_san_pham', 'dsSanPham', 'Fetched products:');
        $scope.fetchData('http://localhost:8080/api/ad_danh_muc', 'dsDanhMuc', 'Fetched categories:');
        fetchChatLieu();
        fetchMauSac();
        fetchKichThuoc();
    }

    // Initial data fetch
    initializeData();

    // Function to save a product (add or edit)
    $scope.saveProduct = function () {
        if ($scope.selectedProduct.idSanPham) {
            $http.put('http://localhost:8080/api/ad_san_pham/' + $scope.selectedProduct.idSanPham, $scope.selectedProduct)
                .then(function (response) {
                    console.log('Sản phẩm được sửa thành công:', response.data);
                    $('#addProductModal').modal('hide');
                    initializeData(); // Re-fetch products
                }, function (error) {
                    console.error('Error updating product:', error);
                });
        } else {
            $http.post('http://localhost:8080/api/ad_san_pham', $scope.selectedProduct).then(function (response) {
                console.log('Sản phẩm được thêm thành công:', response.data);
                $('#addProductModal').modal('hide');
                initializeData(); // Re-fetch products
            }, function (error) {
                console.error('Error adding product:', error);
            });
        }
    };

    // Function to edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        $('#addProductModal').modal('show');
    };

    // Function to delete a product
    $scope.xoaSanPham = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            $http.delete('http://localhost:8080/api/ad_san_pham/' + id).then(function (response) {
                alert("Xóa sản phẩm thành công!");
                initializeData(); // Re-fetch products
            }, function (error) {
                console.error('Error deleting product:', error);
            });
        }
    };

    // Reset the form when opening the modal
    $scope.resetModal = function () {
        $scope.SanPham = {
            tenSanPham: "",
            soLuong: "",
            danhMuc: "",
            trangThai: ""
        };
        $scope.isEditing = false; // Reset editing state
        $('#addProductModal').modal('hide'); // Hide the modal
    };




    // Function to update product status
    $scope.updateTrangThai = function (idSanPham, newStatus) {
        $http.put('http://localhost:8080/api/ad_san_pham/update_status/' + idSanPham, { trangThai: newStatus })
            .then(function (response) {
                console.log('Cập nhật trạng thái sản phẩm thành công:', response.data);
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
                const product = $scope.dsSanPham.find(p => p.idSanPham === idSanPham);
                if (product) {
                    product.trangThai = !product.trangThai; // Toggle back
                }
            });
    };





};
