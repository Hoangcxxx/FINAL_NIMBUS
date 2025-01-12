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
        maSanPham: "",
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
        $scope.fetchData('http://localhost:8080/api/admin/san_pham', 'dsSanPham', 'Fetched products:');
        $scope.fetchData('http://localhost:8080/api/admin/danh_muc', 'dsDanhMuc', 'Fetched categories:');
    }

    // Initial data fetch
    initializeData();


    $scope.loadProduct = function (idSanPham) {
        if (!idSanPham) {
            console.error('Invalid product ID:', idSanPham);
            return;
        }

        $http.get(`http://localhost:8080/api/admin/san_pham/findSanPham/${idSanPham}`)
            .then(function (response) {
                const product = response.data;
                $scope.SanPham = {
                    idSanPham: product.idSanPham,
                    maSanPham: product.maSanPham,
                    tenSanPham: product.tenSanPham,
                    giaBan: product.giaBan,
                    moTa: product.moTa,
                    trangThai: product.trangThai,
                    ngayTao: product.ngayTao,
                    ngayCapNhat: product.ngayCapNhat,
                    // Gán giá trị idDanhMuc từ product.danhMuc.idDanhMuc
                    idDanhMuc: product.danhMuc.idDanhMuc, // Đây là phần cần sửa
                };

                // Lưu danh mục vào một biến riêng để sử dụng trong dropdown
                $scope.selectedCategory = product.danhMuc;

                // Xử lý URL hình ảnh và lưu vào SanPham.hinhAnh
                $scope.SanPham.hinhAnh = product.allUrlAnh.split(',').map(url => url.trim());
                console.log('Product loaded:', $scope.SanPham);
            })
            .catch(function (error) {
                console.error('Error fetching product:', error);
            });
    };
    $scope.onUpdate = function () {
        // Tạo đối tượng dữ liệu cập nhật từ form
        let updatedProduct = {
            idSanPham: $scope.SanPham.idSanPham,
            maSanPham: $scope.SanPham.maSanPham,
            tenSanPham: $scope.SanPham.tenSanPham,
            giaBan: $scope.SanPham.giaBan,
            moTa: $scope.SanPham.moTa,
            trangThai: $scope.SanPham.trangThai,
            ngayTao: $scope.SanPham.ngayTao,
            ngayCapNhat: new Date(), // Cập nhật ngày giờ mới
            danhMuc: {
                idDanhMuc: $scope.SanPham.idDanhMuc,
                tenDanhMuc: $scope.selectedCategory.tenDanhMuc,
                moTa: $scope.selectedCategory.moTa,
                ngayTao: $scope.selectedCategory.ngayTao,
                ngayCapNhat: $scope.selectedCategory.ngayCapNhat
            },
            allUrlAnh: $scope.SanPham.hinhAnh.join(', ') // Cập nhật tất cả các URL hình ảnh
        };

        // Log giá trị để kiểm tra
        console.log("Dữ liệu cập nhật sản phẩm:", updatedProduct);

        // Gửi PUT request để cập nhật sản phẩm
        $http.put(`http://localhost:8080/api/admin/san_pham/${$scope.SanPham.idSanPham}`, updatedProduct)
            .then(function (response) {
                // Thông báo thành công
                Swal.fire({
                    icon: 'success',
                    title: 'Cập nhật sản phẩm thành công!',
                    showConfirmButton: false,
                    timer: 1500
                });

                // Cập nhật lại dữ liệu đã chọn (dùng để so sánh trong lần sau)
                $scope.selectedProduct = angular.copy($scope.SanPham);

                // Đóng modal sau khi cập nhật thành công
                $('#updateProductModal').modal('hide');
                // Tải lại danh sách sản phẩm
                $scope.fetchData('http://localhost:8080/api/admin/san_pham', 'dsSanPham', 'Fetched products:');
            }, function (error) {
                // Thông báo lỗi
                Swal.fire({
                    icon: 'error',
                    title: 'Đã xảy ra lỗi khi cập nhật sản phẩm!',
                    text: error.data ? error.data.message : 'Vui lòng thử lại.',
                    showConfirmButton: true
                });

                console.error(error);
            });
    };




    $scope.confirmDelete = function (idSanPham) {
        // Hiển thị hộp thoại xác nhận xóa sản phẩm
        Swal.fire({
            icon: 'warning',
            title: 'Bạn có chắc chắn muốn xóa sản phẩm này?',
            text: 'Hành động này không thể hoàn tác!',
            showCancelButton: true, // Hiển thị nút Hủy
            confirmButtonText: 'Có, xóa sản phẩm!',
            cancelButtonText: 'Hủy',
            reverseButtons: true // Đảo ngược vị trí các nút
        }).then((result) => {
            if (result.isConfirmed) {
                // Nếu người dùng xác nhận xóa, thực hiện gọi API để xóa sản phẩm
                $http.delete('http://localhost:8080/api/admin/san_pham/' + idSanPham)
                    .then(function (response) {
                        // Hiển thị thông báo thành công
                        Swal.fire({
                            icon: 'success',
                            title: 'Xóa sản phẩm thành công!',
                            showConfirmButton: false,
                            timer: 1500
                        });
                        // Tải lại danh sách sản phẩm
                        initializeData(); // Hàm này sẽ tái tải danh sách sản phẩm
                    })
                    .catch(function (error) {
                        // Hiển thị thông báo lỗi khi xóa không thành công
                        Swal.fire({
                            icon: 'error',
                            title: 'Đã xảy ra lỗi khi xóa sản phẩm!',
                            text: error.data ? error.data.message : 'Vui lòng thử lại.',
                            showConfirmButton: true
                        });
                    });
            }
        });
    };

    // Function to edit a product
    $scope.chinhSuaSanPham = function (item) {
        $scope.selectedProduct = angular.copy(item);
        $('#addProductModal').modal('show');
    };

    $scope.xoaSanPham = function (id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này?")) {
            $http.delete('http://localhost:8080/api/admin/san_pham/' + id).then(function (response) {
                // Thông báo thành công khi xóa
                Swal.fire({
                    icon: 'success',
                    title: 'Xóa sản phẩm thành công!',
                    showConfirmButton: false,
                    timer: 1500
                });
                initializeData(); // Re-fetch products
            }, function (error) {
                // Thông báo lỗi khi xóa
                Swal.fire({
                    icon: 'error',
                    title: 'Đã xảy ra lỗi khi xóa sản phẩm!',
                    text: error.data ? error.data.message : 'Vui lòng thử lại.',
                    showConfirmButton: true
                });
                console.error('Error deleting product:', error);
            });
        }
    };

    // Reset the form when opening the modal
    $scope.resetModal = function () {
        $scope.SanPham = {
            maSanPham: "",
            tenSanPham: "",
            soLuong: "",
            danhMuc: "",
            trangThai: ""
        };
        $scope.isEditing = false; // Reset editing state
        $('#addProductModal').modal('hide'); // Hide the modal
        $('#updateProductModal').modal('hide'); // Hide the modal
    };


    // Function to update product status
    $scope.updateTrangThai = function (idSanPham, newStatus) {
        $http.put('http://localhost:8080/api/admin/san_pham/update_status/' + idSanPham, { trangThai: newStatus })
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
            maSanPham: "",
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
        $scope.hideModal('updateProductModal');  // Đóng modal addProductModal
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
                return $http.post("http://localhost:8080/api/admin/san_pham", $scope.SanPham, {
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
