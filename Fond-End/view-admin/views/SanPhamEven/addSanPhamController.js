window.addSanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedProduct = {};

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
            { url: 'http://localhost:8080/api/san_pham', target: 'dsSanPham', log: 'Fetched products:' },
            { url: 'http://localhost:8080/api/danh_muc', target: 'dsDanhMuc', log: 'Fetched categories:' },
            { url: 'http://localhost:8080/api/chat_lieu', target: 'dsChatLieu', log: 'Fetched materials:' },
            { url: 'http://localhost:8080/api/mau_sac', target: 'dsMauSac', log: 'Fetched colors:' },
            { url: 'http://localhost:8080/api/kich_thuoc', target: 'dsKichThuoc', log: 'Fetched sizes:' }
        ];

        urls.forEach(({ url, target, log }) => $scope.fetchData(url, target, log));
    }

    // Initial data fetch
    initializeData();

    // Save or edit a product
    $scope.saveProduct = function () {
        const method = $scope.selectedProduct.idSanPham ? 'put' : 'post';
        const url = `http://localhost:8080/api/san_pham${$scope.selectedProduct.idSanPham ? '/' + $scope.selectedProduct.idSanPham : ''}`;
        
        $http[method](url, $scope.selectedProduct).then(response => {
            console.log(`Sản phẩm được ${method === 'put' ? 'sửa' : 'thêm'} thành công:`, response.data);
            $('#addProductModal').modal('hide');
            initializeData();
        }).catch(error => console.error(`Error ${method === 'put' ? 'updating' : 'adding'} product:`, error));
    };

    // Edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        $('#addProductModal').modal('show');
    };

    // Delete a product
    $scope.xoaSanPham = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            $http.delete(`http://localhost:8080/api/san_pham/${id}`).then(response => {
                console.log('Sản phẩm được xóa thành công:', response.data);
                initializeData();
            }).catch(error => console.error('Error deleting product:', error));
        }
    };

    // Reset the form
    $scope.resetModal = function () {
        $scope.selectedProduct = {
            tenSanPham: "",
            soLuong: "",
            danhMuc: "",
            trangThai: ""
        };
        $('#addProductModal').modal('hide');
        $('#attributeModal').modal('hide');
    };

    // Save attributes
    $scope.saveAttribute = function () {
        const attributes = {
            materials: Array.from(document.getElementById('material').selectedOptions).map(option => option.value),
            sizes: Array.from(document.getElementById('size').selectedOptions).map(option => option.value),
            colors: Array.from(document.getElementById('color').selectedOptions).map(option => option.value),
        };

        $http.post('http://localhost:8080/api/thuoc_tinh', attributes).then(response => {
            console.log('Thuộc tính được thêm thành công:', response.data);
            $('#attributeModal').modal('hide');
        }).catch(error => console.error('Error adding attribute:', error));
    };
};
