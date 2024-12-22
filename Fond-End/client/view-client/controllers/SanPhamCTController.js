window.SanPhamCTController = function ($scope, $http, $routeParams, $location) {
    var idSanPham = $routeParams.id;
    var idMauSacCT = $routeParams.id; // Initialize idMauSacCT to null
    $scope.dsSanPhamChiTiet = [];
    $scope.dsMauSacChiTiet = [];
    $scope.dsKichThuocChiTiet = [];
    $scope.anhSanPham = [];
    $scope.kichThuocChiTiet = [];
    $scope.selectedColor = null;
    $scope.soluong = 1;
    $scope.slideIndex = 1;
    // Lấy thông tin người dùng từ localStorage
    var userInfo = localStorage.getItem('user');
    if (userInfo) {
        // Nếu có thông tin người dùng, chuyển thành đối tượng và lấy ID người dùng
        userInfo = JSON.parse(userInfo);  // Parse thông tin người dùng từ JSON
        $scope.userId = $scope.infoUser.idNguoiDung;  // Lấy ID người dùng từ thông tin đã lưu
        console.log("thông tin:", $scope.userId);
        localStorage.setItem('idGioHang', $scope.userId);
    } else {
        // Nếu không có thông tin người dùng (người dùng chưa đăng nhập), gán userId là null
        $scope.userId = null;
        // Xóa idGioHang trong localStorage nếu người dùng chưa đăng nhập
        localStorage.removeItem("idGioHang");
    }
    function fetchAnhSanPham() {
        $http.get('http://localhost:8080/api/nguoi_dung/hinh_anh/' + idSanPham)
            .then(function (response) {
                if (response.data.length > 0) {
                    $scope.anhSanPham = response.data;
                    console.log("Ảnh sản phẩm:", $scope.anhSanPham);
                } else {
                    console.log("Không tìm thấy hình ảnh cho sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product images:', error);
            });
    }
    console.log("Thông tin sản phẩm phẩm phẩm phẩm:");
    function fetchSanPhamChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/' + idSanPham)
            .then(function (response) {
                if (response.data) {
                    $scope.sanPhamChiTiet = response.data;
                    console.log("Thông tin sản phẩm:", $scope.sanPhamChiTiet);
                } else {
                    console.log("Không tìm thấy sản phẩm.");
                }
            }, function (error) {
                console.error('Error fetching product details:', error);
            });
    }

    function fetchMauSacChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/mau_sac/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.mauSacChiTiet = response.data;
                    console.log("Màu sắc sản phẩm:", $scope.mauSacChiTiet);
                } else {
                    console.error("Không tìm thấy màu sắc cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    function fetchKichThuocChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/kich_thuoc/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.kichThuocChiTiet = response.data;
                    console.log("Kích thước sản phẩm:", $scope.kichThuocChiTiet);
                } else {
                    console.error("Không tìm thấy kích thước cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    function fetchChatLieuChiTiet() {
        $http.get('http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/chat_lieu/' + idSanPham)
            .then(function (response) {
                if (response.data && response.data.length > 0) {
                    $scope.chatLieuChiTiet = response.data;
                    console.log("Chất liệu sản phẩm:", $scope.chatLieuChiTiet);
                } else {
                    console.error("Không tìm thấy chất liệu cho sản phẩm. Dữ liệu trả về:", response.data);
                }
            }, function (error) {
                console.error('Error fetching product colors:', error);
            });
    }
    // Hàm lấy số lượng tồn của sản phẩm
    function fetchSoLuongTon() {
        if (!$scope.selectedColor || !$scope.selectedSize || !$scope.selectedMaterial) {
            $scope.soLuongTon = 0; // Nếu chưa chọn đủ các thuộc tính, không hiển thị số lượng tồn
            return;
        }

        // Sử dụng URL với tham số idChatLieu, idMauSac, idKichThuoc từ các lựa chọn của người dùng
        var url = `http://localhost:8080/api/nguoi_dung/san_pham_chi_tiet/findSanPhamCT/${idSanPham}?` +
            `idChatLieu=${$scope.selectedMaterial}&idMauSac=${$scope.selectedColor}&idKichThuoc=${$scope.selectedSize}`;

        $http.get(url)
            .then(function (response) {
                console.log("Dữ liệu trả về từ API:", response.data); // Kiểm tra toàn bộ dữ liệu trả về

                if (response.data && response.data.length > 0) {
                    console.log("Dữ liệu sản phẩm chi tiết:", response.data[0]); // Log ra đối tượng đầu tiên trong mảng
                    if (response.data[0].soLuong !== undefined) {
                        $scope.soLuongTon = response.data[0].soLuong;
                        console.log("Số lượng tồn của sản phẩm:", $scope.soLuongTon); // Kiểm tra số lượng tồn
                    } else {
                        $scope.soLuongTon = 0; // Nếu không có số lượng tồn
                        console.log("Không có thông tin số lượng tồn");
                    }
                } else {
                    $scope.soLuongTon = 0; // Nếu không có dữ liệu
                    console.log("Không có dữ liệu sản phẩm chi tiết");
                }
            }, function (error) {
                console.error('Error fetching stock quantity:', error);
                $scope.soLuongTon = 0; // Nếu có lỗi, giả sử là hết hàng
            });

    }

    // Fetch functions
    fetchAnhSanPham();
    fetchSanPhamChiTiet();
    fetchMauSacChiTiet();
    fetchKichThuocChiTiet();
    fetchChatLieuChiTiet();
    fetchSoLuongTon();

    $scope.chonChatLieu = function (chatLieu) {
        // Nếu chất liệu đã được chọn, bỏ chọn (gán giá trị là null)
        if ($scope.selectedMaterial === chatLieu.idChatLieu) {
            $scope.selectedMaterial = null;
        } else {
            // Nếu chưa chọn, gán giá trị đã chọn
            $scope.selectedMaterial = chatLieu.idChatLieu;
        }
        console.log("Chọn chất liệu:", chatLieu);
        fetchSoLuongTon();  // Gọi lại hàm fetchSoLuongTon khi chọn hoặc bỏ chọn chất liệu
    };

    $scope.chonMauSac = function (mauSac) {
        // Nếu màu sắc đã được chọn, bỏ chọn (gán giá trị là null)
        if ($scope.selectedColor === mauSac.idMauSac) {
            $scope.selectedColor = null;
        } else {
            // Nếu chưa chọn, gán giá trị đã chọn
            $scope.selectedColor = mauSac.idMauSac;
        }
        console.log("Chọn màu sắc:", mauSac);
        fetchSoLuongTon();  // Gọi lại hàm fetchSoLuongTon khi chọn hoặc bỏ chọn màu sắc
    };

    $scope.chonKichThuoc = function (kichThuoc) {
        // Nếu kích thước đã được chọn, bỏ chọn (gán giá trị là null)
        if ($scope.selectedSize === kichThuoc.idKichThuoc) {
            $scope.selectedSize = null;
        } else {
            // Nếu chưa chọn, gán giá trị đã chọn
            $scope.selectedSize = kichThuoc.idKichThuoc;
        }
        console.log("Chọn kích thước:", kichThuoc);
        fetchSoLuongTon();  // Gọi lại hàm fetchSoLuongTon khi chọn hoặc bỏ chọn kích thước
    };


    $scope.addToCart = function () {
        // Kiểm tra nếu chưa chọn đầy đủ màu sắc, kích thước và chất liệu
        if (!$scope.selectedColor || !$scope.selectedSize || !$scope.selectedMaterial) {
            Swal.fire({
                title: 'Lỗi',
                text: 'Vui lòng chọn đầy đủ màu sắc, kích thước và chất liệu trước khi thêm vào giỏ hàng.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra số lượng tồn
        if ($scope.soLuongTon === 0) {
            Swal.fire({
                title: 'Hết hàng',
                text: 'Sản phẩm hiện đã hết hàng.',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Lấy ID giỏ hàng từ localStorage
        var idGioHang = localStorage.getItem('idGioHang');

        // Kiểm tra nếu không có ID giỏ hàng, yêu cầu người dùng đăng nhập hoặc tạo giỏ hàng
        if (!idGioHang) {
            Swal.fire({
                title: 'Lỗi',
                text: 'Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
            return;
        }

        // Kiểm tra trạng thái tài khoản người dùng trước khi thao tác với giỏ hàng
        $http.get(`http://localhost:8080/api/nguoi_dung/${idGioHang}`)
            .then(function (response) {
                // Kiểm tra nếu tài khoản người dùng bị khóa
                if (!response.data.trangThai) {
                    Swal.fire({
                        title: 'Tài khoản của bạn đã bị khóa!',
                        text: 'Rất tiếc, tài khoản của bạn đã bị tạm khóa do phát hiện hoạt động bất thường hoặc vi phạm chính sách sử dụng. Để được hỗ trợ mở khóa, vui lòng liên hệ Quản trị viên qua hotline: 0987 233 227 hoặc email: support@example.com.',
                        icon: 'error',
                        confirmButtonText: 'Liên hệ hỗ trợ',
                        footer: '<a href="chinhsach.html">Xem chính sách sử dụng</a>'
                    });
                    return;
                }

                // Lấy thông tin giỏ hàng của người dùng để kiểm tra sản phẩm
                $http.get(`http://localhost:8080/api/nguoi_dung/gio_hang/${idGioHang}`)
                    .then(function (response) {
                        // Log toàn bộ giỏ hàng để kiểm tra
                        console.log("Giỏ hàng hiện tại:", response.data);

                        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                        let existingProduct = response.data.find(item =>
                            item.idSanPhamChiTiet === $scope.sanPhamChiTiet[0].idSanPhamCT &&
                            item.idMauSac === $scope.selectedColor.idMauSac &&
                            item.idKichThuoc === $scope.selectedSize.idKichThuoc &&
                            item.idChatLieu === $scope.selectedMaterial.idChatLieu
                        );
                        // Log số lượng sản phẩm đã có trong giỏ hàng (nếu có)
                        if (existingProduct) {
                            console.log("Sản phẩm đã có trong giỏ hàng:", existingProduct);
                            // Kiểm tra số lượng trong giỏ hàng
                            let totalQuantityInCart = existingProduct.soLuongGioHang + $scope.soluong;
                            console.log("Tổng số lượng trong giỏ hàng sau khi thêm:", totalQuantityInCart);

                            if (totalQuantityInCart > $scope.soLuongTon) {
                                Swal.fire({
                                    title: 'Lỗi',
                                    text: `Số lượng tồn của sản phẩm trong kho không đủ(số lượng còn lại là: ${$scope.soLuongTon}).`,
                                    icon: 'error',
                                    confirmButtonText: 'OK'
                                });
                                return;
                            }
                        } else {
                            console.log("Sản phẩm chưa có trong giỏ hàng.");
                            // Tiến hành thêm sản phẩm vào giỏ hàng
                        }
                        // Log số lượng tồn kho
                        console.log("Số lượng tồn kho của sản phẩm:", $scope.soLuongTon);

                        // Kiểm tra số lượng trong giỏ hàng và số lượng tồn kho
                        if (existingProduct) {
                            let totalQuantityInCart = existingProduct.soLuongGioHang + $scope.soluong;

                            console.log("Tổng số lượng trong giỏ hàng sau khi thêm:", totalQuantityInCart);

                            if (totalQuantityInCart > $scope.soLuongTon) {
                                // Nếu số lượng trong giỏ hàng + số lượng người dùng muốn thêm vượt quá số lượng tồn kho
                                Swal.fire({
                                    title: 'Lỗi',
                                    text: `Số lượng tồn của sản phẩm trong kho không đủ(số lượng còn lại là: ${$scope.soLuongTon}).`,
                                    icon: 'error',
                                    confirmButtonText: 'OK'
                                });
                                return;
                            }
                        }

                        // Nếu không có sản phẩm trong giỏ hàng hoặc có thể thêm, tiếp tục thêm sản phẩm vào giỏ hàng
                        let cartItem = {
                            idSanPham: $scope.sanPhamChiTiet[0].idSanPham,
                            idMauSac: $scope.selectedColor,  // ID màu sắc
                            idKichThuoc: $scope.selectedSize, // ID kích thước
                            idChatLieu: $scope.selectedMaterial, // ID chất liệu
                            soLuong: $scope.soluong // Số lượng mà người dùng chọn
                        };

                        // Gửi yêu cầu POST để thêm sản phẩm vào giỏ hàng
                        $http.post(`http://localhost:8080/api/nguoi_dung/gio_hang/add?idUser=${idGioHang}`, cartItem)
                            .then(function () {
                                Swal.fire({
                                    title: 'Thành công',
                                    text: 'Sản phẩm đã được thêm vào giỏ hàng.',
                                    icon: 'success',
                                    confirmButtonText: 'OK'
                                });
                            })
                            .catch(function (error) {
                                console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
                                Swal.fire({
                                    title: 'Lỗi',
                                    text: error.data.message || "Sản phẩm đã ngừng bán.",
                                    icon: 'error',
                                    confirmButtonText: 'OK'
                                });
                            });
                    })
                    .catch(function (error) {
                        console.error('Error fetching cart items:', error);
                        Swal.fire({
                            title: 'Lỗi',
                            text: "Có lỗi xảy ra khi kiểm tra giỏ hàng.",
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    });
            })
            .catch(function (error) {
                console.error('Error fetching user data:', error);
                Swal.fire({
                    title: 'Lỗi',
                    text: "Có lỗi xảy ra khi kiểm tra tài khoản người dùng.",
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            });
    };








    $scope.checkout = function () {
        console.log("Đang thanh toán cho sản phẩm:", $scope.sanPhamChiTiet);
        // Thực hiện logic thanh toán tại đây
    };

    $scope.currentDiv = currentDiv;

    $scope.showProductDetail = function (index) {
        if ($scope.anhSanPham && $scope.anhSanPham[index]) {
            $scope.slideIndex = index + 1;
            showDivs($scope.slideIndex);
        }
    };
};

// Hàm điều khiển carousel
function currentDiv(n) {
    showDivs(slideIndex = n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("demo");

    if (n > x.length) { n = 1; }
    if (n < 1) { n = x.length; }

    for (i = 0; i < x.length; i++) {
        x[i].style.display = "none";
    }

    if (dots.length > 0 && n > 0 && n <= dots.length) {
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" w3-opacity-off", "");
        }
        x[n - 1].style.display = "block";
        dots[n - 1].className += " w3-opacity-off";
    } else {
        console.error("Không có phần tử dots nào hoặc slideIndex không hợp lệ.");
    }
}
