window.addSanPhamController = function ($scope, $http, $routeParams) {
    $scope.dsSanPham = [];
    $scope.dsSanPhamCT = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.sanPhamChiTietList = [];
    $scope.listProductDetail = [];
    $scope.filteredProducts = [];
    $scope.selectedProduct = [];
    $scope.selectedMaterials = [];
    $scope.selectedColors = [];
    $scope.selectedSizes = [];
    $scope.productData = { idSanPham: null };
    $scope.SanPham = {
        idDanhMuc: null,
        tenSanPham: "",
        moTa: "",
        giaBan: 0,
        ngayTao: new Date(),
        ngayCapNhat: new Date(),
        trangThai: true,
        hinhAnh: [],
        idSanPham: null
    };

    $scope.dsSanPham = [];
    $scope.filteredProducts = [];
    $scope.productData = { idSanPham: null, tenSanPham: "" };

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
        const url = `http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/findAllSanPhamCT/${idSanPham}`;
        $http.get(url)
            .then(response => {
                const products = response.data; // Assuming the response returns an array of product details
                if (products.length > 0) {
                    // Update productData with the first product's information
                    $scope.productData.tenSanPham = products[0].tenSanPham; // Update other fields as necessary
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
                }
            })
            .catch(error => {
                console.error('Error fetching product details:', error);
                $scope.filteredProducts = []; // Reset on error
            });
    };

    // Watch for changes in product ID and fetch details accordingly
    $scope.$watch('productData.idSanPham', function (newVal) {
        if (newVal) {
            $scope.fetchProductDetails(newVal);
        } else {
            $scope.filteredProducts = []; // Reset if no product ID
        }
    });

    $scope.xoaNhieuSanPham = function () {
        // Tạo danh sách các idSanPhamCT được chọn
        let idSanPhamCTs = [];
        // Duyệt qua tất cả các sản phẩm và kiểm tra sản phẩm nào được chọn
        angular.forEach($scope.filteredProducts, function (materialGroup) {
            angular.forEach(materialGroup.colors, function (colorGroup) {
                angular.forEach(colorGroup.products[0].items, function (item) {
                    if (item.selected) {
                        idSanPhamCTs.push(item.idSanPhamCT);
                    }
                });
            });
        });

        // Nếu không có sản phẩm nào được chọn, thông báo cho người dùng
        if (idSanPhamCTs.length === 0) {
            alert("Vui lòng chọn ít nhất một sản phẩm chi tiết để xóa.");
            return;
        }

        // Xác nhận với người dùng trước khi xóa
        if (confirm("Bạn có chắc chắn muốn xóa các sản phẩm chi tiết đã chọn?")) {
            console.log('Đang xóa các sản phẩm chi tiết với các id:', idSanPhamCTs);

            // Gọi API xóa sản phẩm chi tiết
            $http.delete('http://localhost:8080/api/admin/san_pham_chi_tiet', { data: idSanPhamCTs, headers: { 'Content-Type': 'application/json' } })
                .then(function (response) {
                    alert("Xóa sản phẩm chi tiết thành công!");

                    // Cập nhật lại danh sách sản phẩm sau khi xóa
                    if ($scope.productData.idSanPham) {
                        $scope.fetchProductDetails($scope.productData.idSanPham);
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi khi xóa sản phẩm chi tiết:', error);
                    alert("Xóa sản phẩm chi tiết thất bại!");
                });
        }
    };



    // Initialize data
    const initializeData = () => {
        const urls = [
            { url: 'http://localhost:8080/api/admin/san_pham', target: 'dsSanPham', log: 'Fetched products:' },
            { url: 'http://localhost:8080/api/admin/danh_muc', target: 'dsDanhMuc', log: 'Fetched categories:' },
            { url: 'http://localhost:8080/api/admin/chat_lieu', target: 'dsChatLieu', log: 'Fetched materials:' },
            { url: 'http://localhost:8080/api/admin/mau_sac', target: 'dsMauSac', log: 'Fetched colors:' },
            { url: 'http://localhost:8080/api/admin/kich_thuoc', target: 'dsKichThuoc', log: 'Fetched sizes:' }
        ];
        urls.forEach(({ url, target, log }) => fetchData(url, target, log));
    };

    // Initial data fetch
    initializeData();

    // Update selections
    const updateSelection = (list, item, selectedItems, elementId) => {
        if (item.selected) {
            selectedItems.push(item);
        } else {
            selectedItems = selectedItems.filter(i => i.id !== item.id);
        }
        document.getElementById(elementId).value = selectedItems.map(i => i.tenChatLieu || i.tenMauSac || i.tenKichThuoc).join(', ');
        return selectedItems;
    };

    // Update selected material
    $scope.updateMaterial = item => $scope.selectedMaterials = updateSelection($scope.dsChatLieu, item, $scope.selectedMaterials, 'material');

    // Update selected color
    $scope.updateColor = item => $scope.selectedColors = updateSelection($scope.dsMauSac, item, $scope.selectedColors, 'color');

    // Update selected size
    $scope.updateSize = item => $scope.selectedSizes = updateSelection($scope.dsKichThuoc, item, $scope.selectedSizes, 'size');

    // Save or edit a product
    // Save or edit a product
    $scope.saveProduct = function () {
        if (!$scope.productData.idSanPham) {
            alert("Vui lòng chọn sản phẩm!");
            return;
        }
        // Kiểm tra nếu chưa chọn chất liệu, màu sắc hoặc kích thước
        if ($scope.selectedMaterials.length === 0) {
            alert("Vui lòng chọn ít nhất một chất liệu.");
            return;
        }

        if ($scope.selectedColors.length === 0) {
            alert("Vui lòng chọn ít nhất một màu sắc.");
            return;
        }

        if ($scope.selectedSizes.length === 0) {
            alert("Vui lòng chọn ít nhất một kích thước.");
            return;
        }
        const idSanPham = parseInt($scope.productData.idSanPham, 10);
        $scope.sanPhamChiTietList = [];

        // Create all combinations
        $scope.selectedMaterials.forEach(material =>
            $scope.selectedColors.forEach(color =>
                $scope.selectedSizes.forEach(size => {
                    $scope.sanPhamChiTietList.push({
                        sanPham: { idSanPham },
                        chatLieuChiTiet: { idChatLieuChiTiet: material.id },
                        mauSacChiTiet: { idMauSacChiTiet: color.id },
                        kichThuocChiTiet: { idKichThuocChiTiet: size.id }
                    });
                })
            )
        );

        console.log("Payload to send:", JSON.stringify($scope.sanPhamChiTietList, null, 2));
        $http.post('http://localhost:8080/api/admin/san_pham/multiple', $scope.sanPhamChiTietList)
            .then(response => {
                alert("Thêm sản phẩm chi tiết thành công!");
                console.log(response.data);
                $scope.resetSelections();
                $scope.resetForm();

                // Reload the product details to refresh the table
                $scope.fetchProductDetails(idSanPham); // Reload the table with the latest data
            })
            .catch(error => {
                alert("Có lỗi xảy ra khi thêm sản phẩm chi tiết.");
                console.error("Error details:", error);
            });
    };



    console.log($routeParams.id)

    $http({
        method: 'GET',
        url: 'http://localhost:8080/api/admin/san_pham/findSanPham' + "/" + $routeParams.id,
    }).then(function (response) {
        $scope.productData = response.data;
        console.log("log thử giá trị biến $scope.sanpham", response.data);
    })

    // Edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        $scope.selectedMaterials = item.materials || [];
        $scope.selectedColors = item.colors || [];
        $scope.selectedSizes = item.sizes || [];

        const setSelected = (list, selected) => list.forEach(i => i.selected = selected.some(s => s.id === i.id));
        setSelected($scope.dsChatLieu, $scope.selectedMaterials);
        setSelected($scope.dsMauSac, $scope.selectedColors);
        setSelected($scope.dsKichThuoc, $scope.selectedSizes);

        $('#addProductModal').modal('show');
    };

    // Delete a product
    $scope.xoaSanPham = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            console.log('Sản phẩm được xóa thành công:', id);
            $http.delete(`http://localhost:8080/api/admin/san_pham_chi_tiet/findSanPhamCT/${id}`).then(response => {
                console.log('Sản phẩm được xóa thành công:', response.data);
                $scope.fetchProductDetails($scope.productData.idSanPham);
            }).catch(error => console.error('Error deleting product:', error));
        }
    };

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
        // $scope.productData = { idSanPham: null };
        $('#materialModal').modal('hide');
        $('#colorModal').modal('hide');
        $('#sizeModal').modal('hide');

    };

    // Hàm đóng modal bằng AngularJS
    $scope.hideModal = function (modalId) {
        var modal = document.getElementById(modalId); // Dùng pure DOM để lấy modal
        if (modal) {
            $(modal).modal('hide'); // Sử dụng jQuery để ẩn modal
        } else {
            console.error('Không tìm thấy modal với id: ' + modalId);
        }
    };


    // Hàm reset modal
    $scope.resetModal = function () {
        $scope.SanPham = {
            idDanhMuc: null,
            tenSanPham: "",
            moTa: "",
            giaBan: 0,
            ngayTao: new Date(),
            ngayCapNhat: new Date(),
            trangThai: true,
            hinhAnh: [],
            idSanPham: null
        };

        // Reset image preview
        const previewContainer = document.querySelector(".image-preview-container");
        previewContainer.innerHTML = '';
        document.getElementById('up-hinh-anh').value = '';

        // Đóng modal
        $scope.hideModal('addProductModal');  // Đóng modal addProductModal
        $scope.hideModal('materialModal');   // Đóng modal materialModal
        $scope.hideModal('colorModal');      // Đóng modal colorModal
        $scope.hideModal('sizeModal');       // Đóng modal sizeModal
    };





    $scope.editCommonQuantity = function () {
        // Kiểm tra nếu có số lượng được nhập vào
        if ($scope.commonQuantity !== null && $scope.commonQuantity > 0) {
            // Cập nhật số lượng cho các sản phẩm đã chọn
            $scope.filteredProducts.forEach(function (materialGroup) {
                materialGroup.colors.forEach(function (colorGroup) {
                    colorGroup.products[0].items.forEach(function (item) {
                        if (item.selected) {
                            item.soLuong = $scope.commonQuantity; // Cập nhật số lượng cho item đã chọn
                        }
                    });
                });
            });
            // Đóng modal sau khi lưu
            $('#quantityModal').modal('hide');
        } else {
            alert('Vui lòng nhập số lượng hợp lệ!');
        }
    };
    $scope.hasSelectedItems = function () {
        let selected = false;
        $scope.filteredProducts.forEach(function (materialGroup) {
            materialGroup.colors.forEach(function (colorGroup) {
                colorGroup.products[0].items.forEach(function (item) {
                    if (item.selected) {
                        selected = true;
                    }
                });
            });
        });
        return selected;
    };




    $scope.selectAll = false; // Tình trạng chọn tất cả
    $scope.commonQuantity = null; // Biến lưu số lượng chung cho tất cả sản phẩm đã chọn

    // Hàm toggle chọn tất cả sản phẩm trong nhóm màu
    $scope.toggleSelectAll = function (selectAll, items) {
        items.forEach(item => {
            item.selected = selectAll; // Cập nhật trạng thái cho từng item
        });
        // Sau khi chọn tất cả, kiểm tra lại số lượng
        $scope.updateSelectedQuantities();
    };

    // Hàm kiểm tra lại xem tất cả các sản phẩm trong danh sách đã được chọn hay chưa
    $scope.updateSelectAll = function (items) {
        $scope.selectAll = items.every(item => item.selected); // Kiểm tra xem tất cả đã được chọn hay chưa
    };

    // Hàm cập nhật số lượng cho các sản phẩm đã chọn
    $scope.updateSelectedQuantities = function () {
        const selectedItems = [];

        // Lọc ra các sản phẩm được chọn
        $scope.filteredProducts.forEach(function (materialGroup) {
            materialGroup.colors.forEach(function (colorGroup) {
                colorGroup.products[0].items.forEach(function (item) {
                    if (item.selected) {
                        selectedItems.push(item);
                    }
                });
            });
        });

        // Kiểm tra nếu có sản phẩm được chọn và cập nhật số lượng
        selectedItems.forEach(function (item) {
            if (item.soLuong !== undefined) {
                item.soLuong = item.soLuong; // Cập nhật số lượng sản phẩm đã thay đổi
            }
        });
    };


    // Hàm lưu số lượng sản phẩm khi nhấn nút "Lưu số lượng sản phẩm"
    $scope.saveQuantities = function () {
        let updatedProducts = [];

        // Lọc qua các sản phẩm đã chọn và lưu lại thông tin
        $scope.filteredProducts.forEach(function (materialGroup) {
            materialGroup.colors.forEach(function (colorGroup) {
                colorGroup.products[0].items.forEach(function (item) {
                    if (item.soLuong !== undefined && item.selected) {  // Kiểm tra nếu số lượng thay đổi và sản phẩm được chọn
                        updatedProducts.push({
                            idSanPhamCT: item.idSanPhamCT,
                            newQuantity: item.soLuong
                        });
                    }
                });
            });
        });

        if (updatedProducts.length > 0) {
            $http.put('http://localhost:8080/api/admin/san_pham_chi_tiet/updateQuantities', updatedProducts)
                .then(function (response) {
                    console.log('Cập nhật số lượng thành công');
                    alert('Cập nhật số lượng thành công!');
                    $scope.fetchProductDetails($scope.productData.idSanPham);
                }, function (error) {
                    console.error('Cập nhật số lượng thất bại');
                    alert('Cập nhật số lượng thất bại!');
                });
        } else {
            alert('Không có thay đổi nào để lưu.');
        }
    };














    $scope.ChatLieu = {
        tenChatLieu: "",
        moTa: ""
    };
    $scope.onCreateChatLieu = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/admin/chat_lieu",
            data: $scope.ChatLieu
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal

            // Hide the "Add Material" modal and show the "Select Material" modal
            $('#addChatLieuModal').modal('hide');
            $('#materialModal').modal('show');
        });
    };

    // Hàm để lấy danh sách chất liệu
    $scope.MauSac = {
        tenMauSac: "",
        moTa: ""
    };
    $scope.onCreateMauSac = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/admin/mau_sac",
            data: $scope.MauSac
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal

            // Hide the "Add Color" modal and show the "Select Color" modal
            $('#addMauSacModal').modal('hide');
            $('#colorModal').modal('show');
        });
    };

    $scope.KichThuoc = {
        tenKichThuoc: "",
        moTa: ""
    };
    $scope.onCreateKichThuoc = function () {
        $http({
            method: 'POST',
            url: "http://localhost:8080/api/admin/kich_thuoc",
            data: $scope.KichThuoc
        }).then(function (response) {
            alert('Chúc mừng bạn tạo mới thành công');
            $scope.onRefresh(); // Refresh the data
            $scope.resetModal(); // Reset the modal

            // Hide the "Add Size" modal and show the "Select Size" modal
            $('#addKichThuocModal').modal('hide');
            $('#sizeModal').modal('show');
        });
    };
    $scope.onRefresh = function () {
        // Refresh Chat Lieu
        $scope.onRefreshChatLieu();

        // Refresh Mau Sac
        $scope.onRefreshMauSac();

        // Refresh Kich Thuoc
        $scope.onRefreshKichThuoc();
    };

    $scope.onRefreshChatLieu = function () {
        fetchData('http://localhost:8080/api/admin/chat_lieu', 'dsChatLieu');
    };

    $scope.onRefreshMauSac = function () {
        fetchData('http://localhost:8080/api/admin/mau_sac', 'dsMauSac');
    };

    $scope.onRefreshKichThuoc = function () {
        fetchData('http://localhost:8080/api/admin/kich_thuoc', 'dsKichThuoc');
    };



};
