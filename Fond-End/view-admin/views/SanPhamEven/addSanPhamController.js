window.addSanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedProduct = {};
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

        // Add selected materials, colors, and sizes to the product
        $scope.selectedProduct.materials = $scope.dsChatLieu.filter(item => item.selected);
        $scope.selectedProduct.colors = $scope.dsMauSac.filter(item => item.selected);
        $scope.selectedProduct.sizes = $scope.dsKichThuoc.filter(item => item.selected);

        $http[method](url, $scope.selectedProduct).then(response => {
            console.log(`Sản phẩm được ${method === 'put' ? 'sửa' : 'thêm'} thành công:`, response.data);
            $('#addProductModal').modal('hide');
            initializeData();
        }).catch(error => console.error(`Error ${method === 'put' ? 'updating' : 'adding'} product:`, error));
    };

    // Edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        // Set selected materials, colors, and sizes
        $scope.dsChatLieu.forEach(mat => mat.selected = item.materials.some(m => m.id === mat.id));
        $scope.dsMauSac.forEach(color => color.selected = item.colors.some(c => c.id === color.id));
        $scope.dsKichThuoc.forEach(size => size.selected = item.sizes.some(s => s.id === size.id));
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
        $scope.dsChatLieu.forEach(mat => mat.selected = false);
        $scope.dsMauSac.forEach(color => color.selected = false);
        $scope.dsKichThuoc.forEach(size => size.selected = false);
        $('#addProductModal').modal('hide');
        $('#materialModal').modal('hide');
        $('#colorModal').modal('hide');
        $('#sizeModal').modal('hide');
    };

    // Update selected material
    $scope.updateMaterial = function (material) {
        if (material.selected) {
            $scope.selectedMaterials.push(material);
        } else {
            const index = $scope.selectedMaterials.findIndex(m => m.id === material.id);
            if (index > -1) $scope.selectedMaterials.splice(index, 1);
        }
        document.getElementById('material').value = $scope.selectedMaterials.map(m => m.tenChatLieu).join(', ');
    };

    // Update selected color
    $scope.updateColor = function (color) {
        if (color.selected) {
            $scope.selectedColors.push(color);
        } else {
            const index = $scope.selectedColors.findIndex(c => c.id === color.id);
            if (index > -1) $scope.selectedColors.splice(index, 1);
        }
        document.getElementById('color').value = $scope.selectedColors.map(c => c.tenMauSac).join(', ');
    };

    // Update selected size
    $scope.updateSize = function (size) {
        if (size.selected) {
            $scope.selectedSizes.push(size);
        } else {
            const index = $scope.selectedSizes.findIndex(s => s.id === size.id);
            if (index > -1) $scope.selectedSizes.splice(index, 1);
        }
        document.getElementById('size').value = $scope.selectedSizes.map(s => s.tenKichThuoc).join(', ');
    };
};
