window.SanPhamController = function ($scope, $http) {
    $scope.dsChatLieu = [];
    $scope.dsMauSac = [];
    $scope.dsKichThuoc = [];
    $scope.selectedProduct = {};
    $scope.dsSanPham = [];
    $scope.dsSanPhamCT = [];
    $scope.dsDanhMuc = [];
    $scope.sanPhamChiTietList = [];
    $scope.listProductDetail = [];
    $scope.filteredProducts = [];
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
    // Function to fetch data
    $scope.fetchData = function (url, target, logMessage) {
        $http.get(url).then(function (response) {
            $scope[target] = response.data;
            console.log(logMessage, response.data);
        }, function (error) {
            console.error('Error fetching data:', error);
        });
    };


    // Function to initialize data
    function initializeData() {
        $scope.fetchData('http://localhost:8080/api/ad_san_pham', 'dsSanPham', 'Fetched products:');
        $scope.fetchData('http://localhost:8080/api/ad_danh_muc', 'dsDanhMuc', 'Fetched categories:');
    }

    // Initial data fetch
    initializeData();



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





    $scope.resetTable = function () {
        $scope.filteredProducts = [];  // Reset bảng sản phẩm chi tiết
        $scope.resetSelections();  // Reset các lựa chọn trong bảng
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
                return $http.post("http://localhost:8080/api/ad_san_pham", $scope.SanPham, {
                    headers: { 'Content-Type': 'application/json' },
                });
            })
            .then(response => {
                $scope.SanPham.idSanPham = response.data.idSanPham;

                alert('Chúc mừng bạn tạo mới thành công');
                initializeData();
                $scope.resetModal();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra. Vui lòng thử lại.');
            });
    };


    // Get category folder name
    const getCategoryFolder = idDanhMuc => {
        const category = $scope.dsDanhMuc.find(danhMuc => danhMuc.idDanhMuc === idDanhMuc);
        return category ? convertToFolderName(category.tenDanhMuc) : 'others';
    };

    // Chuyển đổi tên danh mục thành tên thư mục
    const convertToFolderName = name => {
        return name
            .normalize('NFD') // Chuyển đổi ký tự Unicode
            .replace(/[\u0300-\u036f]/g, '') // Loại bỏ dấu
            .toLowerCase() // Chuyển thành chữ thường
            .replace(/\s+/g, '_'); // Thay thế khoảng trắng bằng dấu gạch dưới
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
