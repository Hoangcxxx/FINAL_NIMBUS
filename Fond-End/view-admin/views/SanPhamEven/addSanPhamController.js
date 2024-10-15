window.addSanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.sanPhamChiTietList = [];
    $scope.listProductDetail = [];
    $scope.filteredProducts = [];
    $scope.dsDanhMuc = [];
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
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


    $scope.selectAll = false; // Tình trạng chọn tất cả

    // Hàm để kiểm tra trạng thái của tất cả check box
    $scope.toggleSelectAll = function (selectAll, items) {
        items.forEach(item => {
            item.selected = selectAll; // Cập nhật trạng thái cho từng item
        });
    };

    // Hàm để cập nhật tình trạng chọn tất cả
    $scope.updateSelectAll = function (items) {
        $scope.selectAll = items.every(item => item.selected); // Kiểm tra xem tất cả đã được chọn hay chưa
    };

    // Hàm cập nhật số lượng cho các sản phẩm đã chọn
    $scope.selectAll = false; // Tình trạng chọn tất cả

    // Hàm để kiểm tra trạng thái của tất cả check box
    $scope.toggleSelectAll = function (selectAll, items) {
        items.forEach(item => {
            item.selected = selectAll; // Cập nhật trạng thái cho từng item
        });
    };

    // Hàm để cập nhật tình trạng chọn tất cả
    $scope.updateSelectAll = function (items) {
        $scope.selectAll = items.every(item => item.selected); // Kiểm tra xem tất cả đã được chọn hay chưa
    };

    // Hàm cập nhật số lượng cho các sản phẩm đã chọn
    $scope.updateSelectedQuantities = function (updatedItem) {
        // Lấy giá trị số lượng từ ô input của sản phẩm đang được thay đổi
        const quantity = updatedItem.soLuong;

        // Đếm số lượng ô được chọn
        const selectedItems = [];
        $scope.filteredProducts.forEach(materialGroup => {
            materialGroup.colors.forEach(colorGroup => {
                colorGroup.products[0].items.forEach(item => {
                    if (item.selected) {
                        selectedItems.push(item);
                    }
                });
            });
        });

        // Chỉ cập nhật số lượng nếu đúng 2 hoặc 3 ô được chọn
        if (selectedItems.length >= 1) {
            selectedItems.forEach(item => {
                item.soLuong = quantity; // Cập nhật số lượng cho các sản phẩm đã chọn
            });
        }
        // Nếu không có 2 hoặc 3 ô được chọn, không làm gì cả
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
    $scope.saveProduct = function () {
        if (!$scope.productData.idSanPham) {
            alert("Vui lòng chọn sản phẩm!");
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
        $http.post('http://localhost:8080/api/ad_san_pham/multiple', $scope.sanPhamChiTietList)
            .then(response => {
                alert("Thêm sản phẩm chi tiết thành công!");
                console.log(response.data);
                $scope.resetSelections();
                $scope.resetForm();
            })
            .catch(error => {
                alert("Có lỗi xảy ra khi thêm sản phẩm chi tiết.");
                console.error("Error details:", error);
            });
    };

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
            $http.delete(`http://localhost:8080/api/ad_san_pham/${id}`).then(response => {
                console.log('Sản phẩm được xóa thành công:', response.data);
                initializeData();
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

    // Reset the form
    $scope.resetForm = function () {
        $scope.resetSelections();
        $scope.productData = { idSanPham: null };
        $('#addProductModal').modal('hide');
    };

    // Reset modal
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
        $('#addProductModal').modal('hide');
    };

    // Handle product creation
    $scope.onCreate = function () {
        $scope.SanPham.idDanhMuc = parseInt($scope.SanPham.idDanhMuc, 10);
        $scope.SanPham.giaBan = parseInt($scope.SanPham.giaBan, 10);

        if (!$scope.SanPham.idDanhMuc || !$scope.SanPham.tenSanPham || !$scope.SanPham.giaBan || !$scope.SanPham.moTa) {
            alert('Vui lòng điền đầy đủ thông tin sản phẩm.');
            return;
        }

        const images = document.getElementById('up-hinh-anh').files;
        $scope.SanPham.hinhAnh = [];
        const categoryFolder = getCategoryFolder($scope.SanPham.idDanhMuc);

        Promise.all(Array.from(images).map(image => new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = e => {
                const imagePath = `images/${categoryFolder}/${image.name}`;
                $scope.SanPham.hinhAnh.push(imagePath);
                resolve();
            };
            reader.onerror = reject;
            reader.readAsDataURL(image);
        })))
            .then(() => {
                $http.post("http://localhost:8080/api/ad_san_pham", $scope.SanPham, {
                    headers: { 'Content-Type': 'application/json' },
                }).then(response => {
                    $scope.SanPham.idSanPham = response.data.idSanPham;
                    if ($scope.SanPham.hinhAnh.length > 0) {
                        $scope.SanPham.hinhAnh.forEach((url, index) => {
                            $http.post("http://localhost:8080/api/ad_hinh_anh_san_pham", {
                                idSanPham: $scope.SanPham.idSanPham,
                                urlAnh: url,
                                thuTu: index + 1,
                                loaiHinhAnh: "loai_hinh_anh"
                            });
                        });
                    }
                    alert('Chúc mừng bạn tạo mới thành công');
                    initializeData();
                    $scope.resetModal();
                }).catch(error => {
                    console.error('Error creating product:', error);
                    alert('Có lỗi xảy ra. Vui lòng thử lại.');
                });
            }).catch(error => {
                console.error('Error reading images:', error);
                alert('Có lỗi xảy ra khi đọc hình ảnh.');
            });
    };

    // Get category folder name
    const getCategoryFolder = idDanhMuc => {
        const category = $scope.dsDanhMuc.find(danhMuc => danhMuc.idDanhMuc === idDanhMuc);
        return category ? category.tenDanhMuc.toLowerCase().replace(/\s+/g, '_') : 'others';
    };

    // Handle image uploads
    $scope.uploadImages = function (el) {
        if (!el || !el.files) {
            console.error("No file input element or files found.");
            return;
        }

        const files = el.files;
        const maxFiles = 4;

        if (files.length > maxFiles) {
            alert(`Bạn chỉ có thể chọn tối đa ${maxFiles} hình ảnh.`);
            el.value = '';
            return;
        }

        const previewContainer = document.querySelector(".image-preview-container");
        previewContainer.innerHTML = '';
        $scope.SanPham.hinhAnh = [];

        Array.from(files).forEach(file => {
            const reader = new FileReader();
            reader.onload = e => {
                const img = document.createElement("img");
                img.setAttribute("src", e.target.result);
                img.classList.add("upload-image-preview");
                previewContainer.appendChild(img);
                $scope.SanPham.hinhAnh.push(`${file.name}`);
            };
            reader.readAsDataURL(file);
        });
    };






};
