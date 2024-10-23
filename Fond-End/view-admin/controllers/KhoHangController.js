window.KhoHangController = function ($scope,$http) {
    $scope.dsSanPham = [];
    $scope.dsSanPhamCT = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.sanPhamChiTietList = [];

    // Fetch data function
    const fetchData = (url, target, logMessage) => {
        $http.get(url).then(response => {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }).catch(error => console.error('Error fetching data:', error));
    };

    // Fetch product details based on selected product ID
    $scope.fetchProductDetails = function (idSanPham) {
        if (!idSanPham) {
            $scope.filteredProducts = []; // Reset if no product selected
            return;
        }
        const url = `http://localhost:8080/api/san_pham_chi_tiet/findSanPhamCT/${idSanPham}`;
        $http.get(url)
            .then(response => {
                const products = response.data; // Assuming the response returns an array of product details
                const materialMap = {};

                products.forEach(item => {
                    // Initialize material group if it doesn't exist
                    if (!materialMap[item.tenChatLieu]) {
                        materialMap[item.tenChatLieu] = {};
                    }
                    // Initialize color group if it doesn't exist
                    if (!materialMap[item.tenChatLieu][item.tenMauSac]) {
                        materialMap[item.tenChatLieu][item.tenMauSac] = {};
                    }
                    // Initialize product group if it doesn't exist
                    if (!materialMap[item.tenChatLieu][item.tenMauSac][item.tenSanPham]) {
                        materialMap[item.tenChatLieu][item.tenMauSac][item.tenSanPham] = [];
                    }
                    // Push the item into the respective product group
                    materialMap[item.tenChatLieu][item.tenMauSac][item.tenSanPham].push(item);
                });

                // Convert the grouped object into an array format
                $scope.filteredProducts = Object.keys(materialMap).map(material => {
                    return {
                        material,
                        colors: Object.keys(materialMap[material]).map(color => {
                            return {
                                color,
                                products: Object.keys(materialMap[material][color]).map(productName => {
                                    return {
                                        productName,
                                        items: materialMap[material][color][productName]
                                    };
                                })
                            };
                        })
                    };
                });

                console.log('Fetched and grouped product details:', $scope.filteredProducts);
            })
            .catch(error => {
                console.error('Error fetching product details:', error);
                $scope.filteredProducts = []; // Reset on error
            });
    };



    // Initialize data
    const initializeData = () => {
        const urls = [
            { url: 'http://localhost:8080/api/ad_san_pham', target: 'dsSanPham', log: 'Fetched products:' },
            { url: 'http://localhost:8080/api/ad_danh_muc', target: 'dsDanhMuc', log: 'Fetched categories:' },
            { url: 'http://localhost:8080/api/ad_chat_lieu', target: 'dsChatLieu', log: 'Fetched materials:' },
            { url: 'http://localhost:8080/api/ad_mau_sac', target: 'dsMauSac', log: 'Fetched colors:' },
            { url: 'http://localhost:8080/api/ad_kich_thuoc', target: 'dsKichThuoc', log: 'Fetched sizes:' }
        ];
        urls.forEach(({ url, target, log }) => fetchData(url, target, log));
    };

    // Initial data fetch
    initializeData();
    // Reset selections
    $scope.resetSelections = function () {
        $scope.selectedMaterials = [];
        $scope.selectedColors = [];
        $scope.selectedSizes = [];
        document.getElementById('material').value = '';
        document.getElementById('color').value = '';
        document.getElementById('size').value = '';
    };
    $scope.resetTable = function () {
        $scope.filteredProducts = [];  // Reset bảng sản phẩm chi tiết
        $scope.resetSelections();  // Reset các lựa chọn trong bảng
    };
    // Reset the form
    $scope.resetForm = function () {
        $scope.resetSelections();
        $scope.productData = { idSanPham: null };
        $('#addProductModal').modal('hide');
    };

    

}
