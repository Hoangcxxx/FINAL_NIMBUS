window.addSanPhamController = function ($scope, $http) {
    $scope.dsSanPham = [];
    $scope.sanPhamChiTietList = [];
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

        const idSanPham = parseInt($scope.productData.idSanPham, 10);
        $scope.selectedMaterials.forEach((material, index) => {
            const sanPhamChiTiet = {
                sanPham: { idSanPham: idSanPham },
                chatLieuChiTiet: { idChatLieuChiTiet: material.id },
                mauSacChiTiet: { idMauSacChiTiet: $scope.selectedColors[index]?.id },
                kichThuocChiTiet: { idKichThuocChiTiet: $scope.selectedSizes[index]?.id }
            };

            $scope.sanPhamChiTietList.push(sanPhamChiTiet);
        });

        console.log("Payload to send:", JSON.stringify($scope.sanPhamChiTietList, null, 2));

        $http.post('http://localhost:8080/api/ad_san_pham/multiple', $scope.sanPhamChiTietList)
            .then(function (response) {
                alert("Thêm sản phẩm chi tiết thành công!");
                console.log(response.data);
                $scope.sanPhamChiTietList = [];
                $scope.selectedMaterials = [];
                $scope.selectedColors = [];
                $scope.selectedSizes = [];
            })
            .catch(function (error) {
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
        $('#addProductModal').modal('hide');
        $('#materialModal').modal('hide');
        $('#colorModal').modal('hide');
        $('#sizeModal').modal('hide');
    };
    
    $scope.SanPham = {
        idDanhMuc: null,
        tenSanPham: "",
        moTa: "",
        giaBan: 0,
        ngayTao: new Date(),
        ngayCapNhat: new Date(),
        trangThai: true,
        hinhAnh: [],
        idSanPham: null // Thêm trường idSanPham
    };
    
    $scope.chinhSuaSanPham = function (item) {
        $scope.SanPham = angular.copy(item);
        $('#addProductModal').modal('show');
        $scope.isEditing = true;
    };
    
    $scope.onCreate = function () {
        $scope.SanPham.idDanhMuc = parseInt($scope.SanPham.idDanhMuc, 10);
        $scope.SanPham.giaBan = parseInt($scope.SanPham.giaBan, 10);
    
        console.log("Thông tin sản phẩm ban đầu:", JSON.stringify($scope.SanPham, null, 2));
    
        if (!$scope.SanPham.idDanhMuc || !$scope.SanPham.tenSanPham || !$scope.SanPham.giaBan || !$scope.SanPham.moTa) {
            alert('Vui lòng điền đầy đủ thông tin sản phẩm.');
            return;
        }
    
        const images = document.getElementById('up-hinh-anh').files;
        const imagePromises = [];
    
        // Reset mảng hinhAnh trước khi đọc file
        $scope.SanPham.hinhAnh = [];
    
        // Define folder paths based on category or other criteria
        const categoryFolder = getCategoryFolder($scope.SanPham.idDanhMuc); // Get the folder based on category
    
        Array.from(images).forEach(image => {
            const reader = new FileReader();
            const promise = new Promise((resolve, reject) => {
                reader.onload = function (e) {
                    // Add the full path including folder and file name
                    const imagePath = `images/${categoryFolder}/${image.name}`;
                    $scope.SanPham.hinhAnh.push(imagePath);
                    resolve();
                };
                reader.onerror = reject;
                reader.readAsDataURL(image); // Đọc file để hiển thị ảnh
            });
            imagePromises.push(promise);
        });
    
        Promise.all(imagePromises).then(() => {
            console.log("Dữ liệu gửi đi:", JSON.stringify($scope.SanPham));
    
            $http.post("http://localhost:8080/api/ad_san_pham", $scope.SanPham, {
                headers: { 'Content-Type': 'application/json' },
            }).then(response => {
                // Lưu ID sản phẩm
                $scope.SanPham.idSanPham = response.data.idSanPham; // Lưu ID sản phẩm từ phản hồi
    
                // Thêm hình ảnh cho sản phẩm
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
    
    function getCategoryFolder(idDanhMuc) {
        const category = $scope.dsDanhMuc.find(danhMuc => danhMuc.idDanhMuc === idDanhMuc);
        
        if (category) {
            return category.tenDanhMuc.toLowerCase().replace(/\s+/g, '_');
        }
        
        return 'others';
    }
    
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
    
            reader.onload = function (e) {
                const img = document.createElement("img");
                img.setAttribute("src", e.target.result);
                img.classList.add("upload-image-preview");
                previewContainer.appendChild(img);
                $scope.SanPham.hinhAnh.push(`${file.name}`);
                console.log("Selected file info:", $scope.SanPham.hinhAnh);
            };
    
            reader.readAsDataURL(file);
        });
    };
        



};
