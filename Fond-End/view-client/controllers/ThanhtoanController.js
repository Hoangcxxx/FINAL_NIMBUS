
window.ThanhtoanController = function ($scope, $http) {
    // Khởi tạo các thông tin giao hàng và giỏ hàng
    $scope.shippingInfo = {
        email: "",
        name: "",
        phone: "",
        address: "",
        province: "",
        district: "",
        ward: "",
        note: ""
    };
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.cart = []; // Danh sách sản phẩm trong giỏ hàng
    $scope.totalAmount = 0; // Tổng tiền
    
    // Lấy dữ liệu giỏ hàng từ API hoặc nguồn dữ liệu
    $scope.getCartItems = function () {
        const cartId = 3; // Thay đổi nếu cần thiết
        $http.get(`http://localhost:8080/api/giohang/${cartId}`)
            .then(function (response) {
                $scope.cart = response.data;

                // Lấy hình ảnh cho từng sản phẩm
                $scope.cart.forEach((element) => {
                    $http.get("http://localhost:8080/api/hinh_anh/" + element.idSanPham)
                        .then(function (response) {
                            element.urlAnh = response.data[0]?.urlAnh || ''; // Sử dụng toán tử optional chaining
                        })
                        .catch(function (error) {
                            console.error("Lỗi khi lấy hình ảnh sản phẩm:", error);
                        });
                });

                // Kiểm tra và thêm sản phẩm mới từ localStorage
                const newCartItem = localStorage.getItem("cartItem");
                if (newCartItem) {
                    const item = JSON.parse(newCartItem);
                    $scope.cart.push(item);
                    localStorage.removeItem("cartItem");
                }

                // Tính tổng tiền giỏ hàng
                $scope.calculateTotal();
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy sản phẩm trong giỏ hàng:", error);
            });
    };

    // Tính tổng tiền giỏ hàng
    $scope.calculateTotal = function() {
        $scope.totalAmount = $scope.cart.reduce((total, item) => total + item.soLuong * item.giaTien, 0);
    };

    // // Xử lý khi bấm nút 'Đặt hàng'
    // $scope.placeOrder = function() {
    //     const orderData = {
    //         shippingInfo: $scope.shippingInfo,
    //         cartItems: $scope.cart,
    //         totalAmount: $scope.totalAmount,
    //         paymentMethod: "COD" // Có thể cho phép chọn phương thức thanh toán
    //     };
        
    //     $http.post("http://localhost:8080/api/donhang", orderData)
    //         .then(function(response) {
    //             alert("Đơn hàng đã được đặt thành công!");
    //             // Xử lý các bước tiếp theo, ví dụ như điều hướng sang trang cảm ơn
    //         })
    //         .catch(function(error) {
    //             console.error("Lỗi khi đặt hàng:", error);
    //         });
    // };

    // Khởi chạy hàm lấy dữ liệu giỏ hàng khi vào trang
    $scope.getCartItems();


     $scope.getProvinces = function () {
        $http.get("http://localhost:8080/api/dia-chi/all")
            .then(function (response) {
                $scope.provinces = response.data;
            })
            .catch(function (error) {
                console.error("Error fetching provinces:", error);
            });
    };
    

    $scope.$watch("shippingInfo.province", function (newProvince) {
        if (newProvince) {
            $http.get(`http://localhost:8080/api/dia-chi/tinh/${newProvince}`)
                .then(function (response) {
                    $scope.districts = response.data;
                    $scope.wards = []; // Reset wards when a new province is selected
                })
                .catch(function (error) {
                    console.error("Error fetching districts:", error);
                });
        }
    });
    
  
    $scope.$watch("shippingInfo.district", function (newDistrict) {
        if (newDistrict) {
            $http.get(`http://localhost:8080/api/dia-chi/huyen/${newDistrict}`)
                .then(function (response) {
                    $scope.wards = response.data;
                })
                .catch(function (error) {
                    console.error("Error fetching wards:", error);
                });
        }
    });

    // Initialize provinces data on page load
    $scope.getProvinces();
};
